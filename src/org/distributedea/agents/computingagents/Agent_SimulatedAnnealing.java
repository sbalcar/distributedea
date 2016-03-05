package org.distributedea.agents.computingagents;

import java.util.logging.Level;

import jade.core.behaviours.Behaviour;

import org.distributedea.InputConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

public class Agent_SimulatedAnnealing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

    // initial temperature
	double TEMPERATURE = 10000;

    // cooling rate
	double COOLING_RATE = 0.002;
	
	
	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {

		return true;
	}

	
	/**
	 * calculate the acceptance probability
	 * @param energy
	 * @param newEnergy
	 * @param temperature
	 * @return
	 */
    private double acceptanceProbability(double energy, double newEnergy,
    		double temperature, Problem problem) {
    	
        // accept the new better solution
        if (ProblemToolEvaluation.isFistFitnessBetterThanSecond(newEnergy, energy, problem)) {
            return 1.0;
        }
        // for worse solution calculates an acceptance probability
        return Math.exp(-1 * Math.abs(energy - newEnergy) / temperature);
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
		
		//saves data in Agent DataManager
        processComputedIndividual(individualI,
    			fitnessI, generationNumberI, problem);
		
		double temperatureI = TEMPERATURE;
        double coolingRateI = COOLING_RATE;
        
		while (true) {
			// increment next number of generation
			generationNumberI++;
			
			if (temperatureI <= 1) {
				temperatureI = TEMPERATURE;
			}
			
			Individual individualNewI = null;
			try {
				individualNewI =
						problemTool.improveIndividual(individualI, problem, getCALogger());
			} catch (ProblemToolException e) {
				getCALogger().log(Level.INFO, "Problem to improve the individual");
				commitSuicide();
			}
            
			double fitnessNewI =
					problemTool.fitness(individualNewI, problem, getCALogger());
			
            // decide on the acceptance the neighbour
			double acceptanceProbability =
					acceptanceProbability(fitnessI, fitnessNewI, temperatureI, problem);
            if (acceptanceProbability > Math.random()) {
            	individualI = individualNewI;
            	fitnessI = fitnessNewI;
            }
            
			//saves data in Agent DataManager
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
			
            // cooling
            temperatureI *= 1-coolingRateI;
		}
		
	}

	@Override
	public void prepareToDie() {
		
	}

}
