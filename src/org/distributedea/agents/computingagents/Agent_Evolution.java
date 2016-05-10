package org.distributedea.agents.computingagents;

import jade.core.behaviours.Behaviour;

import java.util.Vector;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.evolution.Convertor;
import org.distributedea.agents.computingagents.computingagent.evolution.EACrossoverWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAFitnessWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAMutationWrapper;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;
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


	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problemStruct.getProblemToolClass(), getLogger());
		
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
		
		if (! isAble) {
			getCALogger().logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException("Can't solve problem"));
		}
		
		return isAble;
	}
	
	@Override
	protected void startComputing(Problem problem, Class<?> problemToolClass, String jobID, Behaviour behaviour) throws ProblemToolException {
		
		ProblemTool problemTool = ProblemToolEvaluation.getProblemToolFromClass(problemToolClass);
		problemTool.initialization(problem, getLogger());
		
		int popSize = 50;
		double mutationRate = 0.9;
		double crossRate = 0.5;
		
		// generates Individuals
		Vector<Individual> individuals = new Vector<Individual>();
		for (int i = 0; i < popSize; i++) {
			Individual individualI = problemTool.generateIndividual(problem, getCALogger());
			individuals.add(individualI);
		}
		
		
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		
		// converts Individuals to Chromosomes
		Population population = null;
		try {
			population = new Population(conf);
			
		} catch (InvalidConfigurationException e1) {
			getCALogger().logThrowable("Invalid Configuration", e1);
			commitSuicide();
			return;
		}
		
		try {
			for (Individual individualI : individuals) {
				IChromosome chromI = Convertor.convertToIChromosome(individualI, problem, conf);
				
				population.addChromosome(chromI);
			}
		} catch (InvalidConfigurationException e) {
			getCALogger().logThrowable("Invalid Configuration", e);
			commitSuicide();
			return;
		}
		
		try {
			
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
			Individual individualI =
					Convertor.convertToIndividual(choosenChromosomeI, problem, problemTool, conf);
			double fitnessI =
					problemTool.fitness(individualI, problem, getCALogger());
			
			// save, log and distribute computed Individual
			processIndividualFromInitGeneration(individualI,
					fitnessI, generationNumberI, problem, jobID);
			
			while (computingThread.continueInTheNextGeneration()) {
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
				individualI =
						Convertor.convertToIndividual(choosenChromosomeI, problem, problemTool, conf);

				fitnessI =
						problemTool.fitness(individualI, problem, getCALogger());
				
				// save, log and distribute computed Individual
				processComputedIndividual(individualI,
						fitnessI, generationNumberI, problem);
				
				// send new Individual to distributed neighbors
				if (computingThread.isIndividualDistribution()) {
					distributeIndividualToNeighours(individualI, problem, jobID);
				}
				
				//take received individual to new generation
				IndividualWrapper recievedIndividualW = getRecievedIndividual();
				Individual recievedIndividual = recievedIndividualW.getIndividual();
				double recievedFitnessI = problemTool.fitness(recievedIndividual,
						problem, getCALogger());
				if (computingThread.isIndividualDistribution() &&
						! Double.isNaN(recievedFitnessI) &&
						ProblemToolEvaluation.isFistFitnessBetterThanSecond(
								recievedFitnessI, fitnessI, problem)) {
		
					IChromosome recievedChromI = Convertor
							.convertToIChromosome(recievedIndividual, problem, conf);
					pop.getPopulation().addChromosome(recievedChromI);
					
					processRecievedIndividual(recievedIndividualW,
							recievedFitnessI, generationNumberI, problem);
				}

			}
			
		} catch (InvalidConfigurationException e) {
			getCALogger().logThrowable("Invalid Configuration", e);
			commitSuicide();
			return;
		}
		
		problemTool.exit();

	}
	
}
