package org.distributedea.problemtools.tsp.gps.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.AProblemToolBruteForceTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolGenerateFirstIndividualTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;

public class ProblemToolBruteForceTSPGPS extends AProblemToolBruteForceTSPGPS {

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

		DatasetTSPGPS datasetTSPGPS =  (DatasetTSPGPS) dataset;
		
		return ToolGenerateFirstIndividualTSPGPS.generateFirstIndividual(datasetTSPGPS, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualPermutation individualPermutation = (IndividualPermutation) individual;
		
		return ToolNextPermutationTSPGPS.nextPermutation(individualPermutation);
	}

}
