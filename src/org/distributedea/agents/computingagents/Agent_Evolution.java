package org.distributedea.agents.computingagents;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.evolution.EvolutionPopulationModel;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.ISelector;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.Selector;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
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
	private double crossRate = 0.5;
	
	private String SELECTOR = "selector";
	private ISelector selector;
	
	
	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		Class<?> problem = problemStruct.getProblem().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		if (problem == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemContinousOpt.class) {
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
			AgentConfiguration agentConf) throws Exception {
	
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
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(null, methodDescription);

		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		List<IndividualEvaluated> individuals = new ArrayList<>();
		while (individuals.size() < popSize) {
			IndividualEvaluated individualEvalI = problemTool.
					generateIndividualEval(problem, pedigreeParams, getCALogger());
			
			if (! individuals.contains(individualEvalI)) {
				individuals.add(individualEvalI);
			}
		}
		
		EvolutionPopulationModel populationI = new EvolutionPopulationModel(individuals);
		
		IndividualEvaluated bestIndividualI = populationI.getBestIndividual(problem);
		
		
		if (individualDistribution) {
			distributeIndividualToNeighours(populationI.getIndividuals(), problem, jobID);
		}
		
		while (state == CompAgentState.COMPUTING) {
			
			generationNumberI++;
						
			// process cross
			EvolutionPopulationModel populationNewI = populationI.
					processCross(crossRate, selector, problemTool, problem,
					pedigreeParams, getLogger());
			
			// add all generation before mutation
			populationNewI.addIndividuals(populationI.getIndividuals());
			
			// process mutation on each individual in population
			populationNewI.processMutation(mutationRate, problemTool,
					problem, pedigreeParams, getLogger());
			
			// inserts the best individual from last generation to model
			populationNewI.addIndividual(bestIndividualI);
			
			// distribute individuals to another islands
			if (individualDistribution) {
				distributeIndividualToNeighours(populationNewI.getIndividuals(), problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);

			// save and log received Individual
			processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			
			
			// add received individuals to population
			populationNewI.addIndividual(recievedIndividualW.getIndividualEvaluated());
			
			// correct size of new population to hard coded parameter
			populationNewI.correctedPopulationModel(problem, popSize);
			
			// update population by new population
			populationI = populationNewI;
			bestIndividualI = populationNewI.getBestIndividual(problem);
			
			// save, log and distribute computed Individual
			processComputedIndividual(bestIndividualI,
					generationNumberI, problem, jobID);
		}
		
	}
	
}
