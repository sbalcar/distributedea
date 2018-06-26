package org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Model for a set of received {@link IndividualWrapper} from distribution,
 * structurally formed from one queue of {@link IndividualWrapper}
 * @author stepan
 *
 */
public class ReceivedIndivsOneQueueModel implements IReceivedIndividualsModel {
	
	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private int insertedIndivIndex = -1;
	private int removedIndivIndex = 0;
	
	
	private IndividualWrapper[] individuals =
			new IndividualWrapper[MAX_NUMBER_OF_INDIVIDUAL];
	
	/**
	 * Add Received {@link IndividualWrapper} to Model
	 * @param individualW
	 * @param problem
	 * @param dataset
	 * @param problemTool
	 * @param logger
	 */
	public void addIndividual(IndividualWrapper individualW,
			IProblem problem, Dataset dataset, IAgentLogger logger) {
		
		if (individualW == null) {
			throw new IllegalStateException("Recieved Individual is not valid");
		}
		
		if (insertedIndivIndex == individuals.length -1) {
			insertedIndivIndex = -1;
		}
		
		individuals[++insertedIndivIndex] = individualW;
		
	}
	
	@Override
	public IndividualWrapper removeIndividual(IProblem problem) {
		
		IndividualWrapper result = individuals[removedIndivIndex++];
		
		if (removedIndivIndex == individuals.length) {
			removedIndivIndex = 0;
		}
		
		return result;
	}

	@Override
	public IndividualWrapper getIndividual(IProblem problem) {
		
		return individuals[removedIndivIndex];	
	}

}
