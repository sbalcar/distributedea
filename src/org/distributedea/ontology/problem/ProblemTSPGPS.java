package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPGPS;

/**
 * Ontology for definition TSP GPS problem
 * @author stepan
 *
 */
public class ProblemTSPGPS extends AProblem {

	private static final long serialVersionUID = 1L;	
	
	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}
	
	@Override
	public Class<?> exportDatasetClass() {

		return DatasetTSPGPS.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {

		return true;
	}

	@Override
	public AProblem deepClone() {

		return new ProblemTSPGPS();
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemTSPGPS)) {
	        return false;
	    }
	    
	    return true;
	}
}
