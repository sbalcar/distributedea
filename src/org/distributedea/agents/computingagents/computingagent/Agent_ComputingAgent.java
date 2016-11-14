package org.distributedea.agents.computingagents.computingagent;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.models.BestIndividualModel;
import org.distributedea.agents.computingagents.computingagent.models.HelpersModel;
import org.distributedea.agents.computingagents.computingagent.models.IndividualToDistributionModel;
import org.distributedea.agents.computingagents.computingagent.models.ReceivedIndividualsModel;
import org.distributedea.logging.AgentComputingLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.agentinfo.AgentInfoWrapper;
import org.distributedea.ontology.agentinfo.GetAgentInfo;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.AgentName;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.helpmate.StatisticOfHelpmates;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.management.ReadyToBeKilled;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

/**
 * Abstract class of Agent which is inherited by all Computing Agents
 * @author stepan
 *
 */
public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	protected CompAgentState state = CompAgentState.INITIALIZATION;
	
	// configuration of this agent
	protected AgentConfiguration agentConf = null;
	
	// logger for Computing Agent
	private IAgentLogger logger = null;
	
	private BestIndividualModel bestIndividualModel = new BestIndividualModel();
	
	// the set of received Individuals from distribution
	protected ReceivedIndividualsModel receivedIndividuals = new ReceivedIndividualsModel();
	
	// the set of Individuals to distribution
	protected IndividualToDistributionModel individualsToDistribution = new IndividualToDistributionModel();
	
	private HelpersModel helpers = new HelpersModel();

	
	// computing thread
	protected ComputingThread computingThread = null;

	/**
	 * Specifies whether the agent can solve the {@link Problem} using a given
	 * {@link Individual} representation
	 * @param problem
	 * @param representation
	 * @return
	 */
	protected abstract boolean isAbleToSolve(ProblemStruct problemStruct);
	
	/**
	 * Starts computing a given {@link Problem} by using given {@link ProblemTool}
	 * @param problem
	 * @param behaviour
	 */
	protected abstract void startComputing(ProblemStruct problemStruct,
			AgentConfiguration requiredAgentConfiguration) throws Exception;
	
	/**
	 * Returns basic method description
	 * @return
	 */
	protected abstract AgentInfo getAgentInfo();
	
	/**
	 * Process input {@link Arguments} which computing agent receives.
	 * 
	 * @param arguments
	 */
	protected abstract void processArguments(Arguments arguments) throws Exception;
	
	
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		return ontologies;
	}


	@Override
	protected void registrDF() {
		        
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(Agent_ComputingAgent.class.getName());
        sd.setName(getLocalName());
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd);  
        
        } catch (FIPAException fe) {
        	getLogger().logThrowable("Registration faild", fe);
        }
	}
	
	
	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new AgentComputingLogger(this);
		}
		return logger;
	}
	public AgentComputingLogger getCALogger() {
		return (AgentComputingLogger) getLogger();
	}
	
	
	private AgentConfiguration processJadeArguments(Object[] jadeArguments) {
		
		Object argumentObj0 = jadeArguments[0];
		AgentName agentName = AgentName.importXML((String) argumentObj0);
		
		Object[] argumentsObj = new Object[jadeArguments.length -1];
		System.arraycopy(jadeArguments, 1, argumentsObj, 0, jadeArguments.length -1);
		Arguments arguments = Arguments.importArguments(argumentsObj);
		
		return new AgentConfiguration(agentName, this.getClass(), arguments);
	}
	
	@Override
	protected void setup() {
		
		// process Jade arguments
		this.agentConf = processJadeArguments(getArguments());
		try {
			processArguments(this.agentConf.getArguments());
		} catch (Exception e1) {
			commitSuicide();
		}
		
		initAgent();
		registrDF();

		state = CompAgentState.INITIALIZED;
				
		final MessageTemplate mesTemplateRequest =
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		addBehaviour(new AchieveREResponder(this, mesTemplateRequest) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgRequest) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgRequest);
					
					Concept concept = action.getAction();
					getLogger().log(Level.INFO, "Request for " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof GetAgentInfo) {
						
						return respondToGetMethodDescription(msgRequest, action);
						
					}	else if (concept instanceof AccessesResult) {
						
						return respondToAccessesResult(msgRequest, action);
						
					} else if (concept instanceof StartComputing) {
						
						return respondToStartComputing(msgRequest, action);
						
					} else if (concept instanceof ReportHelpmate) {
						
						return respondToReportHelpmate(msgRequest, action);
						
					} else if (concept  instanceof PrepareYourselfToKill) {
						
						return respondToPrepareYourselfToKill(msgRequest, action);
					}

				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				ACLMessage failure = msgRequest.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
		
		final MessageTemplate mesTemplateResultInform =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(ResultOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateResultInform) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgInform) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgInform);
					
					Concept concept = action.getAction();
					getLogger().log(Level.INFO, "Inform with " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof IndividualWrapper) {

						processIndividual(msgInform, action);
						
					}

				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				return null;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
	}

	
	protected ACLMessage respondToGetMethodDescription(ACLMessage msgRequest,
			Action action) {
		
		@SuppressWarnings("unused")
		GetAgentInfo getMethodDescription = (GetAgentInfo) action.getAction();
		
		ACLMessage reply = msgRequest.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		//active waiting for some result
		AgentInfoWrapper methodDescriptionWrp =
				new AgentInfoWrapper(getAgentInfo(), agentConf);

		Result result = new Result(action.getAction(), methodDescriptionWrp);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending " +
					AgentInfoWrapper.class.getSimpleName(), e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending " +
					AgentInfoWrapper.class.getSimpleName(), e);
		}

		return reply;
	}

	protected void processIndividual(ACLMessage request, Action action) {
		
		IndividualWrapper individualWrapper = (IndividualWrapper)action.getAction();
		
		if (individualWrapper == null || ! individualWrapper.valid(getLogger())) {
			getLogger().log(Level.INFO, "Received invalid " + Individual.class.getSimpleName());
			return;
		}
		
		if (state != CompAgentState.COMPUTING) {
			return;
		}
		
		Problem problem = computingThread.getProblem();
		IProblemTool problemTool = computingThread.getProblemTool();
		
		receivedIndividuals.addIndividual(individualWrapper, problem, problemTool, getLogger());
	}
	
	
	protected ACLMessage respondToAccessesResult(ACLMessage request,
			Action action) {

		@SuppressWarnings("unused")
		AccessesResult accessesResult = (AccessesResult) action.getAction();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ResultOntology.getInstance().getName());
		
		//active waiting for some result
		IndividualWrapper resultOfComputing =
				bestIndividualModel.exportBestIndividualWrp();
		
		if (resultOfComputing == null || ! resultOfComputing.valid(getLogger())) {
			reply.setPerformative(ACLMessage.REFUSE);
			return reply;
		}
		
		Result result = new Result(action.getAction(), resultOfComputing);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}

		return reply;
	}


	private ACLMessage respondToStartComputing(ACLMessage request, Action action) {
		
		StartComputing startComputing = (StartComputing) action.getAction();
		if (! startComputing.valid(getLogger())) {
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("Received " +
					StartComputing.class.getSimpleName() + " is not valid");
			
			return reply;
		}
		
		if (state != CompAgentState.INITIALIZED) {
			
			getLogger().log(Level.INFO, "Agent " + this.getLocalName() +
					" is still not initialized, request for " +
					StartComputing.class.getSimpleName() +
					" was resend onece again to myself");
			return request;
		}
		
		final ProblemWrapper problemWrapper = startComputing.getProblemWrapper();
		
		ProblemStruct problemStruct = problemWrapper.exportProblemStruct(logger);
		
		if (! isAbleToSolve(problemStruct)) {

			getCALogger().logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException("Can't solve problem"));
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("KO");
			
			return reply;
		}
		
		if (this.state != CompAgentState.INITIALIZED) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("Agent not intialized");
			return reply;
		}
		
		startComputation(problemWrapper);
			
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	private ACLMessage respondToPrepareYourselfToKill(ACLMessage request, Action action) {
		
		// changes state in thread
		state = CompAgentState.STOP;
		
		// waits until the end of Thread
		if (computingThread != null && computingThread.isAlive()) {
			try {
				computingThread.join();
			} catch (InterruptedException e) {
				getLogger().logThrowable("Error by waiting to end of computing Thread", e);
			}
		}
		// deregistres agent from DF
		//  before derestration have to be stop computing(after deregistration
		//  agent can no communicate wit another agents)
		deregistrDF();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		
		ReadyToBeKilled everythingPrepared = new ReadyToBeKilled();
		
		Result result = new Result(action.getAction(), everythingPrepared);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending " +
					ReadyToBeKilled.class.getSimpleName(), e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending " +
					ReadyToBeKilled.class.getSimpleName(), e);
		}

		return reply;
	}

	private ACLMessage respondToReportHelpmate(ACLMessage msgRequest,
			Action action) {
		
		ReportHelpmate reportHelpmate = (ReportHelpmate) action.getAction();
		boolean newStatisticsForEachQuery =
				reportHelpmate.isNewStatisticsForEachQuery();
		
		
		ACLMessage reply = msgRequest.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ResultOntology.getInstance().getName());
		

		MethodDescription description = getAgentDescription();
		
		List<MethodDescriptionCounter> helpmateList =
				getHelpmateList(newStatisticsForEachQuery);
		StatisticOfHelpmates statisticOfHelpmates =
				new StatisticOfHelpmates(description, helpmateList);
		
		Result result = new Result(action.getAction(), statisticOfHelpmates);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending " +
					StatisticOfHelpmates.class.getSimpleName(), e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending " +
					StatisticOfHelpmates.class.getSimpleName(), e);
		}

		return reply;
	}

	private void startComputation(final ProblemWrapper problemWrapper) {
		
		ProblemStruct problemStruct = problemWrapper.exportProblemStruct(logger);
		
		// adding cyclic behavior for sending Individual to distribution
		this.addBehaviour(new TickerBehaviour(this, Configuration.INDIVIDUAL_BROADCAST_PERIOD_MS) {

			private static final long serialVersionUID = 1L;

			protected void onTick() {
				 getLogger().log(Level.INFO, "Tick to send individual");
				 IndividualEvaluated individualEval = individualsToDistribution.getIndividual();
				 if (individualEval == null) {
					 getLogger().log(Level.WARNING, "Any Individual to send availible");
					 return;
				 }
				 
				 MethodDescription description = getAgentDescription();

				 IndividualWrapper individualWrapper =
						new IndividualWrapper(problemWrapper.getJobID(), description, individualEval);
				
				 ComputingAgentService.sendIndividualToNeighbours(Agent_ComputingAgent.this,
						individualWrapper, getLogger());
			}
		} );

		// starts thread where is running computation
		this.computingThread = new ComputingThread(this, problemStruct, agentConf);	
		this.computingThread.start();

	}
	
	private List<MethodDescriptionCounter> getHelpmateList(boolean newStatisticsForEachQuery) {
		
		List<MethodDescriptionCounter> helpmateList = this.helpers.getPrioritiesOfHelpers();
		if (newStatisticsForEachQuery) {
			this.helpers.clean();
		}
		
		return helpmateList;
	}
	
	
	protected void commitSuicide() {
		
		getLogger().log(Level.INFO, "Waiting for killing himself");

		ManagerAgentService.sendKillAgent(this, getAID(), getLogger());
	}
	

	protected void distributeIndividualToNeighours(IndividualsEvaluated individualsEval,
			Problem problem, JobID jobID) {
		
		individualsToDistribution.addIndividual(
				individualsEval.getIndividualsEvaluated(), problem);
	}
	
	protected void distributeIndividualToNeighours(List<IndividualEvaluated> individualsEval,
			Problem problem, JobID jobID) {
		
		individualsToDistribution.addIndividual(individualsEval, problem);
	}

	protected void distributeIndividualToNeighours(IndividualEvaluated individualEval,
			Problem problem, JobID jobID) {
		
		individualsToDistribution.addIndividual(individualEval, problem);
	}
	
	private MethodDescription getAgentDescription() {

		Class<?> problemToolClass = null;
		
		if (computingThread != null) {
			IProblemTool problemTool = computingThread.getProblemTool();
			
			if (problemTool != null) {
				problemToolClass = problemTool.getClass();
			}
		}
		
		try {
			return new MethodDescription(agentConf, problemToolClass);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected void processIndividualFromInitGeneration(IndividualEvaluated individualEval,
			long generationNumber, Problem problem, JobID jobID) {
		
		if (individualEval == null || ! individualEval.valid(getLogger())) {
			throw new IllegalArgumentException();
		}
		if (generationNumber != -1) {
			throw new IllegalArgumentException();
		}
		
		// log in local file
		String resultLog = "Generation " +
				generationNumber + ": " +
				individualEval.getFitness();
		getLogger().log(Level.INFO, resultLog);
		
		
		IProblemTool problemTool = this.computingThread.getProblemTool();
		
		MethodDescription description =
				new MethodDescription(agentConf, problemTool.getClass());
		
		IndividualWrapper computedIndividualWrp =
				new IndividualWrapper(jobID, description, individualEval);

		this.bestIndividualModel.update(computedIndividualWrp, problem,
				generationNumber, getCALogger());
		
		// log individual as best result
		getCALogger().logBestSolution(individualEval, jobID);
	}

	protected void processComputedIndividual(IndividualEvaluated individualEval ,
			long generationNumber, Problem problem, JobID jobID) {

		if (generationNumber == -1) {
			throw new IllegalStateException();
		}
		
		IProblemTool problemTool = this.computingThread.getProblemTool();
		
		MethodDescription description =
				new MethodDescription(agentConf, problemTool.getClass());
		
		IndividualWrapper computedIndividualWrp =
				new IndividualWrapper(jobID, description, individualEval);
		
		if (this.bestIndividualModel.isNewBetter(computedIndividualWrp,
				problem, getCALogger())) {
			
			this.bestIndividualModel.update(computedIndividualWrp, problem,
					generationNumber, getCALogger());
		}

		//log finess as result
		getCALogger().logComputedResult(individualEval, generationNumber, jobID);

		// log individual as best solution
		getCALogger().logBestSolution(individualEval, jobID);
		
	}
		
	/**
	 * save, send to DataManager and log received individual
	 * @param receivedIndividual
	 * @param receivedFitness
	 * @param generationNumber
	 * @param problem
	 */
	protected void processRecievedIndividual(IndividualWrapper individualWrp,
			long generationNumber, Problem problem) {
		
		if (individualWrp == null || ! individualWrp.valid(getLogger())) {
			throw new IllegalArgumentException();
		}
		if (problem == null || ! problem.valid(getLogger())) {
			throw new IllegalArgumentException();
		}
		
		MethodDescription description =
				individualWrp.getAgentDescription();
		
		if (bestIndividualModel.isNewBetter(individualWrp, problem,
				getCALogger())) {
			
			helpers.addHelper(description);
			
			bestIndividualModel.update(individualWrp, problem,
					generationNumber, getCALogger());
		}
	}
	
}
