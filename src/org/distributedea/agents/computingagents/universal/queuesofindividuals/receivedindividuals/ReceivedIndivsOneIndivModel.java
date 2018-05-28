package org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

public class ReceivedIndivsOneIndivModel implements IReceivedIndividualsModel {

	private IndividualWrapper individualW;
	
	@Override
	public void addIndividual(IndividualWrapper individualW, IProblem problem,
			Dataset dataset, IProblemTool problemTool, IAgentLogger logger) {
		
		if (this.individualW == null ||
				FitnessTool.isFistIndividualWBetterThanSecond(
						individualW, this.individualW, problem)) {
			
			this.individualW = individualW;
		}
	}

	@Override
	public IndividualWrapper removeIndividual(IProblem problem) {
		return this.individualW;
	}

}
