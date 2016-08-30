package org.distributedea.agents.systemagents.centralmanager.structures.plan;

import jade.core.AID;

import java.util.List;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.iteration.Iteration;

public class InputPlan {
	
	private Iteration iteration;
	private List<Pair<AID,InputAgentDescription>> schedule;

	/**
	 * Constructor
	 * @param iteration
	 * @param schedule
	 */
	public InputPlan (Iteration iteration, List<Pair<AID,InputAgentDescription>> schedule) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		this.schedule = schedule;
	}

	public List<Pair<AID, InputAgentDescription>> getSchedule() {
		return schedule;
	}
	
	public Iteration getIteration() {
		return iteration;
	}
	
}
