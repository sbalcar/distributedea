package org.distributedea.problemtools.tsp.point.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.permutation.operators.Operator2Opt;
import org.distributedea.problemtools.tsp.point.AProblemToolSimulatedAnnealingTSPPoint;

public class ProblemToolSimulatedAnnealingTSPPoint2opt extends AProblemToolSimulatedAnnealingTSPPoint {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_SimulatedAnnealing.class);
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
	public Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return Operator2Opt.create(individualPerm);
	}
}
