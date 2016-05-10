package org.distributedea.agents.systemagents.centralmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.InputTSP;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class InputJobQueue {

	public static List<JobWrapper> getInputJobs() throws FileNotFoundException {
		
		List<JobWrapper> jobs = new ArrayList<JobWrapper>();
				
		File folder = new File(Configuration.getJobsDirectory());
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

	public static void exportJobsToJobQueueDirectory() {
		
		// removing old job-files in the input queue
		File folder = new File(Configuration.getJobsDirectory());
		File[] listOfFiles = folder.listFiles();

		for (File fileI : listOfFiles) {
			if (fileI.isFile() && fileI.getName().endsWith(Configuration.JOB_SUFIX)) {
				System.out.println("File " + fileI.getName() + " deleted");
				fileI.delete();
			}
		}
		
		// selecting jobs for computing
		List<JobWrapper> jobs = new ArrayList<JobWrapper>();		
		//jobs.add(InputTSP.test04());
		
		for (int i = 0; i < 6; i++)
			jobs.add(InputTSP.test05(i));
		
		
		// exporting selected jobs to the input directory
		try {
			exportToJobQueueDirectory(jobs);
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private static void exportToJobQueueDirectory(List<JobWrapper> jobs) throws FileNotFoundException, JAXBException {
		
		for (JobWrapper jobI : jobs) {
			String jobFileI = Configuration.getJobsDirectory() +
					File.separator + jobI.getJobID() + ".job";
			jobI.exportXML(jobFileI);
		}
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		
		exportJobsToJobQueueDirectory();
		JobWrapper job = getInputJobs().get(0);
		System.out.println(job.getScheduler());
	}
}
