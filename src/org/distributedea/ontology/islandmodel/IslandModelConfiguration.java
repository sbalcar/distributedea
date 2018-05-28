package org.distributedea.ontology.islandmodel;

import jade.content.Concept;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
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
	private long replanPeriodMS;

	/**
	 * Period of sending {@link Individual} from Computing Agent to another Computing Agents
	 */
	private long individualBroadcastPeriodMS;
	
	/**
	 * Specify about type of {@link IReadyToSendIndividualsModel}
	 */
	private String readyToSendIndividualsModelClassName;
	
	/**
	 * Specify about type of {@link IReceivedIndividualsModel}
	 */
	private String receivedIndividualsModelClassName;
	
	
	public IslandModelConfiguration() {
		setNeighbourCount(Integer.MAX_VALUE);
	}
	
	
	/**
	 * Copy Constructor
	 * @param islandModelConf
	 */
	public IslandModelConfiguration(IslandModelConfiguration islandModelConf) {
		if (islandModelConf == null || ! islandModelConf.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		
		setIndividualDistribution(
				islandModelConf.isIndividualDistribution());
		setNeighbourCount(
				islandModelConf.getNeighbourCount());
		setReplanPeriodMS(
				islandModelConf.getReplanPeriodMS());
		setIndividualBroadcastPeriodMS(
				islandModelConf.getIndividualBroadcastPeriodMS());
		importReadyToSendIndividualsModelClass(
				islandModelConf.exportReadyToSendIndividualsModel(new TrashLogger()));
		importReceivedIndividualsModelClass(
				islandModelConf.exportReceivedIndividualsModel(new TrashLogger()));
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
		if (replanPeriodMS <= 0) {
			throw new IllegalArgumentException("Argument " +
					Long.class.getSimpleName() + " is nto valid");
		}		
		this.replanPeriodMS = replanPeriodMS;
	}

	public long getIndividualBroadcastPeriodMS() {
		return individualBroadcastPeriodMS;
	}
	public void setIndividualBroadcastPeriodMS(long individualBroadcastPeriodMS) {
		if (individualBroadcastPeriodMS <= 0) {
			throw new IllegalArgumentException("Argument " +
					Long.class.getSimpleName() + " is nto valid");
		}
		this.individualBroadcastPeriodMS = individualBroadcastPeriodMS;
	}

	@Deprecated
	public String getReadyToSendIndividualsModelClassName() {
		return readyToSendIndividualsModelClassName;
	}
	@Deprecated
	public void setReadyToSendIndividualsModelClassName(
			String readyToSendIndividualsModelClassName) {
		this.readyToSendIndividualsModelClassName = readyToSendIndividualsModelClassName;
	}
	
	@Deprecated
	public String getReceivedIndividualsModelClassName() {
		return receivedIndividualsModelClassName;
	}
	@Deprecated
	public void setReceivedIndividualsModelClassName(
			String receivedIndividualsModelClassName) {
		this.receivedIndividualsModelClassName = receivedIndividualsModelClassName;
	}

	
	/**
	 * Exports {@link IReadyToSendIndividualsModel} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportReadyToSendIndividualsModel(IAgentLogger logger) {
		
		try {
			return Class.forName(getReadyToSendIndividualsModelClassName());
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Import {@link IReadyToSendIndividualsModel} class
	 * @param problemToSolve
	 */
	public void importReadyToSendIndividualsModelClass(Class<?> readyToSendIndividualsModelClassName) {
		if (readyToSendIndividualsModelClassName == null) {
			setReadyToSendIndividualsModelClassName(null);
			return;
		}
		setReadyToSendIndividualsModelClassName(
				readyToSendIndividualsModelClassName.getName());
	}
	

	/**
	 * Exports {@link IReceivedIndividualsModel} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportReceivedIndividualsModel(IAgentLogger logger) {
		
		try {
			return Class.forName(getReceivedIndividualsModelClassName());
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Import {@link IReceivedIndividualsModel} class
	 * @param problemToSolve
	 */
	public void importReceivedIndividualsModelClass(Class<?> receivedIndividualsModel) {
		if (receivedIndividualsModel == null) {
			setReceivedIndividualsModelClassName(null);
			return;
		}
		setReceivedIndividualsModelClassName(
				receivedIndividualsModel.getName());
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
		if (exportReadyToSendIndividualsModel(logger) == null) {
			return false;
		}
		if (exportReceivedIndividualsModel(logger) == null) {
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
