package org.distributedea.agents.computingagents.universal;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.universal.behaviors.CompAgentIndivDistributionBehavior;
import org.distributedea.agents.computingagents.universal.localsaver.LocalSaver;
import org.distributedea.agents.computingagents.universal.models.BestIndividualModel;
import org.distributedea.agents.computingagents.universal.models.HelpersModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsTwoQueuesModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsOneQueueModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.IReadyToSendIndividualsInserter;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.IReceivedIndividualSelector;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.agentconfiguration.AgentName;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.agentinfo.AgentInfoWrapper;
import org.distributedea.ontology.agentinfo.GetAgentInfo;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.helpmate.StatisticOfHelpmates;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.management.ReadyToBeKilled;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.problems.AProblemTool;
import org.distributedea.services.ManagerAgentService;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;


/**
 * Abstract class of Agent which is inherited by all Computing Agents
 * @author stepan
 *
 */
public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	private final static boolean DEBUG = false;
	
	protected CompAgentState state = CompAgentState.INITIALIZATION;
	
	// configuration of this agent
	protected AgentConfiguration agentConf = null;
	
	// method IDs
	protected MethodIDs methodIDs = null;
	
	// logger for Computing Agent
	private IAgentLogger logger = null;
	
	protected LocalSaver localSaver = null;
	
	// data model for the best created or received individual
	protected BestIndividualModel bestIndividualModel = new BestIndividualModel();
	
	private HelpersModel helpers = new HelpersModel();
	
	// selector of received individual
	protected IReceivedIndividualSelector receivedIndividualSelector = null;
	
	// inserter of created individual to distribution
	protected IReadyToSendIndividualsInserter readyToSendIndividualsInserter = null;
	
	// the set of received Individuals from distribution
	private IReceivedIndividualsModel receivedIndividuals = new ReceivedIndivsOneQueueModel();
	
	// the set of Individuals to distribution
	private IReadyToSendIndividualsModel readyToSendIndividuals = new ReadyToSendIndivsTwoQueuesModel();

	
	// computing thread
	protected ComputingThread computingThread = null;

	/**
	 * Specifies whether the agent can solve the {@link Problem} using a given
	 * {@link Individual} representation
	 * @param problem
	 * @param representation
	 * @return
	 */
	protected abstract boolean isAbleToSolve(ProblemWrapper problemWrp);
	
	/**
	 * Starts computing a given {@link Problem} by using given {@link AProblemTool}
	 * @param problemWrp
	 * @param islandModelConf
	 * @param agentConf
	 * @throws Exception
	 */
	protected abstract void startComputing(ProblemWrapper problemWrp,
			IslandModelConfiguration islandModelConf,
			AgentConfiguration agentConf,
			MethodIDs methodIDs) throws Exception;
	
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
			this.logger = new FileLogger(this);
		}
		return logger;
	}
	public IAgentLogger getCALogger() {
		return getLogger();
	}
	
	
	private Pair<AgentConfiguration, MethodIDs> processAgentArguments(Object[] jadeArguments) {
		
		Object argumentObj0 = jadeArguments[0];
		Object argumentObj1 = jadeArguments[1];
		
		AgentName agentName = AgentName.importXML((String) argumentObj0);
		MethodIDs methodIDs = new MethodIDs((int)argumentObj1);
		
		
		Object[] argumentsObj = new Object[jadeArguments.length -2];
		System.arraycopy(jadeArguments, 2, argumentsObj, 0, jadeArguments.length -2);
		Arguments arguments = Arguments.importArguments(argumentsObj);
		
		AgentConfiguration agentConfiguration =
				new AgentConfiguration(agentName, this.getClass(), arguments);
				
		return new Pair<AgentConfiguration, MethodIDs>(agentConfiguration, methodIDs);
	}
	
	
	
	@Override
	protected void setup() {
		
		// process Jade arguments
		Pair<AgentConfiguration, MethodIDs> pair = processAgentArguments(getArguments());
		this.agentConf = pair.first;
		this.methodIDs = pair.second;
		
		getLogger().log(Level.INFO, "AgentConf: " + agentConf.toLogString());
		getLogger().log(Level.INFO, "MethodIDs: " + methodIDs.toLogString());
		
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
					e.printStackTrace();
					
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
					Serializable content =  msgInform.getContentObject();
					
					if (content instanceof IndividualWrapper) {
						
						IndividualWrapper individualWrapper =
								(IndividualWrapper) content;
						processIndividualWrp(msgInform, individualWrapper);
					}
					
				} catch (Exception e) {
					getLogger().logThrowable("Problem extracting content", e);
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

	@SuppressWarnings("unused")
	protected void processIndividualWrp(ACLMessage request, IndividualWrapper individualWrp) {
		
		if (individualWrp == null) {
			getLogger().log(Level.INFO, "Received invalid " + Individual.class.getSimpleName());
			return;
		}
		if (DEBUG && (! individualWrp.valid(getLogger()))) {
			getLogger().log(Level.INFO, "Received invalid " + Individual.class.getSimpleName());
			return;
		}

		
		if (state != CompAgentState.COMPUTING) {
			return;
		}
		
		IProblem problem = computingThread.getProblemDefinition();
		Dataset dataset = computingThread.getDataset();
		
		// add individual to received model
		receivedIndividuals.addIndividual(individualWrp, problem, dataset, getLogger());
		
		// update best helpers model
		helpers.processReceivedIndiv(individualWrp,
				bestIndividualModel.getTheBestIndividualWrp(), problem);
		
		// update the best Individual model
		bestIndividualModel.update(individualWrp, problem, getLogger());
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
				bestIndividualModel.exportTheBestIndividualWrp();
		
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
			
			startComputing.valid(getLogger());
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("Received " +
					StartComputing.class.getSimpleName() + " is not valid");
			
			return reply;
		}
		
		getLogger().log(Level.INFO, "StartComputing ontology received");
		getLogger().log(Level.INFO, "ProblemWrapper: " + startComputing.getProblemWrapper().toLogString());
		getLogger().log(Level.INFO, "IslandModelConfiguration: " +  startComputing.getIslandModelConfiguration().toLogString());
		
		if (state != CompAgentState.INITIALIZED) {
			
			getLogger().log(Level.INFO, "Agent " + this.getLocalName() +
					" is still not initialized, request for " +
					StartComputing.class.getSimpleName() +
					" was resend onece again to myself");
			return request;
		}
		
		final ProblemWrapper problemWrapper =
				startComputing.getProblemWrapper();
		final IslandModelConfiguration islandModelConf =
				startComputing.getIslandModelConfiguration();
		
		
		if (! isAbleToSolve(problemWrapper)) {

			getCALogger().logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException("Can't solve problem"));
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("KO");
			
			return reply;
		}
		
		if (methodIDs == null) {
			System.out.println("methodIDs are null");
		}
		
		try {
			startComputation(problemWrapper, islandModelConf, methodIDs);
		} catch (Exception e) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("Agent not intialized");
			return reply;		}
			
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
		

		MethodDescription description = getMethodDescription();
		
		List<MethodDescriptionNumber> helpmateList =
				helpers.getPrioritiesOfHelpersAndClean(newStatisticsForEachQuery);
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

	private void startComputation(final ProblemWrapper problemWrp,
			final IslandModelConfiguration islandModel, MethodIDs methodIDs) throws Exception {
		
		if (this.state != CompAgentState.INITIALIZED) {
			throw new Exception("Agent is not initialized");
		}
		
		// initialization of structures
		this.readyToSendIndividuals = (IReadyToSendIndividualsModel)islandModel
				.exportReadyToSendIndividualsModel(getLogger()).newInstance();
		this.readyToSendIndividualsInserter = (IReadyToSendIndividualsInserter) islandModel
				.exportReadyToSendIndividualInserter(getLogger()).newInstance();
		this.readyToSendIndividualsInserter.init(this.readyToSendIndividuals);
		
		this.receivedIndividuals = (IReceivedIndividualsModel)islandModel
				.exportReceivedIndividualsModel(getLogger()).newInstance();
		this.receivedIndividualSelector = (IReceivedIndividualSelector) islandModel
				.exportReceivedIndividualSelector(getLogger()).newInstance();
		this.receivedIndividualSelector.init(this.receivedIndividuals);
		
		
		IProblem problem = problemWrp.getProblem();
		ProblemToolDefinition problemToolDef = problemWrp.getProblemToolDefinition();
		MethodDescription methodDescr = new MethodDescription(agentConf, methodIDs, problem, problemToolDef);
		
		// adding cyclic behavior for sending Individual to distribution
		CompAgentIndivDistributionBehavior indivDistributionBehavior = 
				new CompAgentIndivDistributionBehavior(
						this, readyToSendIndividuals, methodDescr,
						problemWrp, islandModel, getLogger());
		
		this.addBehaviour(indivDistributionBehavior);
		
		
		// starts thread where is running computation
		this.computingThread = new ComputingThread(this, problemWrp, islandModel, agentConf, methodIDs);	
		this.computingThread.start();

	}
	
	protected void commitSuicide() {
		
		getLogger().log(Level.INFO, "Waiting for killing himself");

		ManagerAgentService.sendKillAgent(this, getAID(), getLogger());
	}
	
	private MethodDescription getMethodDescription() {

		IProblem problem = null;
		ProblemToolDefinition problemToolDef = null;
		
		if (computingThread != null) {
			
			problem = computingThread.getProblemDefinition();
			problemToolDef = computingThread.getProblemToolDefinition();			
		}
		
		try {
			return new MethodDescription(agentConf, methodIDs, problem, problemToolDef);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected void processIndividualFromInitGeneration(IndividualEvaluated individualEval,
			long generationNumber, IProblem problem, JobID jobID) {
		
		if (individualEval == null || ! individualEval.valid(getLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		if (generationNumber != -1) {
			throw new IllegalArgumentException("Argument " +
					Long.class.getSimpleName() + " is not valid");

		}
		
		// log in local file
		String resultLog = "Generation " + generationNumber + ": " + individualEval.getFitness();
		getLogger().log(Level.INFO, resultLog);
				
		MethodDescription methodDescript = getMethodDescription();
		
		IndividualWrapper computedIndividualWrp =
				new IndividualWrapper(jobID, methodDescript, individualEval);

		this.bestIndividualModel.update(computedIndividualWrp, problem,
				getCALogger());
		
		//log fitness as result
//		if (DEBUG) {
//			localSaver.logComputedFitnessResult(individualEval, generationNumber);
//		}
		
		// log individual as solution
//		if (DEBUG) {
//			localSaver.logSolution(individualEval);
//		}
	}

	protected void processComputedIndividual(IndividualEvaluated individualEval ,
			long generationNumber, JobID jobID, IProblem problem,
			MethodDescription methodDescript, LocalSaver localSaver) {
		
		if (generationNumber == -1) {
			throw new IllegalStateException();
		}

		// log in local file
		String resultLog = "Generation " + generationNumber + ": " + individualEval.getFitness();
		getLogger().log(Level.INFO, resultLog);
		
		IndividualWrapper computedIndividualWrp =
				new IndividualWrapper(jobID, methodDescript, individualEval);
		

		// log only better individuals
		if (bestIndividualModel.update(computedIndividualWrp, problem, getLogger())) {
	
			//log fitness as result
//			if (DEBUG) {
//				localSaver.logComputedFitnessResult(individualEval, generationNumber);
//			}
	
			// log individual as solution
//			if (DEBUG) {
//				localSaver.logSolution(individualEval);
//			}
		}
	}
		
	/**
	 * save, send to DataManager and log received individual
	 * @param receivedIndividual
	 * @param receivedFitness
	 * @param generationNumber
	 * @param problem
	 */
	protected void processRecievedIndividual(IndividualEvaluated currentIndiv,
			IndividualWrapper receivedIndividualWrp, long generationNumber,
			IProblem problem, LocalSaver localSaver) {
		
//		if (DEBUG) {
//			localSaver.logDiffImprovementOfDistribution(receivedIndividualWrp,
//					generationNumber, currentIndiv);
//		}
	}
	
}
