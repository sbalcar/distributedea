package org.distributedea.problemtools.tsp.point.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.permutation.operators.Operator2Opt;
import org.distributedea.problemtools.tsp.gps.permutation.operators.OperatorSinglePointCrossover;
import org.distributedea.problemtools.tsp.point.AProblemToolEvolutionTSPPoint;

public class ProblemToolEvolutionTSPPointSinglePointCross2optMutation extends AProblemToolEvolutionTSPPoint {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
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
	public Individual improveIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return Operator2Opt.create(individualPerm);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return OperatorSinglePointCrossover.crossover(ind1, ind2);
	}

}
