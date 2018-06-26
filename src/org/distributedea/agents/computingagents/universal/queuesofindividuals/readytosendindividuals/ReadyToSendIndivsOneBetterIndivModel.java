package org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals;

import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problem.IProblem;

/**
 * Structure serves as model for {@link IndividualEvaluated}s ready to send,
 * structurally formed from one better {@link IndividualEvaluated}
 * @author stepan
 *
 */
public class ReadyToSendIndivsOneBetterIndivModel implements IReadyToSendIndividualsModel {

	private IndividualEvaluated individualEval;
	
	@Override
	public void addIndividual(List<IndividualEvaluated> individualsEval,
			IProblem problem) {
		if (individualsEval == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
				
		IndividualsEvaluated individuals =
				new IndividualsEvaluated(individualsEval);
		IndividualEvaluated theBestIndividual =
				individuals.exportTheBestIndividual(problem);
		
		addIndividual(theBestIndividual, problem);
	}

	@Override
	public void addIndividual(IndividualEvaluated individualEval,
			IProblem problem) {

		if (this.individualEval == null ||
				FitnessTool.isFirstIndividualEBetterThanSecond(
						individualEval, this.individualEval, problem)) {
			
			this.individualEval = individualEval;
		}
	}
	
	@Override
	public IndividualEvaluated getIndividual(IProblem problem) {
		return this.individualEval;
	}

	@Override
	public IndividualEvaluated removeIndividual(IProblem problem) {
		
		IndividualEvaluated result = this.individualEval;
		this.individualEval = null;
		
		return result;
	}

}
