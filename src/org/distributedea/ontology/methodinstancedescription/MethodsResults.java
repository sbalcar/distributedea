package org.distributedea.ontology.methodinstancedescription;

import java.util.List;
import java.util.Random;

import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodInstanceDescription;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.problems.ProblemToolEvaluation;


public class MethodsResults {

	private JobID jobID;
	private Iteration iteration;
	private MethodInstanceDescription methodInstanceDescription;
	
	private List<MethodStatistic> methodStatistic;

	
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	public Iteration getIteration() {
		return iteration;
	}
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}

	public MethodInstanceDescription getMethodInstanceDescription() {
		return methodInstanceDescription;
	}
	public void setMethodInstanceDescription(
			MethodInstanceDescription methodInstanceDescription) {
		this.methodInstanceDescription = methodInstanceDescription;
	}
	
	public List<MethodStatistic> getMethodStatistic() {
		return methodStatistic;
	}
	public void setMethodStatistic(List<MethodStatistic> methodStatistic) {
		this.methodStatistic = methodStatistic;
	}
	
	public long exportNumberOfMethods() {
		return methodStatistic.size();
	}

	public MethodStatistic exportRandomMethodStatistic() {
		if (methodStatistic == null || methodStatistic.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int index = ran.nextInt(methodStatistic.size());
		return methodStatistic.get(index);
	}
	
	public long exportNumberOfImprovementsAchievedByAllMethods() {
		
		long numberOfImprovement = 0;
		for (MethodStatistic statisticI : methodStatistic) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 
			numberOfImprovement += statisticResultI
					.getNumberOfTheBestCreatedIndividuals();
		}
		return numberOfImprovement;
	}

	public long exportNumberOfGoodMaterialAchievedByAllMethods() {
		
		long numberOfGoodMaterial = 0;
		for (MethodStatistic statisticI : methodStatistic) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 			
			numberOfGoodMaterial += statisticResultI
					.getNumberOfGoodCreatedMaterial();
		}
		return numberOfGoodMaterial;
	}

	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfImprovement() {
		
		if (this.methodStatistic == null || this.methodStatistic.isEmpty()) {
			return null;
		}
		
		MethodStatistic bestMethodStatistic = this.methodStatistic.get(0);
		
		for (MethodStatistic statisticI : methodStatistic) {
			
			MethodStatisticResult statisticRsltI =
					statisticI.getMethodStatisticResult();
			MethodStatisticResult bestMethodStResult =
					bestMethodStatistic.getMethodStatisticResult();
			
			if (statisticRsltI.getNumberOfTheBestCreatedIndividuals() >
				bestMethodStResult.getNumberOfTheBestCreatedIndividuals()) {
				bestMethodStatistic = statisticI;
			}
		}
		return bestMethodStatistic;
	}

	public MethodStatistic exportMethodAchievedTheLeastQuantityOfImprovement() {
		
		if (this.methodStatistic == null || this.methodStatistic.isEmpty()) {
			return null;
		}
		
		MethodStatistic worstMethodStatistic = this.methodStatistic.get(0);
		
		for (MethodStatistic statisticI : methodStatistic) {
			
			MethodStatisticResult statisticRsltI =
					statisticI.getMethodStatisticResult();
			MethodStatisticResult worstMethodStResult =
					worstMethodStatistic.getMethodStatisticResult();
			
			if (statisticRsltI.getNumberOfTheBestCreatedIndividuals() <
				worstMethodStResult.getNumberOfTheBestCreatedIndividuals()) {
				worstMethodStatistic = statisticI;
			}
		}
		return worstMethodStatistic;
	}
	
	public IndividualEvaluated exportBestIndividual() {
		
		if (this.methodStatistic == null || this.methodStatistic.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodStatistic.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		Class<?> problemClass = ProblemToolEvaluation.
				getProblemClassFromProblemTool(problemToolClass);
		
		IndividualEvaluated bestOfAllI = method0.exportBestIndividual();
		
		for (MethodStatistic statisticI : methodStatistic) {
			
			IndividualEvaluated bestI =
					statisticI.exportBestIndividual();
			
			boolean isNewBetter = ProblemToolEvaluation
					.isFistIndividualWBetterThanSecond(bestI,
					bestOfAllI, problemClass);
			if (isNewBetter) {
				bestOfAllI = bestI;
			}
		}
		return bestOfAllI;
	}
	
	public IndividualWrapper exportBestResultOfComputing() {
		
		MethodStatistic bestMethod = exportMethodAchievedTheGreatestQuantityOfImprovement();
		AgentDescription agentDescription = bestMethod.getAgentDescription();
		IndividualEvaluated bestIndividual = bestMethod.getMethodStatisticResult().getBestIndividual();
		
		IndividualWrapper resultOfComputing = new IndividualWrapper();
		resultOfComputing.setJobID(getJobID());
		resultOfComputing.setAgentDescription(agentDescription);
		resultOfComputing.setIndividualEvaluated(bestIndividual);
		
		return resultOfComputing;
	}
}
