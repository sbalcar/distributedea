package org.distributedea.agents.computingagents;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddescription.MethodDescription;
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
	protected MethodDescription getMethodDescription() {
		
		MethodDescription description = new MethodDescription();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(true);
		description.setExploration(false);
		
		return description;
	}
	
	@Override
	protected Individual getNewIndividual(Individual individual,
			Problem problem, ProblemTool problemTool) {

		return problemTool.generateIndividual(problem, getLogger());
	}
	
}
