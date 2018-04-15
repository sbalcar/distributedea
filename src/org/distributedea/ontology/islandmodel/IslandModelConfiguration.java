package org.distributedea.ontology.islandmodel;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;

/**
 * Ontology represents configuration of the island model
 * @author stepan
 *
 */
public class IslandModelConfiguration implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;

	/**
	 * Turns number of neighbours to {@link Individual} distribution
	 */
	private int neighbourCount;
	
	/**
	 * Period of Planner re-planning
	 */
	public long replanPeriodMS;

	/**
	 * Period of sending {@link Individual} from Computing Agent to another Computing Agents
	 */
	public long individualBroadcastPeriodMS;

	
	@Deprecated
	public IslandModelConfiguration() { // only for Jade
		setNeighbourCount(Integer.MAX_VALUE);
	}
	
	/**
	 * Constructor
	 * @param individualDistribution
	 * @param replanPeriodMS
	 * @param individualBroadcastPeriodMS
	 */
	public IslandModelConfiguration(boolean individualDistribution,
			long replanPeriodMS, long individualBroadcastPeriodMS) {
		
		setIndividualDistribution(individualDistribution);
		setNeighbourCount(Integer.MAX_VALUE);
		setReplanPeriodMS(replanPeriodMS);
		setIndividualBroadcastPeriodMS(individualBroadcastPeriodMS);
	}
	
	/**
	 * Constructor
	 * @param individualDistribution
	 * @param neighbourCount
	 * @param replanPeriodMS
	 * @param individualBroadcastPeriodMS
	 */
	public IslandModelConfiguration(boolean individualDistribution,
			int neighbourCount, long replanPeriodMS,
			long individualBroadcastPeriodMS) {
		
		setIndividualDistribution(individualDistribution);
		setNeighbourCount(neighbourCount);
		setReplanPeriodMS(replanPeriodMS);
		setIndividualBroadcastPeriodMS(individualBroadcastPeriodMS);
	}
	
	
	/**
	 * Copy Constructor
	 * @param configuration
	 */
	public IslandModelConfiguration(IslandModelConfiguration configuration) {
		if (configuration == null || ! configuration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		
		setIndividualDistribution(
				configuration.isIndividualDistribution());
		setNeighbourCount(
				configuration.getNeighbourCount());
		setReplanPeriodMS(
				configuration.getReplanPeriodMS());
		setIndividualBroadcastPeriodMS(
				configuration.getIndividualBroadcastPeriodMS());
	}

		
	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	
	public int getNeighbourCount() {
		return neighbourCount;
	}
	public void setNeighbourCount(int neighbourCount) {
		this.neighbourCount = neighbourCount;
	}

	
	public long getReplanPeriodMS() {
		return replanPeriodMS;
	}
	public void setReplanPeriodMS(long replanPeriodMS) {
		
		this.replanPeriodMS = replanPeriodMS;
	}

	public long getIndividualBroadcastPeriodMS() {
		return individualBroadcastPeriodMS;
	}
	public void setIndividualBroadcastPeriodMS(long individualBroadcastPeriodMS) {
		this.individualBroadcastPeriodMS = individualBroadcastPeriodMS;
	}

	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (getReplanPeriodMS() <= 0) {
			return false;
		}
		if (getIndividualBroadcastPeriodMS() <= 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public IslandModelConfiguration deepClone() {
		return new IslandModelConfiguration(this);
	}
}
