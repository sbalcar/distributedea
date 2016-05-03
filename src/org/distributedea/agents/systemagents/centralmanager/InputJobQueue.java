package org.distributedea.agents.systemagents.centralmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.InputTSP;
import org.distributedea.ontology.job.Job;

public class InputJobQueue {

	public static List<Job> getInputJobs() throws FileNotFoundException {
		
		List<Job> jobs = new ArrayList<Job>();
				
		File folder = new File(Configuration.getJobsDirectory());
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

	public static void exportJobsToJobQueueDirectory() {
		
		List<Job> jobs = new ArrayList<Job>();		
		jobs.add(InputTSP.test04());
		
		
		try {
			exportToJobQueueDirectory(jobs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void exportToJobQueueDirectory(List<Job> jobs) throws FileNotFoundException {
		
		for (Job jobI : jobs) {
			String jobFileI = Configuration.getJobsDirectory() +
					File.separator + jobI.getJobID() + ".job";
			jobI.exportXML(jobFileI);
		}
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		getInputJobs();
	}
}
