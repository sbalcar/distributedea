package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

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
		

		IProblem problem = problemStruct.getProblem();
		
		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		//Class<?> dataset = problemStruct.getDataset().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		if (problem instanceof ProblemTSPGPS) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemTSPPoint) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemBinPacking) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemContinuousOpt) {
			if (representation == IndividualPoint.class) {
				isAble = true;
			}			
		} else if (problem instanceof ProblemMachineLearning) {
			if (representation == IndividualArguments.class) {
				isAble = true;
			}			
		}
		
		return isAble;
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
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(problem, dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		IndividualEvaluated individualEvalI =
				problemTool.generateIndividualEval(problem, dataset, pedigreeParams, getCALogger());
		
		// send new Individual to distributed neighbors
		distributeIndividualToNeighours(individualEvalI, problem, jobID);
		
		// logs data
		processIndividualFromInitGeneration(individualEvalI,
    			generationNumberI, problem, jobID);
		
		double temperatureI = temperature;
        double coolingRateI = coolingRate;
        
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			if (temperatureI <= 1) {
				temperatureI = temperature;
			}
			
			IndividualEvaluated individualEvalNewI = 
					problemTool.improveIndividualEval(individualEvalI, problem, dataset,
					pedigreeParams, getCALogger());
	
            // decide on the acceptance the neighbour
			double acceptanceProbability = acceptanceProbability(individualEvalI,
					individualEvalNewI, temperatureI, problem);
            if (acceptanceProbability > Math.random()) {
            	individualEvalI = individualEvalNewI;
            }
            
			//saves data in Agent DataManager
            processComputedIndividual(individualEvalI,
        			generationNumberI, problem, jobID, localSaver);
            
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(individualEvalI, problem, jobID);
            
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem)) {
				
				// save and log received Individual
				processRecievedIndividual(individualEvalI, recievedIndividualW,
						generationNumberI, problem, localSaver);
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
			}
			
            // cooling
            temperatureI *= 1-coolingRateI;
		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}

   	/**
   	 * Calculate the acceptance probability
   	 * @param individual
   	 * @param individualNew
   	 * @param temperature
   	 * @param problem
   	 * @return
   	 */
	private double acceptanceProbability(IndividualEvaluated individual, IndividualEvaluated individualNew,
    		double temperature, IProblem problem) {
		
		double energy = individual.getFitness();
		double newEnergy = individualNew.getFitness();
		
        // accept the new better solution
        if (FitnessTool.isFistFitnessBetterThanSecond(newEnergy, energy, problem)) {
            return 1.0;
        }
        // for worse solution calculates an acceptance probability
        return Math.exp(-1 * Math.abs(energy - newEnergy) / temperature);
	}
	    
}
