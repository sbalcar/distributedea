package org.distributedea.problems.tsp.point.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolException;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

/**
 * Represents {@link ProblemTool} for TSP {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements 2opt algorithm. 
 * @author stepan
 *
 */
public class ProblemToolPoint2opt extends ProblemTSPPointPermutationTool {

	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
		
		Class<?> individualClass = IndividualPermutation.class;
		Class<?> problemClass = problemWhichSolves();
		
		ProblemToolGPSEuc2D2opt tool = new ProblemToolGPSEuc2D2opt();
		return tool.improveIndividual(individual, problem, individualClass, problemClass, logger);
	}
	
}
