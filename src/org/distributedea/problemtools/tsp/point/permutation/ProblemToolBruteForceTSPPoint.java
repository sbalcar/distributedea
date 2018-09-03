package org.distributedea.problemtools.tsp.point.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;
import org.distributedea.problemtools.tsp.point.AProblemToolBruteForceTSPPoint;
import org.distributedea.problemtools.tsp.point.permutation.tools.ToolGenerateFirstIndividualTSPPoint;

public class ProblemToolBruteForceTSPPoint extends AProblemToolBruteForceTSPPoint {

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
	public Individual generateFirstIndividual(IProblem proble,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint)dataset;
		
		return ToolGenerateFirstIndividualTSPPoint.generate(datasetTSPPoint, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		List<Integer> perm = individualPerm.getPermutation();
		
		List<Integer> permNew = ToolNextPermutationTSPGPS.nextPermutation(perm);
		
		return new IndividualPermutation(permNew);
	}

}
