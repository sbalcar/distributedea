package org.distributedea.agents.systemagents.centralmanager.planner.history;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.ontology.agentdescription.AgentDescription;

import com.thoughtworks.xstream.XStream;

public class MethodInstanceDescription implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String agentClassName;
	private String problemToolClass;
	
	private int instanceNumber;
	
	
	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	public Class<?> exportAgentClass() {
		try {
			return Class.forName(agentClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	public void importAgentClass(Class<?> agentClass) {
		this.agentClassName = agentClass.getName();
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
	
	public int getInstanceNumber() {
		return instanceNumber;
	}
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String exportInstanceName() {
		return "" + exportAgentClass().getSimpleName() + "-" +
				exportProblemToolClass().getSimpleName() + "-" +
				instanceNumber;
	}
	
	public boolean exportAreTheSameType(MethodInstanceDescription instance) {
		
		return instance.exportAgentClass() == exportAgentClass() &&
				instance.exportProblemToolClass() == exportProblemToolClass();
	}
	
	public boolean exportAreTheSameType(AgentDescription agentDescription) {
		if (agentDescription == null) {
			return false;
		}
		Class<?> agentClass = agentDescription.getAgentConfiguration().exportAgentType();
		Class<?> problemToolClass = agentDescription.exportProblemToolClass();
		
		return exportAgentClass() == agentClass &&
				exportProblemToolClass() == problemToolClass;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodInstanceDescription)) {
	        return false;
	    }
	    
	    MethodInstanceDescription methodInstOuther = (MethodInstanceDescription)other;
	    
	    return this.toString().equals(methodInstOuther);
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return agentClassName + problemToolClass + instanceNumber;
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
		
		String fileName = dir.getAbsolutePath() + File.separator + "instance.xml";
		
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
	 * Import the {@link MethodInstanceDescription} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static MethodInstanceDescription importXML(File file)
			throws FileNotFoundException {
		
		if (file == null || (!file.exists()) || (!file.isFile())) {
			return null;
		}
		
		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link MethodInstanceDescription} from the String
	 */
	public static MethodInstanceDescription importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (MethodInstanceDescription) xstream.fromXML(xml);
	}

}
