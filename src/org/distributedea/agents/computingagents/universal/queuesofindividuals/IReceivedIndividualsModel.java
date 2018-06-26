package org.distributedea.agents.computingagents.universal.queuesofindividuals;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

public interface IReceivedIndividualsModel {

	public void addIndividual(IndividualWrapper individualW,
			IProblem problem, Dataset dataset, IAgentLogger logger);
	
	public IndividualWrapper getIndividual(IProblem problem);
	public IndividualWrapper removeIndividual(IProblem problem);

}
