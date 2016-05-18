package org.distributedea.ontology.job;

import jade.content.Concept;

public class JobID implements Concept {

	private static final long serialVersionUID = 1L;

	private String jobID;
	private String batchID;
	
	public JobID() {}
	
	public JobID(String batchName, String jobName) {
		this.batchID = batchName;
		this.jobID = jobName;
	}

	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchName) {
		this.batchID = batchName;
	}
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobName) {
		this.jobID = jobName;
	}
}
