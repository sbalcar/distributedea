package org.distributedea.problemtools.binpacking.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.binpacking.AProblemToolBruteForceBP;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolGenerateFirstIndividualBinPacking;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolNextPermutationBinPacking;

public class ProblemToolBruteForceBP extends AProblemToolBruteForceBP {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_BruteForce.class);
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
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		DatasetBinPacking problemBinPacking = (DatasetBinPacking) dataset;

		return ToolGenerateFirstIndividualBinPacking.generateFirstIndividual(problemBinPacking, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex, IAgentLogger logger) {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return ToolNextPermutationBinPacking.nextPermutation(individualPerm);
	}
	
}
