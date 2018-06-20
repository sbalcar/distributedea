package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Interface for selectors of received {@link IndividualWrapper}s
 * @author stepan
 *
 */
public interface IReceivedIndividualSelector {

	public void init(IReceivedIndividualsModel receivedIndividuals);
	
	public IndividualWrapper getIndividual(IProblem problem);
}
