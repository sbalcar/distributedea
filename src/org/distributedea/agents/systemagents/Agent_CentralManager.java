package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ComputeBatchesBehaviour;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ConsoleAutomatBehaviour;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.FileAndConsoleLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.MonitorOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.services.CentralLogerService;
import org.distributedea.services.ManagerAgentService;

/**
 * System-agent who manages all system and computation process
 * @author stepan
 *
 */
public class Agent_CentralManager extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	public List<Behaviour> computingBehaviours = new ArrayList<>();
	
	
	/**
     * Returns list of all ontologies that are used by {@link Agent_CentralManager} agent.
     */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		ontologies.add(MonitorOntology.getInstance());
		
		return ontologies;
	}
	
	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileAndConsoleLogger(this);
		}
		return logger;
	}
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		// waiting for initialization of all System Agents
		while (! areAllServicesAvailable()) {
		}
		
		CentralLogerService.logMessage(this, "Initialization OK", logger);
		
		Batches batches = null;
		try {
			File batchesDir = new File(FileNames.getDirectoryOfInputBatches());
			batches = Batches.importXML(batchesDir);
			
		} catch (Exception e) {
			getLogger().log(Level.INFO, "Can not load input Batches");
			CentralLogerService.logMessage(this, "Input Batches wasn't loaded", getLogger());
			ManagerAgentService.killAllContainers(this, getLogger());
			return;
		}
		
		boolean automaticStart;
		try {
			automaticStart = InputConfiguration.getConf().automaticStart;
		} catch (Exception e) {
			getLogger().log(Level.INFO, "Can not load input configuration - automatic start");
			CentralLogerService.logMessage(this, "Can not load input configuration - automatic start", getLogger());
			ManagerAgentService.killAllContainers(this, getLogger());
			return;
		}
		
		// adding Behaviour for computing
		if (automaticStart) {

			addBehaviour(new ComputeBatchesBehaviour(batches, getLogger()));
		} else {
			
			addBehaviour(new ConsoleAutomatBehaviour(batches, getLogger()));
		}
		
	}
	
	/**
	 * Tests if are available all services for computation
	 * @return
	 */
	private boolean areAllServicesAvailable() {
		
		AID [] managerAgentAIDs = searchDF(
				Agent_ManagerAgent.class.getName());
		if (managerAgentAIDs.length == 0) {
			return false;
		}
		AID [] monitorAIDs = searchDF(
				Agent_DataManager.class.getName());
		if (monitorAIDs.length == 0) {
			return false;
		}
		AID [] dataManagerAIDs = searchDF(
				Agent_Monitor.class.getName());
		if (dataManagerAIDs.length == 0) {
			return false;
		}
		
		return true;
	}
	
	public void exit() {
		
		ManagerAgentService.killAllComputingAgent(this, getLogger());
		
		endsComputationOfAllBatches();
		
		ManagerAgentService.killAllContainers(this, getLogger());
	}
	
	/**
	 * Removes all Computing-Behaviour
	 */
	private void endsComputationOfAllBatches() {
		
		for (Behaviour behaviourI : computingBehaviours) {
			this.removeBehaviour(behaviourI);
		}
	}
}
