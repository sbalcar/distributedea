package org.distributedea.agents.computingagents;

import java.util.Arrays;
import java.util.logging.Level;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.universal.CompAgentState;
import org.distributedea.agents.computingagents.universal.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.ProblemEVCharging;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.IProblemToolHillClimbing;
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
	protected boolean isAbleToSolve(ProblemWrapper problemWrp) {

		IProblem problem = problemWrp.getProblem();
		
		ProblemToolDefinition problemToolDef =
				problemWrp.getProblemToolDefinition();
		IProblemTool problemTool = problemToolDef.exportProblemTool(getLogger());
		
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
		} else if (problem instanceof ProblemVertexCover) {
			if (representation == IndividualSet.class) {
				isAble = true;
			}			
		} else if (problem instanceof ProblemMatrixFactorization) {
			if (representation == IndividualLatentFactors.class) {
				isAble = true;
			}			
		} else if (problem instanceof ProblemEVCharging) {
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
	protected void startComputing(ProblemWrapper problemWrp,
			IslandModelConfiguration islandModelConf, AgentConfiguration agentConf, MethodIDs methodIDs) throws Exception {
		
  		if (problemWrp == null || ! problemWrp.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		if (islandModelConf == null || ! islandModelConf.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		if (agentConf == null || ! agentConf.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		
		this.state = CompAgentState.INITIALIZATION;
		
		JobID jobID = problemWrp.getJobID();
		ProblemToolDefinition problemToolDef = problemWrp.getProblemToolDefinition();
		IProblem problem = problemWrp.getProblem();
		boolean individualDistribution = islandModelConf.isIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, methodIDs, problem, problemToolDef);
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemWrp.getPedigreeDefinition(), methodDescription);
		
		IProblemToolHillClimbing problemTool = (IProblemToolHillClimbing) problemToolDef.exportProblemTool(getLogger());
		
		IDatasetDescription datasetDescr = problemWrp.getDatasetDescription();
		Dataset dataset = problemTool.readDataset(datasetDescr, problem, getLogger());
				
		
		this.localSaver = new LocalSaver(this, jobID);
		
		
		problemTool.initialization(problem, dataset, agentConf, methodIDs, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;

		// save, log and distribute computed Individual
		IndividualEvaluated individualEvalI = problemTool
				.generateIndividualEval(problem, dataset,
				pedigreeParams, getCALogger());

		// send new Individual to distributed neighbors
		readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);

		// logs data
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
					generationNumberI, jobID, problem, methodDescription, localSaver);
			
			// send new Individual to distributed neighbors
			readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);

			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividualSelector.getIndividual(problem);
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
			IProblem problem, Dataset dataset, IProblemToolHillClimbing problemTool,
			int numberOfNeighbors, PedigreeParameters pedigreeParams) throws Exception {
		
		IndividualEvaluated[] neighbours = new IndividualEvaluated[numberOfNeighbors];
				
		for (int i = 0; i < numberOfNeighbors; i++) {
			
			IndividualEvaluated indivI = problemTool.getNeighborEval(individualEval,
					problem, dataset, 0, pedigreeParams, getCALogger());
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
