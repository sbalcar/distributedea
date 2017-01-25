package org.distributedea.agents.computingagents.computingagent.evolution;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.evolution.selectors.ISelector;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.IProblemTool;

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
	 * @param problemDef
	 * @return
	 */
	public IndividualEvaluated getBestIndividual(IProblemDefinition problemDef) {
		
		return individuals.exportTheBestIndividual(problemDef);
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
			IProblemTool tool, IProblemDefinition problemDef, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		List<IndividualEvaluated> improvedIndividuals = new ArrayList<>();
		
		for (IndividualEvaluated individualEvalI :
				individuals.getIndividualsEvaluated()) {
			
			IndividualEvaluated newIndividualEvalI = individualEvalI;
			
			if (Math.random() < probOfMutation) {
				newIndividualEvalI = tool.improveIndividualEval(
						individualEvalI, problemDef, dataset, pedigreeParams, logger);
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
	 * @param problemDef
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */
	public EvolutionPopulationModel processCross(double probOfCross, ISelector selector,
			IProblemTool tool, IProblemDefinition problemDef, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		List<IndividualEvaluated> individualsCopy = new ArrayList<>();
		
		for (int i = 0; i < individuals.size(); i++) {
			
			IndividualEvaluated indivEval1 = selector.select(individuals, problemDef);
			IndividualEvaluated indivEval2 = selector.select(individuals, problemDef);
			
			IndividualEvaluated[] indivEvalNew = tool.createNewIndividual(
					indivEval1, indivEval2, problemDef, dataset, pedigreeParams, logger);
			
			individualsCopy.add(indivEvalNew[0]);
		}
		
		return new EvolutionPopulationModel(individualsCopy);
	}
	
	/**
	 * Correct Model to given size. Function removes duplicities and
	 * the set of worst individuals
	 * @param problemDef
	 * @param popSize
	 */
	public void correctedPopulationModel(IProblemDefinition problemDef, int popSize) {
		
		individuals.removeDuplicates();
		
		List<IndividualEvaluated> individualsCopy =
				individuals.exportSortedFromBestToWorst(problemDef);

		this.individuals = new IndividualsEvaluated(
				individualsCopy.subList(0, popSize));
	}
	
}
