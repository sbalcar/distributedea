package org.distributedea.agents.computingagents;

import java.util.logging.Level;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;


public class Agent_HillClimbing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	
	@Override
	public void prepareToDie() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		boolean isAble = false;
		
		if (problem == ProblemTSP.class) {
			
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
			
		}
		
		if (! isAble) {
			logger.logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException());
		}
		
		return isAble;
	}

	@Override
	public void startComputing(Problem problem) {
		
		if (! isAbleToSolve(problem)) {
			return;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), logger);
		
		
		Individual individual =
				problemTool.generateIndividual(problem, logger);
		
		long generation = 0;
		
		while (true) {
			logger.log(Level.INFO, "Generation: " + generation);
			
			double fitness =
					problemTool.fitness(individual, problem, logger);
			logger.log(Level.INFO, "Finess: " + fitness);
			
			Individual individualNew =
					getNewIndividual(individual, problem, problemTool);
			
			double fitnessNew =
					problemTool.fitness(individualNew, problem, logger);
			
			if (fitnessNew < fitness) {
				logger.log(Level.INFO, "JUMP");
				fitness = fitnessNew;
				individual = individualNew;
			}
			
			generation++;
		}
	}

	protected Individual getNewIndividual(Individual individual,
			Problem problem, ProblemTool problemTool) {
		
		return problemTool.improveIndividual(individual, problem, logger);
	}
	
}
