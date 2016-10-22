package org.distributedea.ontology.individualwrapper;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Ontology represents model for list of {@link CmpIndividualEvaluated}
 * @author stepan
 *
 */
public class IndividualsEvaluated  implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<IndividualEvaluated> individualsEvaluated;

	/**
	 * Constructor
	 */
	public IndividualsEvaluated() {
		this.individualsEvaluated = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param individualsEvaluated
	 */
	public IndividualsEvaluated(List<IndividualEvaluated> individualsEvaluated) {
		this.individualsEvaluated = new ArrayList<>();
		add(individualsEvaluated);
	}
	
	/**
	 * Copy Constructor
	 * @param individualsEvaluated
	 */
	public IndividualsEvaluated(IndividualsEvaluated individualsEvaluated) {
		if (individualsEvaluated == null ||
				! individualsEvaluated.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				IndividualsEvaluated.class.getSimpleName() + "is not valid");
		}
		this.individualsEvaluated = new ArrayList<>();
		add(individualsEvaluated.getIndividualsEvaluated());
	}

	
	public List<IndividualEvaluated> getIndividualsEvaluated() {
		return individualsEvaluated;
	}
	@Deprecated
	public void setIndividualsEvaluated(
			List<IndividualEvaluated> individualsEvaluated) {
		this.individualsEvaluated = new ArrayList<>();
		add(individualsEvaluated);
	}

	/**
	 * Adds {@link List<IndividualEvaluated>}
	 * @param individualsEvaluated
	 */
	public void add(List<IndividualEvaluated> individualsEvaluated) {
		if (individualsEvaluated == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + "can not be null");
		}
		for (IndividualEvaluated indivEvalI : individualsEvaluated) {
			add(indivEvalI);
		}
	}
	
	/**
	 * Adds {@link IndividualEvaluated}
	 * @param individualEvaluated
	 */
	public void add(IndividualEvaluated individualEvaluated) {
		if (individualEvaluated == null ||
				! individualEvaluated.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualsEvaluated.class.getSimpleName() + "is not valid");			
		}
		
		if (this.individualsEvaluated == null) {
			this.individualsEvaluated = new ArrayList<>();
		}
		this.individualsEvaluated.add(individualEvaluated);
	}
	
	/**
	 * Returns number of {@link IndividualEvaluated}s in structure
	 * @return
	 */
	public int size() {
		return this.individualsEvaluated.size();
	}
	
	/**
	 * Sorts {@link IndividualEvaluated}s from the best to the worst
	 * @param problem
	 */
	public void sortFromTheBestToTheWorst(Problem problem) {
		
		Collections.sort(individualsEvaluated,
				new CmpIndividualEvaluated(problem));
	}
	
	/**
	 * Exports sorted list of {@link IndividualEvaluated} - from best to worst
	 * @param problem
	 * @return
	 */
	public List<IndividualEvaluated> exportSortedFromBestToWorst(Problem problem) {
		
		List<IndividualEvaluated> individualsCopy =
				new ArrayList<IndividualEvaluated>(individualsEvaluated);
		Collections.sort(individualsCopy, new CmpIndividualEvaluated(problem));

		return individualsCopy;
	}
	
	/**
	 * Exports N best {@link IndividualEvaluated}
	 * @param numberOfIndividuals
	 * @param problem
	 * @return
	 */
	public IndividualsEvaluated exportNBestSorted(int numberOfIndividuals, Problem problem) {
		
		List<IndividualEvaluated> newList =
				new ArrayList<IndividualEvaluated>(individualsEvaluated);
		Collections.sort(newList, new CmpIndividualEvaluated(problem));
				
		List<IndividualEvaluated> sublist = newList.subList(
				0, Math.min(numberOfIndividuals,newList.size()));
		
		return new IndividualsEvaluated(sublist);
	}
	
	/**
	 * Returns number of {@link IndividualEvaluated}s
	 * @return
	 */
	public int exportNumberOfIndividuals() {
		return this.individualsEvaluated.size();
	}
	
	public IndividualEvaluated remove(int index) {
		return this.individualsEvaluated.remove(index);
	}
	
	public void removeDuplicates() {
		
		List<IndividualEvaluated> individualsUnique =
				new ArrayList<>();
		
		for (IndividualEvaluated individualI : individualsEvaluated) {
			if (! individualsUnique.contains(individualI)) {
				individualsUnique.add(individualI);
			}
		}
		
		this.individualsEvaluated = individualsUnique;
	}
	
	public synchronized IndividualEvaluated exportIndividual(int index) {

		return individualsEvaluated.get(index);
	}

	
	/**
	 * Exports the best {@link IndividualEvaluated} of structure
	 * @param problem
	 * @return
	 */
	public IndividualEvaluated exportTheBestIndividual(Problem problem) {
		return Collections.min(individualsEvaluated,
				new CmpIndividualEvaluated(problem));
	}
	
	/**
	 * Exports sorted list of {@link IndividualEvaluated}s
	 * @param problem
	 * @return
	 */
	public List<IndividualEvaluated> exportIndividualsFromBestToWorst(Problem problem) {
		
		ArrayList<IndividualEvaluated> individualsList =
				new ArrayList<IndividualEvaluated>(individualsEvaluated);
		
		Collections.sort(individualsList, new CmpIndividualEvaluated(problem));
		
		return individualsList;
	}
	
	/**
	 * Export random {@link IndividualEvaluated}
	 * @return
	 */
	public IndividualEvaluated exportRandomIndividualEvaluated() {
		
		Random ran = new Random();
		int indexAC = ran.nextInt(individualsEvaluated.size());
		return individualsEvaluated.get(indexAC);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		for (IndividualEvaluated indivEvalI : individualsEvaluated) {
			if (indivEvalI == null || ! indivEvalI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
