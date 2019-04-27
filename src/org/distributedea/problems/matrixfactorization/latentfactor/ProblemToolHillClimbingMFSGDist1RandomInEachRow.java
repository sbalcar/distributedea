package org.distributedea.problems.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.AProblemToolHillClimbingMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ownsgd.ToolSGDist1RandomInEachRowMF;

public class ProblemToolHillClimbingMFSGDist1RandomInEachRow extends AProblemToolHillClimbingMF {

	private double stepAlpha;

	@Deprecated
	public ProblemToolHillClimbingMFSGDist1RandomInEachRow() {
	}

	/**
	 * Constructor
	 * @param stepAlpha
	 */
	public ProblemToolHillClimbingMFSGDist1RandomInEachRow(double stepAlpha) {
		this.stepAlpha = stepAlpha;
	}

	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_HillClimbing.class);
		return agents;
	}
	
	@Override
	public Arguments exportArguments() {
		Arguments arguments = new Arguments();
		arguments.addArgument(new Argument("stepAlpha", stepAlpha));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		this.stepAlpha = arguments.exportArgument("stepAlpha").exportValueAsDouble();
	}
	
	@Override
	public Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {

		IndividualLatentFactors individualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolSGDist1RandomInEachRowMF.improve(individualLF, stepAlpha, problemMF,
				datasetMF, logger);		
	}
}
