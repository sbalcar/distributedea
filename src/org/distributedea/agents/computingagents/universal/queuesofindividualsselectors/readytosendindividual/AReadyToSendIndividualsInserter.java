package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.IReadyToSendIndividualsInserter;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

/**
 * Abstract parent-class for inserters to queue of {@link IndividualEvaluated}s to distribution
 * @author stepan
 *
 */
public abstract class AReadyToSendIndividualsInserter implements IReadyToSendIndividualsInserter{

	protected IReadyToSendIndividualsModel readyToSendIndividuals;
	
	@Override
	public void init(IReadyToSendIndividualsModel readyToSendIndividuals) {
		if (readyToSendIndividuals == null) {
			throw new IllegalArgumentException("Argument " +
					IReadyToSendIndividualsModel.class.getSimpleName() + " is not valid");
		}
		this.readyToSendIndividuals = readyToSendIndividuals;
	}
}
