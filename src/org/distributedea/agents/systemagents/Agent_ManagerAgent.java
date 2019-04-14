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

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.logging.ConsoleLogger;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.agentconfiguration.AgentName;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
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
						respondToKillContainer(request, action);
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
		
		getLogger().log(Level.CONFIG, describeNode.getClass().getSimpleName() + " received");
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ManagementOntology.getInstance().getName());
		
		double coresDouble = Configuration.COUNT_OF_METHODS_ON_CORE * (double) Runtime.getRuntime().availableProcessors();
		int cores = (int) coresDouble;
		
		getLogger().log(Level.CONFIG, "CPU cores: " + cores);
		
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
		InputAgentConfiguration inputAgentConf = createAgent.getConfiguration();
		MethodIDs methodIDs = createAgent.getMethodIDs();
		
		Class<?> agentType = inputAgentConf.exportAgentClass();
		String agentName = inputAgentConf.getAgentName();
		
		String s = "Creating agentName " + agentName + " agentType " + agentType.getSimpleName();
		getLogger().log(Level.INFO, s);
		
		AgentConfiguration createdAgentConfiguration =
				createAgent(this, inputAgentConf, methodIDs, getLogger());
				
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
		ConsoleLogger.log(Level.INFO, "Killing container");

		
		final boolean isAgentOnMainControler =  isAgentOnMainControler(this, getLogger());
		
		Runnable myRunnable = new Runnable(){
			
		     public void run() {

		    	int seconds = 0;
		    	if (isAgentOnMainControler) {
		    		seconds = 30000;
		    	} else {
		    		seconds = 1000;
		    	}
		    	 
		        try {
					Thread.sleep(seconds);
				} catch (InterruptedException e) {
					getLogger().logThrowable("Error by Thread sleep", e);
				}
		        
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					//getLogger().logThrowable("StaleProxyException by killing container", e);
				}

		     }
		   };

		
		Thread thread = new Thread(myRunnable);		
		thread.start();

		
		deregistrDF();
		doSuspend();
		
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
			InputAgentConfiguration inputAgentConf, MethodIDs methodIDs, IAgentLogger logger) {
	
		// starts another agents
		String containerID;
		try {
			containerID = getIDOfContainer();
		} catch (UnknownHostException e) {
			return null;
		}

		AgentName agentName = new AgentName(inputAgentConf.getAgentName());
		agentName.setContainerID(containerID);
		agentName.setNumberOfAgent(0);
		agentName.setNumberOfContainer(0);
		
		AgentConfiguration agentConfI = new AgentConfiguration(inputAgentConf);
		agentConfI.setAgentName(agentName);
		
		AgentConfiguration controller = tryCreateAgent(agent, agentConfI, methodIDs, logger);
		while (controller == null) {
			
			if (agent instanceof Agent_Initiator) {
				agentConfI.incrementNumberOfContainer();
			} else if (agent instanceof Agent_ManagerAgent){
				agentConfI.incrementNumberOfAgent();
			}
			
			controller = tryCreateAgent(agent, agentConfI, methodIDs, logger);
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
			AgentConfiguration agentConf, MethodIDs methodIDs, IAgentLogger logger) {
		
		try {
			return createAndStartAgent(agent, agentConf, methodIDs);
			
		} catch (ControllerException e) {
			return null;
		}
		
	}
	
	/**
	 * Creates and starts Agent
	 * @param agent
	 * @param agentConf
	 * @return
	 * @throws ControllerException
	 */
	private static AgentConfiguration createAndStartAgent(Agent_DistributedEA agent,
			AgentConfiguration agentConf, MethodIDs methodIDs) throws ControllerException {
		
		Class<?> agentClass = agentConf.exportAgentClass();
		AgentName agentName = agentConf.getAgentName();
		String agentNameString = agentConf.exportAgentname();
		Arguments arguments = agentConf.getArguments();
		
		Object[] jadeArgs = null;
		if (agentClass == Sniffer.class) {
			
			jadeArgs = Arguments.transformAgrumentsForSniffer(arguments);
		} else {
			
			Object agentNameArg0 = agentName.exportXML();
			Object[] argumentsArgs = agentConf.getArguments().exportAgrumentsForJade();
			
			Object[] jadeArguments = new Object[argumentsArgs.length + 2];
			jadeArguments[0] = agentNameArg0;
			jadeArguments[1] = methodIDs.getMethodGlobalID();
			System.arraycopy(argumentsArgs, 0, jadeArguments, 2, argumentsArgs.length);
			
			
			jadeArgs = jadeArguments;
		}
		
		// get a container controller
		PlatformController container = agent.getContainerController();
		
		AgentController createdAgent = container.
				createNewAgent(agentNameString, agentClass.getName(), jadeArgs);
		
		createdAgent.start();
		
		// provide agent time to register with DF etc.
		agent.doWait(300);
		
		return agentConf;
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
