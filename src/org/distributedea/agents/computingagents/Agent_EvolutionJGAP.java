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
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
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
public class Agent_EvolutionJGAP extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private String POP_SIZE = "popSize";
	private int popSize = 10;
	
	private String MUTATION_RATE = "mutationRate";
	private double mutationRate = 0.9;
	
	private String CROSS_RATE = "crossRate";
	private double crossRate = 0.1;
	
	
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
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(null, methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		
		// generates Individuals
		List<IndividualEvaluated> individuals = new ArrayList<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualI = problemTool.generateIndividualEval(
					problem, dataset, pedigreeParams, getCALogger());
			individuals.add(individualI);
		}		
		
		// converts Individuals to Chromosomes
		Population population = new Population(conf);
		for (IndividualEvaluated individualI : individuals) {
			IChromosome chromI = Convertor.convertToIChromosome(individualI.getIndividual(), dataset, conf);
			//chromI.setFitnessValue(individualI.getFitness());
			
			population.addChromosome(chromI);
		}
		
		if (individualDistribution) {
			distributeIndividualToNeighours(individuals, problem, jobID);
		}

		
		conf.setSampleChromosome(population.getChromosome(0));
		conf.setFitnessFunction(
				new EAFitnessWrapper(conf, dataset, problem, problemTool, getCALogger()));
		conf.setPopulationSize(popSize);
		
        //conf.removeNaturalSelectors(false);
        //conf.removeNaturalSelectors(true);
		conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
		
		conf.getGeneticOperators().clear();
		conf.addGeneticOperator(
				new EAMutationWrapper(mutationRate, dataset, problemTool, conf, getCALogger()));
		conf.addGeneticOperator(
				new EACrossoverWrapper(crossRate, problem, dataset, problemTool, conf, getCALogger()));
		conf.addNaturalSelector(new StandardPostSelector(conf), false);
		
		Genotype pop = new Genotype(conf, population);
		
		
		long generationNumberI = -1;
		
		// best chromosome from actual generation
		IChromosome choosenChromosomeI = pop.getFittestChromosome();
		Individual individualI = Convertor.convertToIndividual(
				choosenChromosomeI, dataset, problemTool, conf);
		double fitnessI = problemTool.fitness(individualI, problem, dataset, getCALogger());
		IndividualEvaluated individualEvalI = new IndividualEvaluated(individualI, fitnessI, null);
		
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
					dataset, problemTool, conf);
			fitnessI = problemTool.fitness(individualI, problem, dataset, getCALogger());
			IndividualEvaluated individualEvalI_ = new IndividualEvaluated(individualI, fitnessI, null);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI_,
					generationNumberI, problem, jobID, localSaver);
			
			List<IndividualEvaluated> populationOntol =
					convertPopulationToOntology(pop.getPopulation(), problem, dataset,
					problemTool, conf);
			
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(populationOntol, problem, jobID);
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFirstIndividualWBetterThanSecond(
							recievedIndividualW, fitnessI, problem)) {
				
				IndividualEvaluated recievedIndividual = recievedIndividualW.getIndividualEvaluated();
				
				IChromosome recievedChromI = Convertor
						.convertToIChromosome(recievedIndividual.getIndividual(), dataset, conf);
				pop.getPopulation().addChromosome(recievedChromI);
				
				processRecievedIndividual(individualEvalI_, recievedIndividualW,
						generationNumberI, problem, localSaver);
			}

		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}


	private List<IndividualEvaluated> convertPopulationToOntology(Population population,
			IProblem problem, Dataset dataset, IProblemTool problemTool,
			Configuration conf) throws InvalidConfigurationException {
		
		List<IndividualEvaluated> individuals = new ArrayList<>();
		
		for (int i = 0; i < population.size(); i++) {
			IChromosome iChromosomeI = population.getChromosome(i);
			
			Individual individualI = Convertor.convertToIndividual(
					iChromosomeI, dataset, problemTool, conf);
			
			double fitnessI = problemTool.fitness(individualI, problem,
					dataset, getCALogger());
			
			IndividualEvaluated individualEvalI =
					new IndividualEvaluated(individualI, fitnessI, null);
			
			individuals.add(individualEvalI);
		}
		return individuals;
	}
	
	
}
