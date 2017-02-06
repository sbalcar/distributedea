package org.distributedea.agents.computingagents.computingagent.models;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

/**
 * Model for a set of received {@link IndividualWrapper} from distribution
 * @author stepan
 *
 */
public class ReceivedIndividualsModel {

	private boolean DEBUG;
	
	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private int indivCount = 0;
	private IndividualWrapper[] individuals =
			new IndividualWrapper[MAX_NUMBER_OF_INDIVIDUAL];

	/**
	 * Constructor
	 * @param DEBUG
	 */
	public ReceivedIndividualsModel(boolean DEBUG) {
		this.DEBUG = DEBUG;
	}
	
	/**
	 * Add Received {@link IndividualWrapper} to Model
	 * @param individualW
	 * @param problem
	 * @param problem
	 * @param problemTool
	 * @param logger
	 */
	public void addIndividual(IndividualWrapper individualW,
			IProblem problem, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger) {
		
		if (individualW == null) {
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		if (DEBUG && (! individualW.validation(problem, dataset, problemTool, logger))) {
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		if (indivCount == individuals.length) {
			indivCount = 0;
		}
		individuals[indivCount++] = individualW;
		
	}
	
	/**
	 * Get the best {@link IndividualWrapper} from Model 
	 * @param problem
	 * @return
	 */
	public IndividualWrapper removeTheBestIndividual(IProblem problem) {
		
		if (indivCount == 0) {
			return null;
		}
		
		// list is sorted from the best to the worst
		return individuals[0];
	}

}
