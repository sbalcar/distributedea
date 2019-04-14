package org.distributedea.ontology.methoddescriptioninput;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.methoddesriptionsplanned.PlannedMethodDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;


/**
 * Ontology represents input method description.
 * @author stepan
 *
 */
public class InputMethodDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent specification including class, name and parameters
	 */
	private InputAgentConfiguration inputAgentConfiguration;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private ProblemToolDefinition problemToolDefinition;

	
	
	@Deprecated
	public InputMethodDescription() {} // only for Jade
	
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 * @param problemToolDef
	 */
	public InputMethodDescription(InputAgentConfiguration agentConfiguration,
			ProblemToolDefinition problemToolDef) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfiguration.class.getSimpleName() + " can not be null");
		}
		if (problemToolDef == null ||
				! problemToolDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " can not be null");
		}
	
		setInputAgentConfiguration(agentConfiguration);
		setProblemToolDefinition(problemToolDef);
	}
	
	/**
	 * Copy constructor
	 * @param inputMethodDesc
	 */
	public InputMethodDescription(InputMethodDescription inputMethodDesc) {
		if (inputMethodDesc == null ||
				! inputMethodDesc.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " can not be null");
		}
		
		InputAgentConfiguration inputAgentConfClone =
				inputMethodDesc.getInputAgentConfiguration().deepClone();
		
		ProblemToolDefinition problemToolDefClone =
				inputMethodDesc.getProblemToolDefinition().deepClone();
		
		this.setInputAgentConfiguration(inputAgentConfClone);
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

	public PlannedMethodDescription exportPlannedMethodDescription(MethodIDs methodIDs) {
		return new PlannedMethodDescription(
				getInputAgentConfiguration(),
				methodIDs,
				getProblemToolDefinition());
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
		
		if (getProblemToolDefinition() == null ||
				! getProblemToolDefinition().valid(logger)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof InputMethodDescription)) {
	        return false;
	    }
	    
	    InputMethodDescription iadOuther = (InputMethodDescription)other;
	    
	    boolean areInputAgentConfigurationEqual =
	    		this.getInputAgentConfiguration().equals(iadOuther.getInputAgentConfiguration());
	    
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolDefinition().equals(iadOuther.getProblemToolDefinition());
	    	    
	    return areInputAgentConfigurationEqual && areProblemToolClassesEqual;
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

		String problemToolDefStr = "null";
		if (problemToolDefinition != null) {
			problemToolDefStr = problemToolDefStr.toString();
		}
		return inputAgentConfStr + "-" + problemToolDefStr;
	}
	
	/**
	 * Exports clone
	 * @return
	 */
	public InputMethodDescription deepClone() {
		
		return new InputMethodDescription(this);
	}
}