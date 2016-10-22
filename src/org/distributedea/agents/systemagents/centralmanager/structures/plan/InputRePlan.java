package org.distributedea.agents.systemagents.centralmanager.structures.plan;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;


/**
 * Represents input for re-planning. Contains new agents to create and
 * currently running agents to be killed in the given {@link Iteration}.
 * @author stepan
 *
 */
public class InputRePlan implements Concept {

	private static final long serialVersionUID = 1L;
	
	private Iteration iteration;
	private AgentDescriptions agentsToKill;
	private InputAgentDescriptions agentsToCreate;
	
	@Deprecated
	public InputRePlan() { // only for Jade
	}
	
	/**
	 * Constructor for no change situation
	 * @param teration
	 */
	public InputRePlan(Iteration teration) {
		if (teration == null || ! teration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		iteration = teration;
		agentsToKill = new AgentDescriptions();
		agentsToCreate = new InputAgentDescriptions();
	}
	
	/**
	 * Constructor for one method to create and one to kill
	 * @param teration
	 * @param agentToKill
	 * @param agentToCreate
	 */
	public InputRePlan(Iteration iteration, AgentDescription agentToKill,
			InputAgentDescription agentToCreate) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		addAgentsToKill(agentToKill);
		addAgentsToCreate(agentToCreate);
	}
	
	/**
	 * Constructor
	 * @param teration
	 * @param agentsToKill
	 * @param agentsToCreate
	 */
	public InputRePlan(Iteration iteration, AgentDescriptions agentsToKill,
			InputAgentDescriptions agentsToCreate) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescriptions.class.getSimpleName() + " is not valid");
		}
		if (agentsToCreate == null || ! agentsToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescriptions.class.getSimpleName() + " is not valid");
		}
		
		this.iteration = iteration;
		this.agentsToKill = agentsToKill;
		this.agentsToCreate = agentsToCreate;
	}

	/**
	 * Copy Constructor
	 * @param rePlan
	 */
	public InputRePlan(InputRePlan rePlan) {
		if (rePlan == null || ! rePlan.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputRePlan.class.getSimpleName() + " is not valid");
		}
		
		this.iteration = rePlan.getIteration().deepClone();
		this.agentsToKill = rePlan.getAgentsToKill().deepClone();
		this.agentsToCreate = rePlan.getAgentsToCreate().deepClone();		
	}
	
	/**
	 * Returns {@link Iteration} of current replanning
	 * @return
	 */
	public Iteration getIteration() {
		return this.iteration;
	}
	@Deprecated
	public void setIteration(Iteration iteration) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
	}
	
	/**
	 * Returns methods to kill
	 * @return
	 */
	public AgentDescriptions getAgentsToKill() {
		return this.agentsToKill;
	}
	@Deprecated
	public void setAgentsToKill(AgentDescriptions agentsToKill) {
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToKill = agentsToKill;
	}
	/**
	 * Adds method to kill
	 * @param agentToKill
	 */
	public void addAgentsToKill(AgentDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescriptions.class.getSimpleName() + " is not valid");
		}
			
		if (this.agentsToKill == null) {
			this.agentsToKill = new AgentDescriptions();
		}
		
		this.agentsToKill.addAgentDescriptions(agentToKill);
	}
	public boolean containsAgentToKill(AgentDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescription.class.getSimpleName() + " is not valid");
		}
		if (agentsToKill == null) {
			return false;
		}
		return agentsToKill.containsAgentDescription(agentToKill);
	}
	
	/**
	 * Returns agents to create
	 * @return
	 */
	public InputAgentDescriptions getAgentsToCreate() {
		return this.agentsToCreate;
	}
	@Deprecated
	public void setAgentsToCreate(InputAgentDescriptions agentsToCreate) {
		if (agentsToCreate == null) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToCreate = agentsToCreate;
	}
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(InputAgentDescription agentToCreate) {
		if (agentToCreate == null ||
				! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescription.class.getSimpleName() + " is not valid");
		}
		
		if (this.agentsToCreate == null) {
			this.agentsToCreate = new InputAgentDescriptions();
		}
		
		this.agentsToCreate.addAgentDescriptions(agentToCreate);

	}

	public boolean isEmpty() {
		return agentsToKill.isEmpty() && agentsToCreate.isEmpty();
	}
	
	public InputRePlan exportOptimalizedInpuRePlan() {
		
		InputAgentDescriptions inputAgentDescriptions =
				agentsToKill.exportInputAgentDescriptions();
		
		InputAgentDescriptions intersection =
				inputAgentDescriptions.exportIntersection(agentsToCreate);
		
		AgentDescriptions agentsToKillClone = agentsToKill.deepClone();
		agentsToKillClone.removeAll(intersection);
		
		InputAgentDescriptions agentsToCreateClone = agentsToCreate.deepClone();
		agentsToCreateClone.removeAll(intersection);
		
		return new InputRePlan(iteration.deepClone(),
				agentsToKillClone,
				agentsToCreateClone);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		if (agentsToCreate == null || ! agentsToCreate.valid(logger)) {
			return false;
		}
		if (agentsToKill == null || ! agentsToKill.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public InputRePlan deepClone() {
		return new InputRePlan(this);
	}
}
