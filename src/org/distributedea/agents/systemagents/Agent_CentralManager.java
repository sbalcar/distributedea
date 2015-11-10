package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.StartComputingBehaviour;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;

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
			Thread.sleep(15 * 1000);
		} catch (Exception e) {
			logger.logThrowable("Unable to wait for initialization", e);
			return;
		}
	
		addBehaviour(new StartComputingBehaviour(logger));
		
	}
	

	
}
