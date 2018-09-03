package org.distributedea.problemtools.tsp.gps.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.AProblemToolDifferentialEvolutionTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.operators.OperatorDifferential;

public class ProblemToolDifferentialEvolutionTSPGPS extends AProblemToolDifferentialEvolutionTSPGPS {

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
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		IndividualPermutation ind3 = (IndividualPermutation) individual2;
		
		return OperatorDifferential.create(ind1, ind2, ind3);
	}

}
