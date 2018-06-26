package org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals;

import java.util.List;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

/**
 * Structure serves as model for {@link IndividualEvaluated}s ready to send,
 * structurally formed by one last {@link IndividualEvaluated}
 * @author stepan
 *
 */
public class ReadyToSendIndivsOneLastIndivModel implements IReadyToSendIndividualsModel {
	
	private IndividualEvaluated individualEval;
	
	@Override
	public void addIndividual(List<IndividualEvaluated> individualsEval,
			IProblem problem) {
		if (individualsEval == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		if (! individualsEval.isEmpty()) {
			this.individualEval = individualsEval.get(0);
		}
	}

	@Override
	public void addIndividual(IndividualEvaluated individualEval,
			IProblem problem) {

		if (individualEval != null) {
			this.individualEval = individualEval;
		}
	}

	@Override
	public IndividualEvaluated removeIndividual(IProblem problem) {

		IndividualEvaluated result = this.individualEval;
		this.individualEval = null;
		
		return result;
	}

	@Override
	public IndividualEvaluated getIndividual(IProblem problem) {
		return this.individualEval;
	}
}
