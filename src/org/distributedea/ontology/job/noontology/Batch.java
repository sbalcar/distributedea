package org.distributedea.ontology.job.noontology;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.ontology.job.JobID;

import com.thoughtworks.xstream.XStream;

public class Batch implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String batchID;
	
	private List<JobWrapper> jobWrappers;

	private String description;
	
	
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	
	
	public List<JobWrapper> getJobWrappers() {
		return jobWrappers;
	}
	public void setJobWrappers(List<JobWrapper> jobWrappers) {
		this.jobWrappers = jobWrappers;
	}
	public void addJobWrapper(JobWrapper jobWrappers) {
		
		if (this.jobWrappers == null) {
			this.jobWrappers = new ArrayList<>();
		}
		
		this.jobWrappers.add(jobWrappers);
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public List<JobID> exportJobIDs() {
		
		String batchID = getBatchID();
		
		List<JobID> list = new ArrayList<>();
		
		for (JobWrapper jobWrpI : jobWrappers) {
			
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
		
		for (JobWrapper jobWrpI : jobWrappers) {
			
			String descriptionI = jobWrpI.getDescription();
			list.add(descriptionI);
		}
		
		return list;
	}
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(String fileName) throws FileNotFoundException, JAXBException {

		String xml = exportXML();
		System.out.println(xml);
		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();
		
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
	 * Import the {@link Batch} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static JobWrapper importXML(File file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link Batch} from the String
	 */
	public static JobWrapper importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		xstream.aliasAttribute("type", "class");

		return (JobWrapper) xstream.fromXML(xml);
	}
}
