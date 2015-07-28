package org.distributedea.agents.systemagents;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

		logInfo("Agent Intitator is starting - " + "AID: " + getAID().getName());

		String fileName = null;
		if (Agent_ManagerAgent.isAgentOnMainControler(this)) {

			fileName = org.distributedea.Configuration.getConfigurationFile();
		} else {

			fileName = org.distributedea.Configuration
					.getConfigurationSlaveFile();
		}

		logInfo("Reading configuration from: " + fileName);

		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(this, fileName);

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
				logSevere("Error in the config file - isn't any Agent Manager");
			}
			if (managerAgentConfigurations.size() > 1) {
				logSevere("Error in the config file - More than one Agent Manager");
			}

			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				logException("Exception by killing container", e);
			}
		}

		AgentConfiguration agentManagerConf = managerAgentConfigurations.get(0);

		String agentType = agentManagerConf.getAgentType();
		String agentName = agentManagerConf.getAgentName();
		List<Argument> arguments = agentManagerConf.getArguments();

		AgentController aManagerAgent = createAgent(this, agentType, agentName,
				new Arguments(arguments));
		

		if (aManagerAgent == null) {

			logSevere("Error by creating agent");
			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				logException("Exception by killing container", e);
			}
		}

		logSevere("-------" + this.getAID().getName() + " "
				+ this.getAID().getLocalName());

		String agentname = null;
		try {
			String fullName = aManagerAgent.getName();
			agentname = fullName.substring(0, fullName.indexOf('@'));
		} catch (StaleProxyException e) {
			logException("Get name by agent", e);
		}

		AID aManagerAgentAID = new AID(agentname, false);

		for (AgentConfiguration configurationI : noManagerAgentConfigurations) {

			String agentTypeI = configurationI.getAgentType();
			String agentNameI = configurationI.getAgentName();
			List<Argument> argumentsI = configurationI.getArguments();

			Boolean result = ManagerAgentService.sendCreateAgent(this,
					aManagerAgentAID, agentTypeI, agentNameI, argumentsI);

			if (!result) {
				logSevere("Error by creating agent");
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					logException("Exception by killing container", e);
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

		return Agent_ManagerAgent.createAgent(this, type, name, null);
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
			logException("df", e);
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