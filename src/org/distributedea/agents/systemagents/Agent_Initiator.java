package org.distributedea.agents.systemagents;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;

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

		getLogger().log(Level.INFO, "Agent Intitator is starting - " + "AID: " + getAID().getName());

		String fileName = null;
		if (Agent_ManagerAgent.isAgentOnMainControler(this, getLogger())) {

			fileName = org.distributedea.Configuration.getConfigurationFile();
		} else {

			fileName = org.distributedea.Configuration
					.getConfigurationSlaveFile();
		}

		getLogger().log(Level.INFO, "Reading configuration from: " + fileName);

		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(fileName, getLogger());

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
				getLogger().log(Level.SEVERE, "Error in the config file - isn't any Agent Manager");
			}
			if (managerAgentConfigurations.size() > 1) {
				getLogger().log(Level.SEVERE, "Error in the config file - More than one Agent Manager");
			}

			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				getLogger().logThrowable("Exception by killing container", e);
			}
		}

		AgentConfiguration agentManagerConf = managerAgentConfigurations.get(0);

		AgentController aManagerAgent = createAgent(this, agentManagerConf);
		

		if (aManagerAgent == null) {

			getLogger().log(Level.SEVERE, "Error by creating agent");
			try {
				getContainerController().kill();
			} catch (StaleProxyException e) {
				getLogger().logThrowable("Exception by killing container", e);
			}
		}

		getLogger().log(Level.INFO, "-------" + this.getAID().getName() + " "
				+ this.getAID().getLocalName());

		String agentname = null;
		try {
			String fullName = aManagerAgent.getName();
			agentname = fullName.substring(0, fullName.indexOf('@'));
		} catch (StaleProxyException e) {
			getLogger().logThrowable("Get name by agent", e);
		}

		AID aManagerAgentAID = new AID(agentname, false);

		for (AgentConfiguration configurationI : noManagerAgentConfigurations) {

			AID result = ManagerAgentService.sendCreateAgent(this,
					aManagerAgentAID, configurationI, getLogger());

			if (result == null) {
				getLogger().log(Level.SEVERE, "Error by creating agent");
				try {
					getContainerController().kill();
				} catch (StaleProxyException e) {
					getLogger().logThrowable("Exception by killing container", e);
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
	public AgentController createAgent(Agent_DistributedEA agent, AgentConfiguration agentManagerConf) {

		return Agent_ManagerAgent.createAgent(this, agentManagerConf, getLogger());
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
			getLogger().logThrowable("df", e);
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