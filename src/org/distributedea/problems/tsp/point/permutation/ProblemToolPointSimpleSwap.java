package org.distributedea.problems.tsp.point.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolException;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2DSimpleSwap;

/**
 * Represents {@link ProblemTool} for TSP Point {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements simple gene swap algorithm. 
 * @author stepan
 *
 */
public class ProblemToolPointSimpleSwap extends ProblemTSPPointPermutationTool {
	
	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.improveIndividual(individual, problem, logger);
	}

}
