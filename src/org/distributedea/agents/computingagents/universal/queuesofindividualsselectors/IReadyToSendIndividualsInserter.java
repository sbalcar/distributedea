package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors;

import java.util.List;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

/**
 * Inteface for inserters to queue of {@link IndividualEvaluated}s to distribution
 * @author stepan
 *
 */
public interface IReadyToSendIndividualsInserter {

	public void init(IReadyToSendIndividualsModel readyToSendIndividuals);
	
	public void insertIndiv(IndividualEvaluated individualEval, IProblem problem);
	public void insertIndivs(List<IndividualEvaluated> individualsEval, IProblem problem);
}
