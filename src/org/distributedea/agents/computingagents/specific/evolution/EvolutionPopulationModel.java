package org.distributedea.agents.computingagents.specific.evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.computingagents.specific.evolution.selectors.ISelector;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Structure for representation model of individuals for evolution algorithm
 * @author stepan
 *
 */
public class EvolutionPopulationModel {

	private IndividualEvaluated[] individuals;

	/**
	 * Constructor
	 * @param individuals
	 */
	public EvolutionPopulationModel(IndividualEvaluated[] individuals) {
		
		if (individuals == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		this.individuals = individuals;
	}

	/**
	 * Returns List{@link List} of {@link IndividualsEvaluated}s in model
	 * @return
	 */
	public IndividualEvaluated[] getIndividuals() {
		return individuals;
	}

	/**
	 * Returns List{@link List} of {@link IndividualsEvaluated}s in model
	 * @return
	 */
	public List<IndividualEvaluated> getIndividualsList() {
		return new ArrayList<>(Arrays.asList(individuals));
	}
	
	public void addIndividual(IndividualEvaluated indivToAdd) {
		IndividualEvaluated[] individualsNew =
				new IndividualEvaluated[1];
		individualsNew[0] = indivToAdd;
		
		addIndividuals(individualsNew);
	}
	
	/**
	 * Adds {@link IndividualEvaluated} to model
	 * @param indivsToAdd
	 */
	public void addIndividuals(IndividualEvaluated[] indivsToAdd) {
		
		IndividualEvaluated[] individualsNew =
				new IndividualEvaluated[individuals.length + indivsToAdd.length];
		
		for (int i = 0; i < individuals.length; i++) {
			individualsNew[i] = individuals[i];
		}
		for (int i = 0; i < indivsToAdd.length; i++) {
			individualsNew[i + individuals.length] = indivsToAdd[i];
		}
		
		this.individuals = individualsNew;
	}
	
	/**
	 * Returns the best {@link IndividualEvaluated} in model
	 * @param problem
	 * @return
	 */
	public IndividualEvaluated getBestIndividual(IProblem problem) {
		
		Arrays.sort(individuals, new CmpIndividualEvaluated(problem));
		return individuals[0];
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
			IProblemTool tool, IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		IndividualEvaluated[] improvedIndividuals =
				new IndividualEvaluated[individuals.length];
		
		for (int i = 0; i < individuals.length; i++) {
			
			IndividualEvaluated individualEvalI = individuals[i];
			IndividualEvaluated newIndividualEvalI = individualEvalI;
			
			if (Math.random() < probOfMutation) {
				newIndividualEvalI = tool.improveIndividualEval(
						individualEvalI, problem, dataset, pedigreeParams, logger);
			}
			improvedIndividuals[i] = newIndividualEvalI;
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
			IProblemTool tool, IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		IndividualEvaluated[] individualsCopy =
				new IndividualEvaluated[individuals.length];
		
		for (int i = 0; i < individuals.length; i++) {
			
			IndividualEvaluated indivEval1 = selector.select(individuals, problem);
			IndividualEvaluated indivEval2 = selector.select(individuals, problem);
			
			IndividualEvaluated[] indivEvalNew = tool.createNewIndividual(
					indivEval1, indivEval2, problem, dataset, pedigreeParams, logger);
			
			individualsCopy[i] = indivEvalNew[0];
		}
		
		return new EvolutionPopulationModel(individualsCopy);
	}
	
	/**
	 * Correct Model to given size. Function removes duplicities and
	 * the set of worst individuals
	 * @param problem
	 * @param popSize
	 */
	public void correctedPopulationModel(IProblem problem, int popSize) {
		
		Arrays.sort(individuals, new CmpIndividualEvaluated(problem));
		
		int j = 0;
		IndividualEvaluated[] individualsCorr = new IndividualEvaluated[popSize];
		
		IndividualEvaluated indivPrev = null;
		for (IndividualEvaluated indivI : individuals) {
			
			if (! indivI.equals(indivPrev)) {
				
				individualsCorr[j++] = indivI;
				if (j == individualsCorr.length) {
					break;
				}
			}
			
			indivPrev = indivI;
		}
		
		individuals = individualsCorr;
	}
	
}
