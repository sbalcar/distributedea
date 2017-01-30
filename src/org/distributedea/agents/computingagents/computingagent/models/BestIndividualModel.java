package org.distributedea.agents.computingagents.computingagent.models;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

public class BestIndividualModel {

	private IndividualWrapper bestIndividualWrp;

	/**
	 * Returns the best {@link IndividualWrapper}
	 * @return
	 */
	public IndividualWrapper getTheBestIndividualWrp() {
		return bestIndividualWrp;
	}
	
	/**
	 * Returns clone of the best {@link IndividualWrapper}
	 * @return
	 */
	public IndividualWrapper exportTheBestIndividualWrp() {
		
		if (bestIndividualWrp == null) {
			return null;
		} else {
			return bestIndividualWrp.deepClone();
		}
	}
	
	public boolean isGivenindividualBetter(IndividualWrapper iIndividualWpr, IProblem problem,
			IAgentLogger logger) {
		
		if (iIndividualWpr == null || ! iIndividualWpr.valid(logger)) {
			throw new IllegalArgumentException();
		}
		if (problem == null || ! problem.valid(logger)) {
			throw new IllegalArgumentException();
		}
		
		return FitnessTool.isFistIndividualWBetterThanSecond(
						iIndividualWpr, bestIndividualWrp, problem);
	}
	
	public boolean update(IndividualWrapper individualWpr, IProblem problem,
			IAgentLogger logger) {
		
//		if (individualWpr == null || ! individualWpr.valid(logger)) {
//			throw new IllegalArgumentException();
//		}
//		if (problem == null || ! problem.valid(logger)) {
//			throw new IllegalArgumentException();
//		}
		
		if (this.bestIndividualWrp == null) {
			this.bestIndividualWrp = individualWpr;
			return true;
		}
		
		boolean isNewIndividualBetter =
				FitnessTool.isFistIndividualWBetterThanSecond(
						individualWpr, bestIndividualWrp, problem);
		
		if (isNewIndividualBetter) {
			this.bestIndividualWrp = individualWpr;			
		}
		
		return isNewIndividualBetter;
	}
}
