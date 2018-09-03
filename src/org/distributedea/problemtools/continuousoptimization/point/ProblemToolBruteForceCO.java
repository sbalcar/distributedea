package org.distributedea.problemtools.continuousoptimization.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problemtools.continuousoptimization.AProblemToolBruteForceCO;
import org.distributedea.problemtools.continuousoptimization.point.tools.ToolGenerateNextIndividualCO;

public class ProblemToolBruteForceCO extends AProblemToolBruteForceCO {

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
		
		return generateIndividual(problem, dataset, logger);
	}
	
	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {

		ProblemContinuousOpt problemCO = (ProblemContinuousOpt)problem;
		DatasetContinuousOpt datasetCO = (DatasetContinuousOpt) dataset;
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return ToolGenerateNextIndividualCO.create(individualPoint,
				problemCO, datasetCO, logger);
	}
}
