package org.distributedea.ontology.monitor;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;

import jade.content.Concept;

/**
 * Ontology represents statistic of one computational method in the system.
 * @author stepan
 *
 */
public class Statistic implements Concept {

	private static final long serialVersionUID = 1L;

	private JobID jobID;
	
	private MethodStatistics methodStatistics;

	@Deprecated // only for Jade
	public Statistic() {
	}
	
	/**
	 * Constructor
	 * @param jobID
	 */
	public Statistic(JobID jobID, MethodStatistics methodStatistics) {
		this.setJobID(jobID);
		this.setMethodStatistics(methodStatistics);
	}
	
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	public MethodStatistics getMethodStatistics() {
		return methodStatistics;
	}
	@Deprecated
	public void setMethodStatistics(MethodStatistics methodStatistics) {
		this.methodStatistics = methodStatistics;
	}



	public MethodStatistic exportRandomMethodStatistic() {
		return methodStatistics.exportRandomMethodStatistic();
	}
	
	
	
	
	
	public long exportNumberOfImprovementsAchievedByAllMethods() {
		return methodStatistics
				.exportNumberOfImprovementsAchievedByAllMethods();
	}

	public long exportNumberOfGoodMaterialAchievedByAllMethods() {
		return methodStatistics
				.exportNumberOfGoodMaterialAchievedByAllMethods();
	}

	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfImprovement() {
		return methodStatistics
				.exportMethodAchievedTheGreatestQuantityOfImprovement();
	}

	public MethodStatistic exportMethodAchievedTheLeastQuantityOfImprovement() {
		return methodStatistics
				.exportMethodAchievedTheLeastQuantityOfImprovement();
	}
	
	public IndividualHash exportBestIndividual() {
		return methodStatistics
				.exportBestIndividual();
	}
	
	/**
	 * Exports {@link MethodDescriptions} clones
	 * @return
	 */
	public MethodDescriptions exportAgentDescriptions() {
		return methodStatistics
				.exportAgentDescriptions();
	}
	
	/**
	 * Tests if this {@link Statistic} contains all {@link MethodDescription}
	 * from given {@link MethodDescriptions}.
	 * @param agentDescriptions
	 * @return
	 */
	public boolean containsAllAgentDescriptions(MethodDescriptions agentDescriptions) {
		return methodStatistics
				.containsAllAgentDescriptions(agentDescriptions);
	}
	
	/**
	 * Tests if this {@link Statistic} contains given {@link MethodDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsAgentDescriptions(MethodDescription agentDescription) {
		return methodStatistics
				.containsAgentDescriptions(agentDescription);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}
		if (methodStatistics == null || ! methodStatistics.valid(logger)) {
			return false;
		}
		
		return true;
	}
}
