package org.distributedea.agents.computingagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.management.KillHimself;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

public abstract class Agent_ComputingAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

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

					/*
					 * Logging action
					 */
					if (action.getAction() instanceof StartComputing) {
						
						return respondToStartComputing(request, action);
						
					} else if (action.getAction() instanceof KillHimself) {
						
						return respondToKillHimself(request, action);
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

	private ACLMessage respondToStartComputing(ACLMessage request, Action action) {
		
		StartComputing startComputing = (StartComputing) action.getAction();
		final Problem problem = startComputing.getProblem();
		
		addBehaviour(new SimpleBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				startComputing(problem);
				
			}

			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return false;
			}} );
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
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

	public abstract void startComputing(Problem problem);
	public abstract void prepareToDie();
	
	protected ProblemTool instanceProblemTool(String className) {
		
		@SuppressWarnings("rawtypes")
		Class toolClass = null;
		try {
			toolClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.logThrowable(
					"Class of problemTool was not found", e);
		}
		
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) toolClass.newInstance();
		} catch (InstantiationException e) {
			logger.logThrowable(
					"Class of problemTool can't be instanced", e);
		} catch (IllegalAccessException e) {
			logger.logThrowable(
					"Class of problemTool can't be instanced", e);
		}

		return problemTool;
	}
	
}
