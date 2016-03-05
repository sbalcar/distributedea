package org.distributedea.ontology.problem;

import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;

import jade.content.Concept;

public abstract class Problem  implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String problemToolClass;

	
	public abstract boolean isMaximizationProblem();
	
	public String getProblemToolClass() {
		return problemToolClass;
	}

	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

	/**
	 * Tests validation of the Problem Ontology
	 * @param logger
	 * @return
	 */
	public boolean testIsValid(AgentLogger logger) {
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				getProblemToolClass(), logger);
		
		if ( this.getClass() == problemTool.problemWhichSolves() ) {
			return true;
		}
		
		logger.log(Level.SEVERE, "Problem is not valid");
		
		return false;
	}

}