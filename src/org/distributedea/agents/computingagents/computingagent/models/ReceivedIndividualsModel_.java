package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;
import org.distributedea.structures.comparators.CmpIndividualWrapper;

public class ReceivedIndividualsModel_ {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;

	private List<IndividualWrapper> receivedIndividuals = new ArrayList<>();
	
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
		
		if (individualW == null || (! individualW.validation(problem, dataset, problemTool, logger))) {
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		// resize model to the max-defined size
		while (receivedIndividuals.size() > MAX_NUMBER_OF_INDIVIDUAL) {
			receivedIndividuals.remove(receivedIndividuals.size() -1);
		}
		
		receivedIndividuals.add(individualW);
		
		Collections.sort(receivedIndividuals, new CmpIndividualWrapper(problem));
	}
	
	/**
	 * Get the best {@link IndividualWrapper} from Model 
	 * @param problem
	 * @return
	 */
	public IndividualWrapper removeTheBestIndividual(IProblem problem) {
		
		if (receivedIndividuals.isEmpty()) {
			return null;
		}
		
		// list is sorted from the best to the worst
		return receivedIndividuals.get(0);
	}

}
