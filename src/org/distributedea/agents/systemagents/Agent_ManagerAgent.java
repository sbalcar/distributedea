package org.distributedea.agents.systemagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.tools.sniffer.Sniffer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.agent.Arguments;

public class Agent_ManagerAgent extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(ManagementOntology.getInstance());
		
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
					 * CreateAgent action
					 */
					if (action.getAction() instanceof CreateAgent) {
						return respondToCreateAgent(request, action);
					}
					if (action.getAction() instanceof KillContainer) {
						return respondToKillContainer(request, action);
					}

				} catch (OntologyException e) {
					logException("Problem extracting content", e);
				} catch (CodecException e) {
					logException("Codec problem", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}

		});
		
	}
	
	/**
	 * Respond to CreateAgent message
	 * 
	 * @param request
	 * @param action
	 * @return
	 * @throws UngroundedException
	 * @throws CodecException
	 * @throws OntologyException
	 */
	protected ACLMessage respondToCreateAgent(ACLMessage request, Action action) throws UngroundedException, CodecException, OntologyException {
		
		Action a = (Action) getContentManager().extractContent(request);
		CreateAgent createAgent = (CreateAgent) a.getAction();

		String agentType = createAgent.getType();
		String agentName = createAgent.getName();
		Arguments arguments = createAgent.getArguments();
		List<Argument> argumentList = arguments.getArguments();
		
		AgentController createdAgent = createAgent(this, agentType, agentName, argumentList);

		ACLMessage reply = request.createReply();
		
		if (createdAgent != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("OK");
			logInfo("Agent " + agentName + " created.");
		} else {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("KO");
			logInfo("Fail by creating agent " + agentName + ".");

		}
		
		return reply;
		
	}
	
	/**
	 * Respond to KillAgent message
	 * 
	 * @param request
	 * @param action
	 * @return
	 */
	protected ACLMessage respondToKillContainer(ACLMessage request,
			Action action) {

		logInfo("Killing container");
		
		// TODO - kill agents
	
		final boolean isAgentOnMainControler =  isAgentOnMainControler(this);
		
		Runnable myRunnable = new Runnable(){
			
		     public void run(){

		    	int seconds = 0;
		    	if (isAgentOnMainControler) {
		    		seconds = 15000;
		    	} else {
		    		seconds = 5000;
		    	}
		    	 
		        try {
					Thread.sleep(seconds);
				} catch (InterruptedException e1) {
					logException("Error by Thread sleep", e1);
				}
		        
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					logException("StaleProxyException by killing container", e);
				}

		     }
		   };

		Thread thread = new Thread(myRunnable);
		
		thread.start();
		   
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}

	
	/**
	 * Creates agent in this container
	 * 
	 * @param type - agent type = name of class
	 * @param name - agent name
	 * @return - confirms creation
	 */
	public static AgentController createAgent(Agent_DistributedEA agent,
			String type, String name, List<Argument> argumentList) {
		
		if (type.isEmpty()) {
			agent.logSevere("Can't create agent with type = null");
			return null;
		}

		if (name.isEmpty()) {
			agent.logSevere("Can't create agent with name = null");
			return null;
		}

		String numberOfContainer = "";
		if (agent instanceof Agent_Initiator) {
			Agent_Initiator aIntitiator = (Agent_Initiator) agent;
			numberOfContainer = aIntitiator.cutFromHosntameContainerID();
		} else {
			numberOfContainer = agent.getNumberOfContainer();
		}

		if (numberOfContainer == null) {
			agent.logSevere("Number of container can't be null");
			return null;
		}
		
		String agentName = name + "-" + numberOfContainer;
		
		// get a container controller
		PlatformController container = agent.getContainerController();

		int numberOfAgent = 0;
		while (true) {
			try {
				
				AgentController createdAgent = null;
								
				if (type.equals(Sniffer.class.getName())) {

					Arguments arguments = new Arguments(argumentList);
					
					String argumet1 = "";					
					for (Argument argumentI : arguments.getArguments()) {
						String agentNameI = argumentI.getValue() + "-" + numberOfContainer;
						argumet1 += agentNameI + "; ";
					}
					argumet1.trim();
					
					Object[] args = null;
					
					//removes the last semicolon
					if (! argumet1.isEmpty()) {
						args = new Object[1];
						args[0] = argumet1.substring(0, argumet1.length() -2);
					}
										
					createdAgent = container.createNewAgent(
							agentName, type, args);
				} else {
					createdAgent = container.createNewAgent(
							agentName, type, null);
					
				}
				
				createdAgent.start();
				
				// provide agent time to register with DF etc.
				agent.doWait(300);
				return createdAgent;
				
			} catch (ControllerException e) {
				char agentChar = (char) ('b' + numberOfAgent);
				agentName += agentChar; 
			}
		}
	}
	
	/**
	 * Is Agent in the Main Container
	 * 
	 * @param agent
	 * @return
	 */
	public static boolean isAgentOnMainControler(Agent_DistributedEA agent) {
		
		String containerName = null;
		try {
			containerName = agent.getContainerController().getContainerName();
		} catch (ControllerException e2) {

			agent.logException("Error by getting name of container", e2);
			try {
				agent.getContainerController().kill();
			} catch (StaleProxyException e) {
				agent.logException("Exception by killing container", e);
			}

		}

		return containerName.equals("Main-Container");

	}
}
