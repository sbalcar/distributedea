package org.distributedea.ontology.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Ontology represents identification of {@link Batch}, {@link Job}
 * and {@link JobRun}
 * @author stepan
 *
 */
public class JobID implements Concept {

	private static final long serialVersionUID = 1L;

	private String batchID;
	private String jobID;
	private int runNumber;
	
	@Deprecated
	public JobID() {} // only for Jade
	
	/**
	 * Constructor
	 * @param batchName
	 * @param jobName
	 * @param runNumber
	 */
	public JobID(String batchName, String jobName, int runNumber) {
		if (batchName == null || jobName == null || runNumber < 0) {
			throw new IllegalArgumentException();
		}
		this.batchID = batchName;
		this.jobID = jobName;
		this.runNumber = runNumber;
	}

	/**
	 * Copy Constructor
	 */
	public JobID(JobID jobIDStruct) {
		if (jobIDStruct == null || ! jobIDStruct.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.batchID = jobIDStruct.getBatchID();
		this.jobID = jobIDStruct.getJobID();
		this.runNumber = jobIDStruct.getRunNumber();
	}

	/**
	 * Returns {@link Batch} identification
	 * @return
	 */
	public String getBatchID() {
		return batchID;
	}
	@Deprecated
	public void setBatchID(String batchName) {
		this.batchID = batchName;
	}
	
	/**
	 * Returns {@link Job} identification
	 * @return
	 */
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobName) {
		this.jobID = jobName;
	}

	/**
	 * Returns {@link JobRun} identification
	 * @return
	 */
	public int getRunNumber() {
		return runNumber;
	}
	@Deprecated
	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}
	
	/**
	 * Validation
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (batchID == null) {
			return false;
		}
		if (jobID == null) {
			return false;
		}
		if (runNumber < -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
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
	    
	    
	    return aregBatchIDsEqual && aregJobIDsEqual && aregRunNumbersEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return batchID + jobID + runNumber;
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws IOException 
	 */
	public void exportXML(File dir) throws IOException {

		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String jobIDFile = dir.getAbsolutePath() + File.separator + "jobID.txt";
		File file = new File(jobIDFile);
		file.getParentFile().mkdirs(); 
		
		String xml = exportXML();
		
		PrintWriter fileWr = new PrintWriter(jobIDFile);
		fileWr.println(xml);
		fileWr.close();
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static JobID importXML(File file)
			throws IOException {

		if (file == null || ! file.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link JobID} from the String
	 */
	public static JobID importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (JobID) xstream.fromXML(xml);
	}
}
