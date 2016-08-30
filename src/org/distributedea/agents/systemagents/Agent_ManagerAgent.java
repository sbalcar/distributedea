package org.distributedea.agents.systemagents;

import jade.content.Concept;
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
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.services.ComputingAgentService;

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
	
	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileLogger(this);
		}
		return logger;
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

					Concept concept = action.getAction();
					getLogger().log(Level.INFO, "Request for " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof DescribeNode) {
						// DescribeNode request action
						return respondToDescribeNode(request, action);
						
					} else if (concept instanceof CreateAgent) {
						// CreateAgent request action
						return respondToCreateAgent(request, action);
					
					} else if (concept instanceof KillAgent) {
						// KillAgent request action
						return respondToKillAgent(request, action);
					
					} else if (concept instanceof KillContainer) {
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
		InputAgentConfiguration configuration = createAgent.getConfiguration();

		Class<?> agentType = configuration.exportAgentClass();
		String agentName = configuration.getAgentName();
		
		String s = "Creating agentName " + agentName + " agentType " + agentType.getSimpleName();
		getLogger().log(Level.INFO, s);
		
		AgentConfiguration createdAgentConfiguration =
				createAgent(this, configuration, getLogger());
		
		// to Computing Agent sends required Agent Configuration
		if (configuration.exportIsComputingAgent()) {
		
			ComputingAgentService.sendRequiredAgent(this,
					createdAgentConfiguration.exportAgentAID(),
					createdAgentConfiguration, getLogger());
		}
				
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());

		CreatedAgent createdAgent =
				new CreatedAgent(createdAgentConfiguration);
		
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

		String s = "Killing agentName " + agentNameToKill;
		getLogger().log(Level.INFO, s);
		
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

 		// deregistr agent takes some time
 		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			logger.logThrowable("Error by waiting", e);
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
		
	
		final boolean isAgentOnMainControler =  isAgentOnMainControler(this, getLogger());
		
		Runnable myRunnable = new Runnable(){
			
		     public void run() {

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
			InputAgentConfiguration configuration, IAgentLogger logger) {
	
		// starts another agents
		String containerID = getNumberOfContainer();

		
		AgentConfiguration configurationI = new AgentConfiguration(configuration);
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
	 * @param configuration
	 * @param logger
	 * @return
	 */
	private static AgentConfiguration tryCreateAgent(Agent_DistributedEA agent,
			AgentConfiguration configuration, IAgentLogger logger) {
		
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
		
		Class<?> agentClass = configuration.exportAgentClass();
		String agentName = configuration.exportAgentname();
		Arguments arguments = configuration.getArguments();
		
		Object[] jadeArgs = null;
		if (agentClass == Sniffer.class) {
			
			jadeArgs = Arguments.transformAgrumentsForSniffer(arguments);
		}
		
		// get a container controller
		PlatformController container = agent.getContainerController();
		
		AgentController createdAgent = container.
				createNewAgent(agentName, agentClass.getName(), jadeArgs);
		
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
			IAgentLogger logger) {
		
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
