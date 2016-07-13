package org.distributedea.agents.computingagents;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Agent represents Brute Force Algorithm Method
 * @author stepan
 *
 */
public class Agent_BruteForce extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {
		
		return true;
	}

	@Override
	protected MethodDescription getMethodDescription() {
		
		MethodDescription description = new MethodDescription();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(false);
		description.setExploration(true);
		
		return description;
	}
	
	@Override
	protected void startComputing(Problem problem, ProblemTool problemTool,
			JobID jobID, AgentConfiguration agentConf) throws ProblemToolException {
				
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateFirstIndividual(problem, getLogger());
		double fitnessI =
				problemTool.fitness(individualI, problem, getLogger());
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem, jobID);
		
		while (individualI != null && state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			individualI =
					problemTool.generateNextIndividual(problem, individualI, getLogger());
			fitnessI =
					problemTool.fitness(individualI, problem, getLogger());
			
			// save, log and distribute new computed Individual
			processComputedIndividual(individualI,
					fitnessI, generationNumberI, problem, jobID);
			
			// send new Individual to distributed neighbors
			if (computingThread.isIndividualDistribution()) {
				distributeIndividualToNeighours(individualI, fitnessI, problem, jobID);
			}
			
			// take received individual to new generation
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


}
