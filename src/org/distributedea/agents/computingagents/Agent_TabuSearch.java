package org.distributedea.agents.computingagents;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import jade.core.behaviours.Behaviour;

import org.distributedea.InputConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents TabuSearch Algorithm Method
 * @author stepan
 *
 */
public class Agent_TabuSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		return true;
	}

	@Override
	public void startComputing(Problem problem, Behaviour behaviour) throws ProblemToolException {
		
		if (! isAbleToSolve(problem)) {
			getCALogger().log(Level.INFO, "Agent can't solve this Problem");
			commitSuicide();
			return;
		}
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), getCALogger());
		problemTool.initialization(problem, getLogger());

        int tabuListSize = 500;
		Queue<Individual> tabuList = new LinkedList<Individual>();
        
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateFirstIndividual(problem, getCALogger());
		double fitnessI =
				problemTool.fitness(individualI, problem, getCALogger());
		
		// add actual individual in the Tabu Set
		tabuList.offer(individualI);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem);
		
		while (true) {
			
			//adjust the size of the taboo on the acceptable limit
			while (tabuList.size() >= tabuListSize) {
				tabuList.poll();
			}
			
			// going through neighbors
			Individual neighborJ = null;
			double neighborFitnessJ = -1;
			
			long neighborIndex = 0;
			while (true) {
				// increment next number of generation
				generationNumberI++;
				
				try {
					neighborJ = problemTool.getNeighbor(individualI, problem,
							neighborIndex, getCALogger());
				} catch (ProblemToolException e) {
					getCALogger().log(Level.INFO, "Problem to find the neighbour to individual");
					commitSuicide();
					return;
				}
				
				// not available next better neighbor 
				if (neighborJ == null) {
					break;
				}
				
				neighborFitnessJ =
					problemTool.fitness(neighborJ, problem, getCALogger());
				
				boolean isNeighborlBetter =
						ProblemToolEvaluation.isFistFitnessBetterThanSecond(
								neighborFitnessJ, fitnessI, problem);
				
				// new better indiviual found
				if (isNeighborlBetter && ! tabuList.contains(neighborJ) ) {
					break;
				}
				
				neighborIndex++;
			}
			
			individualI = neighborJ;
			fitnessI = neighborFitnessJ;
			// generate new individual in local extreme
			if (individualI == null) {
			    individualI = problemTool.generateFirstIndividual(problem, getCALogger());
			    fitnessI = problemTool.fitness(individualI, problem, getCALogger());
			}
			
			// add actual individual in the Tabu Set
			tabuList.offer(individualI);
			
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
					! tabuList.contains(recievedIndividual) &&
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							recievedFitnessI, fitnessI, problem)) {
				
				// update if better that actual
				individualI = recievedIndividual;
				fitnessI = recievedFitnessI;
				
				tabuList.offer(individualI);
				
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
