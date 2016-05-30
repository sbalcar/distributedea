package org.distributedea.ontology.job.noontology;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.JobID;


public class Batch implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String batchID;
	private String description;
	
	private List<Job> jobs;

	private List<PostProcessing> postProcessings;
	
	
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	public void addJobWrapper(Job jobWrappers) {
		
		if (this.jobs == null) {
			this.jobs = new ArrayList<>();
		}
		
		this.jobs.add(jobWrappers);
	}
	
	public List<PostProcessing> getPostProcessings() {
		return postProcessings;
	}
	public void setPostProcessings(List<PostProcessing> postProcessings) {
		this.postProcessings = postProcessings;
	}
	public void addPostProcessings(PostProcessing postProcessings) {
		
		if (this.postProcessings == null) {
			this.postProcessings = new ArrayList<>();
		}
		
		this.postProcessings.add(postProcessings);
	}	
	
	public List<JobID> exportJobIDs() {
		
		String batchID = getBatchID();
		
		List<JobID> list = new ArrayList<>();
		
		for (Job jobWrpI : jobs) {
			
			String jobIDStringI = jobWrpI.getJobID();
			
			JobID jobID = new JobID();
			jobID.setBatchID(batchID);
			jobID.setJobID(jobIDStringI);
			
			list.add(jobID);
		}
		
		return list;	
	}
	
	public List<String> exportDescriptions() {

		List<String> list = new ArrayList<>();
		
		for (Job jobWrpI : jobs) {
			
			String descriptionI = jobWrpI.getDescription();
			list.add(descriptionI);
		}
		
		return list;
	}
	
	

	
	public void exportBatchToJobQueueDirectory() throws FileNotFoundException, JAXBException {
		
		String jobsDirectory = Configuration.getBatchDirectory(batchID);
		
		File dir = new File(jobsDirectory);
		dir.mkdir();
		
		expotBatchDescription(description, batchID);
		
		// exporting jobs
		for (Job jobWrpI : jobs) {
			
			exportJobToJobQueueDirectory(jobWrpI, batchID);
		}

		// exporting post-processings
		for (PostProcessing postProcI : postProcessings) {
			
			exportPostProcessingToJobQueueDirectory(postProcI, batchID);
		}
		
	}
	
	private void expotBatchDescription(String batchDescription, String batchID) throws FileNotFoundException {
		
		String descriptionFile = Configuration.getBatchDescriptionFile(batchID);
		try(  PrintWriter out = new PrintWriter(descriptionFile)  ){
		    out.print(batchDescription);
		}
	}
	
	
	private void exportJobToJobQueueDirectory(Job jobWrp, String batchID) throws FileNotFoundException, JAXBException {

		String jobID = jobWrp.getJobID();
		
		String jobsDirectory = Configuration.getJobFile(batchID, jobID);
		
		jobWrp.exportXML(jobsDirectory);
		
	}
	
	private void exportPostProcessingToJobQueueDirectory(PostProcessing postProc, String batchID) throws FileNotFoundException, JAXBException {

		String psID = postProc.getClass().getSimpleName();
		
		String postProcsDirectory = Configuration.getPostProcessingFile(batchID, psID);
		
		postProc.exportXML(postProcsDirectory);
		
	}
	
}
