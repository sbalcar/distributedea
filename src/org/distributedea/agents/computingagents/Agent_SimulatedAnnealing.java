package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
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
	private String TEMPERATURE = "temperature";
	private double temperature = 10000;

    // cooling rate
	private String COOLING_RATE = "coolingRate";
	private double coolingRate = 0.002;
	
	

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

	protected void processArguments(Arguments arguments) throws Exception {
		
		// set temperature value
		Argument temperatureArg = arguments.exportArgument(TEMPERATURE);
		this.temperature = temperatureArg.exportValueAsInteger();
		
		// set cooling rate value
		Argument coolingRateArg = arguments.exportArgument(COOLING_RATE);
		this.coolingRate = coolingRateArg.exportValueAsInteger();
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
		IProblemDefinition problemDefinition = problemStruct.getProblemDefinition();
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemDefinition, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		IndividualEvaluated individualEvalI =
				problemTool.generateIndividualEval(problemDefinition, problem, pedigreeParams, getCALogger());
		
		//saves data in Agent DataManager
		processIndividualFromInitGeneration(individualEvalI,
    			generationNumberI, problemDefinition, jobID);
		
		double temperatureI = temperature;
        double coolingRateI = coolingRate;
        
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			if (temperatureI <= 1) {
				temperatureI = temperature;
			}
			
			IndividualEvaluated individualEvalNewI = 
					problemTool.improveIndividualEval(individualEvalI, problemDefinition, problem,
					pedigreeParams, getCALogger());
	
            // decide on the acceptance the neighbour
			double acceptanceProbability = acceptanceProbability(individualEvalI,
					individualEvalNewI, temperatureI, problemDefinition);
            if (acceptanceProbability > Math.random()) {
            	individualEvalI = individualEvalNewI;
            }
            
			//saves data in Agent DataManager
            processComputedIndividual(individualEvalI,
        			generationNumberI, problemDefinition, jobID);
            
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problemDefinition, jobID);
			}
            
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problemDefinition);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problemDefinition)) {
								
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problemDefinition);
			}
			
            // cooling
            temperatureI *= 1-coolingRateI;
		}
		
		problemTool.exit();
	}

   	
	private double acceptanceProbability(IndividualEvaluated individual, IndividualEvaluated individualNew,
    		double temperature, IProblemDefinition problemDef) {
		
		double energy = individual.getFitness();
		double newEnergy = individualNew.getFitness();
		
		return acceptanceProbability(energy, newEnergy, temperature, problemDef);
	}
	
	/**
	 * calculate the acceptance probability
	 * @param energy
	 * @param newEnergy
	 * @param temperature
	 * @return
	 */
    private double acceptanceProbability(double energy, double newEnergy,
    		double temperature, IProblemDefinition problemDef) {
    	
        // accept the new better solution
        if (FitnessTool.isFistFitnessBetterThanSecond(newEnergy, energy, problemDef)) {
            return 1.0;
        }
        // for worse solution calculates an acceptance probability
        return Math.exp(-1 * Math.abs(energy - newEnergy) / temperature);
    }
    
}
