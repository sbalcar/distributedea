package org.distributedea.problems.binpacking.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.binpacking.AProblemToolDifferentialEvolutionBP;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorDifferential;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorTwoPointCrossoverPermutation;

public class ProblemToolDifferentialEvolutionBP extends AProblemToolDifferentialEvolutionBP {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_DifferentialEvolution.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
	}
	
	@Override
	public Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm1 = (IndividualPermutation) individual1;
		IndividualPermutation individualPerm2 = (IndividualPermutation) individual2;
		IndividualPermutation individualPerm3 = (IndividualPermutation) individual3;

		return OperatorDifferential.create(individualPerm1, individualPerm2, individualPerm3);
	}

	@Override
	public Individual cross(Individual individualOld, Individual individualNew,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPermOld = (IndividualPermutation) individualOld;
		IndividualPermutation individualPermNew = (IndividualPermutation) individualNew;
		
		return OperatorTwoPointCrossoverPermutation.crossover(
				individualPermOld, individualPermNew);
	}
	
}
