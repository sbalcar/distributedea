package org.distributedea.ontology.problemdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.ProblemTSPGPS;

/**
 * Ontology for definition TSP GPS problem
 * @author stepan
 *
 */
public class ProblemTSPGPSDef extends AProblemDefinition {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}
	
	@Override
	public Class<?> exportDatasetClass() {

		return ProblemTSPGPS.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {

		return true;
	}

	@Override
	public AProblemDefinition deepClone() {

		return new ProblemTSPGPSDef();
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemTSPGPSDef)) {
	        return false;
	    }
	    
	    return true;
	}
}
