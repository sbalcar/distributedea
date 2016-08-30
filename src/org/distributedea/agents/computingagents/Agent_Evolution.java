package org.distributedea.agents.computingagents;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.evolution.Convertor;
import org.distributedea.agents.computingagents.computingagent.evolution.EACrossoverWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAFitnessWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAMutationWrapper;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
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
public class Agent_Evolution extends Agent_ComputingAgent {

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
		
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		
		// generates Individuals
		List<IndividualEvaluated> individuals = new ArrayList<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualI = problemTool.generateIndividualEval(problem, getCALogger());
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
			distributeIndividualToNeighours(individuals, problem, jobID);
		}

		
		conf.setSampleChromosome(population.getChromosome(0));
		conf.setFitnessFunction(
				new EAFitnessWrapper(conf, problem, problemTool, getCALogger()));
		conf.setPopulationSize(popSize);
		
        //conf.removeNaturalSelectors(false);
        //conf.removeNaturalSelectors(true);
		conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
		
		conf.getGeneticOperators().clear();
		conf.addGeneticOperator(
				new EAMutationWrapper(mutationRate, problem, problemTool, conf, getCALogger()));
		conf.addGeneticOperator(
				new EACrossoverWrapper(crossRate, problem, problemTool, conf, getCALogger()));
		conf.addNaturalSelector(new StandardPostSelector(conf), false);
		
		Genotype pop = new Genotype(conf, population);
		
		
		long generationNumberI = -1;
		
		// best chromosome from actual generation
		IChromosome choosenChromosomeI = pop.getFittestChromosome();
		Individual individualI = Convertor.convertToIndividual(
				choosenChromosomeI, problem, problemTool, conf);
		double fitnessI = problemTool.fitness(individualI, problem, getCALogger());
		IndividualEvaluated individualEvalI = new IndividualEvaluated(individualI, fitnessI);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);

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
			fitnessI = problemTool.fitness(individualI, problem, getCALogger());
			IndividualEvaluated individualEvalI_ = new IndividualEvaluated(individualI, fitnessI);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI_,
					generationNumberI, problem, jobID);
			
			List<IndividualEvaluated> populationOntol =
					convertPopulationToOntology(pop.getPopulation(), problem,
					problemTool, conf);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(populationOntol, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFirstIndividualWBetterThanSecond(
							recievedIndividualW, fitnessI, problem)) {
				
				IndividualEvaluated recievedIndividual = recievedIndividualW.getIndividualEvaluated();
				
				IChromosome recievedChromI = Convertor
						.convertToIChromosome(recievedIndividual.getIndividual(), problem, conf);
				pop.getPopulation().addChromosome(recievedChromI);
				
				processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			}

		}
		
		problemTool.exit();
	}


	private List<IndividualEvaluated> convertPopulationToOntology(Population population, Problem problem,
			IProblemTool problemTool, Configuration conf) throws InvalidConfigurationException {
		
		List<IndividualEvaluated> individuals = new ArrayList<>();
		
		for (int i = 0; i < population.size(); i++) {
			IChromosome iChromosomeI = population.getChromosome(i);
			
			Individual individualI = Convertor.convertToIndividual(
					iChromosomeI, problem, problemTool, conf);
			
			double fitnessI = problemTool.fitness(individualI, problem,
					getCALogger());
			
			IndividualEvaluated individualEvalI =
					new IndividualEvaluated(individualI, fitnessI);
			
			individuals.add(individualEvalI);
		}
		return individuals;
	}
	
	
}
