package org.distributedea.agents.computingagents;

import java.util.Arrays;
import java.util.logging.Level;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Agent represents Hill Climbing Algorithm Method
 * @author stepan
 *
 */
public class Agent_HillClimbing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	
    // initial number of neighbors
	private String NUMBER_OF_NEIGHBORS = "numberOfNeighbors";
	private int numberOfNeighbors = 10;

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
		
		if (problem.getClass() == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}	
		} else if (problem.getClass() == DatasetTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem.getClass() == DatasetBinPacking.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}			
		} else if (problem.getClass() == DatasetContinuousOpt.class) {
			if (representation == IndividualPoint.class) {
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
		description.setExploitation(false);
		description.setExploration(true);
		
		return description;
	}
	
	protected void processArguments(Arguments arguments) throws Exception {
		
	    // initial number of neighbors
		Argument numberOfNeighborsArg = arguments.exportArgument(NUMBER_OF_NEIGHBORS);
		this.numberOfNeighbors = numberOfNeighborsArg.exportValueAsInteger();
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
		
		problemTool.initialization(dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		// save, log and distribute computed Individual
		IndividualEvaluated individualEvalI = problemTool
				.generateIndividualEval(problem, dataset,
				pedigreeParams, getCALogger());

		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
	
			IndividualEvaluated [] neighbours = getNeighbours(
					individualEvalI, problem, dataset, problemTool,
					numberOfNeighbors, pedigreeParams); 

			Arrays.sort(neighbours, new CmpIndividualEvaluated(problem));
			IndividualEvaluated individualEvalNew = neighbours[0];
			
			boolean isNewIndividualBetter =
					FitnessTool.isFirstIndividualEBetterThanSecond(
							individualEvalNew, individualEvalI, problem);

			if (isNewIndividualBetter) {
				getCALogger().log(Level.INFO, "JUMP new COMPUTED solution " + individualEvalNew.getFitness());
				individualEvalI = individualEvalNew;
			}
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(individualEvalI, problem, jobID);


			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem)) {

				getCALogger().log(Level.INFO, "JUMP new RECEIVED solution " + individualEvalNew.getFitness());
				
				// save and log received Individual
				processRecievedIndividual(individualEvalI, recievedIndividualW,
						generationNumberI, problem, localSaver);
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
			}
			
		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}

	protected IndividualEvaluated[] getNeighbours(IndividualEvaluated individualEval,
			IProblem problem, Dataset dataset, IProblemTool problemTool,
			int numberOfNeighbors, PedigreeParameters pedigreeParams) throws Exception {
		
		IndividualEvaluated[] neighbours = new IndividualEvaluated[numberOfNeighbors];
				
		for (int i = 0; i < numberOfNeighbors; i++) {
			
			IndividualEvaluated indivI = problemTool.improveIndividualEval(individualEval,
					problem, dataset, pedigreeParams, getCALogger());
			neighbours[i] = indivI;
			
			if (state != CompAgentState.COMPUTING) {
				IndividualEvaluated [] shortedNeighbours = new IndividualEvaluated[i+1];
				System.arraycopy(neighbours, 0, shortedNeighbours, 0, i+1);
				return shortedNeighbours;
			}
		}
		
		return neighbours;
	}
	
}
