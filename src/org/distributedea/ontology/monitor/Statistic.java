package org.distributedea.ontology.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.problems.ProblemToolEvaluation;

import jade.content.Concept;

public class Statistic implements Concept {

	private static final long serialVersionUID = 1L;

	private JobID jobID;
	
	private List<MethodStatistic> methodStatistics;

	@Deprecated
	public Statistic() {}
	
	public Statistic(JobID jobID) {
		this.jobID = jobID;
		this.methodStatistics = new ArrayList<>();
	}
	
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	public List<MethodStatistic> getStatistics() {
		return methodStatistics;
	}
	@Deprecated
	public void setStatistics(List<MethodStatistic> statistics) {
		this.methodStatistics = statistics;
	}
	public void addMethodStatistic(MethodStatistic methodStatistic) {
		if (methodStatistics == null) {
		    methodStatistics = new ArrayList<>();
		}
		methodStatistics.add(methodStatistic);
	}
	
	
	public long exportNumberOfMethods() {
		return methodStatistics.size();
	}

	public MethodStatistic exportRandomMethodStatistic() {
		Random ran = new Random();
		int index = ran.nextInt(methodStatistics.size());
		return methodStatistics.get(index);
	}
	
	public long exportNumberOfImprovementsAchievedByAllMethods() {
		
		long numberOfImprovement = 0;
		for (MethodStatistic statisticI : methodStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 
			numberOfImprovement += statisticResultI
					.getNumberOfTheBestCreatedIndividuals();
		}
		return numberOfImprovement;
	}

	public long exportNumberOfGoodMaterialAchievedByAllMethods() {
		
		long numberOfGoodMaterial = 0;
		for (MethodStatistic statisticI : methodStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 			
			numberOfGoodMaterial += statisticResultI
					.getNumberOfGoodCreatedMaterial();
		}
		return numberOfGoodMaterial;
	}

	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfImprovement() {
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic bestMethodStatistic = this.methodStatistics.get(0);
		
		for (MethodStatistic statisticI : methodStatistics) {
			
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
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic worstMethodStatistic = this.methodStatistics.get(0);
		
		for (MethodStatistic statisticI : methodStatistics) {
			
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
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodStatistics.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		Class<?> problemClass = ProblemToolEvaluation.
				getProblemClassFromProblemTool(problemToolClass);
		
		IndividualEvaluated bestOfAllI = method0.exportBestIndividual();
		
		for (MethodStatistic statisticI : methodStatistics) {
			
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
	
	public IndividualWrapper exportBestIndividualWrapper() {
		
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
