package org.distributedea.agents.computingagents;

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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.agents.computingagents.computingagent.logging.AgentComputingLogger;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.results.PartResult;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
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
	
	// best result of computing (Individual and fitness)
	private ResultOfComputing bestResultOfComputing = null;
	
	// set of received Individuals from distribution
	protected List<Individual> receivedIndividuals =
			Collections.synchronizedList(new ArrayList<Individual>());

	protected Thread thread = null;

	/**
	 * Specifies whether the agent can solve the Problem using a given
	 * Individual representation
	 * @param problem
	 * @param representation
	 * @return
	 */
	protected abstract boolean isAbleToSolve(Class<?> problem, Class<?> representation);
	
	/**
	 * Starts computing a given Problem
	 * @param problem
	 * @param behaviour
	 */
	public abstract void startComputing(Problem problem, Behaviour behaviour) throws ProblemToolException;
	
	/**
	 * Prepares for the killing
	 */
	public abstract void prepareToDie();
	
	
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
	
	public ResultOfComputing getBestresultOfComputing() {
		return bestResultOfComputing;
	}
	public void setBestresultOfComputing(ResultOfComputing resultOfComputing) {
		this.bestResultOfComputing = resultOfComputing;
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
					
					if (action.getAction() instanceof Individual) {
						getLogger().log(Level.INFO, "Inform with Individual");
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
		
		Individual individual = (Individual)action.getAction();
		if(! individual.validation()) {
			individual.validation();
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		synchronized(receivedIndividuals) {
			receivedIndividuals.add(individual);
		}
	}
	
	protected Individual getRecievedIndividual() {
		
		synchronized(receivedIndividuals) {
			if (! receivedIndividuals.isEmpty()) {
				return receivedIndividuals.remove(0);
			}
		}
		return null;
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
		ResultOfComputing resultOfComputing = getBestresultOfComputing();
		while (resultOfComputing == null) {
			getLogger().log(Level.INFO, "resultOfComputing is null");
			resultOfComputing = getBestresultOfComputing();
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

	public class ComputingRunnable implements Runnable {

		private Agent_ComputingAgent agent;
		private Problem problem;
		
		public ComputingRunnable(Agent_ComputingAgent agent, Problem problem) {
			this.agent = agent;
			this.problem = problem;
		}
		
		@Override
		public void run() {
			
			try {
				agent.startComputing(problem, null);
			} catch (ProblemToolException e) {
				this.agent.getLogger().logThrowable("Error in the ProblemTool", e);
				this.agent.commitSuicide();
			}
			
		}}
	
	private ACLMessage respondToStartComputing(ACLMessage request, Action action) {
		
		StartComputing startComputing = (StartComputing) action.getAction();
		final Problem problem = startComputing.getProblem();
		
		if (! isAbleToSolve(problem)) {

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("KO");
			
			return reply;
		}
		
		
		Runnable myRunnable = new ComputingRunnable(this, problem);

		thread = new Thread(myRunnable);		
		thread.start();

			
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	protected boolean isAbleToSolve(Problem problem) {
		
		// tests Problem validation
		if (! problem.testIsValid(getLogger()) ) {
			return false;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), getLogger());
	
		// check if this agent is able to solve this type of problem
		if (! isAbleToSolve(problem.getClass(),
				problemTool.reprezentationWhichUses()) ) {
			return false;
		}
		
		return true;
	}
	
	private ACLMessage respondToPrepareYourselfToKill(ACLMessage request, Action action) {
		
		prepareToDie();
		
		return null;
		
	}
	

	protected void commitSuicide() {
		
		getLogger().log(Level.INFO, "Waiting for killing himself");

		ManagerAgentService.sendKillAgent(this, getAID(), getLogger());
	}
	
	private long timeOfLastLogMs = System.currentTimeMillis();
	protected void logResultByUsingDatamanager(long generationNumber, double fitnessValue) {
				
		PartResult result = new PartResult();
		result.setGenerationNumber(generationNumber);
		result.setFitnessResult(fitnessValue);
		
		long nowMs = System.currentTimeMillis();
		if (timeOfLastLogMs + Configuration.LOG_PERIOD_MS < nowMs) {
			
			result.setAgentDescription("" + this.getLocalName());
			DataManagerService.sendPartResultMessage(this, result, getLogger());
			
			timeOfLastLogMs = nowMs;
		}
		
	}
	
	private long timeOfLastIndividualDistributionMs = System.currentTimeMillis();
	protected void distributeIndividualToNeighours(Individual individual) {
		
		if (individual == null) {
			return;
		}
		
		if (! individual.validation()) {
			throw new IllegalStateException("Individual to distribution is not valid");
		}
		
		long nowMs = System.currentTimeMillis();
		if (timeOfLastIndividualDistributionMs +
				Configuration.INDIVIDUAL_BROADCAST_PERIOD_MS < nowMs) {
			
			ComputingAgentService.sendIndividualToNeighbours(this, individual, getLogger());
			
			timeOfLastIndividualDistributionMs = nowMs;
		}
	}
	
	protected void processIndividualFromInitGeneration(Individual individual,
			double fitness, long generationNumber, Problem problem) {
		
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
		resultOfComputingNew.setBestIndividual(individual);
		resultOfComputingNew.setFitnessValue(fitness);
		
		setBestresultOfComputing(resultOfComputingNew);
		
		
		// send individual description to Agent DataManager
		logResultByUsingDatamanager(generationNumber, fitness);
		
		
		// log individual as best result
		getCALogger().logBestResult(individual, fitness);
	}

	protected void processComputedIndividual(Individual individual,
			double fitness, long generationNumber, Problem problem) {

		if (generationNumber == -1) {
			throw new IllegalStateException();
		}
		
		// log in local file
		String resultLog = "Generation " +
				generationNumber + ": " +
				fitness;
		getLogger().log(Level.INFO, resultLog);
		
		// update saved best result of computing
		double fitnessSavedAsBest = getBestresultOfComputing().getFitnessValue();
		boolean isNewIndividualBetter =
				ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitness, fitnessSavedAsBest, problem);

		if (isNewIndividualBetter) {
		
			ResultOfComputing resultOfComputingNew = new ResultOfComputing();
			resultOfComputingNew.setBestIndividual(individual);
			resultOfComputingNew.setFitnessValue(fitness);
			
			setBestresultOfComputing(resultOfComputingNew);
			
			
			// send individual description to Agent DataManager
			logResultByUsingDatamanager(generationNumber, fitness);

			
			// log individual as best result
			getCALogger().logBestResult(individual, fitness);
						
		}
		
	}
	
	
	/**
	 * save, send to DataManager and log received individual
	 * @param receivedIndividual
	 * @param receivedFitness
	 * @param generationNumber
	 * @param problem
	 */
	protected void processRecievedIndividual(Individual receivedIndividual,
			double receivedFitness, long generationNumber, Problem problem) {
		
		ResultOfComputing resultOfComputing = getBestresultOfComputing();
		double bestFitness = resultOfComputing.getFitnessValue();
		
		boolean isReceivedIndividualBetter =
				ProblemToolEvaluation.isFistFitnessBetterThanSecond(
						receivedFitness, bestFitness, problem);
		
		if (isReceivedIndividualBetter) {
			
			ResultOfComputing resultOfComputingNew = new ResultOfComputing();
			resultOfComputingNew.setBestIndividual(receivedIndividual);
			resultOfComputingNew.setFitnessValue(receivedFitness);
			
			setBestresultOfComputing(resultOfComputingNew);
			
			
			double fitnessImprovement = Math.abs(receivedFitness -bestFitness);
			
			getCALogger().logDiffImprovementOfDistribution(fitnessImprovement, generationNumber);
		}

	}
	
}
