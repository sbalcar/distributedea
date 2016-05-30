package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.InputJobQueue;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ComputeBatchBehaviour;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ConsoleAutomatBehaviour;
import org.distributedea.agents.systemagents.centralmanager.behaviours.KillAllContainersBehaviour;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.job.noontology.Batch;

public class Agent_CentralManager extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	/**
     * Returns list of all ontologies that are used by CentralManager agent.
     */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		// waiting for initialization of all System Agents
		try {
			Thread.sleep(4 * 1000);
		} catch (Exception e) {
			getLogger().logThrowable("Unable to wait for initialization", e);
			return;
		}

		List<Batch> batches = null;
		try {
			batches = InputJobQueue.getInputBatches();
		} catch (IOException e) {
			getLogger().log(Level.INFO, "Can not load input jobs");
		}
		
		// adding Behaviour for computing
		if (InputConfiguration.automaticStart && batches != null) {

			addToAgentBatchesBehaviour(batches);
			
		} else {
			
			addToAgentConsoleAutomatBehaviour(batches);
		}
		
	}

	private void addToAgentBatchesBehaviour(List<Batch> batches) {

		for (int batchIndexI = 0; batchIndexI < batches.size(); batchIndexI++) {
			
			Batch batchI = batches.get(batchIndexI);
			
			Behaviour behaviourNext = null;
			if (batchIndexI == batches.size() -1) {
				behaviourNext = new KillAllContainersBehaviour(getLogger());
			}
			
			Behaviour behaviourCompI =
					new ComputeBatchBehaviour(batchI, behaviourNext, getLogger());
			
			addBehaviour(behaviourCompI);
		}
	}
	
	private void addToAgentConsoleAutomatBehaviour(List<Batch> batches) {
	
		Behaviour behaviourAutI =
				new ConsoleAutomatBehaviour(batches, getLogger());
		addBehaviour(behaviourAutI);
	}
	
}
