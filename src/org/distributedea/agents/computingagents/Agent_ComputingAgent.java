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
import org.distributedea.problems.ProblemToolValidation;

/**
 * Abstract class of Agent which is inherited by all Computing Agents
 * @author stepan
 *
 */
public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	private ProblemTool problemTool = null;
	private ResultOfComputing bestResultOfComputing = null;
	
	protected List<Individual> receivedIndividuals =
			Collections.synchronizedList(new ArrayList<Individual>());

	protected Thread thread = null;

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		return ontologies;
	}

	/**
	 * Agent DF registration as Computing Agent
	 */
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
        	logger.logThrowable("Registration faild", fe);
        }
	}
	
	@Override
	protected void setup() {
		
		// rewriting by another type of logge
		this.logger = new AgentComputingLogger(this);
		
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
						logger.log(Level.INFO, "Request for AccessesResult");
						return respondToAccessesResult(msgRequest, action);
						
					} else if (action.getAction() instanceof StartComputing) {
						logger.log(Level.INFO, "Request for StartComputing");
						return respondToStartComputing(msgRequest, action);
						
					} else if (action.getAction() instanceof PrepareYourselfToKill) {
						logger.log(Level.INFO, "Request for PrepareYourselfToKill");
						return respondToPrepareYourselfToKill(msgRequest, action);
					}

				} catch (OntologyException e) {
					logger.logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					logger.logThrowable("Codec problem", e);
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
						logger.log(Level.INFO, "Inform with Individual");
						processIndividual(msgInform, action);
					}

				} catch (OntologyException e) {
					logger.logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					logger.logThrowable("Codec problem", e);
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
			logger.log(Level.INFO, "resultOfComputing is null");
			resultOfComputing = getBestresultOfComputing();
		}
		
		Result result = new Result(action.getAction(), resultOfComputing);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by sending ResultOfComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending ResultOfComputing", e);
		}

		return reply;
	}

	public class MyRunnable implements Runnable {

		private Agent_ComputingAgent agent;
		private Problem problem;
		
		public MyRunnable(Agent_ComputingAgent agent, Problem problem) {
			this.agent = agent;
			this.problem = problem;
		}
		
		@Override
		public void run() {
			agent.startComputing(problem, null);
			
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
		
		
		Runnable myRunnable = new MyRunnable(this, problem);

		thread = new Thread(myRunnable);		
		thread.start();

			
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	protected boolean isAbleToSolve(Problem problem) {
		
		// tests Problem validation
		if (! problem.testIsValid(logger) ) {
			return false;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), logger);
	
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
	
	
	protected final ProblemTool getProblemTool() {
		return problemTool;
	}
	protected final void setProblemTool(ProblemTool problemTool) {
		this.problemTool = problemTool;
	}

	public ResultOfComputing getBestresultOfComputing() {
		return bestResultOfComputing;
	}
	public void setBestresultOfComputing(ResultOfComputing resultOfComputing) {
		this.bestResultOfComputing = resultOfComputing;
	}
	
	protected void commitSuicide() {
		
		logger.log(Level.INFO, "Waiting for killing himself");

		ManagerAgentService.sendKillAgent(this, getAID(), logger);
	}
	
	private long timeOfLastLogMs = System.currentTimeMillis();
	protected void logResultByUsingDatamanager(PartResult result) {
		
		String resultLog = "Generation " +
				result.getGenerationNumber() + ": " +
				result.getFitnessResult();
		logger.log(Level.INFO, resultLog);
		
		long nowMs = System.currentTimeMillis();
		if (timeOfLastLogMs + Configuration.LOG_PERIOD_MS < nowMs) {
			
			DataManagerService.sendPartResultMessage(this, result, logger);
			
			timeOfLastLogMs = nowMs;
		}
		
	}
	
	private long timeOfLastIndividualDistributionMs = System.currentTimeMillis();
	protected void distributeIndividualToNeighours(Individual individual) {
		
		if(! individual.validation()) {
			throw new IllegalStateException("Individual to distribution is not valid");
		}
		
		long nowMs = System.currentTimeMillis();
		if (timeOfLastIndividualDistributionMs +
				Configuration.INDIVIDUAL_BROADCAST_PERIOD_MS < nowMs) {
			
			ComputingAgentService.sendIndividualToNeighbours(this, individual, logger);
			
			timeOfLastIndividualDistributionMs = nowMs;
		}
	}

	protected abstract boolean isAbleToSolve(Class<?> problem, Class<?> representation);
	public abstract void startComputing(Problem problem, Behaviour behaviour);
	public abstract void prepareToDie();
	
}
