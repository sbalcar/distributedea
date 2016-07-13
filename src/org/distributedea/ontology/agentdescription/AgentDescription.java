package org.distributedea.ontology.agentdescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodInstanceDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.problems.ProblemTool;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;
import jade.core.AID;

public class AgentDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent configuration
	 */
	private AgentConfiguration agentConfiguration;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;


	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	public void importProblemToolClass(Class<?> problemToolClass) {
		this.problemToolClass = problemToolClass.getName();
	}
	
	public void importProblemTool(ProblemTool problemTool) {
		this.problemToolClass = problemTool.getClass().getName();
	}
	
	public AID exportAgentAID() {
		return agentConfiguration.exportAgentAID();
	}
	
	public String exportAgentName() {
		
		 String problemToolClassName = exportProblemToolClass().getSimpleName();
		 
		 return agentConfiguration.exportAgentname() + "-" + problemToolClassName;
	}

	public MethodInstanceDescription exportMethodInstanceDescription(
			int instanceNumber) {
		
		MethodInstanceDescription methodInstanceDescrittion =
				new MethodInstanceDescription();
		
		methodInstanceDescrittion.setInstanceNumber(instanceNumber);
		methodInstanceDescrittion.importAgentClass(
				getAgentConfiguration().exportAgentType());
		methodInstanceDescrittion.importProblemToolClass(
				exportProblemToolClass());

		return methodInstanceDescrittion;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentDescription)) {
	        return false;
	    }
	    
	    AgentDescription adOuther = (AgentDescription)other;
	    
	    boolean areAgentagentConfigurationsEqual =
	    		this.getAgentConfiguration().equals(adOuther.getAgentConfiguration());
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolClass().equals(adOuther.getProblemToolClass());
	    
	    if (areAgentagentConfigurationsEqual && 
	    		areProblemToolClassesEqual) {
	    	return true;
	    }
	    
	    return false;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		if (agentConfiguration == null) {
			return "null" + problemToolClass;
		} else {
			return agentConfiguration.toString() + problemToolClass;
		}
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File dir) throws FileNotFoundException, JAXBException {

		if (dir == null || (! dir.exists()) || (! dir.isDirectory())) {
			return;
		}
		
		String fileName = dir.getAbsolutePath() + File.separator + "description.xml";
		String xml = exportXML();

		PrintWriter fileWr = new PrintWriter(fileName);
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
	public static AgentDescription importXML(File file)
			throws FileNotFoundException {

		if (file == null || ! file.exists()) {
			return null;
		}
		
		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link JobID} from the String
	 */
	public static AgentDescription importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (AgentDescription) xstream.fromXML(xml);
	}
}
