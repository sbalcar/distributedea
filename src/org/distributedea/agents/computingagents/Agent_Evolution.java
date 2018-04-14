package org.distributedea.agents.computingagents;


import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.evolution.EvolutionPopulationModel;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.ISelector;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.Selector;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
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
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Agent represents Evolution Algorithm Method
 * @author stepan
 *
 */
public class Agent_Evolution extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private String POP_SIZE = "popSize";
	private int popSize = 10;
	
	private String MUTATION_RATE = "mutationRate";
	private double mutationRate = 0.9;
	
	private String CROSS_RATE = "crossRate";
	private double crossRate = 0.1;
	
	private String SELECTOR = "selector";
	private ISelector selector;
	
	
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
		} else if (problem instanceof ProblemVertexCover) {
			if (representation == IndividualSet.class) {
				isAble = true;
			}			
		} else if (problem instanceof ProblemMatrixFactorization) {
			if (representation == IndividualLatentFactors.class) {
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
		
		// set mutation rate
		Argument mutationRateArg = arguments.exportArgument(MUTATION_RATE);
		this.mutationRate = mutationRateArg.exportValueAsDouble();
		
		// set selector
		Argument selectorArg = arguments.exportArgument(SELECTOR);
		
		Class<?> selectorClass = selectorArg.exportValueAsClass();
		
		this.selector = Selector.createInstance(selectorClass);
		
	}
	
	@Override
	protected void startComputing(ProblemStruct problemStruct,
			IslandModelConfiguration configuration, AgentConfiguration agentConf) throws Exception {
	
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		if (agentConf == null || ! agentConf.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = configuration.isIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);

		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(problem, dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		IndividualEvaluated[] individuals = new IndividualEvaluated[popSize];
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualEvalI = problemTool.
					generateIndividualEval(problem, dataset, pedigreeParams, getCALogger());
		
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(individualEvalI, problem, jobID);

			individuals[i] = individualEvalI;
		}
		
		EvolutionPopulationModel populationI = new EvolutionPopulationModel(individuals);
		
		IndividualEvaluated bestIndividualI = populationI.getBestIndividual(problem);
		
		//saves data in Agent DataManager
		processIndividualFromInitGeneration(bestIndividualI, generationNumberI,
				problem, jobID);
		
		while (state == CompAgentState.COMPUTING) {
			
			generationNumberI++;
						
			// process cross
			EvolutionPopulationModel populationNewI = populationI.
					processCross(crossRate, selector, problemTool, problem,
					dataset, pedigreeParams, getLogger());
			
			// add all generation before mutation
			populationNewI.addIndividuals(populationI.getIndividuals());
			
			// process mutation on each individual in population
			populationNewI = populationNewI.processMutation(mutationRate, problemTool,
					problem, dataset, pedigreeParams, getLogger());
			
			// inserts the best individual from last generation to model
			populationNewI.addIndividual(bestIndividualI);
			
			// distribute individuals to another islands
			distributeIndividualToNeighours(populationNewI.getIndividuals(), problem, jobID);
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);

			if (individualDistribution && recievedIndividualW != null) {
				
				// save and log received Individual
				processRecievedIndividual(bestIndividualI, recievedIndividualW,
						generationNumberI, problem, localSaver);
				
				// add received individuals to population
				populationNewI.addIndividual(recievedIndividualW.getIndividualEvaluated());
			}
			
			// correct size of new population to hard coded parameter
			populationNewI.correctedPopulationModel(problem, popSize);
			
			// update population by new population
			populationI = populationNewI;
			bestIndividualI = populationNewI.getBestIndividual(problem);
			
			// save, log and distribute computed Individual
			processComputedIndividual(bestIndividualI,
					generationNumberI, problem, jobID, localSaver);
		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}
	
}
