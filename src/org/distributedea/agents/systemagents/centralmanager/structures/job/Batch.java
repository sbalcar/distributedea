package org.distributedea.agents.systemagents.centralmanager.structures.job;

import jade.content.Concept;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.logging.IAgentLogger;

/**
 * Ontology represents one batch. 
 * @author stepan
 *
 */
public class Batch implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String batchID;
	private String description;
	
	private List<Job> jobs;

	private List<PostProcessing> postProcessings;
	
	
	/**
	 * Constructor
	 */
	public Batch() {
		this.jobs = new ArrayList<>();
		this.postProcessings = new ArrayList<>();
	}
	
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
	public void addJob(Job job) {
		
		if (this.jobs == null) {
			this.jobs = new ArrayList<>();
		}
		
		this.jobs.add(job);
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
	
	public List<String> exportDescriptions() {

		List<String> list = new ArrayList<>();
		
		for (Job jobWrpI : jobs) {
			
			String descriptionI = jobWrpI.getDescription();
			list.add(descriptionI);
		}
		
		return list;
	}
	
	/**
	 * Imports {@link Batch} from given directory
	 * @param batchDir
	 * @return
	 * @throws IOException
	 */
	public static Batch importXML(File batchDir) throws Exception {
		if (batchDir == null || ! batchDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		
		String batchDescription = batchDir.getAbsolutePath() + File.separator +
				"description.txt";
		String descriptionI = getInputBatchDescription(new File(batchDescription));

		List<Job> jobs = new ArrayList<>();
		for (File fileI : batchDir.listFiles()) {
			if (fileI.isFile() && fileI.getName().endsWith(FileNames.JOB_SUFIX)) {
				
				Job jobI = Job.importXML(fileI);
				jobs.add(jobI);
			}
		}

		List<PostProcessing> postProcessings = new ArrayList<>();	
		for (File fileI : batchDir.listFiles()) {
			if (fileI.isFile() && fileI.getName().endsWith(FileNames.POSTPROCESSING_SUFIX)) {
				
				PostProcessing jobI = PostProcessing.importXML(fileI);
				postProcessings.add(jobI);
			}
		}

		
		Batch batch = new Batch();
		batch.setBatchID(batchDir.getName());
		batch.setDescription(descriptionI);
		batch.setJobs(jobs);
		batch.setPostProcessings(postProcessings);
		
		return batch;
	}
	
	private static String getInputBatchDescription(File descriptionFile) throws IOException  {
		
		String descriptionFileName = descriptionFile.getAbsolutePath();
		
		BufferedReader br = new BufferedReader(new FileReader(descriptionFileName));
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
	
	/**
	 * Exports {@link Batch} to given directory
	 * @param batchDir
	 * @throws IOException
	 */
	public void exportXML(File batchDir) throws Exception {
		if (batchDir == null || ! batchDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String descriptionFile = batchDir.getAbsolutePath() + File.separator +
				"description.txt";
		try(  PrintWriter out = new PrintWriter(descriptionFile)  ){
		    out.print(description);
		}
		
		// exporting jobs
		for (Job jobWrpI : jobs) {
			String jobDirectoryI = batchDir.getAbsolutePath() + File.separator +
					jobWrpI.getJobID() + "." + FileNames.JOB_SUFIX;
			jobWrpI.exportXML(new File(jobDirectoryI));
		}

		// exporting post-processings
		for (PostProcessing postProcI : postProcessings) {
			String postProcDirectoryI = batchDir.getAbsolutePath() +
					File.separator + postProcI.getClass().getSimpleName() + "." +
					FileNames.POSTPROCESSING_SUFIX;			
			postProcI.exportXML(new File(postProcDirectoryI));
		}
		
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (batchID == null || batchID.isEmpty()) {
			return false;
		}
		if (description == null || description.isEmpty()) {
			return false;
		}
		if (jobs == null) {
			return false;
		}
		for (Job jobI : jobs) {
			if (jobI == null || ! jobI.valid(logger)) {
				return false;
			}
		}
		if (postProcessings == null) {
			return false;
		}
		for (PostProcessing postI : postProcessings) {
			if (postI == null || ! postI.valid(logger)) {
				return false;
			}
		}

		return true;
	}
}
