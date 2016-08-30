package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;

/**
 * Agent represents Simulated Annealing Algorithm Method
 * @author stepan
 *
 */
public class Agent_SimulatedAnnealing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

    // initial temperature
	private double TEMPERATURE = 10000;

    // cooling rate
	private double COOLING_RATE = 0.002;
	
	

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		return true;
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(true);
		description.setExploration(true);
		
		return description;
	}
	
   	@Override
	protected void startComputing(ProblemStruct problemStruct,
			AgentConfiguration agentConf) throws Exception {
		
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		
				
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		IndividualEvaluated individualEvalI =
				problemTool.generateIndividualEval(problem, getCALogger());
		
		//saves data in Agent DataManager
		processIndividualFromInitGeneration(individualEvalI,
    			generationNumberI, problem, jobID);
		
		double temperatureI = TEMPERATURE;
        double coolingRateI = COOLING_RATE;
        
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			if (temperatureI <= 1) {
				temperatureI = TEMPERATURE;
			}
			
			IndividualEvaluated individualEvalNewI = 
					problemTool.improveIndividualEval(individualEvalI.getIndividual(), problem, getCALogger());
	
            // decide on the acceptance the neighbour
			double acceptanceProbability = acceptanceProbability(individualEvalI,
					individualEvalNewI, temperatureI, problem);
            if (acceptanceProbability > Math.random()) {
            	individualEvalI = individualEvalNewI;
            }
            
			//saves data in Agent DataManager
            processComputedIndividual(individualEvalI,
        			generationNumberI, problem, jobID);
            
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problem, jobID);
			}
            
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem)) {
								
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			}
			
            // cooling
            temperatureI *= 1-coolingRateI;
		}
		
		problemTool.exit();
	}

   	
	private double acceptanceProbability(IndividualEvaluated individual, IndividualEvaluated individualNew,
    		double temperature, Problem problem) {
		
		double energy = individual.getFitness();
		double newEnergy = individualNew.getFitness();
		
		return acceptanceProbability(energy, newEnergy, temperature, problem);
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
        if (FitnessTool.isFistFitnessBetterThanSecond(newEnergy, energy, problem)) {
            return 1.0;
        }
        // for worse solution calculates an acceptance probability
        return Math.exp(-1 * Math.abs(energy - newEnergy) / temperature);
    }
    
}
