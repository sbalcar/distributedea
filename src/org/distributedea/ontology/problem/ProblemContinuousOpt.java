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
	
	/** Identification of function */
	private String functionID;
	
	/** Size of space (the number of intervals) */
	private int dimension;
	
	private boolean isMaximizationProblem;

	@Deprecated
	public ProblemContinuousOpt() {} // only for Jade
	
	/**
	 * Constructor
	 * @param isMaximizationProblem
	 */
	public ProblemContinuousOpt(String functionID, int dimension,
			boolean isMaximizationProblem) {
		
		setFunctionID(functionID);
		setDimension(dimension);
		setMaximizationProblem(isMaximizationProblem);
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
		
		this.functionID = problem.getFunctionID();
		this.dimension = problem.getDimension();
		this.isMaximizationProblem = problem.isMaximizationProblem();
	}

	public String getFunctionID() {
		return functionID;
	}
	@Deprecated
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}
	
	public int getDimension() {
		return dimension;
	}
	@Deprecated
	public void setDimension(int dimension) {
		this.dimension = dimension;
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
		
		if (functionID == null) {
			return false;
		}
		if (dimension < 1) {
			return false;
		}
		
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
