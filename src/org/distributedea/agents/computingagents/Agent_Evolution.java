package org.distributedea.agents.computingagents;

import java.util.Vector;

import org.distributedea.agents.computingagents.computingagent.evolution.Convertor;
import org.distributedea.agents.computingagents.computingagent.evolution.EAFitnessWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAMutationWrapper;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;
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
	public void prepareToDie() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		boolean isAble = false;
		
		if (problem == ProblemTSP.class) {
			
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
			
		}
		
		if (! isAble) {
			logger.logThrowable(
					"Agent is not able to solve this type of Problem by using "
					+ "this reperesentation",
					new IllegalStateException());
		}
		
		return isAble;
	}
	
	@Override
	public void startComputing(Problem problem) {

		if (! isAbleToSolve(problem)) {
			return;
		}
	
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), logger);
		setProblemTool(problemTool);
		
		int popSize = 5;
		int maxGen = 50000;
		double mutationRate = 0.9;
		
		// generates Individuals
		Vector<IndividualPermutation> individuals = new Vector<IndividualPermutation>();
		for (int i = 0; i < popSize; i++) {
			Individual individualI = problemTool.generateIndividual(problem, logger);
			// cast tested before
			individuals.add((IndividualPermutation) individualI);
		}
		
		
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		
		// converts Individuals to Chromosomes
		Population population = null;
		try {
			population = new Population(conf);
			
		} catch (InvalidConfigurationException e1) {
			logger.logThrowable("Invalid Configuration", e1);
			//TODO: konec agenta
		}
		
		try {
			for (IndividualPermutation individualI : individuals) {
				IChromosome chromI = Convertor.convertToIChromosome(individualI, conf);
				
				population.addChromosome(chromI);
			}
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Invalid Configuration", e);
			//TODO: konec agenta
		}
		
		try {

			conf.setSampleChromosome(population.getChromosome(0));
			conf.setFitnessFunction(
					new EAFitnessWrapper(conf, false,  problem, problemTool, logger));
			conf.setPopulationSize(popSize);
			
            //conf.removeNaturalSelectors(false);
            //conf.removeNaturalSelectors(true);
			conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
			
			conf.getGeneticOperators().clear();
			conf.addGeneticOperator(
					new EAMutationWrapper(mutationRate, problem, problemTool, conf, logger));
			conf.addNaturalSelector(new StandardPostSelector(conf), false);
			
			Genotype pop = new Genotype(conf, population);
			
			IChromosome bestChromosome = pop.getFittestChromosome();
			Individual individual =
					Convertor.convertToIndividual(bestChromosome, conf);
			double fitnessValue =
					problemTool.fitness(individual, problem, logger);
			
			//System.out.println("Generation -1: " + pop.getFittestChromosome().toString());
			System.out.println("Generation -1: " + fitnessValue);
			
			long i = 0;
			while (true) {
				
				// try - for situation when some operator doesn't work correctly
				try {
					pop.evolve();
				} catch (NullPointerException e) {
					//:TODO KILL AGENT
					return;
				}
				
				IChromosome bestChromosomeI = pop.getFittestChromosome();
				Individual individualI =
						Convertor.convertToIndividual(bestChromosomeI, conf);
				double fitnessValueI =
						problemTool.fitness(individualI, problem, logger);
				
				// export best result of computing
				ResultOfComputing resultOfComputing = new ResultOfComputing();
				resultOfComputing.setBestIndividual(individualI);
				resultOfComputing.setFitnessValue(fitnessValueI);
	
				setBestresultOfComputing(resultOfComputing);
				
				//System.out.println("Generation " + i + ": " + pop.getFittestChromosome().toString());
				System.out.println("Generation " + i + ": " + fitnessValueI);
				
				i++;
			}
		}
		catch (InvalidConfigurationException e) {
			logger.logThrowable("Invalid Configuration", e);
		}
		

	}
	
}
