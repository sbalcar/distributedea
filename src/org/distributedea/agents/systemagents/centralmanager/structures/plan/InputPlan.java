package org.distributedea.agents.systemagents.centralmanager.structures.plan;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.PlannedMethodDescription;

public class InputPlan {
	
	private Iteration iteration;
	private List<Pair<AID,PlannedMethodDescription>> schedule;

	/**
	 * Constructor
	 * @param iteration
	 */
	public InputPlan (Iteration iteration) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		this.schedule = new ArrayList<Pair<AID,PlannedMethodDescription>>();
	}
	
	/**
	 * Constructor
	 * @param iteration
	 * @param schedule
	 */
	public InputPlan (Iteration iteration, List<Pair<AID,PlannedMethodDescription>> schedule) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		this.schedule = schedule;
	}

	/**
	 * Constructor
	 * @param manager
	 * @param agent
	 */
	public void add(AID manager, PlannedMethodDescription pMethodDesr) {
		if (manager == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getSimpleName() + " can't be null");
		}
		if (pMethodDesr == null || ! pMethodDesr.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " is not valid");
		}
		
		schedule.add(new Pair<AID, PlannedMethodDescription>(manager, pMethodDesr));
	}
	
	public List<Pair<AID, PlannedMethodDescription>> getSchedule() {
		return schedule;
	}
	
	public Iteration getIteration() {
		return iteration;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		return true;
	}
}
