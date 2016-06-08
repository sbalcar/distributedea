package org.distributedea.ontology.job;

import jade.content.Concept;

public class JobID implements Concept {

	private static final long serialVersionUID = 1L;

	private String batchID;
	private String jobID;
	private int runNumber;
	
	public JobID() {}
	
	public JobID(String batchName, String jobName, int runNumber) {
		this.batchID = batchName;
		this.jobID = jobName;
		this.runNumber = runNumber;
	}

	public JobID(JobID jobIDStruct) {
		
		setBatchID(jobIDStruct.getBatchID());
		setJobID(jobIDStruct.getJobID());
		setRunNumber(jobIDStruct.getRunNumber());
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

	public int getRunNumber() {
		return runNumber;
	}
	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}
	
	public JobID deepClone() {
		return new JobID(this);
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof JobID)) {
	        return false;
	    }
	    
	    JobID jobIDOuther = (JobID)other;
	    
	    boolean aregBatchIDsEqual =
	    		this.getBatchID().equals(jobIDOuther.getBatchID());
	    boolean aregJobIDsEqual =
	    		this.getJobID().equals(jobIDOuther.getJobID());
	    boolean aregRunNumbersEqual =
	    		this.getRunNumber() == jobIDOuther.getRunNumber();
	    
	    if (aregBatchIDsEqual && aregJobIDsEqual && aregRunNumbersEqual) {
	    	return true;
	    }
	    
	    return false;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return batchID + jobID + runNumber;
	}
}
