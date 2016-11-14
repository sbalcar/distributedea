package org.distributedea.agents.systemagents;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.services.ManagerAgentService;

import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Profile;
import jade.wrapper.StaleProxyException;

/**
 * Agent which is responsible for initialization of agents on node
 * @author stepan
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

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileLogger(this);
		}
		return logger;
	}
	
	protected void setup() {

		// initialization of logger parameter
		FileNames.mainControllerIP = this.getProperty(Profile.MAIN_HOST, null);
		
		boolean isOnMainControler = Agent_ManagerAgent.isAgentOnMainControler(this, new TrashLogger());
		
		// cleaning log directory
		if (isOnMainControler) {
			try {
				FilesystemInitTool.clearLogDir(new TrashLogger());
			} catch (IOException e) {
				new TrashLogger().logThrowable("Can not clean log directory", e);
				hardKill();
			}
		}
		
		initAgent();
		// Agent Initiator doesn't have any DF registration

		getLogger().log(Level.INFO, "" + Agent_Initiator.class.getSimpleName() +
				" is starting - " + "AID: " + getAID().getName());

		// check java version
		String javaVersion = System.getProperty("java.version");
		if (! javaVersion.contains("1.7")) {
			getLogger().log(Level.INFO, "The system requires java 1.7");
			System.out.println("The system requires java 1.7");
			hardKill();
		}
		
		executeUlimit();
		
		File agentConfFile = null;
		if (isOnMainControler) {
			agentConfFile = new File(FileNames.getConfigurationFile());
		} else {
			agentConfFile = new File(FileNames.getConfigurationSlaveFile());
		}
		initAgents(agentConfFile);
		
		// kill this agent
		doDelete();
	}

	private void executeUlimit() {
		
		String processString = ManagementFactory.getRuntimeMXBean().getName();
		String processPID = processString.substring(0, processString.indexOf("@"));
		
		Runtime rt = Runtime.getRuntime();
		try {
			
			Process pr0 = rt.exec("renice +19 -p " + processPID);
			pr0.waitFor();
//			Process pr1 = rt.exec("ulimit -t unlimited");
//			pr1.waitFor();
			Process pr2 = rt.exec("kinit -R");
			pr2.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void initAgents(File agentConfFile) {
		if (agentConfFile == null || ! agentConfFile.isFile()) {
			getLogger().log(Level.INFO, "File with system agents is not valid");
			hardKill();
		}
		
		getLogger().log(Level.INFO, "Reading configuration from: " + agentConfFile.getName());

		InputAgentConfigurations configurations = InputAgentConfigurations.importFromXML(
				agentConfFile);
		if (configurations == null || ! configurations.valid(logger)) {
			hardKill();
		}

		List<InputAgentConfiguration> managerAgentConfigurations =
				configurations.exportAgentConfigurations(Agent_ManagerAgent.class);
		if (managerAgentConfigurations.isEmpty()) {
			getLogger().log(Level.SEVERE, "Error in the config file - isn't any Agent Manager");
			hardKill();
		}
		if (managerAgentConfigurations.size() > 1) {
			getLogger().log(Level.SEVERE, "Error in the config file - More than one Agent Manager");
			hardKill();
		}

		AgentConfiguration aManagerAgent = Agent_ManagerAgent.createAgent(
				this, managerAgentConfigurations.get(0), getLogger());
		
		if (aManagerAgent == null) {
			getLogger().log(Level.SEVERE, "Error by creating " + Agent_ManagerAgent.class.getSimpleName());
			hardKill();
		}
		
		
		
		List<InputAgentConfiguration> noManagerAgentConfigurations =
				configurations.exportsComplement(managerAgentConfigurations);

		AID aManagerAgentAID = aManagerAgent.exportAgentAID();
		
		for (InputAgentConfiguration configurationI : noManagerAgentConfigurations) {

			AgentConfiguration result =
					ManagerAgentService.sendCreateAgent(this, aManagerAgentAID,
					configurationI, getLogger());

			if (result == null) {
				getLogger().log(Level.SEVERE, "Error by creating agent");
				ManagerAgentService.killAllContainers(this, logger);
				hardKill();
			}

		}

	}
	
	/**
	 * Hard kill local container
	 */
	private void hardKill() {

		try {
			System.out.println("Killing");
			getContainerController().kill();
		} catch (StaleProxyException e) {
			getLogger().logThrowable("Exception by killing container", e);
		}

		//waiting to kill
		while(true) {}
	}

	/**
	 *  Get from hostname Container ID
	 *  
	 * @return Number as possible suffix for container
	 */
	public double cutFromHosntameContainerID() {

		String containerNumber = "";

		String hosname;
		try {
			hosname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			getLogger().logThrowable("df", e);
			return -1;
		}

		for (int charIndex = 0; charIndex < hosname.length(); charIndex++) {
			char charI = hosname.charAt(charIndex);
			if ('0' <= charI && charI <= '9') {
				containerNumber += charI;
			}
		}

		try {
			return Integer.parseInt(containerNumber);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

}