package org.distributedea.agents.computingagents;

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
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

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

		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		Class<?> dataset = problemStruct.getDataset().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		if (dataset == DatasetTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}	
		} else if (dataset == DatasetTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (dataset == DatasetBinPacking.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}			
		} else if (dataset == DatasetContinuousOpt.class) {
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
		IProblemDefinition problemDefinition = problemStruct.getProblemDefinition();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemDefinition, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		// save, log and distribute computed Individual
		IndividualEvaluated individualEvalI = problemTool
				.generateIndividualEval(problemDefinition, dataset,
				pedigreeParams, getCALogger());

		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problemDefinition, jobID);
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
									
			IndividualsEvaluated neighbours = getNeighbours(
					individualEvalI, problemDefinition, dataset, problemTool,
					numberOfNeighbors, pedigreeParams); 
			
			IndividualEvaluated individualEvalNew =
					neighbours.exportTheBestIndividual(problemDefinition);
			
//			IndividualEvaluated individualEvalNew =
//					getNewIndividual(individualEvalI, problemDefinition, dataset, problemTool, pedigreeParams); 
			
			boolean isNewIndividualBetter =
					FitnessTool.isFirstIndividualEBetterThanSecond(
							individualEvalNew, individualEvalI, problemDefinition);

			if (isNewIndividualBetter) {
//				getCALogger().log(Level.INFO, "JUMP " + individualEvalNew.getFitness());
				individualEvalI = individualEvalNew;
			}
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problemDefinition, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problemDefinition, jobID);
			}


			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problemDefinition);
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problemDefinition)) {

				// save and log received Individual
				processRecievedIndividual(individualEvalI, recievedIndividualW,
						generationNumberI, problemDefinition, localSaver);
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
			}
			
		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}

	protected IndividualsEvaluated getNeighbours(IndividualEvaluated individualEval,
			IProblemDefinition problemDef, Dataset dataset, IProblemTool problemTool,
			int numberOfNeighbors, PedigreeParameters pedigreeParams) throws Exception {
		
		IndividualsEvaluated neighbours = new IndividualsEvaluated();
		
		for (int i = 0; i < numberOfNeighbors; i++) {
			
			IndividualEvaluated indivI = getNewIndividual(individualEval,
					problemDef, dataset, problemTool, pedigreeParams);
			neighbours.add(indivI);
		}
		
		return neighbours;
	}

	protected IndividualEvaluated getNewIndividual(IndividualEvaluated individualEval,
			IProblemDefinition problemDef, Dataset dataset, IProblemTool problemTool,
			PedigreeParameters pedigreeParams) throws Exception {
		
		return problemTool.improveIndividualEval(individualEval, problemDef,
				dataset, pedigreeParams, getCALogger());
	}
	
}
