package org.distributedea.agents.computingagents.computingagent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemToolEvaluation;

/**
 * Set of received Individuals from distribution
 * @author Stepan
 *
 */
public class IndividualModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private List<IndividualWrapper> receivedIndividuals =
			Collections.synchronizedList(new ArrayList<IndividualWrapper>());
	
	/**
	 * Add Received Individual to Model
	 * @param individualW
	 * @param logger
	 */
	public synchronized void addIndividual(IndividualWrapper individualW, Problem problem, AgentLogger logger) {
					
		if (individualW == null || (! individualW.validation(problem, logger))) {
			Exception exception = new IllegalStateException("Recieved Individual is not valid");
			logger.logThrowable("", exception);
			return;
		}
		
		// resize model to the max-defined size
		while (receivedIndividuals.size() > MAX_NUMBER_OF_INDIVIDUAL) {
			receivedIndividuals.remove(0);
		}
		
		receivedIndividuals.add(individualW);
	}

	/**
	 * Get the best Individual from Model 
	 * @param problem
	 * @return
	 */
	public synchronized IndividualWrapper getBestIndividual(Problem problem) {
		
		if (receivedIndividuals.isEmpty()) {
			return null;
		}
		
		IndividualWrapper bestIndividual = receivedIndividuals.get(0);
		for (IndividualWrapper indWrapI : receivedIndividuals) {
			
			boolean isIndividualIBetter =
					ProblemToolEvaluation.isFistIndividualBetterThanSecond(
							indWrapI.getIndividualEvaluated(),
							bestIndividual.getIndividualEvaluated(),
							problem);
			
			if (isIndividualIBetter) {
				bestIndividual = indWrapI;
			}
		}
		
		return bestIndividual;
	}

}
