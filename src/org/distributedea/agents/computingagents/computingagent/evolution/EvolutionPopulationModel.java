package org.distributedea.agents.computingagents.computingagent.evolution;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.evolution.selectors.ISelector;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemToolException;

/**
 * Structure for representation model of individuals for evolution algorithm
 * @author stepan
 *
 */
public class EvolutionPopulationModel {

	private IndividualsEvaluated individuals;

	/**
	 * Constructor
	 * @param individual
	 */
	public EvolutionPopulationModel(IndividualEvaluated individual) {

		if (individual == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}

		this.individuals = new IndividualsEvaluated();
		this.individuals.add(individual);
	}
	/**
	 * Constructor
	 * @param individuals
	 */
	public EvolutionPopulationModel(List<IndividualEvaluated> individuals) {
		
		if (individuals == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		this.individuals = new IndividualsEvaluated(individuals);
	}
	
	/**
	 * Returns {@link IndividualsEvaluated} in model
	 * @return
	 */
	public IndividualsEvaluated getIndividuals() {
		return individuals;
	}
	
	/**
	 * Adds {@link IndividualEvaluated} to model
	 * @param individual
	 */
	public void addIndividual(IndividualEvaluated individual) {
		if (this.individuals == null) {
			this.individuals = new IndividualsEvaluated();
		}
		individuals.add(individual);
	}

	public void addIndividuals(IndividualsEvaluated indivToAdd) {
		if (this.individuals == null) {
			this.individuals = new IndividualsEvaluated();
		}
		for (IndividualEvaluated indivEvalI :
			indivToAdd.getIndividualsEvaluated()) {
			individuals.add(indivEvalI);
		}
	}
	
	/**
	 * Returns the best {@link IndividualEvaluated} in model
	 * @param problem
	 * @return
	 */
	public IndividualEvaluated getBestIndividual(Problem problem) {
		
		return individuals.exportTheBestIndividual(problem);
	}
	
	/**
	 * Mutation
	 * @param probOfMutation
	 * @param tool
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */
	public EvolutionPopulationModel processMutation(double probOfMutation,
			IProblemTool tool, Problem problem, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws ProblemToolException {
		
		List<IndividualEvaluated> improvedIndividuals = new ArrayList<>();
		
		for (IndividualEvaluated individualEvalI :
				individuals.getIndividualsEvaluated()) {
			
			IndividualEvaluated newIndividualEvalI = individualEvalI;
			
			if (Math.random() < probOfMutation) {
				newIndividualEvalI = tool.improveIndividualEval(
						individualEvalI, problem, pedigreeParams, logger);
			}
			improvedIndividuals.add(newIndividualEvalI);
		}
		
		return new EvolutionPopulationModel(improvedIndividuals);
	}
	
	/**
	 * CrossOver
	 * @param probOfCross
	 * @param selector
	 * @param tool
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */
	public EvolutionPopulationModel processCross(double probOfCross, ISelector selector,
			IProblemTool tool, Problem problem, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws ProblemToolException {
		
		List<IndividualEvaluated> individualsCopy = new ArrayList<>();
		
		for (int i = 0; i < individuals.size(); i++) {
			
			IndividualEvaluated indivEval1 = selector.select(individuals, problem);
			IndividualEvaluated indivEval2 = selector.select(individuals, problem);
			
			IndividualEvaluated[] indivEvalNew = tool.createNewIndividual(
					indivEval1, indivEval2, problem, pedigreeParams, logger);
			
			individualsCopy.add(indivEvalNew[0]);
		}
		
		return new EvolutionPopulationModel(individualsCopy);
	}
	
	/**
	 * Correct Model to given size. Function removes duplicities and
	 * the set of worst individuals
	 * @param problem
	 * @param popSize
	 */
	public void correctedPopulationModel(Problem problem, int popSize) {
		
		individuals.removeDuplicates();
		
		List<IndividualEvaluated> individualsCopy =
				individuals.exportSortedFromBestToWorst(problem);

		this.individuals = new IndividualsEvaluated(
				individualsCopy.subList(0, popSize));
	}
	
}
