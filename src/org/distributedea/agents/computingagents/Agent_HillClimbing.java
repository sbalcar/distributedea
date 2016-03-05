package org.distributedea.agents.computingagents;

import jade.core.behaviours.Behaviour;

import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
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
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
			
		}
		
		
		if (! isAble) {
			getCALogger().logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException());
		}
		
		return isAble;
	}

	@Override
	public void startComputing(Problem problem, Behaviour behaviour) {
		
		if (! isAbleToSolve(problem)) {
			getCALogger().log(Level.INFO, "Agent can't solve this Problem");
			commitSuicide();
			return;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), getCALogger());
		
		
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateIndividual(problem, getCALogger());
		
		double fitnessI =
				problemTool.fitness(individualI, problem, getCALogger());
		
		// save, log and distribute computed Individual
		processComputedIndividual(individualI,
				fitnessI, generationNumberI, problem);
		
		
		while (true) {
			// increment next number of generation
			generationNumberI++;
			
			
			fitnessI = problemTool.fitness(individualI, problem, getCALogger());
						
			logResultByUsingDatamanager(generationNumberI, fitnessI);
			
			Individual individualNew = null;
			try {
				individualNew = getNewIndividual(individualI, problem, problemTool);
			} catch (ProblemToolException e) {
				getCALogger().log(Level.INFO, "Problem to generate a new individual");
				commitSuicide();
				return;
			}
			
			double fitnessNew =
					problemTool.fitness(individualNew, problem, getCALogger());
			
			boolean isNewIndividualBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitnessNew, fitnessI, problem);

			if (isNewIndividualBetter) {
				getCALogger().log(Level.INFO, "JUMP");
				fitnessI = fitnessNew;
				individualI = individualNew;
			}
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualI,
					fitnessI, generationNumberI, problem);
			
			// send new Individual to distributed neighbors
			if (InputConfiguration.individualDistribution) {
				distributeIndividualToNeighours(individualI);
			}
			
			//take received individual to new generation
			Individual recievedIndividual = getRecievedIndividual();
			double recievedFitnessI = problemTool.fitness(recievedIndividual,
					problem, getCALogger());
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

	protected Individual getNewIndividual(Individual individual,
			Problem problem, ProblemTool problemTool) throws ProblemToolException {
		
		return problemTool.improveIndividual(individual, problem, getCALogger());
	}
	
}
