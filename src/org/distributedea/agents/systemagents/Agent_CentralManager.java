package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.InputJobQueue;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ConsoleAutomatBehaviour;
import org.distributedea.agents.systemagents.centralmanager.behaviours.StartComputingBehaviour;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.job.noontology.JobWrapper;

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

		List<JobWrapper> jobs = null;
		try {
			jobs = InputJobQueue.getInputJobs();
		} catch (FileNotFoundException e) {
			getLogger().log(Level.INFO, "Can not load input jobs");
		}
		
		// adding Behaviour for computing
		if (InputConfiguration.automaticStart) {

			if (jobs != null)
				for (JobWrapper jobI: jobs) {
					getLogger().log(Level.INFO, "Receiving job: " + jobI.getJobID());
					Behaviour behaviourCompI =
							new StartComputingBehaviour(jobI, getLogger());
					Agent_DataManager.createSpaceForJob(jobI.getJobID());
					addBehaviour(behaviourCompI);
				}
			
		} else {
			Behaviour behaviourAutI =
					new ConsoleAutomatBehaviour(jobs, getLogger());
			addBehaviour(behaviourAutI);
		}
	}

	
}
