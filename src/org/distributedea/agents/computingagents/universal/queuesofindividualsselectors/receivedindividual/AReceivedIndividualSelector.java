package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.IReceivedIndividualSelector;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;

/**
 * Abstract paren-class for selectors of received {@link IndividualWrapper}s
 * @author stepan
 *
 */
public abstract class AReceivedIndividualSelector implements IReceivedIndividualSelector {

	protected IReceivedIndividualsModel receivedIndividuals;
	
	@Override
	public void init(IReceivedIndividualsModel receivedIndividuals) {
		if (receivedIndividuals == null) {
			throw new IllegalArgumentException("Argument " +
					IReceivedIndividualsModel.class.getSimpleName() + " is not valid");
		}
		
		this.receivedIndividuals = receivedIndividuals;
	}
}
