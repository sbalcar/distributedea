package org.distributedea.agents.computingagents;

import jade.core.behaviours.Behaviour;

import org.distributedea.InputConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents Brute Force Algorithm Method
 * @author stepan
 *
 */
public class Agent_BruteForce extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		return true;
	}

	@Override
	public void startComputing(Problem problem, Behaviour behaviour) throws ProblemToolException {
		
		if (! isAbleToSolve(problem)) {
			commitSuicide();
			return;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), getLogger());
		problemTool.initialization(problem, getLogger());
		
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateFirstIndividual(problem, getLogger());
		double fitnessI =
				problemTool.fitness(individualI, problem, getLogger());
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem);
		
		while (individualI != null) {
			// increment next number of generation
			generationNumberI++;
			
			individualI =
					problemTool.generateNextIndividual(problem, individualI, getLogger());
			fitnessI =
					problemTool.fitness(individualI, problem, getLogger());
			
			// save, log and distribute new computed Individual
			processComputedIndividual(individualI,
					fitnessI, generationNumberI, problem);
			
			// send new Individual to distributed neighbors
			if (InputConfiguration.individualDistribution) {
				distributeIndividualToNeighours(individualI);
			}
			
			// take received individual to new generation
			Individual recievedIndividual = getRecievedIndividual();
			double recievedFitnessI = problemTool.fitness(recievedIndividual,
					problem, getLogger());
			if (InputConfiguration.individualDistribution &&
					! Double.isNaN(recievedFitnessI) &&
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							recievedFitnessI, fitnessI, problem)) {
				
				// update if better that actual
				individualI = recievedIndividual;
				fitnessI = recievedFitnessI;
				
				// save and log received Individual
				processRecievedIndividual(individualI,
						fitnessI, generationNumberI, problem);
			}
				
		}
				
	}

	@Override
	public void prepareToDie() {
	}

}
