package org.distributedea.agents.systemagents.monitor.bestindividualmodel;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

public class IndividualModel {

	private IndividualWrapper individualWrp;
	
	/**
	 * Constructor
	 * @param individualWrp
	 */
	public IndividualModel(IndividualWrapper individualWrp) {
		if (individualWrp == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " can not be null");
		}
		this.individualWrp = individualWrp;
	}
	
	public IndividualWrapper getIndividualWrapper() {
		return this.individualWrp;
	}
	
	public void update(IndividualWrapper newIndivWrp, IProblem problem) {
		
		if (FitnessTool.isFirstIndividualWBetterThanSecond(
				newIndivWrp, individualWrp.getIndividualEvaluated().getFitness(), problem)) {
			this.individualWrp = newIndivWrp;
		}
	}
}
