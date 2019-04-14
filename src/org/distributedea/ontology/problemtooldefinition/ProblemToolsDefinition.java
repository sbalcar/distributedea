package org.distributedea.ontology.problemtooldefinition;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.problems.IProblemTool;

/**
 * Represents set of ProblemTools which are available to use
 * for {@link Agent_ComputingAgent}s.
 * @author stepan
 *
 */
public class ProblemToolsDefinition implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<ProblemToolDefinition> problemToolsDefinition;

	
	@Deprecated
	public ProblemToolsDefinition() {   // only for Jade
		this.problemToolsDefinition = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param problemTool
	 */
	public ProblemToolsDefinition(ProblemToolDefinition problemToolDef) {
		this.problemToolsDefinition = new ArrayList<>();
		this.problemToolsDefinition.add(problemToolDef);
	}
	
	/**
	 * Constructor
	 * @param problemTools
	 */
	public ProblemToolsDefinition(List<ProblemToolDefinition> problemToolsDef) {
		
		setProblemToolsDefinition(problemToolsDef);		
	}
	
	/**
	 * Copy Constructor
	 * @param problemTools
	 */
	public ProblemToolsDefinition(ProblemToolsDefinition problemTools) {
		
		if (problemTools == null) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolsDefinition.class.getSimpleName() + " is not valid");
		}

		List<ProblemToolDefinition> problemToolsList = problemTools.getProblemToolsDefinition();
		setProblemToolsDefinition(problemToolsList);
	}

	
	/**
	 * Returns {@link IProblemTool} classes
	 * @return
	 */
	public List<ProblemToolDefinition> getProblemToolsDefinition() {
		return problemToolsDefinition;
	}
	@Deprecated
	public void setProblemToolsDefinition(List<ProblemToolDefinition> problemToolsDef) {
		if (problemToolsDef == null || problemToolsDef.isEmpty()) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (ProblemToolDefinition probToolI : problemToolsDef) {
			if (probToolI == null || ! probToolI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						ProblemToolDefinition.class.getSimpleName() + " is not valid");
			}
		}

		this.problemToolsDefinition = problemToolsDef;
	}
	
	
	
	/**
	 * Adds {@link IProblemTool} class
	 * @param problemTool
	 */
	public void addProblemTool(ProblemToolDefinition problemToolDef) {
		
		if (problemToolDef == null || ! problemToolDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		
		if (problemToolsDefinition == null) {
			problemToolsDefinition = new ArrayList<>();
		}
		
		this.problemToolsDefinition.add(problemToolDef);
	}
	/**
	 * Export random selected {@link IProblemTool} class
	 * @return
	 */
	public ProblemToolDefinition exportRandomSelectedProblemToolDefinition() {
		
		if (problemToolsDefinition == null || problemToolsDefinition.isEmpty()) {
			return null;
		}
		
		//random select problem tool
		Random ran = new Random();
		int indexPT = ran.nextInt(problemToolsDefinition.size());
		return problemToolsDefinition.get(indexPT);		
	}	

	/**
	 * Exports as string
	 * @return
	 */
	public String exportAsString() {
		
		String string = "";
		for (ProblemToolDefinition problemToolDefI : problemToolsDefinition) {
			
			String simpleNameI = problemToolDefI.toString();
			string += simpleNameI + ", ";
		}
		
		return string.substring(0, string.length() -2);
	}
	
	/**
	 * Test validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (problemToolsDefinition == null) {
			return false;
		}
		
		for (ProblemToolDefinition problemToolDefI : problemToolsDefinition) {
			if (! problemToolDefI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Test validity of this {@link ProblemToolsDefinition} structure
	 * @param problemToSolve
	 * @param logger
	 * @return
	 */
	public boolean valid(Class<?> problemToSolve, IAgentLogger logger) {
		
		// tests validation of Problem Tools
		if (problemToolsDefinition == null || problemToolsDefinition.isEmpty()) {
			logger.log(Level.INFO, "Any problemTool available");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ProblemToolsDefinition deepClone() {
		return new ProblemToolsDefinition(this);
	}
	
}
