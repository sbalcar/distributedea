package org.distributedea.ontology.monitor;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.job.JobID;

import jade.content.AgentAction;

/**
 * Ontology represents request for statistic.
 * @author stepan
 *
 */
public class GetStatistic implements AgentAction {

	private static final long serialVersionUID = 1L;

	private JobID jobID;

	@Deprecated 
	public GetStatistic() {} // only for Jade
	
	/**
	 * Constructor
	 * @param jobID
	 */
	public GetStatistic(JobID jobID) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.jobID = jobID;
	}

	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
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
		return true;
	}
}
