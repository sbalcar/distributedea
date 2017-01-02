package org.distributedea.agents.computingagents.computingagent.models;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.AgentComputingLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

public class BestIndividualModel {

	private IndividualWrapper bestIndividualWrp;

	/**
	 * Returns clone of the best {@link IndividualWrapper}
	 * @return
	 */
	public IndividualWrapper exportBestIndividualWrp() {
		
		if (bestIndividualWrp == null) {
			return null;
		} else {
			return bestIndividualWrp.deepClone();
		}
	}
	
	public boolean isNewBetter(IndividualWrapper iIndividualWpr, IProblemDefinition problemDef,
			AgentComputingLogger logger) {
		
		if (iIndividualWpr == null || ! iIndividualWpr.valid(logger)) {
			iIndividualWpr.valid(logger);
			throw new IllegalArgumentException();
		}
		if (problemDef == null || ! problemDef.valid(logger)) {
			throw new IllegalArgumentException();
		}
		
		return FitnessTool.isFistIndividualWBetterThanSecond(
						iIndividualWpr, bestIndividualWrp, problemDef);
	}
	
	public void update(IndividualWrapper individualWpr, IProblemDefinition problemDef,
			long generationNumber, AgentComputingLogger logger) {
		
		if (individualWpr == null || ! individualWpr.valid(logger)) {
			throw new IllegalArgumentException();
		}
		if (problemDef == null || ! problemDef.valid(logger)) {
			throw new IllegalArgumentException();
		}
		
		if (this.bestIndividualWrp == null) {
			this.bestIndividualWrp = individualWpr;
			return;
		}
		
		boolean isReceivedIndividualBetter =
				isNewBetter(individualWpr, problemDef, logger);
		
		if (isReceivedIndividualBetter) {
			
			IndividualEvaluated receivedIndividual =
					individualWpr.getIndividualEvaluated();
			MethodDescription description =
					individualWpr.getAgentDescription();
			JobID jobID =
					individualWpr.getJobID();
			
			double fitnessImprovement = Math.abs(
					receivedIndividual.getFitness() -
					this.bestIndividualWrp.getIndividualEvaluated().getFitness());
			
			this.bestIndividualWrp = individualWpr;
			
			logger.logDiffImprovementOfDistribution(fitnessImprovement,
					generationNumber, receivedIndividual.getIndividual(),
					description, jobID);
		}
	}
}
