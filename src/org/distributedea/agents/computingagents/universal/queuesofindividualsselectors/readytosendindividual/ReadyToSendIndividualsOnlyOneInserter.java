package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual;

import java.util.List;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;


public class ReadyToSendIndividualsOnlyOneInserter extends AReadyToSendIndividualsInserter {

	private IReadyToSendIndividualsModel readyToSendIndividuals;

	@Override
	public void insertIndiv(IndividualEvaluated individualEval, IProblem problem) {
		if (individualEval == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		readyToSendIndividuals.addIndividual(individualEval, problem);
	}
	
	@Override
	public void insertIndivs(List<IndividualEvaluated> individualsEval, IProblem problem) {
		if (individualsEval == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		if (individualsEval.isEmpty()) {
			return;
		}
		
		IndividualEvaluated individualE = individualsEval.get(0);
		readyToSendIndividuals.addIndividual(individualE, problem);
	}

}
