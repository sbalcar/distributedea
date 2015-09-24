package org.distributedea.agents.computingagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.UseIndividual;
import org.distributedea.ontology.management.KillHimself;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;

public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	protected volatile Vector<Individual> receivedIndividuals = new Vector<>();
	private ProblemTool problemTool = null;
	private ResultOfComputing bestResultOfComputing = null;


	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		return ontologies;
	}

	@Override
	protected void setup() {
		
		initAgent();
		registrDF();


		MessageTemplate mesTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					
					if (action.getAction() instanceof AccessesResult) {
						
						return respondToAccessesResult(request, action);
						
					} else if (action.getAction() instanceof StartComputing) {
						
						return respondToStartComputing(request, action);
						
					} else if (action.getAction() instanceof KillHimself) {
						
						return respondToKillHimself(request, action);
					
					} else if (action.getAction() instanceof UseIndividual) {
					
						return respondToUseIndividual(request, action);
					}

				} catch (OntologyException e) {
					logger.logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					logger.logThrowable("Codec problem", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}

		});

	}

	protected ACLMessage respondToAccessesResult(ACLMessage request,
			Action action) {

		@SuppressWarnings("unused")
		AccessesResult accessesResult = (AccessesResult) action.getAction();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		ResultOfComputing resultOfComputing = getBestresultOfComputing();
		
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

	private ACLMessage respondToStartComputing(ACLMessage request, Action action) {
		
		StartComputing startComputing = (StartComputing) action.getAction();
		final Problem problem = startComputing.getProblem();
		
		if (! isAbleToSolve(problem)) {

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("KO");
			
			return reply;
		}
		
		addBehaviour(new SimpleBehaviour() {

			private static final long serialVersionUID = 1L;

			private boolean isDone = false;
			
			@Override
			public void action() {
				startComputing(problem);
				isDone = true;
				
			}

			@Override
			public boolean done() {
				return isDone;
			}} );
		
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
	
	private ACLMessage respondToKillHimself(ACLMessage request, Action action) {
		
		prepareToDie();
		
		Runnable myRunnable = new Runnable(){

		     public void run(){
		    	 logger.log(Level.INFO, "Killing himself");
		        
		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
				doDelete();
				
		     }
		   };

		Thread thread = new Thread(myRunnable);
		thread.start();
		   
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
		
	}

	private ACLMessage respondToUseIndividual(ACLMessage request,
			Action action) {
		
		UseIndividual useIndividual = (UseIndividual) action.getAction();
		Individual individual = useIndividual.getIndividual();
		
		receivedIndividuals.add(individual);
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
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
	
	protected abstract boolean isAbleToSolve(Class<?> problem, Class<?> representation);
	public abstract void startComputing(Problem problem);
	public abstract void prepareToDie();
	
	
}
