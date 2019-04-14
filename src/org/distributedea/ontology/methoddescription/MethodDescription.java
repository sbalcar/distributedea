package org.distributedea.ontology.methoddescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
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
	 * Method identificators
	 */
	private MethodIDs methodIDs;
	
	/**
	 * Problem to solve definition
	 */
	private IProblem problem;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private ProblemToolDefinition problemToolDefinition;

	
	
	@Deprecated
	public MethodDescription() {} // only for Jade
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 * @param problemToolClass
	 */
	public MethodDescription(AgentConfiguration agentConfiguration,
			MethodIDs methodIDs, IProblem problem, ProblemToolDefinition problemToolDefinition) {
		
		setAgentConfiguration(agentConfiguration);
		setMethodIDs(methodIDs);
		setProblem(problem);
		setProblemToolDefinition(problemToolDefinition);
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
		MethodIDs methodIDsClone =
				methodDescription.getMethodIDs().deepClone();
		IProblem problemClone =
				methodDescription.getProblem().deepClone();
		ProblemToolDefinition problemToolClone =
				methodDescription.getProblemToolDefinition().deepClone();
		
		setAgentConfiguration(agentConfigurationClone);
		setMethodIDs(methodIDsClone);
		setProblem(problemClone);
		setProblemToolDefinition(problemToolClone);
	}
	
	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	@Deprecated
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		if (agentConfiguration == null || ! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		this.agentConfiguration = agentConfiguration;
	}
	
	
	public MethodIDs getMethodIDs() {
		return methodIDs;
	}
	@Deprecated
	public void setMethodIDs(MethodIDs methodIDs) {
		if (methodIDs == null || ! methodIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " is not valid");
		}
		this.methodIDs = methodIDs;
	}

	
	public IProblem getProblem() {
		return problem;
	}
	@Deprecated
	public void setProblem(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not vlid");
		}
		this.problem = problem;
	}
	

	public ProblemToolDefinition getProblemToolDefinition() {
		return problemToolDefinition;
	}
	@Deprecated
	public void setProblemToolDefinition(ProblemToolDefinition problemToolDefinition) {
		if (problemToolDefinition == null ||
				! problemToolDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " is not vlid");
		}
		this.problemToolDefinition = problemToolDefinition;
	}

	/**
	 * Export Agent class
	 * @return
	 */
	public Class<?> exportAgentClass() {
		return agentConfiguration.exportAgentClass();
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
				 
		 return agentConfiguration.exportAgentname() + "-" + getProblemToolDefinition().toString();
	}

	/**
	 * Export {@link MethodType}
	 * @return
	 */
	public MethodType exportMethodType() {
		
		Class<?> agentClass = getAgentConfiguration().exportAgentClass();
		Arguments arguments = getAgentConfiguration().getArguments();
		
		return new MethodType(agentClass, getProblemToolDefinition(), arguments);
	}
	
	/**
	 * Exports {@link InputMethodDescription}
	 * @return
	 */
	public InputMethodDescription exportInputMethodDescription() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		
		InputAgentConfiguration inputAgentConfClone =
				getAgentConfiguration().exportInputAgentConfiguration();
		
		return new InputMethodDescription(inputAgentConfClone,
				getProblemToolDefinition());
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentConfiguration == null || ! agentConfiguration.valid(logger)) {
			return false;
		}
		if (getProblemToolDefinition() == null || ! getProblemToolDefinition().valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Test same method type
	 * @param other
	 * @return
	 */
	public boolean equalsMetodTyppe(Object other) {
		
	    if (!(other instanceof MethodDescription)) {
	        return false;
	    }
	    
	    MethodDescription adOuther = (MethodDescription)other;
	    
	    Class<?> agentClass =
	    		adOuther.getAgentConfiguration().exportAgentClass();
	    Class<?> problemToolClass =
	    		adOuther.getProblemToolDefinition().exportProblemToolClass(new TrashLogger());
	    
	    return getAgentConfiguration().exportAgentClass() == agentClass &&
	    		getProblemToolDefinition().exportProblemToolClass(new TrashLogger()) == problemToolClass;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodDescription)) {
	        return false;
	    }
	    
	    MethodDescription adOuther = (MethodDescription)other;
	    
	    boolean areAgentagentConfigurationsEqual =
	    		this.getAgentConfiguration().equals(adOuther.getAgentConfiguration());

	    boolean areMethodIDsEqual =
	    		this.getMethodIDs().equals(adOuther.getMethodIDs());
	    
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolDefinition().equals(adOuther.getProblemToolDefinition());
	    
	    return areAgentagentConfigurationsEqual && areMethodIDsEqual &&
	    		areProblemToolClassesEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String agentConfigurationStr = "null";
		if (getAgentConfiguration() != null) {
			agentConfigurationStr = getAgentConfiguration().toString();
		}

		String methodIDsStr = "null";
		if (getMethodIDs() != null) {
			methodIDsStr = getMethodIDs().toString();
		}
		
		String problemToolDefStr = "null";
		if (getProblemToolDefinition() != null) {
			problemToolDefStr = getProblemToolDefinition().toString();
		}
		
		return agentConfigurationStr + "-" + methodIDsStr + "-" + problemToolDefStr;
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
