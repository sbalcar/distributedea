package org.distributedea.agents.systemagents.centralmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

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
			List<Job> jobsI = getInputJobsWrappers(batchIDI);
			
			List<PostProcessing> postProcI = getInputPostProcessings(batchIDI);
			
			Batch batchI = new Batch();
			batchI.setBatchID(batchIDI);
			batchI.setDescription(descriptionI);
			batchI.setJobs(jobsI);
			batchI.setPostProcessings(postProcI);
			
			batches.add(batchI);

		}
		
		return batches;
	}
	
	private static String getInputBatchDescription(String batchID) throws IOException  {
		
		String descriptionFile = Configuration.getInputBatchDescriptionFile(batchID);
		
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
	
	private static List<Job> getInputJobsWrappers(String batchID) throws FileNotFoundException {
		
		List<Job> jobs = new ArrayList<>();
				
		File folder = new File(Configuration.getInputBatchDirectory(batchID));
		File[] listOfFiles = folder.listFiles();

		for (File fileI : listOfFiles) {
			if (fileI.isFile() && fileI.getName().endsWith(Configuration.JOB_SUFIX)) {
				System.out.println("File " + fileI.getName());
				Job jobI = Job.importXML(fileI);
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
	
}
