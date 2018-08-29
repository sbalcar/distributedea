package org.distributedea.ontology.individualwrapper;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;

/**
 * Ontology represents model for list of {@link IndividualWrapper}
 * @author stepan
 *
 */
public class IndividualsWrappers implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<IndividualWrapper> individualsWrappers;

	
	@Deprecated
	public IndividualsWrappers() { //only for Jade
		this.individualsWrappers = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param individuals
	 */
	public IndividualsWrappers(List<IndividualWrapper> individuals) {
		if (individuals == null) {
			throw new IllegalArgumentException();
		}
		for (IndividualWrapper individualWrpI : individuals) {
			if (individualWrpI == null ||
					! individualWrpI.valid(new TrashLogger())) {
				throw new IllegalArgumentException();
			}
		}
		this.individualsWrappers = individuals;
	}
	
	/**
	 * Copy Constructor
	 * @param individualWrp
	 */
	public IndividualsWrappers(IndividualsWrappers individualWrp) {
		if (individualWrp == null || ! individualWrp.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		individualsWrappers = new ArrayList<>();
		for (IndividualWrapper individualWrpI :
				individualWrp.getIndividualsWrappers()) {
			individualsWrappers.add(individualWrpI.deepClone());
		}
	}
	
	public List<IndividualWrapper> getIndividualsWrappers() {
		return individualsWrappers;
	}
	@Deprecated
	public void setIndividualsWrappers(List<IndividualWrapper> individualsWrappers) {
		this.individualsWrappers = individualsWrappers;
	}
	
	/**
	 * Adds {@link IndividualWrapper}
	 * @param individualWrapper
	 */
	public void addIndividualWrapper(IndividualWrapper individualWrapper) {
		
		if (this.individualsWrappers == null) {
			this.individualsWrappers = new ArrayList<>();
		}
		
		this.individualsWrappers.add(individualWrapper);
	}

	/**
	 * Export {@link ProblemToolDefinition} of the first {@link IndividualWrapper}
	 * @return
	 */
	public ProblemToolDefinition exportProblemToolDefinition() {
		if (individualsWrappers == null || individualsWrappers.isEmpty()) {
			return null;
		}
		IndividualWrapper individualWrp0 = individualsWrappers.get(0);
		return individualWrp0.exportProblemToolDefinition();
	}
	
	
	public boolean exportContainsMoreThanOneMethod() {
		
		if (this.individualsWrappers == null ||
				this.individualsWrappers.size() <= 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Exports the best {@link IndividualWrapper}
	 * @param problem
	 * @return
	 */
	public IndividualWrapper exportBestResultOfComputing(IProblem problem) {
		
		if (individualsWrappers == null || individualsWrappers.isEmpty()) {
			return null;
		}
		
		IndividualWrapper bestResult = individualsWrappers.get(0);
		
		for (IndividualWrapper resultOfComputingI : individualsWrappers) {
			
			IndividualEvaluated individualEvalI =
					resultOfComputingI.getIndividualEvaluated();
			
			boolean isNewIndividualBetter = FitnessTool.
					isFirstIndividualEBetterThanSecond(individualEvalI,
							bestResult.getIndividualEvaluated(), problem);
			
			if (isNewIndividualBetter) {
				bestResult = resultOfComputingI;
			}
		}
		
		return bestResult;
	}

	/**
	 * Exports the worst {@link IndividualWrapper}
	 * @param problem
	 * @return
	 */
	public IndividualWrapper exportWorstResultOfComputing(IProblem problem) {
		
		if (individualsWrappers == null || individualsWrappers.isEmpty()) {
			return null;
		}
		
		IndividualWrapper worstResult = individualsWrappers.get(0);
		
		for (IndividualWrapper resultOfComputingI : individualsWrappers) {
			
			IndividualEvaluated individualEvalI =
					resultOfComputingI.getIndividualEvaluated();
			
			boolean isNewIndividualWorse = FitnessTool.
					isFirstIndividualEWorseThanSecond(individualEvalI,
							worstResult.getIndividualEvaluated(), problem);
			if (isNewIndividualWorse) {
				worstResult = resultOfComputingI;
			}
		}
		
		return worstResult;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (individualsWrappers == null) {
			return false;
		}
		for (IndividualWrapper individualWrpI : individualsWrappers) {
			if (! individualWrpI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IndividualsWrappers deepClone() {
		return new IndividualsWrappers(this);
	}
	
}
