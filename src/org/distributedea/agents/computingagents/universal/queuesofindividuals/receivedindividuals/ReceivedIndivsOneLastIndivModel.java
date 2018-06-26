package org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Model for a set of received {@link IndividualWrapper} from distribution,
 * structurally formed from one last {@link IndividualWrapper}
 * @author stepan
 *
 */
public class ReceivedIndivsOneLastIndivModel implements IReceivedIndividualsModel {

	private IndividualWrapper individualW;
	
	@Override
	public void addIndividual(IndividualWrapper individualW, IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		if (this.individualW == null ||
				FitnessTool.isFistIndividualWBetterThanSecond(
						individualW, this.individualW, problem)) {
			
			this.individualW = individualW;
		}
	}

	@Override
	public IndividualWrapper removeIndividual(IProblem problem) {
		
		IndividualWrapper indivW = this.individualW;
		this.individualW = null;
		
		return indivW;
	}

	@Override
	public IndividualWrapper getIndividual(IProblem problem) {
		
		return this.individualW;
	}

}
