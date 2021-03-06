package org.distributedea.agents.systemagents.centralmanager.structures.job;

import jade.content.Concept;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.preprocessing.PreProcessing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.structures.comparators.CmpJob;

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

	private List<PreProcessing> preProcessings;
	private List<PostProcessing> postProcessings;
	
	
	/**
	 * Constructor
	 */
	public Batch() {
		this.jobs = new ArrayList<>();
		this.preProcessings = new ArrayList<>();
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

	public List<PreProcessing> getPreProcessings() {
		return preProcessings;
	}
	public void setPreProcessings(List<PreProcessing> preProcessings) {
		this.preProcessings = preProcessings;
	}
	public void addPreProcessings(PreProcessing preProcessing) {
		if (preProcessing == null ||
				! preProcessing.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PreProcessing.class.getSimpleName() + " is not valid");
		}
		
		if (this.preProcessings == null) {
			this.preProcessings = new ArrayList<>();
		}
		
		this.preProcessings.add(preProcessing);
	}
	
	public List<PostProcessing> getPostProcessings() {
		return postProcessings;
	}
	public void setPostProcessings(List<PostProcessing> postProcessings) {
		this.postProcessings = postProcessings;
	}
	public void addPostProcessings(PostProcessing postProcessing) {
		if (postProcessing == null ||
				! postProcessing.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PostProcessing.class.getSimpleName() + " is not valid");
		}
		
		if (this.postProcessings == null) {
			this.postProcessings = new ArrayList<>();
		}
		
		this.postProcessings.add(postProcessing);
	}

	public IslandModelConfiguration exportIslandModelConfiguration() {
		if (jobs == null || jobs.isEmpty()) {
			return null;
		}
		return getJobs().get(0).getIslandModelConfiguration();
	}
	public List<String> exportDescriptions() {

		List<String> list = new ArrayList<>();
		
		for (Job jobI : jobs) {
			
			String descriptionI = jobI.getDescription();
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

		List<PreProcessing> preProcessings = new ArrayList<>();
		List<Job> jobs = new ArrayList<>();
		List<PostProcessing> postProcessings = new ArrayList<>();
		
		for (File fileI : batchDir.listFiles()) {
			if (!fileI.isFile()) {
				continue;
			}
			if (fileI.getName().endsWith(FileNames.PREPROCESSING_SUFIX)) {				
				preProcessings.add(PreProcessing.importXML(fileI));
				
			} else if(fileI.getName().endsWith(FileNames.JOB_SUFIX)){
				jobs.add(Job.importXML(fileI));
				
			} else if(fileI.getName().endsWith(FileNames.POSTPROCESSING_SUFIX)) {
				postProcessings.add(PostProcessing.importXML(fileI));
			}
		}
		
		Batch batch = new Batch();
		batch.setBatchID(batchDir.getName());
		batch.setDescription(descriptionI);
		batch.setPreProcessings(preProcessings);
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

		// exporting pre-processings
		for (PreProcessing preProcI : preProcessings) {
			String preProcDirectoryI = batchDir.getAbsolutePath() +
					File.separator + preProcI.getClass().getSimpleName() + "." +
					FileNames.PREPROCESSING_SUFIX;			
			preProcI.exportXML(new File(preProcDirectoryI));
		}

		// exporting post-processings
		for (PostProcessing postProcI : postProcessings) {
			String postProcDirectoryI = batchDir.getAbsolutePath() +
					File.separator + postProcI.getClass().getSimpleName() + "." +
					FileNames.POSTPROCESSING_SUFIX;			
			postProcI.exportXML(new File(postProcDirectoryI));
		}

	}
	
	public void sortJobsByID() {
		
		Collections.sort(jobs, new CmpJob());
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
		
		if (preProcessings == null) {
			return false;
		}
		for (PreProcessing preI : preProcessings) {
			if (preI == null || ! preI.valid(logger)) {
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
