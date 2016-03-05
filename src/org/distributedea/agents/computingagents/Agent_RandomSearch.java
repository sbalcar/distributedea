package org.distributedea.agents.computingagents;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

/**
 * Agent represents Random Search Algorithm Method
 * @author stepan
 *
 */
public class Agent_RandomSearch extends Agent_HillClimbing {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected Individual getNewIndividual(Individual individual,
			Problem problem, ProblemTool problemTool) {

		return problemTool.generateIndividual(problem, getLogger());
	}
	
}
