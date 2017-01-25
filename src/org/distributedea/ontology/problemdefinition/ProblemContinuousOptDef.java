package org.distributedea.ontology.problemdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;

/**
 * Ontology for definition Continuous optimization problem
 * @author stepan
 *
 */
public class ProblemContinuousOptDef extends AProblemDefinition {

	private static final long serialVersionUID = 1L;
	
	private boolean isMaximizationProblem;

	@Deprecated
	public ProblemContinuousOptDef() {} // only for Jade
	
	/**
	 * Constructor
	 * @param isMaximizationProblem
	 */
	public ProblemContinuousOptDef(boolean isMaximizationProblem) {
		
		this.isMaximizationProblem = isMaximizationProblem;
	}
	
	/**
	 * Copy constructor
	 * @param problemDef
	 */
	public ProblemContinuousOptDef(ProblemContinuousOptDef problemDef) {
		if (problemDef == null || ! problemDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemContinuousOptDef.class.getSimpleName() + " is not valid");
		}
		this.isMaximizationProblem = problemDef.isMaximizationProblem();
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
	public ProblemContinuousOptDef deepClone() {
		
		return new ProblemContinuousOptDef(this);
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemContinuousOptDef)) {
	        return false;
	    }
	    
	    IProblemDefinition otherDef = (ProblemContinuousOptDef)other;
	    
	    return isMaximizationProblem() == otherDef.exportIsMaximizationProblem();
	}
}
