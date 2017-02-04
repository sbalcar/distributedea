package org.distributedea.ontology.methoddescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;
import jade.core.AID;

/**
 * Ontology for Method Description
 * @author stepan
 */
public class MethodDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent specification including class, name and parameters
	 */
	private AgentConfiguration agentConfiguration;
	
	/**
	 * Problem to solve definition
	 */
	private IProblem problemDefinition;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;

	
	
	@Deprecated
	public MethodDescription() {} // only for Jade
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 * @param problemToolClass
	 */
	public MethodDescription(AgentConfiguration agentConfiguration,
			IProblem problem, Class<?> problemToolClass) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
	
		setAgentConfiguration(agentConfiguration);
		setProblemDefinition(problem);
		importProblemToolClass(problemToolClass);
	}

	/**
	 * Copy constructor
	 * @param methodDescription
	 */
	public MethodDescription(MethodDescription methodDescription) {
		if (methodDescription == null ||
				! methodDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}

		AgentConfiguration agentConfigurationClone =
				methodDescription.getAgentConfiguration().deepClone();
		IProblem problemClone =
				methodDescription.getProblemDefinition().deepClone();
		Class<?> problemToolClassClone =
				methodDescription.exportProblemToolClass();
		
		setAgentConfiguration(agentConfigurationClone);
		setProblemDefinition(problemClone);
		importProblemToolClass(problemToolClassClone);

	}
	
	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	@Deprecated
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		if (agentConfiguration == null || ! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.agentConfiguration = agentConfiguration;
	}
	
	public IProblem getProblemDefinition() {
		return problemDefinition;
	}
	@Deprecated
	public void setProblemDefinition(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not vlid");
		}
		this.problemDefinition = problem;
	}
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	@Deprecated
	public void setProblemToolClass(String problemToolClass) {
		if (problemToolClass == null) {
			throw new IllegalArgumentException();
		}
		this.problemToolClass = problemToolClass;
	}

	/**
	 * Export Agent class
	 * @return
	 */
	public Class<?> exportAgentClass() {
		return agentConfiguration.exportAgentClass();
	}
	
	/**
	 * Export {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToolClass
	 */
	public void importProblemToolClass(Class<?> problemToolClass) {
		this.problemToolClass = problemToolClass.getName();
	}
	
	/**
	 * Export Agent AID
	 * @return
	 */
	public AID exportAgentAID() {
		return agentConfiguration.exportAgentAID();
	}
	
	/**
	 * Export Agent user friendly name containing {@link IProblemTool} class
	 * name
	 * @return
	 */
	public String exportMethodName() {
		
		 String problemToolClassName = exportProblemToolClass().getSimpleName();
		 
		 return agentConfiguration.exportAgentname() + "-" + problemToolClassName;
	}

	/**
	 * Export {@link MethodType}
	 * @return
	 */
	public MethodType exportMethodType() {
		
		Class<?> agentClass = getAgentConfiguration().exportAgentClass();
		Class<?> problemToolClass = exportProblemToolClass();
		
		Arguments arguments = agentConfiguration.getArguments();
		
		return new MethodType(agentClass, problemToolClass, arguments);
	}
	
	/**
	 * Exports {@link InputMethodDescription}
	 * @return
	 */
	public InputMethodDescription exportInputAgentDescription() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		
		InputAgentConfiguration inputAgentConfClone =
				getAgentConfiguration().exportInputAgentConfiguration();
		
		return new InputMethodDescription(inputAgentConfClone,
				exportProblemToolClass());
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentConfiguration == null || ! agentConfiguration.valid(logger)) {
			return false;
		}
		if (exportProblemToolClass() == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodDescription)) {
	        return false;
	    }
	    
	    MethodDescription adOuther = (MethodDescription)other;
	    
	    boolean areAgentagentConfigurationsEqual =
	    		this.getAgentConfiguration().equals(adOuther.getAgentConfiguration());
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolClass().equals(adOuther.getProblemToolClass());
	    
	    return areAgentagentConfigurationsEqual && 
	    		areProblemToolClassesEqual;
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
	 * @throws IOException
	 */
	public void exportXML(File dir) throws IOException {

		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
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
	public static MethodDescription importXML(File file)
			throws FileNotFoundException {

		if (file == null || ! file.exists()) {
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
	public static MethodDescription importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (MethodDescription) xstream.fromXML(xml);
	}
	
	/**
	 * Exports clone
	 * @return
	 */
	public MethodDescription deepClone() {
		
		return new MethodDescription(this);
	}
}