package org.distributedea.agents.systemagents;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.agent.Arguments;

import jade.content.onto.Ontology;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * Agent which is responsible for initialization of agents on node
 * @author balcs7am
 *
 */
public class Agent_Initiator extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());

		return ontologies;
	}

	protected void setup() {

		initAgent();
		// Agent Initiator doesn't have any DF registration

		logger.log(Level.INFO, "Agent Intitator is starting - " + "AID: " + getAID().getName());

		String fileName = null;
		if (Agent_ManagerAgent.isAgentOnMainControler(this, logger)) {

			fileName = org.distributedea.Configuration.getConfigurationFile();
		} else {

			fileName = org.distributedea.Configuration
					.getConfigurationSlaveFile();
		}

		logger.log(Level.INFO, "Reading configuration from: " + fileName);

		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(fileName, logger);

		List<AgentConfiguration> agentConfigurations =
				configuration.getAgentConfigurations();

		List<AgentConfiguration> managerAgentConfigurations =
				new ArrayList<AgentConfiguration>();

		List<AgentConfiguration> noManagerAgentConfigurations =
				new ArrayList<AgentConfiguration>();

		for (AgentConfiguration agentConfigurationI : agentConfigurations) {
			String agentType = agentConfigurationI.getAgentType();

			if (agentType.equals(Agent_ManagerAgent.class.getName())) {
				managerAgentConfigurations.add(agentConfigurationI);
			} else {
				noManagerAgentConfigurations.add(agentConfigurationI);
			}
		}

		if (managerAgentConfigurations.size() != 1) {

			if (managerAgentConfigurations.isEmpty()) {
				logger.log(Level.SEVERE, "Error in the config file - isn't any Agent Manager");
			}
			if (managerAgentConfigurations.size() > 1) {
				logger.log(Level.SEVERE, "Error in the config file - More than one Agent Manager");
			}

			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				logger.logThrowable("Exception by killing container", e);
			}
		}

		AgentConfiguration agentManagerConf = managerAgentConfigurations.get(0);

		String agentType = agentManagerConf.getAgentType();
		String agentName = agentManagerConf.getAgentName();
		List<Argument> arguments = agentManagerConf.getArguments();

		AgentController aManagerAgent = createAgent(this, agentType, agentName,
				new Arguments(arguments));
		

		if (aManagerAgent == null) {

			logger.log(Level.SEVERE, "Error by creating agent");
			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				logger.logThrowable("Exception by killing container", e);
			}
		}

		logger.log(Level.INFO, "-------" + this.getAID().getName() + " "
				+ this.getAID().getLocalName());

		String agentname = null;
		try {
			String fullName = aManagerAgent.getName();
			agentname = fullName.substring(0, fullName.indexOf('@'));
		} catch (StaleProxyException e) {
			logger.logThrowable("Get name by agent", e);
		}

		AID aManagerAgentAID = new AID(agentname, false);

		for (AgentConfiguration configurationI : noManagerAgentConfigurations) {

			String agentTypeI = configurationI.getAgentType();
			String agentNameI = configurationI.getAgentName();
			List<Argument> argumentsI = configurationI.getArguments();

			AID result = ManagerAgentService.sendCreateAgent(this,
					aManagerAgentAID, agentTypeI, agentNameI, argumentsI, logger);

			if (result == null) {
				logger.log(Level.SEVERE, "Error by creating agent");
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					logger.logThrowable("Exception by killing container", e);
				}
			}

		}

		doDelete();

	}

	/**
	 * Creates agent in this container
	 * 
	 * @param type - agent type = name of class
	 * @param name - agent name
	 * @return - confirms creation
	 */
	public AgentController createAgent(Agent_DistributedEA agent, String type,
			String name, Arguments arguments) {

		return Agent_ManagerAgent.createAgent(this, type, name, null, logger);
	}


	/**
	 *  Get from hostname Container ID
	 *  
	 * @return Number as possible suffix for container
	 */
	public String cutFromHosntameContainerID() {

		String containerNumber = "";

		String hosname;
		try {
			hosname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.logThrowable("df", e);
			return null;
		}

		for (int charIndex = 0; charIndex < hosname.length(); charIndex++) {
			char charI = hosname.charAt(charIndex);
			if ('0' <= charI && charI <= '9') {
				containerNumber += charI;
			}
		}

		return containerNumber;
	}

}