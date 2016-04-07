package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.log4j.lf5.LogLevel;
import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.ConsoleAutomatBehaviour;
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
		
		boolean isInputValid = InputConfiguration.isValid();
		if (! isInputValid) {
			getLogger().log(Level.WARNING, "No valid input");
			return;
		}
		
		// waiting for initialization of all System Agents
		try {
			Thread.sleep(4 * 1000);
		} catch (Exception e) {
			getLogger().logThrowable("Unable to wait for initialization", e);
			return;
		}
		
		// adding Behaviour for computing
		if (InputConfiguration.automaticStart) {
			addBehaviour(instanceStartComputingBehaviour());
		} else {
			addBehaviour(new ConsoleAutomatBehaviour(getLogger()));
		}
	}
	
	/**
	 * Prepares Behaviour which start Distributed computing
	 * @return Instance of Behaviour, which contains all parameters 
	 */
	public Behaviour instanceStartComputingBehaviour() {
		
		String problemFileName = Configuration.getInputProblemFile();
		
		String methodsFileName = Configuration.getMethodsFile();

		
		Behaviour behaviour = new StartComputingBehaviour(
				InputConfiguration.problemToSolve,
				problemFileName,
				methodsFileName,
				InputConfiguration.availableProblemTools,
				getLogger());
		
		return behaviour;
	}

	
}
