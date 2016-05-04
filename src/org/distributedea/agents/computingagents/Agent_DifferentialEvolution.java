package org.distributedea.agents.computingagents;

import java.util.Random;
import java.util.Vector;

import jade.core.behaviours.Behaviour;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

public class Agent_DifferentialEvolution extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problemStruct.getProblemToolClass(), getLogger());
		
		Class<?> problem = problemStruct.getProblem().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		
		if (problem == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemContinousOpt.class) {
			if (representation == IndividualPoint.class) {
				isAble = true;
			}			
		}
		
		return isAble;
	}

	@Override
	protected void startComputing(Problem problem, Class<?> problemToolClass, String jobID, Behaviour behaviour) throws ProblemToolException {
	
		ProblemTool problemTool = ProblemToolEvaluation.getProblemToolFromClass(problemToolClass);
		problemTool.initialization(problem, getLogger());
		
		
		Random random = new Random();
		
		int popSize = 50;
		
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		Vector<Individual> population = new Vector<Individual>();
		for (int i = 0; i < popSize; i++) {
			Individual individualI = problemTool.generateIndividual(problem, getCALogger());
			population.add(individualI);
		}
		
		ResultOfComputing bestGeneratedIndividual =
				getBestIndividual(population, problem, problemTool, getLogger());
		
		Individual individualI = bestGeneratedIndividual.getIndividual();
		double fitnessI = bestGeneratedIndividual.getFitnessValue();
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem, jobID);
		
		
		while (computingThread.continueInTheNextGeneration()) {
			
			// increment next number of generation
			generationNumberI++;
			
			//pick random point from population
			int candidateIndex = Math.abs(random.nextInt()) % (popSize-1);
			
			int index1;
			do {
				index1 = Math.abs(random.nextInt()) % (popSize-1);
			} while (index1 == candidateIndex);

			int index2;
			do {
				index2 = Math.abs(random.nextInt()) % (popSize-1);
			} while (index2 == candidateIndex || index2 == index1);
			
			int index3;
			do {
				index3 = Math.abs(random.nextInt()) % (popSize-1);
			} while (index3 == candidateIndex || index3 == index1 || index3 == index2);
			
			Individual individualCandidate = population.get(candidateIndex);
			fitnessI = problemTool.fitness(individualCandidate, problem, getCALogger());
			
			Individual individual1 = population.get(index1);
			Individual individual2 = population.get(index2);
			Individual individual3 = population.get(index3);
			
			Individual[] individualsNew =
					problemTool.createNewIndividual(individual1, individual2,
							individual3, problem, getCALogger() );
			Individual individualNew = individualsNew[0];

			double fitnessNew =
					problemTool.fitness(individualNew, problem, getCALogger());
			
			boolean isNewIndividualBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitnessNew, fitnessI, problem);
			
			if (isNewIndividualBetter) {
				// switching Individuals
				population.set(candidateIndex, individualNew);				
			}
			
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualNew,
					fitnessNew, generationNumberI, problem);
			
			// send new Individual to distributed neighbors
			if (InputConfiguration.individualDistribution) {
				distributeIndividualToNeighours(individualNew, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = getRecievedIndividual();
			Individual recievedIndividual = recievedIndividualW.getIndividual();
			double recievedFitnessI = problemTool.fitness(recievedIndividual,
					problem, getCALogger());
			if (InputConfiguration.individualDistribution &&
					! Double.isNaN(recievedFitnessI) &&
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							recievedFitnessI, fitnessI, problem) &&
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							recievedFitnessI, fitnessNew, problem)) {
				
				population.set(candidateIndex, recievedIndividual);
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW,
						recievedFitnessI, generationNumberI, problem);
			}
		}
		
		problemTool.exit();
		
	}

	private ResultOfComputing getBestIndividual(Vector<Individual> individuals, Problem problem,
			ProblemTool problemTool, AgentLogger logger) {
		
		if (individuals == null || individuals.size() == 0) {
			return null;
		}
		
		Individual bestIndividual = individuals.get(0);
		double bestFitness = problemTool.fitness(bestIndividual, problem, logger);
		
		for (Individual individualI : individuals) {
			
			double fitnessI = problemTool.fitness(individualI, problem, logger);
			
			boolean isIndividualBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitnessI, bestFitness, problem);
			if (isIndividualBetter) {
				bestIndividual = individualI;
				bestFitness = fitnessI;
			}
			
		}
		
		ResultOfComputing result = new ResultOfComputing();
		result.setFitnessValue(bestFitness);
		result.setBestIndividual(bestIndividual);
		
		return result;
	}


}
