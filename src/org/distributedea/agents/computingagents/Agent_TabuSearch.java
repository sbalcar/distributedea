package org.distributedea.agents.computingagents;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

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
 * Agent represents TabuSearch Algorithm Method
 * @author stepan
 *
 */
public class Agent_TabuSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	
	private TabuModel tabu = new TabuModel();
	
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
	protected void startComputing(Problem problem, ProblemTool problemTool, JobID jobID,
			AgentConfiguration agentConf) throws ProblemToolException {
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
        
		long generationNumberI = -1;
		
		Individual individualI =
				problemTool.generateFirstIndividual(problem, getCALogger());
		double fitnessI =
				problemTool.fitness(individualI, problem, getCALogger());
		
		// add actual individual in the Tabu Set
		tabu.offer(individualI);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualI,
				fitnessI, generationNumberI, problem, jobID);
		
		while (state == CompAgentState.COMPUTING) {
			
			// going through neighbors
			Individual neighborJ = null;
			double neighborFitnessJ = -1;
			
			long neighborIndex = 0;
			while (state == CompAgentState.COMPUTING) {
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
				if (isNeighborlBetter && (! tabu.contains(neighborJ)) ) {
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
			tabu.offer(individualI);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualI,
					fitnessI, generationNumberI, problem, jobID);
			
			// send new Individual to distributed neighbors
			if (computingThread.isIndividualDistribution()) {
				distributeIndividualToNeighours(individualI, fitnessI, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			boolean isReceivedBetter =
					ProblemToolEvaluation.isFistIndividualWBetterThanSecond(
							recievedIndividualW, fitnessI, problem);

			if (computingThread.isIndividualDistribution() &&
					(! tabu.contains(recievedIndividualW)) &&
					isReceivedBetter) {
				
				IndividualEvaluated recievedIndividual = recievedIndividualW.getIndividualEvaluated();
				
				// update if better that actual
				individualI = recievedIndividual.getIndividual();
				fitnessI = recievedIndividual.getFitness();
				
				tabu.offer(individualI);
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW,
						generationNumberI, problem);
			}
			
		}
		
		problemTool.exit();

	}

}


class TabuModel {
	
	private int tabuListSize = 500;
	
	private Queue<Individual> tabuList = new LinkedList<Individual>();
	
	public boolean contains(IndividualWrapper individualWrapper) {
		
		if (individualWrapper == null) {
			return false;
		}
		
		Individual individual = individualWrapper.getIndividualEvaluated().getIndividual();
		
		return tabuList.contains(individual);
	}

	public boolean contains(Individual individual) {
		
		if (individual == null) {
			return false;
		}
		
		return tabuList.contains(individual);
	}
	
	public void offer(Individual individual) {
		
		resize();
		
		tabuList.offer(individual);
	}
	
	
	private void resize() {
		
		//adjust the size of the taboo on the acceptable limit
		while (tabuList.size() >= tabuListSize) {
			tabuList.poll();
		}
	}
}