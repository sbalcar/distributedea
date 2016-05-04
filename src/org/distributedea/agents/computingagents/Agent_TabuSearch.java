package org.distributedea.agents.computingagents;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import jade.core.behaviours.Behaviour;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents TabuSearch Algorithm Method
 * @author stepan
 *
 */
public class Agent_TabuSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {
		
		return true;
	}

	@Override
	protected void startComputing(Problem problem, Class<?> problemToolClass, String jobID, Behaviour behaviour) throws ProblemToolException {
		
		ProblemTool problemTool = ProblemToolEvaluation.getProblemToolFromClass(problemToolClass);
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
				fitnessI, generationNumberI, problem, jobID);
		
		while (computingThread.continueInTheNextGeneration()) {
			
			//adjust the size of the taboo on the acceptable limit
			while (tabuList.size() >= tabuListSize) {
				tabuList.poll();
			}
			
			// going through neighbors
			Individual neighborJ = null;
			double neighborFitnessJ = -1;
			
			long neighborIndex = 0;
			while (computingThread.continueInTheNextGeneration()) {
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
				distributeIndividualToNeighours(individualI, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = getRecievedIndividual();
			Individual recievedIndividual = recievedIndividualW.getIndividual();
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
				processRecievedIndividual(recievedIndividualW,
						fitnessI, generationNumberI, problem);
			}
			
		}
		
		problemTool.exit();

	}

}
