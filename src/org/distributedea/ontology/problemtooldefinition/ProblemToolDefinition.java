package org.distributedea.ontology.problemtooldefinition;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.problemtools.IProblemTool;

/**
 * Ontology represents problem tool with arguments 
 * @author stepan
 *
 */
public class ProblemToolDefinition implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;
	
	
	private Arguments arguments;
	
	
	@Deprecated
	public ProblemToolDefinition() {} // Only for Jade
	
	
	/**
	 * Constructor
	 * @param problemTool
	 * @param arguments
	 */
	public ProblemToolDefinition(Class<?> problemTool, Arguments arguments) {
		
		importProblemToolClass(problemTool);
		setArguments(arguments);
	}

	/**
	 * Constructor
	 * @param problemTool
	 */
	public ProblemToolDefinition(IProblemTool problemTool) {
		importProblemToolClass(problemTool.getClass());
		this.setArguments(problemTool.exportArguments());
	}
	
	/**
	 * Copy constructor
	 * @param problemTool
	 * @param arguments
	 */
	public ProblemToolDefinition(ProblemToolDefinition problemToolDefinition) {

		if (problemToolDefinition == null || ! problemToolDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " is not valid");
		}

		importProblemToolClass(problemToolDefinition.exportProblemToolClass(new TrashLogger()));
		setArguments(problemToolDefinition.getArguments().deepClone());
	}
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	
	@Deprecated
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

	
	
	public Arguments getArguments() {
		return arguments;
	}
	@Deprecated
	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	
	/**
	 * Export {@link IProblemTool}
	 * @param logger
	 * @return
	 */
	public IProblemTool exportProblemTool(IAgentLogger logger) {
		
		Class<?> probToolclass = exportProblemToolClass(logger);
		 
		IProblemTool problemTool = null;
		try {
			problemTool = (IProblemTool) probToolclass.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			logger.logThrowable("Can't create ProblemTool", e);
		}
		
		problemTool.importArguments(arguments);
		return problemTool;
	}
	
	/**
	 * Export {@link IProblemTool} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportProblemToolClass(IAgentLogger logger) {
		
		try {
			return Class.forName(getProblemToolClass());
		} catch (ClassNotFoundException e) {
			logger.logThrowable("Can not find class for ProblemToll", e);
		}
		return null;
	}
	
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToolClass
	 */
	public void importProblemToolClass(Class<?> problemToolClass) {
		if (problemToolClass == null) {
			return;
		}
		this.problemToolClass = problemToolClass.getName();
	}
	
	
	/**
	 * Test validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (exportProblemToolClass(logger) == null) {
			return false;
		}
		if (arguments == null || ! arguments.valid(logger)) {
			return false;
		}
		
		return true;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemToolDefinition)) {
	        return false;
	    }
	    
	    ProblemToolDefinition problemToolDef = (ProblemToolDefinition) other;
	    
	    boolean areProblemToolClassEqual =
	    		exportProblemToolClass(new TrashLogger()) == problemToolDef.exportProblemToolClass(new TrashLogger());
	    boolean areArgumentsEqual =
	    		getArguments().equals(problemToolDef.getArguments());
	    
	    return areProblemToolClassEqual && areArgumentsEqual;
	}

   @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return problemToolClass + arguments.toString();
	}

	/**
	 * Returns clone
	 * @return
	 */
	public ProblemToolDefinition deepClone() {
		return new ProblemToolDefinition(this);
	}	

}
