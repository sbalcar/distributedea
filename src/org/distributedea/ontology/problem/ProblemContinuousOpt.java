package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;

/**
 * Ontology for definition Continuous optimization problem
 * @author stepan
 *
 */
public class ProblemContinuousOpt extends AProblem {

	private static final long serialVersionUID = 1L;
	
	private boolean isMaximizationProblem;

	@Deprecated
	public ProblemContinuousOpt() {} // only for Jade
	
	/**
	 * Constructor
	 * @param isMaximizationProblem
	 */
	public ProblemContinuousOpt(boolean isMaximizationProblem) {
		
		this.isMaximizationProblem = isMaximizationProblem;
	}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public ProblemContinuousOpt(ProblemContinuousOpt problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemContinuousOpt.class.getSimpleName() + " is not valid");
		}
		this.isMaximizationProblem = problem.isMaximizationProblem();
	}

	
	public boolean isMaximizationProblem() {
		
		return isMaximizationProblem;
	}
	@Deprecated
	public void setMaximizationProblem(boolean isMaximizationProblem) {
		
		this.isMaximizationProblem = isMaximizationProblem;
	}

	@Override
	public boolean exportIsMaximizationProblem() {
		
		return isMaximizationProblem();
	}
	
	@Override
	public Class<?> exportDatasetClass() {

		return DatasetContinuousOpt.class;
	}	

	@Override
	public boolean valid(IAgentLogger logger) {
		
		return true;
	}

	@Override
	public ProblemContinuousOpt deepClone() {
		
		return new ProblemContinuousOpt(this);
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemContinuousOpt)) {
	        return false;
	    }
	    
	    IProblem otherProblem = (ProblemContinuousOpt)other;
	    
	    return isMaximizationProblem() == otherProblem.exportIsMaximizationProblem();
	}
}
