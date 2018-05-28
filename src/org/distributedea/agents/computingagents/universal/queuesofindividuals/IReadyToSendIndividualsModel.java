package org.distributedea.agents.computingagents.universal.queuesofindividuals;

import java.util.List;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

public interface IReadyToSendIndividualsModel {

	public void addIndividual(List<IndividualEvaluated> individualsEval,
			IProblem problem);
	
	public void addIndividual(IndividualEvaluated individualEval,
			IProblem problem);
	
	public IndividualEvaluated getIndividual(IProblem problem);
	
}
