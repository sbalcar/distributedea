package org.distributedea.agents.computingagents;

import java.util.logging.Level;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.results.PartResult;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents Hill Climbing Algorithm Method
 * @author stepan
 *
 */
public class Agent_HillClimbing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	
	@Override
	public void prepareToDie() {
		
		// deregistre agent from DF
		deregistrDF();
	}
	
	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		boolean isAble = false;
		
		if (problem == ProblemTSPGPS.class) {
			
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
			commitSuicide();
			return;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), logger);
		
		
		Individual individual =
				problemTool.generateIndividual(problem, logger);
		
		long generation = -1;
		
		while (true) {
			
			double fitness =
					problemTool.fitness(individual, problem, logger);
			
			PartResult resultI = new PartResult();
			resultI.setGenerationNumber(generation);
			resultI.setFitnessResult(fitness);
			
			logResultByUsingDatamanager(resultI);
			
			Individual individualNew = null;
			try {
				individualNew = getNewIndividual(individual, problem, problemTool);
			} catch (ProblemToolException e) {
				commitSuicide();
				return;
			}
			
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
			Problem problem, ProblemTool problemTool) throws ProblemToolException {
		
		return problemTool.improveIndividual(individual, problem, logger);
	}
	
}
