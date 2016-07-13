package org.distributedea.ontology.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.ontology.job.noontology.Job;

import com.thoughtworks.xstream.XStream;

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
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File dir) throws FileNotFoundException, JAXBException {

		if (dir == null) {
			return;
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
			throws FileNotFoundException {

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
