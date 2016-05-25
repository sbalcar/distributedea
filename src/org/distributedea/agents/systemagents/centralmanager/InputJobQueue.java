package org.distributedea.agents.systemagents.centralmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.input.PostProcessing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class InputJobQueue {

	public static List<Batch> getInputBatches() throws IOException {
		
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
			
			String descriptionI = getInputBatchDescription(batchIDI);
			List<JobWrapper> jobsI = getInputJobsWrappers(batchIDI);
			
			List<PostProcessing> postProcI = getInputPostProcessings(batchIDI);
			
			Batch batchI = new Batch();
			batchI.setBatchID(batchIDI);
			batchI.setDescription(descriptionI);
			batchI.setJobWrappers(jobsI);
			batchI.setPostProcessings(postProcI);
			
			batches.add(batchI);

		}
		
		return batches;
	}
	
	private static String getInputBatchDescription(String batchID) throws IOException  {
		
		String descriptionFile = Configuration.getBatchDescriptionFile(batchID);
		
		BufferedReader br = new BufferedReader(new FileReader(descriptionFile));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString().trim();
	    } finally {
	        br.close();
	    }
	}
	
	private static List<JobWrapper> getInputJobsWrappers(String batchID) throws FileNotFoundException {
		
		List<JobWrapper> jobs = new ArrayList<>();
				
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
	
	private static List<PostProcessing> getInputPostProcessings(String batchID) throws FileNotFoundException {
		
		List<PostProcessing> postProcessings = new ArrayList<>();
		
		File folder = new File(Configuration.getInputBatchDirectory(batchID));
		File[] listOfFiles = folder.listFiles();

		for (File fileI : listOfFiles) {
			if (fileI.isFile() && fileI.getName().endsWith(Configuration.POSTPROCESSING_SUFIX)) {
				System.out.println("File " + fileI.getName());
				PostProcessing jobI = PostProcessing.importXML(fileI);
				postProcessings.add(jobI);
			}
		}
		
		return postProcessings;
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
		
		expotBatchDescription(batch.getDescription(), batchID);
		
		// exporting jobs
		for (JobWrapper jobWrpI : jobWrappers) {
			
			exportJobToJobQueueDirectory(jobWrpI, batchID);
		}
		
		List<PostProcessing> postProcessings = batch.getPostProcessings();
		// exporting post-processings
		for (PostProcessing postProcI : postProcessings) {
			
			exportPostProcessingToJobQueueDirectory(postProcI, batchID);
		}
		
	}
	
	private static void expotBatchDescription(String batchDescription, String batchID) throws FileNotFoundException {
		
		String descriptionFile = Configuration.getBatchDescriptionFile(batchID);
		try(  PrintWriter out = new PrintWriter(descriptionFile)  ){
		    out.print(batchDescription);
		}
	}
	
	
	private static void exportJobToJobQueueDirectory(JobWrapper jobWrp, String batchID) throws FileNotFoundException, JAXBException {

		String jobID = jobWrp.getJobID();
		
		String jobsDirectory = Configuration.getInputJobFile(batchID, jobID);
		
		jobWrp.exportXML(jobsDirectory);
		
	}
	
	private static void exportPostProcessingToJobQueueDirectory(PostProcessing postProc, String batchID) throws FileNotFoundException, JAXBException {

		String psID = postProc.getClass().getSimpleName();
		
		String postProcsDirectory = Configuration.getPostProcessingFile(batchID, psID);
		
		postProc.exportXML(postProcsDirectory);
		
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		
		cleanJobsInQueueDirectory();
	}
}
