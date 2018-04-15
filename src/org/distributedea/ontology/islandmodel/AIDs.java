package org.distributedea.ontology.islandmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;
import jade.core.AID;

/**
 * Ontology represents {@link AID} {@link List}
 * @author stepan
 *
 */
public class AIDs implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<AID> agentIDs;
	
	@Deprecated
	public AIDs() { // only for Jade
		this.agentIDs = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentIDs
	 */
	public AIDs(List<AID> agentIDs) {
		
		this.setAgentIDs(agentIDs);
	}
	
	/**
	 * Copy Constructor
	 * @param configuration
	 */
	public AIDs(AIDs configuration) {
		if (configuration == null || ! configuration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AIDs.class.getSimpleName() + " is not valid");
		}
		ArrayList<AID> agentIDsClone = new ArrayList<>();
		for (AID aidI : configuration.getAgentIDs()) {
			agentIDsClone.add(aidI);
		}
		this.setAgentIDs(agentIDsClone);
	}
	

	public List<AID> getAgentIDs() {
		return agentIDs;
	}
	public void setAgentIDs(List<AID> agentIDs) {
		if (agentIDs == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.agentIDs = agentIDs;
	}
	
	/**
	 * Return size
	 * @return
	 */
	public int size() {
		return this.agentIDs.size();
	}
	
	/**
	 * Shuffles list of {@link AID}
	 */
	public void shuffle() {
		Collections.shuffle(this.agentIDs);
	}
	
	/**
	 * Exports random {@link AIDs}
	 * @param number
	 * @return
	 */
	public AIDs exportRanomAIDs(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		AIDs thisClone = this.deepClone();
		List<AID> listClone = thisClone.getAgentIDs();
		Collections.shuffle(listClone);
		
		if (listClone.size() <= number) {
			return thisClone;
		}
			
		return new AIDs(listClone.subList(0, number));
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return agentIDs != null;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public AIDs deepClone() {
		return new AIDs(this);
	}
	
}
