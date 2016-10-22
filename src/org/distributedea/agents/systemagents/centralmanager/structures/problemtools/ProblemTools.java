package org.distributedea.agents.systemagents.centralmanager.structures.problemtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.logging.IAgentLogger;
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
	 * Copy Constructor
	 * @param problemToolsStruct
	 */
	public ProblemTools(ProblemTools problemToolsStruct) {
		
		if (problemToolsStruct == null) {
			return;
		}
		
		List<Class<?>> problemTools = problemToolsStruct.getProblemTools();
		for (Class<?> problemTollI : problemTools) {
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
		
		if (problemTools == null) {
			problemTools = new ArrayList<>();
		}
		
		this.problemTools.add(problemTool);
	}
	/**
	 * Export random selected {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportRandomSelectedProblemTool() {
		
		if (problemTools == null || problemTools.isEmpty()) {
			return null;
		}
		
		//random select problem tool
		Random ran = new Random();
		int indexPT = ran.nextInt(problemTools.size());
		return problemTools.get(indexPT);
	}
	
	public int exportNumberOfProblemTools() {
		if (problemTools == null) {
			return 0;
		}
		return problemTools.size();
	}
	
	public static IProblemTool exportProblemTool(Class<?> problemToolClass,
			IAgentLogger logger) {
		
		if (problemToolClass == null || logger == null) {
			throw new IllegalArgumentException();
		}
		return ProblemTool.createInstanceOfProblemTool(problemToolClass,
				logger);
	}

	/**
	 * Test validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
				
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
					problemToolI.problemWhichSolves() != problemToSolve) {
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
