package org.distributedea.agents.systemagents.centralmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class InputJobQueue {

	public static List<Batch> getInputBatches() throws FileNotFoundException {
		
		File folder = new File(Configuration.getDirectoryOfInputBatches());
		File[] listOfFiles = folder.listFiles();
		
		List<String> batchIDs = new ArrayList<>();

		for (File fileI : listOfFiles) {
			if (fileI.isDirectory()) {
				String batchID = fileI.getName();
				batchIDs.add(batchID);
			}
		}
		
		List<Batch> batches = new ArrayList<>();
		
		for (String batchIDI : batchIDs) {
				
			List<JobWrapper> jobsI = getInputJobsWrappers(batchIDI);
			
			Batch batchI = new Batch();
			batchI.setBatchID(batchIDI);
			batchI.setJobWrappers(jobsI);
			
			batches.add(batchI);

		}
		
		return batches;
	}
	
	public static List<JobWrapper> getInputJobsWrappers(String batchID) throws FileNotFoundException {
		
		List<JobWrapper> jobs = new ArrayList<JobWrapper>();
				
		File folder = new File(Configuration.getInputBatchDirectory(batchID));
		File[] listOfFiles = folder.listFiles();

		for (File fileI : listOfFiles) {
			if (fileI.isFile() && fileI.getName().endsWith(Configuration.JOB_SUFIX)) {
				System.out.println("File " + fileI.getName());
				JobWrapper jobI = JobWrapper.importXML(fileI);
				jobs.add(jobI);
			}
		}
		
		return jobs;
	}

	public static void cleanJobsInQueueDirectory() {
	
		// removing old batches in the input queue
		File folder = new File(Configuration.getDirectoryOfInputBatches());
		File[] listOfFiles = folder.listFiles();

		for (File fileI : listOfFiles) {
			System.out.println("File " + fileI.getName() + " deleted");
			fileI.delete();
		}
		
	}

	
	public static void exportBatchesToJobQueueDirectory(List<Batch> batches) throws FileNotFoundException, JAXBException {
		
		for (Batch batchI : batches) {
			
			exportBatchToJobQueueDirectory(batchI);
		}
	}

	public static void exportBatchToJobQueueDirectory(Batch batch) throws FileNotFoundException, JAXBException {
		
		String batchID = batch.getBatchID();
		String jobsDirectory = Configuration.getInputBatchDirectory(batchID);
		
		File dir = new File(jobsDirectory);
		dir.mkdir();
		
		List<JobWrapper> jobWrappers = batch.getJobWrappers();
		
		for (JobWrapper jobWrpI : jobWrappers) {
			
			exportToJobQueueDirectory(jobWrpI, batchID);
		}
		
	}
	
	private static void exportToJobQueueDirectory(JobWrapper jobWrp, String batchID) throws FileNotFoundException, JAXBException {

		String jobID = jobWrp.getJobID();
		
		String jobsDirectory = Configuration.getInputJobFile(batchID, jobID);
		
		jobWrp.exportXML(jobsDirectory);
		
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		
		cleanJobsInQueueDirectory();
	}
}
