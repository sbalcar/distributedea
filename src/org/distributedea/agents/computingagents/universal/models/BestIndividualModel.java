package org.distributedea.agents.computingagents.universal.models;

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
	
	public boolean isGivenindividualBetter(IndividualWrapper individualWpr,
			IProblem problem, IAgentLogger logger) {
		
		if (individualWpr == null ||
				(! individualWpr.validQuickly(logger))) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (problem == null || (! problem.valid(logger))) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		
		return FitnessTool.isFistIndividualWBetterThanSecond(
				individualWpr, bestIndividualWrp, problem);
	}
	
	public boolean update(IndividualWrapper individualWpr, IProblem problem,
			IAgentLogger logger) {
		
		if (individualWpr == null ||
				(! individualWpr.validQuickly(logger))) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (problem == null || (! problem.valid(logger))) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
				
		if (FitnessTool.isFistIndividualWBetterThanSecond(
				individualWpr, bestIndividualWrp, problem)) {
			
			this.bestIndividualWrp = individualWpr;
			return true;
		}
		
		return false;
	}
}
