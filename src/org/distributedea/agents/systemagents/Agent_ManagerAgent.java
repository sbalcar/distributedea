package org.distributedea.agents.systemagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.FailureException;
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
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.agent.Arguments;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;

/**
 * Agent represents ruler of node
 * @author stepan
 *
 */
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

					if (action.getAction() instanceof DescribeNode) {
						// DescribeNode action
						return respondToDescribeNode(request, action);
						
					} else if (action.getAction() instanceof CreateAgent) {
						// CreateAgent action
						return respondToCreateAgent(request, action);
					
					} else if (action.getAction() instanceof KillAgent) {
						// KillAgent action
						return respondToKillAgent(request, action);
					
					} else if (action.getAction() instanceof KillContainer) {
						
						return respondToKillContainer(request, action);
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
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}
		});
		
	}
	

	/**
	 * Respond to DescribeNode message
	 * 
	 * @param request
	 * @param action
	 * @return
	 * @throws UngroundedException
	 * @throws CodecException
	 * @throws OntologyException
	 */
	protected ACLMessage respondToDescribeNode(ACLMessage request, Action action) {

		@SuppressWarnings("unused")
		DescribeNode describeNode = (DescribeNode) action.getAction();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		int cores = Runtime.getRuntime().availableProcessors();
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setNumberCPU(cores);
		
		Result result = new Result(action.getAction(), nodeInfo);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by sending NodeInfo", e);
		} catch (OntologyException e) {
			logger.logThrowable(e.getMessage(), e);
		}

		return reply;
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
	protected ACLMessage respondToCreateAgent(ACLMessage request, Action action) {

		CreateAgent createAgent = (CreateAgent) action.getAction();

		String agentType = createAgent.getType();
		String agentName = createAgent.getName();
		Arguments arguments = createAgent.getArguments();
		List<Argument> argumentList = arguments.getArguments();
		
		AgentController createdAgent = createAgent(this, agentType, agentName,
				argumentList, logger);

		ACLMessage reply = request.createReply();
		
		if (createdAgent != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("OK");
			logger.log(Level.INFO, "Agent " + agentName + " created.");
		} else {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("KO");
			logger.log(Level.INFO, "Fail by creating agent " + agentName + ".");

		}
		
		return reply;
		
	}
	
	protected ACLMessage respondToKillAgent(ACLMessage request, Action action) {
		
		KillAgent killAgent = (KillAgent) action.getAction();
		String agentNameToKill = killAgent.getAgentName();

		AgentController agentController = null;
		try {
			agentController =
					getContainerController().getAgent(agentNameToKill);
		} catch (ControllerException e1) {
			logger.logThrowable("Error by accessing agent controller", e1);
			return null;
		}
		
		AID aid = new AID(agentNameToKill, false);
		ComputingAgentService.sendPrepareYourselfToKill(this, aid, logger);
		
		
		final String agentNameToKillFinal = agentNameToKill;
		final AgentController agentControllerFinal = agentController;
		
		Runnable myRunnable = new Runnable(){

		     public void run(){
		    	 logger.log(Level.INFO, "Waiting for killing " + agentNameToKillFinal);
		     
		    	 try {
		    		 Thread.sleep(1000);
		    	 } catch (InterruptedException e1) {
					 logger.log(Level.SEVERE, "Can't wait for killing himself");
				 }
		        
		 		try {
		 			agentControllerFinal.kill();
				} catch (StaleProxyException e) {
					logger.log(Level.INFO, "Agent had already killed himself");
				}
				
				logger.log(Level.INFO, "Agent " + agentNameToKillFinal + " was killed");

				
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
	 * Respond to KillAgent message
	 * 
	 * @param request
	 * @param action
	 * @return
	 */
	protected ACLMessage respondToKillContainer(ACLMessage request,
			Action action) {

		logger.log(Level.INFO, "Killing container");
		
		// TODO - kill agents
	
		final boolean isAgentOnMainControler =  isAgentOnMainControler(this, logger);
		
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
					logger.logThrowable("Error by Thread sleep", e1);
				}
		        
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					logger.logThrowable("StaleProxyException by killing container", e);
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
			String type, String name, List<Argument> argumentList, AgentLogger logger) {
		
		String containerID = "";
		if (agent instanceof Agent_Initiator) {
			Agent_Initiator aIntitiator = (Agent_Initiator) agent;
			containerID = aIntitiator.cutFromHosntameContainerID();
		} else {
			containerID = agent.getNumberOfContainer();
		}

		int numberOfAgentI = 0;
		int numberOfContainerI = 0;
		
		AgentController controller = null;
		while (true) {
			
			controller = tryCreateAgent(agent, type, name, numberOfAgentI,
					numberOfContainerI, containerID, argumentList, logger);
			if (controller != null) {
				return controller;
			}
			
			if (agent instanceof Agent_Initiator) {
				numberOfContainerI++;
			} else if (agent instanceof Agent_ManagerAgent){
				numberOfAgentI++;
			} else {
				logger.logThrowable("", null);
			}
		}
		
	}
	
	private static AgentController tryCreateAgent(Agent_DistributedEA agent,
			String type, String name, int numberOfAgent, int numberOfContainer,
			String containerID, List<Argument> argumentList, AgentLogger logger) {
		
		if (type.isEmpty()) {
			logger.log(Level.SEVERE, "Can't create agent with type = null");
			return null;
		}

		if (name.isEmpty()) {
			logger.log(Level.SEVERE, "Can't create agent with name = null");
			return null;
		}

		if (containerID == null) {
			logger.log(Level.SEVERE, "Number of container can't be null");
			return null;
		}
		
		try {
			String  agentChar = "";
			if (numberOfAgent > 0) {
				char aChar = (char) ('a' + numberOfAgent);
				agentChar = "" + AGENT_NUMBER_PREFIX + aChar;
			}
			
			String  containerChar = "";
			if (numberOfContainer > 0) {
				char cChar = (char) ('a' + numberOfContainer);
				containerChar = "" + cChar;
			}
			
			String agentNameWitID = name + agentChar;
			String containerNameWitID = containerID + containerChar;
			
			String agentFullName = agentNameWitID + CONTAINER_NUMBER_PREFIX
					+ containerNameWitID;
			
			AgentController agentController = createAndStartAgent(
					agent, agentFullName, containerNameWitID, type, argumentList);
			return agentController;
			
		} catch (ControllerException e) {
			return null;
		}
		
	}
	
	/**
	 * Creates and starts Agent
	 * 
	 * @param agent
	 * @param agentName
	 * @param numberOfContainer
	 * @param type
	 * @param argumentList
	 * @return
	 * @throws ControllerException
	 */
	private static AgentController createAndStartAgent(Agent_DistributedEA agent,
			String agentName, String numberOfContainer, String type,
			List<Argument> argumentList) throws ControllerException {
		
		// get a container controller
		PlatformController container = agent.getContainerController();
		
		AgentController createdAgent = null;
		
		if (type.equals(Sniffer.class.getName())) {

			Arguments arguments = new Arguments(argumentList);
			
			String argumet1 = "";					
			for (Argument argumentI : arguments.getArguments()) {
				String agentNameI = argumentI.getValue() +
						CONTAINER_NUMBER_PREFIX + numberOfContainer;
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
	}
	
	
	/**
	 * Is Agent in the Main Container
	 * 
	 * @param agent
	 * @return
	 */
	public static boolean isAgentOnMainControler(Agent_DistributedEA agent,
			AgentLogger logger) {
		
		String containerName = null;
		try {
			containerName = agent.getContainerController().getContainerName();
		} catch (ControllerException e2) {

			logger.logThrowable("Error by getting name of container", e2);
			try {
				agent.getContainerController().kill();
			} catch (StaleProxyException e) {
				logger.logThrowable("Exception by killing container", e);
			}

		}

		return containerName.equals("Main-Container");

	}
}
