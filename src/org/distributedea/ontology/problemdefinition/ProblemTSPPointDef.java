package org.distributedea.ontology.problemdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.ProblemTSPPoint;

/**
 * Ontology for definition TSP Point problem
 * @author stepan
 *
 */
public class ProblemTSPPointDef extends AProblemDefinition {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}
	
	@Override
	public Class<?> exportDatasetClass() {

		return ProblemTSPPoint.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		
		return true;
	}

	@Override
	public AProblemDefinition deepClone() {
		
		return new ProblemTSPPointDef();
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemTSPPointDef)) {
	        return false;
	    }
	    
	    return true;
	}
}
