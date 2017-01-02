package org.distributedea.agents.computingagents;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.evolutionjgap.Convertor;
import org.distributedea.agents.computingagents.computingagent.evolutionjgap.EACrossoverWrapper;
import org.distributedea.agents.computingagents.computingagent.evolutionjgap.EAFitnessWrapper;
import org.distributedea.agents.computingagents.computingagent.evolutionjgap.EAMutationWrapper;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.StandardPostSelector;
import org.jgap.impl.WeightedRouletteSelector;

/**
 * Agent represents Evolution Algorithm Method
 * @author stepan
 *
 */
public class Agent_EvolutionJGAP extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private int popSize = 10;
	private double mutationRate = 0.9;
	private double crossRate = 0.5;
	
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
		} else if (problem == ProblemBinPacking.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemContinuousOpt.class) {
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
	
	protected void processArguments(Arguments args) throws Exception {
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
		IProblemDefinition problemDefinition = problemStruct.getProblemDefinition();
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemDefinition, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(null, methodDescription);
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		
		// generates Individuals
		List<IndividualEvaluated> individuals = new ArrayList<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualI = problemTool.generateIndividualEval(
					problemDefinition, problem, pedigreeParams, getCALogger());
			individuals.add(individualI);
		}		
		
		// converts Individuals to Chromosomes
		Population population = new Population(conf);
		for (IndividualEvaluated individualI : individuals) {
			IChromosome chromI = Convertor.convertToIChromosome(individualI.getIndividual(), problem, conf);
			//chromI.setFitnessValue(individualI.getFitness());
			
			population.addChromosome(chromI);
		}
		
		if (individualDistribution) {
			distributeIndividualToNeighours(individuals, problemDefinition, jobID);
		}

		
		conf.setSampleChromosome(population.getChromosome(0));
		conf.setFitnessFunction(
				new EAFitnessWrapper(conf, problem, problemDefinition, problemTool, getCALogger()));
		conf.setPopulationSize(popSize);
		
        //conf.removeNaturalSelectors(false);
        //conf.removeNaturalSelectors(true);
		conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
		
		conf.getGeneticOperators().clear();
		conf.addGeneticOperator(
				new EAMutationWrapper(mutationRate, problem, problemTool, conf, getCALogger()));
		conf.addGeneticOperator(
				new EACrossoverWrapper(crossRate, problemDefinition, problem, problemTool, conf, getCALogger()));
		conf.addNaturalSelector(new StandardPostSelector(conf), false);
		
		Genotype pop = new Genotype(conf, population);
		
		
		long generationNumberI = -1;
		
		// best chromosome from actual generation
		IChromosome choosenChromosomeI = pop.getFittestChromosome();
		Individual individualI = Convertor.convertToIndividual(
				choosenChromosomeI, problem, problemTool, conf);
		double fitnessI = problemTool.fitness(individualI, problemDefinition, problem, getCALogger());
		IndividualEvaluated individualEvalI = new IndividualEvaluated(individualI, fitnessI, null);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problemDefinition, jobID);

		while (state == CompAgentState.COMPUTING) {
			// increment next number of generation
			generationNumberI++;
			
			// try - for situation when some operator doesn't work correctly
			try {
				pop.evolve();
			} catch (Exception e) {
				getCALogger().logThrowable("Error by evolving", e);
				commitSuicide();
				return;
			}
			
			// best chromosome from actual generation
			choosenChromosomeI = pop.getFittestChromosome();
			individualI = Convertor.convertToIndividual(choosenChromosomeI,
					problem, problemTool, conf);
			fitnessI = problemTool.fitness(individualI, problemDefinition, problem, getCALogger());
			IndividualEvaluated individualEvalI_ = new IndividualEvaluated(individualI, fitnessI, null);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI_,
					generationNumberI, problemDefinition, jobID);
			
			List<IndividualEvaluated> populationOntol =
					convertPopulationToOntology(pop.getPopulation(), problemDefinition, problem,
					problemTool, conf);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(populationOntol, problemDefinition, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problemDefinition);
			
			if (individualDistribution &&
					FitnessTool.isFirstIndividualWBetterThanSecond(
							recievedIndividualW, fitnessI, problemDefinition)) {
				
				IndividualEvaluated recievedIndividual = recievedIndividualW.getIndividualEvaluated();
				
				IChromosome recievedChromI = Convertor
						.convertToIChromosome(recievedIndividual.getIndividual(), problem, conf);
				pop.getPopulation().addChromosome(recievedChromI);
				
				processRecievedIndividual(recievedIndividualW, generationNumberI, problemDefinition);
			}

		}
		
		problemTool.exit();
	}


	private List<IndividualEvaluated> convertPopulationToOntology(Population population,
			IProblemDefinition problemDef, Problem problem, IProblemTool problemTool,
			Configuration conf) throws InvalidConfigurationException {
		
		List<IndividualEvaluated> individuals = new ArrayList<>();
		
		for (int i = 0; i < population.size(); i++) {
			IChromosome iChromosomeI = population.getChromosome(i);
			
			Individual individualI = Convertor.convertToIndividual(
					iChromosomeI, problem, problemTool, conf);
			
			double fitnessI = problemTool.fitness(individualI, problemDef,
					problem, getCALogger());
			
			IndividualEvaluated individualEvalI =
					new IndividualEvaluated(individualI, fitnessI, null);
			
			individuals.add(individualEvalI);
		}
		return individuals;
	}
	
	
}
