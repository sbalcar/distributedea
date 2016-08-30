package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;

/**
 * Model for a set of received Individuals from distribution
 * @author stepan
 *
 */
public class ReceivedIndividualsModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private List<IndividualWrapper> receivedIndividuals =
			Collections.synchronizedList(new ArrayList<IndividualWrapper>());
	
	/**
	 * Add Received Individual to Model
	 * @param individualW
	 * @param logger
	 */
	public synchronized void addIndividual(IndividualWrapper individualW,
			Problem problem, IProblemTool problemTool, IAgentLogger logger) {
		
		if (individualW == null || (! individualW.validation(problem, problemTool, logger))) {
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
					FitnessTool.isFirstIndividualEBetterThanSecond(
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
