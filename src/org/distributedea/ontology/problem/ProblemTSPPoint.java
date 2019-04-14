package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPPoint;

/**
 * Ontology for definition TSP Point problem
 * @author stepan
 *
 */
public class ProblemTSPPoint extends AProblem {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}
	
	@Override
	public Class<?> exportDatasetClass() {

		return DatasetTSPPoint.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		
		return true;
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemTSPPoint)) {
	        return false;
	    }
	    
	    return true;
	}
	
	@Override
	public String toLogString() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public AProblem deepClone() {
		
		return new ProblemTSPPoint();
	}
}
