package org.distributedea.ontology.methoddesriptionsplanned;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;


/**
 * Ontology represents requested description of method.
 * @author stepan
 *
 */
public class PlannedMethodDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent specification including class, name and parameters
	 */
	private InputAgentConfiguration inputAgentConfiguration;
	
	/**
	 * Method IDs
	 */
	private MethodIDs methodIDs;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private ProblemToolDefinition problemToolDefinition;

	
	
	@Deprecated
	public PlannedMethodDescription() {} // only for Jade
	
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 * @param methodIDs
	 * @param problemToolDef
	 */
	public PlannedMethodDescription(InputAgentConfiguration agentConfiguration,
			MethodIDs methodIDs, ProblemToolDefinition problemToolDef) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfiguration.class.getSimpleName() + " can not be null");
		}
		if (methodIDs == null ||
				! methodIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " can not be null");
		}
		if (problemToolDef == null ||
				! problemToolDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " can not be null");
		}
	
		setInputAgentConfiguration(agentConfiguration);
		setMethodIDs(methodIDs);
		setProblemToolDefinition(problemToolDef);
	}
	
	/**
	 * Copy constructor
	 * @param plannedMethodDesc
	 */
	public PlannedMethodDescription(PlannedMethodDescription plannedMethodDesc) {
		if (plannedMethodDesc == null ||
				! plannedMethodDesc.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PlannedMethodDescription.class.getSimpleName() + " can not be null");
		}
		
		InputAgentConfiguration inputAgentConfClone =
				plannedMethodDesc.getInputAgentConfiguration().deepClone();
		
		MethodIDs methodIDsClone =
				plannedMethodDesc.getMethodIDs().deepClone();
		
		ProblemToolDefinition problemToolDefClone =
				plannedMethodDesc.getProblemToolDefinition().deepClone();
		
		this.setInputAgentConfiguration(inputAgentConfClone);
		this.setMethodIDs(methodIDsClone);
		this.setProblemToolDefinition(problemToolDefClone);
	}
	

	public InputAgentConfiguration getInputAgentConfiguration() {
		return inputAgentConfiguration;
	}
	@Deprecated
	public void setInputAgentConfiguration(InputAgentConfiguration agentConfiguration) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.inputAgentConfiguration = agentConfiguration;
	}


	public MethodIDs getMethodIDs() {
		return methodIDs;
	}
	@Deprecated
	public void setMethodIDs(MethodIDs methodIDs) {
		if (methodIDs == null ||
				! methodIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " is not valid");
		}
		this.methodIDs = methodIDs;
	}

	
	public ProblemToolDefinition getProblemToolDefinition() {
		return problemToolDefinition;
	}
	@Deprecated
	public void setProblemToolDefinition(ProblemToolDefinition problemToolDefinition) {
		if (problemToolDefinition == null ||
				! problemToolDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemToolDefinition = problemToolDefinition;
	}

	
	/**
	 * Export Agent class
	 * @return
	 */
	public Class<?> exportAgentClass() {
		return inputAgentConfiguration.exportAgentClass();
	}
	
	
	/**
	 * Export {@link MethodType}
	 * @return
	 */
	public MethodType exportMethodType() {
		
		Class<?> agentClass = getInputAgentConfiguration().exportAgentClass();
		Arguments arguments = inputAgentConfiguration.getArguments();
		
		return new MethodType(agentClass, getProblemToolDefinition(), arguments);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (inputAgentConfiguration == null ||
				! inputAgentConfiguration.valid(logger)) {
			return false;
		}
		
		if (getMethodIDs() == null ||
				! getMethodIDs().valid(logger)) {
			return false;
		}
		
		if (getProblemToolDefinition() == null ||
				! getProblemToolDefinition().valid(logger)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof PlannedMethodDescription)) {
	        return false;
	    }
	    
	    PlannedMethodDescription pmdOuther = (PlannedMethodDescription)other;
	    
	    boolean areInputAgentConfigurationEqual =
	    		this.getInputAgentConfiguration().equals(pmdOuther.getInputAgentConfiguration());
	    
	    boolean aretMethodIDsEqual =
	    		this.getMethodIDs().equals(pmdOuther.getMethodIDs());
	    
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolDefinition().equals(pmdOuther.getProblemToolDefinition());
	    	    
	    return areInputAgentConfigurationEqual && aretMethodIDsEqual &&
	    		areProblemToolClassesEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String inputAgentConfStr = "null";
		if (inputAgentConfiguration != null) {
			inputAgentConfStr = inputAgentConfiguration.toString();
		}

		String methodIDsStr = "null";
		if (methodIDs != null) {
			methodIDsStr = methodIDs.toString();
		}
		
		String problemToolDefStr = "null";
		if (problemToolDefinition != null) {
			problemToolDefStr = problemToolDefStr.toString();
		}
		return inputAgentConfStr + "-" + methodIDsStr + "-" + problemToolDefStr;
	}
	
	/**
	 * Exports clone
	 * @return
	 */
	public PlannedMethodDescription deepClone() {
		
		return new PlannedMethodDescription(this);
	}
}