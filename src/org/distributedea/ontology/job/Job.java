package org.distributedea.ontology.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

public class Job implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Declares Job IDentification
	 */
	private String jobID;
	
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
	private List<Class<?>> availableProblemTools;
	
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Declares the Scheduler Class which will be used to direction of the evolution
	 */
	private Class<?> scheduler;
	
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
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
	
	public List<Class<?>> getAvailableProblemTools() {
		return availableProblemTools;
	}
	public void setAvailableProblemTools(List<Class<?>> availableProblemTools) {
		this.availableProblemTools = availableProblemTools;
	}

	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	public Class<?> getScheduler() {
		return scheduler;
	}
	public void setScheduler(Class<?> scheduler) {
		this.scheduler = scheduler;
	}
	

	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 */
	public void exportXML(String fileName) throws FileNotFoundException {

		String xml = exportXML();

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
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static Job importXML(File file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
	}

	/**
	 * Import the {@link Job} from the String
	 */
	public static Job importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		xstream.aliasAttribute("type", "class");

		return (Job) xstream.fromXML(xml);
	}
	
}
