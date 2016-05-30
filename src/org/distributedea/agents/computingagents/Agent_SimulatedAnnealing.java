package org.distributedea.agents.computingagents;

import java.util.logging.Level;

import jade.core.behaviours.Behaviour;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents Simulated Annealing Algorithm Method
 * @author stepan
 *
 */
public class Agent_SimulatedAnnealing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

    // initial temperature
	double TEMPERATURE = 10000;

    // cooling rate
	double COOLING_RATE = 0.002;
	
	

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

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
   	protected void startComputing(Problem problem, ProblemTool problemTool, JobID jobID, Behaviour behaviour) throws ProblemToolException {
				
		problemTool.initialization(problem, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateIndividual(problem, getCALogger());
		
		double fitnessI =
				problemTool.fitness(individualI, problem, getCALogger());
		
		//saves data in Agent DataManager
		processIndividualFromInitGeneration(individualI,
    			fitnessI, generationNumberI, problem, jobID);
		
		double temperatureI = TEMPERATURE;
        double coolingRateI = COOLING_RATE;
        
		while (state == CompAgentState.COMPUTING) {
			
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
			if (computingThread.isIndividualDistribution()) {
				distributeIndividualToNeighours(individualI, fitnessI, problem, jobID);
			}
            
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			if (computingThread.isIndividualDistribution() &&
					ProblemToolEvaluation.isFistIndividualWBetterThanSecond(
							recievedIndividualW, fitnessI, problem)) {
				
				IndividualEvaluated recievedIndividual = recievedIndividualW.getIndividualEvaluated();
				
				// update if better that actual
				individualI = recievedIndividual.getIndividual();
				fitnessI = recievedIndividual.getFitness();
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			}
			
            // cooling
            temperatureI *= 1-coolingRateI;
		}
		
		problemTool.exit();
	}

}
