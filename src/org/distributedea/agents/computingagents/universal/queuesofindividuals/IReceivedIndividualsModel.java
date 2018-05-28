package org.distributedea.agents.computingagents.universal.queuesofindividuals;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

public interface IReceivedIndividualsModel {

	public void addIndividual(IndividualWrapper individualW,
			IProblem problem, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger);
	
	public IndividualWrapper removeIndividual(IProblem problem);
}
