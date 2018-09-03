package org.distributedea.problemtools.continuousoptimization.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.continuousoptimization.AProblemToolDifferentialEvolutionCO;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorDifferential;

public class ProblemToolDifferentialEvolutionCO extends AProblemToolDifferentialEvolutionCO {

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
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		return OperatorDifferential.create(individualP1, individualP2,
				individualP3, 1, dataset, logger);
	}
}
