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
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
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

		MessageTemplate mesTemplateRequest =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mesTemplateRequest) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
			
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					if (action.getAction() instanceof DescribeNode) {
						// DescribeNode request action
						return respondToDescribeNode(request, action);
						
					} else if (action.getAction() instanceof CreateAgent) {
						// CreateAgent request action
						return respondToCreateAgent(request, action);
					
					} else if (action.getAction() instanceof KillAgent) {
						// KillAgent request action
						return respondToKillAgent(request, action);
					
					} else if (action.getAction() instanceof KillContainer) {
						// KillContainer request action
						return respondToKillContainer(request, action);
					}

				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
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
		
		AID [] localComputingAgentAIDs =
				searchLocalContainerDF(Agent_ComputingAgent.class.getName());
		int freeCPUnumber = cores -localComputingAgentAIDs.length;
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setManagerAgentAID(getAID());
		nodeInfo.setTotalCPUNumber(cores);
		nodeInfo.setFreeCPUnumber(freeCPUnumber);
		
		Result result = new Result(action.getAction(), nodeInfo);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending NodeInfo", e);
		} catch (OntologyException e) {
			getLogger().logThrowable(e.getMessage(), e);
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
		AgentConfiguration configuration = createAgent.getConfiguration();

		String agentType = configuration.getAgentType();
		String agentName = configuration.getAgentName();
		
		String s = "Creating agentName " + agentName + " agentType " + agentType;
		getLogger().log(Level.INFO, s);
		
		AgentConfiguration createdAgentConfiguration = createAgent(this, configuration, getLogger());
		
		AID createdAgentAID = new AID(createdAgentConfiguration.exportAgentname(), false);

		// to Computing Agent sends required Agent Configuration
		if (configuration.exportIsComputingAgent()) {
		
			ComputingAgentService.sendRequiredAgent(this, createdAgentAID,
					createdAgentConfiguration, getLogger());
		}
				
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());

		CreatedAgent createdAgent = new CreatedAgent();
		createdAgent.importCreatedAgentName(createdAgentAID);
		
		Result result = new Result(action.getAction(), createdAgent);

		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			getLogger().logThrowable("CodecException by sending NodeInfo", e);
		} catch (OntologyException e) {
			getLogger().logThrowable(e.getMessage(), e);
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
	protected ACLMessage respondToKillAgent(ACLMessage request, Action action) {
		
		KillAgent killAgent = (KillAgent) action.getAction();
		String agentNameToKill = killAgent.getAgentName();

		AgentController agentController = null;
		try {
			agentController =
					getContainerController().getAgent(agentNameToKill);
		} catch (ControllerException e1) {
			getLogger().logThrowable("Error by accessing agent controller", e1);
			return null;
		}
		
		AID aid = new AID(agentNameToKill, false);
		ComputingAgentService.sendPrepareYourselfToKill(this, aid, getLogger());
		
		
		final AgentController agentControllerFinal = agentController;

 		try {
 			agentControllerFinal.kill();
		} catch (StaleProxyException e) {
			getLogger().log(Level.INFO, "Agent had already killed himself");
		}
		
		
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

		getLogger().log(Level.INFO, "Killing container");
		System.out.println("Killing container");
		
		// TODO - kill agents
	
		final boolean isAgentOnMainControler =  isAgentOnMainControler(this, getLogger());
		
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
					getLogger().logThrowable("Error by Thread sleep", e1);
				}
		        
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					getLogger().logThrowable("StaleProxyException by killing container", e);
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
	public static AgentConfiguration createAgent(Agent_DistributedEA agent,
			AgentConfiguration configuration, AgentLogger logger) {
	
		// starts another agents
		int containerID;
		if (agent instanceof Agent_Initiator) {
			Agent_Initiator aIntitiator = (Agent_Initiator) agent;
			containerID = aIntitiator.cutFromHosntameContainerID();
		} else {
			containerID = agent.getNumberOfContainer();
		}

		
		AgentConfiguration configurationI = new AgentConfiguration();
		configurationI.setAgentName(configuration.getAgentName());
		configurationI.setAgentType(configuration.getAgentType());
		configurationI.setArguments(configuration.getArguments());
		configurationI.setContainerID(containerID);
		configurationI.setNumberOfAgent(0);
		configurationI.setNumberOfContainer(0);
		
		AgentConfiguration controller = tryCreateAgent(agent, configurationI, logger);
		while (controller == null) {
			
			if (agent instanceof Agent_Initiator) {
				configurationI.incrementNumberOfContainer();
			} else if (agent instanceof Agent_ManagerAgent){
				configurationI.incrementNumberOfAgent();
			}
			
			controller = tryCreateAgent(agent, configurationI, logger);
		}
		
		return controller;
	}
	
	/**
	 * Tries to create Agent with specific numberOfAgent ID and numberOfContainer ID
	 * @param agent
	 * @param type
	 * @param name
	 * @param numberOfAgent
	 * @param numberOfContainer
	 * @param containerID
	 * @param argumentList
	 * @param logger
	 * @return
	 */
	private static AgentConfiguration tryCreateAgent(Agent_DistributedEA agent,
			AgentConfiguration configuration, AgentLogger logger) {
		
		try {
			return createAndStartAgent(agent, configuration);
			
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
	private static AgentConfiguration createAndStartAgent(Agent_DistributedEA agent,
			AgentConfiguration configuration) throws ControllerException {
		
		String agentType = configuration.getAgentType();
		String agentName = configuration.exportAgentname();
		List<Argument> arguments = configuration.getArguments();
		
		Object[] jadeArgs = null;
		if (agentType.equals(Sniffer.class.getName())) {
			
			jadeArgs = Arguments.transformAgrumentsForSniffer(arguments);
		}
		
		// get a container controller
		PlatformController container = agent.getContainerController();
		
		AgentController createdAgent = container.
				createNewAgent(agentName, agentType, jadeArgs);
		
		createdAgent.start();
		
		// provide agent time to register with DF etc.
		agent.doWait(300);
		
		return configuration;
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
