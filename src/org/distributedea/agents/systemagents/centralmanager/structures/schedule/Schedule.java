package org.distributedea.agents.systemagents.centralmanager.structures.schedule;

import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;

/**
 * Structure represents {@link InputPlan} and next candidates to create
 * in next iterations.
 * @author stepan
 *
 */
public class Schedule {

	private InputPlan inputPlan;
	private InputAgentDescriptions nextCandidates;
	
	/**
	 * Constructor
	 * @param inputPlan
	 */
	public Schedule(InputPlan inputPlan) {
		if (inputPlan == null) {
			throw new IllegalArgumentException("Argument " +
					InputPlan.class.getSimpleName() + " can not be null");
		}
		this.inputPlan = inputPlan;
		this.nextCandidates = new InputAgentDescriptions();
	}
	
	/**
	 * Constructor
	 * @param inputPlan
	 * @param nextCandidates
	 */
	public Schedule(InputPlan inputPlan, InputAgentDescriptions nextCandidates) {
		if (inputPlan == null) {
			throw new IllegalArgumentException("Argument " +
					InputPlan.class.getSimpleName() + " can not be null");
		}
		if (nextCandidates == null || ! nextCandidates.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescriptions.class.getSimpleName() + " can not be null");
		}
		this.inputPlan = inputPlan;
		this.nextCandidates = nextCandidates;
	}

	public InputPlan getInputPlan() {
		return inputPlan;
	}

	public InputAgentDescriptions getNextCandidates() {
		return nextCandidates;
	}	
	
}
