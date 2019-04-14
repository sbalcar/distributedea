package org.distributedea.agents.computingagents;

import java.util.Vector;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.specific.differentialevolution.DifferentialModel;
import org.distributedea.agents.computingagents.specific.differentialevolution.DifferentialQuaternion;
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
import org.distributedea.problems.IProblemToolDifferentialEvolution;

/**
 * Agent represents Differential Evolution Algorithm Method
 * @author stepan
 *
 */
public class Agent_DifferentialEvolution extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private String POP_SIZE = "popSize";
	private int popSize = 10;
	
	private String CROSS_RATE = "crossRate";
	private double crossRate = 0;
	
	
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
		description.setNumberOfIndividuals(popSize);
		description.setExploitation(true);
		description.setExploration(true);
		
		return description;
	}
	
	protected void processArguments(Arguments arguments) throws Exception {
		
		// set population size
		Argument popSizeArg = arguments.exportArgument(POP_SIZE);
		this.popSize = popSizeArg.exportValueAsInteger();

		// set cross rate
		Argument crossRateArg = arguments.exportArgument(CROSS_RATE);
		this.crossRate = crossRateArg.exportValueAsDouble();
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
		
		IProblemToolDifferentialEvolution problemTool = (IProblemToolDifferentialEvolution) problemToolDef.exportProblemTool(getLogger());
		
		IDatasetDescription datasetDescr = problemWrp.getDatasetDescription();
		Dataset dataset = problemTool.readDataset(datasetDescr, problem, getLogger());
				
		
		this.localSaver = new LocalSaver(this, jobID);
		
		
		problemTool.initialization(problem, dataset, agentConf, methodIDs, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		Vector<IndividualEvaluated> population = new Vector<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualEvalI = problemTool.
					generateIndividualEval(problem, dataset, pedigreeParams, getCALogger());
			
			// send new Individual to distributed neighbors
			readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);

			population.add(individualEvalI);
		}
		
		DifferentialModel model = new DifferentialModel(population, popSize);
		
		final IndividualEvaluated individualEvalI =
				model.getBestIndividual(problem);
		
		// logs data
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
						
			//pick random point from population			
			DifferentialQuaternion quaternion = model.getQuaternion();
			
			IndividualEvaluated individualCandidateI = quaternion.individualCandidateI;
			
			IndividualEvaluated individual1 = quaternion.individual1;
			IndividualEvaluated individual2 = quaternion.individual2;
			IndividualEvaluated individual3 = quaternion.individual3;
			
									
			IndividualEvaluated individualNew = problemTool.
					differentialOfIndividualsEval(individual1, individual2,
					individual3, problem, dataset, pedigreeParams, getCALogger());
			
			if (Math.random() < crossRate) {
				individualNew = problemTool.cross(individualNew, individualCandidateI,
						problem, dataset, pedigreeParams, getCALogger());
			}
			
			
			IndividualEvaluated betterFromCandidateAndNewIndiv = individualCandidateI;
			if (FitnessTool.isFirstIndividualEBetterThanSecond(
							individualNew, individualCandidateI, problem)) {
				// switching Individuals
				model.replaceIndividual(individualCandidateI, individualNew);
				betterFromCandidateAndNewIndiv = individualNew;
			}
			
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualNew, generationNumberI,
					jobID, problem, methodDescription, localSaver);
			
			// send new Individual to distributed neighbors
			IndividualEvaluated theBestOfPopulation = model.getBestIndividual(problem);
			readyToSendIndividualsInserter.insertIndiv(theBestOfPopulation, problem);
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividualSelector.getIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, betterFromCandidateAndNewIndiv, problem)) {
				
				// save and log received Individual
				processRecievedIndividual(betterFromCandidateAndNewIndiv,
						recievedIndividualW, generationNumberI, problem, localSaver);

				model.replaceIndividual(betterFromCandidateAndNewIndiv,
						recievedIndividualW.getIndividualEvaluated());				
			}

		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}

}
