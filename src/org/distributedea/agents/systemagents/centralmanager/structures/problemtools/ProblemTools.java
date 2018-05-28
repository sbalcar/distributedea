package org.distributedea.agents.systemagents.centralmanager.structures.problemtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Represents set of ProblemTools which are available to use
 * for {@link Agent_ComputingAgent}s.
 * @author stepan
 *
 */
public class ProblemTools {

	private List<Class<?>> problemTools;

	
	@Deprecated
	public ProblemTools() {
		this.problemTools = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param problemTool
	 */
	public ProblemTools(Class<?> problemTool) {
		this.problemTools = new ArrayList<>();
		this.problemTools.add(problemTool);
	}
	
	/**
	 * Constructor
	 * @param problemTools
	 */
	public ProblemTools(List<Class<?>> problemTools) {
		if (problemTools == null || problemTools.isEmpty()) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.problemTools = new ArrayList<>();
		for (Class<?> probToolI : problemTools) {
			addProblemTool(probToolI);
		}
	}
	
	/**
	 * Copy Constructor
	 * @param problemTools
	 */
	public ProblemTools(ProblemTools problemTools) {
		
		if (problemTools == null || ! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemTools.class.getSimpleName() + " is not valid");
		}
		
		List<Class<?>> problemToolsList = problemTools.getProblemTools();
		for (Class<?> problemTollI : problemToolsList) {
			addProblemTool(problemTollI);
		}
	}

	
	/**
	 * Returns {@link IProblemTool} classes
	 * @return
	 */
	public List<Class<?>> getProblemTools() {
		return problemTools;
	}
	/**
	 * Adds {@link IProblemTool} class
	 * @param problemTool
	 */
	public void addProblemTool(Class<?> problemTool) {
		
		if (problemTool == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		
		if (problemTools == null) {
			problemTools = new ArrayList<>();
		}
		
		this.problemTools.add(problemTool);
	}
	/**
	 * Export random selected {@link IProblemTool} class
	 * @return
	 */
	public IProblemTool exportRandomSelectedProblemTool(IAgentLogger logger) {
		
		if (problemTools == null || problemTools.isEmpty()) {
			return null;
		}
		
		//random select problem tool
		Random ran = new Random();
		int indexPT = ran.nextInt(problemTools.size());
		Class<?> problemTollClass =  problemTools.get(indexPT);
		
		return exportProblemTool(problemTollClass, logger);
	}
	
	public static IProblemTool exportProblemTool(Class<?> problemToolClass,
			IAgentLogger logger) {
		
		if (problemToolClass == null || logger == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		return ProblemTool.createInstanceOfProblemTool(problemToolClass,
				logger);
	}

	/**
	 * Exports as string
	 * @return
	 */
	public String exportAsString() {
		
		String string = "";
		for (Class<?> problemToolClassI : problemTools) {
			
			String simpleNameI = problemToolClassI.getSimpleName();
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
		
		if (problemTools == null) {
			return false;
		}
		
		for (Class<?> problemToolClassI : problemTools) {
			
			IProblemTool problemToolI = ProblemTool.
					createInstanceOfProblemTool(problemToolClassI, logger);
			
			if (problemToolI == null) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Test validity of this {@link ProblemTools} structure
	 * @param problemToSolve
	 * @param logger
	 * @return
	 */
	public boolean valid(Class<?> problemToSolve, IAgentLogger logger) {
		
		// tests validation of Problem Tools
		if (problemTools == null || problemTools.isEmpty()) {
			logger.log(Level.INFO, "Any problemTool available");
			return false;
		}
		
		for (Class<?> problemToolClassI : problemTools) {
			
			IProblemTool problemToolI = ProblemTool.
					createInstanceOfProblemTool(problemToolClassI, logger);
			
			if (problemToolI == null ||
					problemToolI.datasetReprezentation() != problemToSolve) {
				logger.log(Level.INFO, IProblemTool.class.getSimpleName() +
						" " + problemToolI.getClass().getSimpleName() +
						"doesn't solve " + problemToSolve.getSimpleName() +
						" problem");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ProblemTools deepClone() {
		return new ProblemTools(this);
	}
	
}
