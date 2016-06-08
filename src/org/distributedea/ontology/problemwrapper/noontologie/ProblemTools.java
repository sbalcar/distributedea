package org.distributedea.ontology.problemwrapper.noontologie;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.problems.ProblemTool;

/**
 * Represents set of ProblemTools which are available for Computing
 * @author Stepan
 *
 */
public class ProblemTools {

	private List<Class<?>> problemTools;

	
	public ProblemTools() {
	}
	
	public ProblemTools(Class<?> problemTool) {
		this.problemTools = new ArrayList<>();
		this.problemTools.add(problemTool);
	}
	
	public ProblemTools(ProblemTools problemToolsStruct) {
		
		List<Class<?>> problemTools = problemToolsStruct.getProblemTools();
		
		for (Class<?> problemTollI : problemTools) {
			addProblemTool(problemTollI);
		}
	}
	
	public List<Class<?>> getProblemTools() {
		return problemTools;
	}
	public void setProblemTools(List<Class<?>> availableProblemTools) {
		this.problemTools = availableProblemTools;
	}
	public void addProblemTool(Class<?> problemTool) {
		
		if (problemTools == null) {
			problemTools = new ArrayList<>();
		}
		
		this.problemTools.add(problemTool);
	}	
	
	public ProblemTool exportProblemTool(int index, AgentLogger logger) {
		
		if (problemTools == null || problemTools.size() <= index) {
			return null;
		}
		
		// Problem reading and testing
		Class<?> problemToolClass1 = problemTools.get(index);
		
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) problemToolClass1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("ProblemTool wasn't initialized", e);
			return null;
		}
		
		return problemTool;
	}
	
	public boolean valid(Class<?> problemToSolve, AgentLogger logger) {
		
		// tests validation of Problem Tools
		if (problemTools.size() == 0) {
			logger.log(Level.INFO, "Incorrect input: Any problemTool available");
			return false;
		}
		
		for (Class<?> problemToolClassI : problemTools) {
			
			ProblemTool problemToolI = null;
			try {
				problemToolI = (ProblemTool) problemToolClassI.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.logThrowable("Error with instance ProblemTool", e);
				return false;
			}
			
			if (problemToolI.problemWhichSolves() != problemToSolve) {
				logger.log(Level.INFO, "ProblemTool " +
						problemToolI.getClass().getSimpleName() +
						"doesn't solve " + problemToSolve.getSimpleName() +
						" problem");
				return false;
			}
		}
		
		return true;
	}
	
	public ProblemTools deepClone() {
		return new ProblemTools(this);
	}
	
}
