package org.distributedea.agents.systemagents.centralmanager.structures.plan;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;


/**
 * Represents input for re-planning. Contains new agents to create and
 * currently running agents to be killed in the given {@link Iteration}.
 * @author stepan
 *
 */
public class InputRePlan implements Concept {

	private static final long serialVersionUID = 1L;
	
	private Iteration iteration;
	private MethodDescriptions agentsToKill;
	private Methods agentsToCreate;
	
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
		agentsToKill = new MethodDescriptions();
		agentsToCreate = new Methods();
	}
	
	/**
	 * Constructor for one method to create and one to kill
	 * @param teration
	 * @param agentToKill
	 * @param agentToCreate
	 */
	public InputRePlan(Iteration iteration, MethodDescription agentToKill,
			InputMethodDescription agentToCreate) {
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
	public InputRePlan(Iteration iteration, MethodDescriptions agentsToKill,
			Methods agentsToCreate) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		if (agentsToCreate == null || ! agentsToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Methods.class.getSimpleName() + " is not valid");
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
	public MethodDescriptions getAgentsToKill() {
		return this.agentsToKill;
	}
	@Deprecated
	public void setAgentsToKill(MethodDescriptions agentsToKill) {
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToKill = agentsToKill;
	}
	/**
	 * Adds method to kill
	 * @param agentToKill
	 */
	public void addAgentsToKill(MethodDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
			
		if (this.agentsToKill == null) {
			this.agentsToKill = new MethodDescriptions();
		}
		
		this.agentsToKill.addAgentDescriptions(agentToKill);
	}
	public boolean containsAgentToKill(MethodDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		if (agentsToKill == null) {
			return false;
		}
		return agentsToKill.containsMethodDescription(agentToKill);
	}
	
	/**
	 * Returns agents to create
	 * @return
	 */
	public Methods getAgentsToCreate() {
		return this.agentsToCreate;
	}
	@Deprecated
	public void setAgentsToCreate(Methods agentsToCreate) {
		if (agentsToCreate == null) {
			throw new IllegalArgumentException("Argument " +
					Methods.class.getSimpleName() + " is not valid");
		}
		this.agentsToCreate = agentsToCreate;
	}
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(InputMethodDescription agentToCreate) {
		if (agentToCreate == null ||
				! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " is not valid");
		}
		
		if (this.agentsToCreate == null) {
			this.agentsToCreate = new Methods();
		}
		
		this.agentsToCreate.addMethodDescriptions(agentToCreate);

	}

	public boolean isEmpty() {
		return agentsToKill.isEmpty() && agentsToCreate.isEmpty();
	}
	
	public InputRePlan exportOptimalizedInpuRePlan() {
		
		Methods inputAgentDescriptions =
				agentsToKill.exportInputAgentDescriptions();
		
		Methods intersection =
				inputAgentDescriptions.exportIntersection(agentsToCreate);
		
		MethodDescriptions agentsToKillClone = agentsToKill.deepClone();
		agentsToKillClone.removeAll(intersection);
		
		Methods agentsToCreateClone = agentsToCreate.deepClone();
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
