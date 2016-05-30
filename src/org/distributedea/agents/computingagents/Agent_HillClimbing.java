package org.distributedea.agents.computingagents;

import jade.core.behaviours.Behaviour;

import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
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
		
		
		if (! isAble) {
			getCALogger().logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException());
		}
		
		return isAble;
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
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem, jobID);
		
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			
			fitnessI = problemTool.fitness(individualI, problem, getCALogger());
			
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
			
		}
		
		problemTool.exit();
	}

	protected Individual getNewIndividual(Individual individual,
			Problem problem, ProblemTool problemTool) throws ProblemToolException {
		
		return problemTool.improveIndividual(individual, problem, getCALogger());
	}
	
}
