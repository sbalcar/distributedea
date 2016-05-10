package org.distributedea.agents.computingagents.computingagent;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.logging.AgentComputingLogger;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionWrapper;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmateList;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.management.EverythingPreparedToBeKilled;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Abstract class of Agent which is inherited by all Computing Agents
 * @author stepan
 *
 */
public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	// logger for Computing Agent
	private AgentLogger logger = null;
	
	// the set of received Individuals from distribution
	protected IndividualModel receivedIndividuals = new IndividualModel();
	
	// computing thread
	protected ComputingThread computingThread = null;

	/**
	 * Specifies whether the agent can solve the Problem using a given
	 * Individual representation
	 * @param problem
	 * @param representation
	 * @return
	 */
	protected abstract boolean isAbleToSolve(ProblemStruct problemStruct);
	
	/**
	 * Starts computing a given Problem
	 * @param problem
	 * @param behaviour
	 */
	protected abstract void startComputing(Problem problem, Class<?> problemTool, String jobID, Behaviour behaviour) throws ProblemToolException;
	
	
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
        sd.setType( Agent_ComputingAgent.class.getName() );
        sd.setName( getLocalName() );
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd );  
        
        } catch (FIPAException fe) {
        	getLogger().logThrowable("Registration faild", fe);
        }
	}
	
	
	public AgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new AgentComputingLogger(this);
		}
		return logger;
	}
	public AgentComputingLogger getCALogger() {
		return (AgentComputingLogger) getLogger();
	}
	
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		
		final MessageTemplate mesTemplateRequest =
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		addBehaviour(new AchieveREResponder(this, mesTemplateRequest) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgRequest) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgRequest);
					
					if (action.getAction() instanceof AccessesResult) {
						getLogger().log(Level.INFO, "Request for AccessesResult");
						return respondToAccessesResult(msgRequest, action);
						
					} else if (action.getAction() instanceof StartComputing) {
						getLogger().log(Level.INFO, "Request for StartComputing");
						return respondToStartComputing(msgRequest, action);
						
					} else if (action.getAction() instanceof ReportHelpmate) {
						getLogger().log(Level.INFO, "Request for StartComputing");
						return respondToReportHelpmate(msgRequest, action);
						
					} else if (action.getAction() instanceof PrepareYourselfToKill) {
						getLogger().log(Level.INFO, "Request for PrepareYourselfToKill");
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
		
		
		final MessageTemplate mesTemplateInform =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(ResultOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateInform) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgInform) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgInform);
					
					if (action.getAction() instanceof IndividualWrapper) {
						getLogger().log(Level.INFO, "Received Individual" );
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

	
	protected void processIndividual(ACLMessage request, Action action) {
		
		IndividualWrapper individualWrapper = (IndividualWrapper)action.getAction();
		Individual individual = individualWrapper.getIndividual();
		
		if(! individual.validation()) {
			individual.validation();
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		receivedIndividuals.add(individualWrapper);
	}
	
	protected IndividualWrapper getRecievedIndividual() {
		
		return receivedIndividuals.getIndividual();		
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
		ResultOfComputing resultOfComputing = computingThread.getBestresultOfComputing();
		
		while (resultOfComputing == null) {
			getLogger().log(Level.INFO, "resultOfComputing is null");
			resultOfComputing = computingThread.getBestresultOfComputing();
		}
		
		Result result = new Result(action.getAction(), resultOfComputing);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending ResultOfComputing", e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending ResultOfComputing", e);
		}

		return reply;
	}


	private ACLMessage respondToStartComputing(ACLMessage request, Action action) {
		
		StartComputing startComputing = (StartComputing) action.getAction();
		final ProblemWrapper problemWrapper = startComputing.getProblemWrapper();
		
		ProblemStruct problemStruct = problemWrapper.exportProblemStruct(logger);
		
		boolean isComputing = (computingThread != null) && computingThread.isAlive();
		if ((! isAbleToSolveProblem(problemStruct)) || isComputing) {

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("KO");
			
			return reply;
		}
		
		this.computingThread = new ComputingThread(this, problemStruct);	
		this.computingThread.start();

			
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	protected boolean isAbleToSolveProblem(ProblemStruct problemStruct) {
		
		// tests Problem validation
		if (! problemStruct.testIsValid(getLogger()) ) {
			return false;
		}
	
		// check if this agent is able to solve this type of problem
		if (! isAbleToSolve(problemStruct) ) {
			return false;
		}
		
		return true;
	}
	
	private ACLMessage respondToPrepareYourselfToKill(ACLMessage request, Action action) {
		
		computingThread.stopComputing();
		
		try {
			computingThread.join();
		} catch (InterruptedException e) {
			getLogger().logThrowable("Error by waiting to end of computing Thread", e);
		}
		
		// deregistres agent from DF
		//  before derestration have to be stop computing(after deregistration
		//  agent can no communicate wit another agents)
		deregistrDF();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		
		EverythingPreparedToBeKilled everythingPrepared = new EverythingPreparedToBeKilled();
		
		Result result = new Result(action.getAction(), everythingPrepared);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending ResultOfComputing", e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending ResultOfComputing", e);
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
		

		AgentDescription description = getAgentDescription(this.computingThread.getProblem());
		
		HelpmateList helpmateList = getHelpmateList(newStatisticsForEachQuery);
		helpmateList.setDescription(description);
		
		Result result = new Result(action.getAction(), helpmateList);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending ResultOfComputing", e);
		} catch (OntologyException e) {
			getLogger().logThrowable("OntologyException by sending ResultOfComputing", e);
		}

		return reply;
	}

	private HelpmateList getHelpmateList(boolean newStatisticsForEachQuery) {
		
		HelpmateList helpmateList = new HelpmateList();
		
		
		if (this.helpers != null) {
			
	        for (Map.Entry<AgentDescription, Integer> entryI: this.helpers.entrySet()) {
				
	        	AgentDescription descriptionI = entryI.getKey();
	        	Integer valueI = entryI.getValue();
	        	
	        	AgentDescriptionWrapper wrapperI = new AgentDescriptionWrapper();
	        	wrapperI.setPriority(valueI);
	        	wrapperI.setDescription(descriptionI);
	        	
	        	helpmateList.addDescription(wrapperI);
	        }
		}

		if (newStatisticsForEachQuery) {
			this.helpers = new HashMap<AgentDescription, Integer>();
		}
		return helpmateList;
	}
	
	
	protected void commitSuicide() {
		
		getLogger().log(Level.INFO, "Waiting for killing himself");

		ManagerAgentService.sendKillAgent(this, getAID(), getLogger());
	}
	

	
	private long timeOfLastIndividualDistributionMs = System.currentTimeMillis();
	protected void distributeIndividualToNeighours(Individual individual, Problem problem, String jobID) {
		
		if (individual == null) {
			return;
		}
		
		if (! individual.validation()) {
			throw new IllegalStateException("Individual to distribution is not valid");
		}
		
		long nowMs = System.currentTimeMillis();
		if (timeOfLastIndividualDistributionMs +
				Configuration.INDIVIDUAL_BROADCAST_PERIOD_MS < nowMs) {
			
			// set description of agent (part of description)
			AgentDescription description = getAgentDescription(problem);
			
			IndividualWrapper individualWrapper = new IndividualWrapper();
			individualWrapper.setJobID(jobID);
			individualWrapper.setAgentDescription(description);
			individualWrapper.setIndividual(individual);
			
			ComputingAgentService.sendIndividualToNeighbours(this, individualWrapper, getLogger());
			
			timeOfLastIndividualDistributionMs = nowMs;
		}
	}
	
	private AgentDescription getAgentDescription(Problem problem) {

		AgentConfiguration agentConfiguration = new AgentConfiguration();
		agentConfiguration.setAgentType(this.getClass().getName());
		agentConfiguration.setAgentName(this.getLocalName());
		
		AgentDescription description = new AgentDescription();
		description.setAgentConfiguration(agentConfiguration);
		description.importProblemToolClass(computingThread.getProblemTool());
		
		return description;
	}
	
	protected void processIndividualFromInitGeneration(Individual individual,
			double fitness, long generationNumber, Problem problem, String jobID) {
		
		if (generationNumber != -1) {
			throw new IllegalStateException();
		}
		
		// log in local file
		String resultLog = "Generation " +
				generationNumber + ": " +
				fitness;
		getLogger().log(Level.INFO, resultLog);
		
		// update saved best result of computing
		ResultOfComputing resultOfComputingNew = new ResultOfComputing();
		resultOfComputingNew.setJobID(jobID);
		resultOfComputingNew.setBestIndividual(individual);
		resultOfComputingNew.setFitnessValue(fitness);
		
		computingThread.setBestresultOfComputing(resultOfComputingNew);
		
		
		// send individual description to Agent DataManager
		//logResultByUsingDatamanager(generationNumber, fitness);
		
		
		// log individual as best result
		getCALogger().logBestSolution(individual, fitness, computingThread.getJobID());
	}

	protected void processComputedIndividual(Individual individual,
			double fitness, long generationNumber, Problem problem) {

		if (generationNumber == -1) {
			throw new IllegalStateException();
		}
		
		//log finess as result
		getCALogger().logComputedResult(fitness, generationNumber, computingThread.getJobID());
		
		
		// update saved best result of computing
		double fitnessSavedAsBest = computingThread.getBestresultOfComputing().getFitnessValue();
		boolean isNewIndividualBetter =
				ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitness, fitnessSavedAsBest, problem);

		if (isNewIndividualBetter) {
		
			ResultOfComputing resultOfComputingNew = new ResultOfComputing();
			resultOfComputingNew.setBestIndividual(individual);
			resultOfComputingNew.setFitnessValue(fitness);
			
			computingThread.setBestresultOfComputing(resultOfComputingNew);
			
			
			// send individual description to Agent DataManager
			//logResultByUsingDatamanager(generationNumber, fitness);

			
			// log individual as best solution
			getCALogger().logBestSolution(individual, fitness, computingThread.getJobID());
			
		}
		
	}
	
	
	Map<AgentDescription, Integer> helpers = new HashMap<AgentDescription, Integer>();
	
	/**
	 * save, send to DataManager and log received individual
	 * @param receivedIndividual
	 * @param receivedFitness
	 * @param generationNumber
	 * @param problem
	 */
	protected void processRecievedIndividual(IndividualWrapper receivedIndividualW,
			double receivedFitness, long generationNumber, Problem problem) {
		
		ResultOfComputing resultOfComputing = computingThread.getBestresultOfComputing();
		double bestFitness = resultOfComputing.getFitnessValue();
		
		boolean isReceivedIndividualBetter =
				ProblemToolEvaluation.isFistFitnessBetterThanSecond(
						receivedFitness, bestFitness, problem);
		
		Individual receivedIndividual = receivedIndividualW.getIndividual();
		AgentDescription description = receivedIndividualW.getAgentDescription();
		
		// put description to the map
		if (helpers.containsKey(description)) {
			int frequency = helpers.get(description);
			helpers.put(description, frequency+1);
		} else  {
			helpers.put(description, 1);
		}
		
		if (isReceivedIndividualBetter) {
			
			ResultOfComputing resultOfComputingNew = new ResultOfComputing();
			resultOfComputingNew.setBestIndividual(receivedIndividual);
			resultOfComputingNew.setFitnessValue(receivedFitness);
			
			computingThread.setBestresultOfComputing(resultOfComputingNew);
			
			String jobID = receivedIndividualW.getJobID();
			double fitnessImprovement = Math.abs(receivedFitness -bestFitness);
			
			getCALogger().logDiffImprovementOfDistribution(fitnessImprovement, generationNumber,
					receivedIndividual, description, jobID);
		}

	}
	
}
