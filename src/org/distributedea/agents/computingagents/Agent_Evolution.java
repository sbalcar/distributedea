package org.distributedea.agents.computingagents;

import jade.core.behaviours.Behaviour;

import java.util.Vector;
import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.computingagents.computingagent.evolution.Convertor;
import org.distributedea.agents.computingagents.computingagent.evolution.EACrossoverWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAFitnessWrapper;
import org.distributedea.agents.computingagents.computingagent.evolution.EAMutationWrapper;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
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
		
		// stops computing in separated thread
		thread.stop();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			getCALogger().logThrowable("Can't wait to stop thread", e);
		}
		
		// deregistres agent from DF
		//  before derestration have to be stop computing(after deregistration
		//  agent can no communicate wit another agents)
		deregistrDF();
		
		// removes all Behaviors in agent (kill himself)
		//doDelete();
	}

	@Override
	protected boolean isAbleToSolve(Class<?> problem, Class<?> representation) {
		
		boolean isAble = false;
		
		if (problem == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
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
	public void startComputing(Problem problem, Behaviour behaviour) {

		if (! isAbleToSolve(problem)) {
			getCALogger().log(Level.INFO, "Agent can't solve this Problem");
			commitSuicide();
			return;
		}
	
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				problem.getProblemToolClass(), getCALogger());
		setProblemTool(problemTool);
		
		int popSize = 50;
		double mutationRate = 0.9;
		double crossRate = 0.5;
		
		// generates Individuals
		Vector<IndividualPermutation> individuals = new Vector<IndividualPermutation>();
		for (int i = 0; i < popSize; i++) {
			Individual individualI = problemTool.generateIndividual(problem, getCALogger());
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
			getCALogger().logThrowable("Invalid Configuration", e1);
			commitSuicide();
			return;
		}
		
		try {
			for (IndividualPermutation individualI : individuals) {
				IChromosome chromI = Convertor.convertToIChromosome(individualI, conf);
				
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
					new EAFitnessWrapper(conf, false,  problem, problemTool, getCALogger()));
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
					Convertor.convertToIndividual(choosenChromosomeI, conf);
			double fitnessI =
					problemTool.fitness(individualI, problem, getCALogger());
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualI,
					fitnessI, generationNumberI, problem);
			
			while (true) {
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
						Convertor.convertToIndividual(choosenChromosomeI, conf);

				fitnessI =
						problemTool.fitness(individualI, problem, getCALogger());
				
				// save, log and distribute computed Individual
				processComputedIndividual(individualI,
						fitnessI, generationNumberI, problem);
				
				// send new Individual to distributed neighbors
				if (InputConfiguration.individualDistribution) {
					distributeIndividualToNeighours(individualI);
				}
				
				//take received individual to new generation
				Individual recievedIndividual = getRecievedIndividual();
				double recievedFitnessI = problemTool.fitness(recievedIndividual,
						problem, getCALogger());
				if (InputConfiguration.individualDistribution &&
						! Double.isNaN(recievedFitnessI) &&
						ProblemToolEvaluation.isFistFitnessBetterThanSecond(
								recievedFitnessI, fitnessI, problem)) {
		
					IChromosome recievedChromI = Convertor
							.convertToIChromosome(recievedIndividual, conf);
					pop.getPopulation().addChromosome(recievedChromI);
				}

			}
			
		} catch (InvalidConfigurationException e) {
			getCALogger().logThrowable("Invalid Configuration", e);
			commitSuicide();
			return;
		}
		

	}
	
}
