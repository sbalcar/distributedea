package org.distributedea.ontology.job.noontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;


@XmlRootElement (name="jobs")
public class JobWrapper implements Concept, Serializable {

	private static final long serialVersionUID = 1L;

	public JobWrapper() {
	}
	
	/**
	 * Declares Job IDentification
	 */
	private String jobID;
	
	/**
	 * Job Description
	 */
	private String description;
	
	/**
	 * Define number of replaning of Scheduler
	 */
	private long countOfReplaning;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private Class<?> problemToSolve;
	
	/**
	 * Defines the filename with the input Problem
	 */
	private String problemFileName;
	
	/**
	 * Defines the filename with method(agent types)
	 */
	private String methodsFileName;
	
	/**
	 * Declares the set of available ProblemTools for Computing Agents
	 */
	private ProblemTools problemTools;
	
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	
	/**
	 * Declares the Scheduler Class which will be used to direction of the evolution
	 */
	private List<Scheduler> schedulers;	// warning scheduler is wrapped in list because it is necessary for XML serialization


	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getCountOfReplaning() {
		return countOfReplaning;
	}
	public void setCountOfReplaning(long countOfReplaning) {
		this.countOfReplaning = countOfReplaning;
	}
	
	public Class<?> getProblemToSolve() {
		return problemToSolve;
	}
	public void setProblemToSolve(Class<?> problemToSolve) {
		this.problemToSolve = problemToSolve;
	}
	
	public String getProblemFileName() {
		return problemFileName;
	}
	public void setProblemFileName(String problemFileName) {
		this.problemFileName = problemFileName;
	}
	
	public String getMethodsFileName() {
		return methodsFileName;
	}
	public void setMethodsFileName(String methodsFileName) {
		this.methodsFileName = methodsFileName;
	}
	
	public ProblemTools getProblemTools() {
		return problemTools;
	}
	public void setProblemTools(ProblemTools problemTools) {
		this.problemTools = problemTools;
	}

	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	public Scheduler getScheduler() {
		if (this.schedulers == null || this.schedulers.isEmpty()) {
			return null;
		}
		return this.schedulers.get(0);
	}
	public void setScheduler(Scheduler scheduler) {
		if (this.schedulers == null) {
			this.schedulers = new ArrayList<Scheduler>();
		}
		this.schedulers.add(scheduler);
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
	 * Import the {@link JobWrapper} from the file
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
	 * Import the {@link JobWrapper} from the String
	 */
	public static JobWrapper importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		xstream.aliasAttribute("type", "class");

		return (JobWrapper) xstream.fromXML(xml);
	}
	
}
