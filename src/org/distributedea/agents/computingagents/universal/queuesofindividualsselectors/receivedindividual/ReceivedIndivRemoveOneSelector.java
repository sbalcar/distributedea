package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual;

import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

public class ReceivedIndivRemoveOneSelector extends AReceivedIndividualSelector {

	@Override
	public IndividualWrapper getIndividual(IProblem problem) {
		return receivedIndividuals.removeIndividual(problem);
	}

}
