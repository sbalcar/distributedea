package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.IProblemTool;
import org.distributedea.structures.comparators.CmpIndividualWrapper;

/**
 * Model for a set of received {@link IndividualWrapper} from distribution
 * @author stepan
 *
 */
public class ReceivedIndividualsModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	//4614
	private List<IndividualWrapper> receivedIndividuals =
//			Collections.synchronizedList(new ArrayList<IndividualWrapper>());
			new ArrayList<IndividualWrapper>();
	
	/**
	 * Add Received {@link IndividualWrapper} to Model
	 * @param individualW
	 * @param problemDef
	 * @param problem
	 * @param problemTool
	 * @param logger
	 */
	public void addIndividual(IndividualWrapper individualW,
			IProblemDefinition problemDef, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger) {
		
		if (individualW == null || (! individualW.validation(problemDef, dataset, problemTool, logger))) {
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		// resize model to the max-defined size
		while (receivedIndividuals.size() > MAX_NUMBER_OF_INDIVIDUAL) {
			receivedIndividuals.remove(receivedIndividuals.size() -1);
		}
		
		receivedIndividuals.add(individualW);
		
		Collections.sort(receivedIndividuals, new CmpIndividualWrapper(problemDef));
	}
	
	/**
	 * Get the best {@link IndividualWrapper} from Model 
	 * @param problemDef
	 * @return
	 */
	public IndividualWrapper removeTheBestIndividual(IProblemDefinition problemDef) {
		
		if (receivedIndividuals.isEmpty()) {
			return null;
		}
		
		// list is sorted from the best to the worst
		return receivedIndividuals.get(0);
	}

}
