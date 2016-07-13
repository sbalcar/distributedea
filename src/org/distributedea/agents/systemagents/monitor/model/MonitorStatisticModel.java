package org.distributedea.agents.systemagents.monitor.model;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.problems.ProblemToolEvaluation;

public class MonitorStatisticModel {

	private JobID jobID;
	
	private Class<?> problemToSolveClass = null;
	
	private IndividualEvaluated bestIndividualEvaluated;
	
	private List<IndividualEvaluated> bestGeneticMaterial = new ArrayList<>();

	private int MAX_SIZE_OF_MATERIAL = 5;
	private List<MethodStatisticModel> methods = new ArrayList<>();	
	
	public MonitorStatisticModel(ResultsOfComputing resultOfComputing) {
		
		List<IndividualWrapper> bestResultsFromPreviousIteration =
				resultOfComputing.getResultOfComputing();
		if (bestResultsFromPreviousIteration == null) {
			return;
		}
		
		for (IndividualWrapper resultOfCompI : bestResultsFromPreviousIteration) {
			if (resultOfCompI.getIndividualEvaluated() != null) {
				addIndividualWrp(resultOfCompI);
			}
		}
		
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	public JobID getJobID() {
		return jobID;
	}
	
	public Class<?> getProblemToSolveClass() {
		return problemToSolveClass;
	}
	
	public IndividualEvaluated getBestIndividualEvaluated() {
		return bestIndividualEvaluated;
	}
	
	
	public IndividualEvaluated getBestIndividualFromMaterial() {
		
		if (bestGeneticMaterial == null || bestGeneticMaterial.isEmpty()) {
			return null;
		}
			
		IndividualEvaluated bestIndividual = bestGeneticMaterial.get(0);
		for (IndividualEvaluated individualI : bestGeneticMaterial) {
		
			if (isFistIndividualWBetterThanSecond(individualI,
							bestIndividual)) {
				
				bestIndividual = individualI;
			}
		}
		
		return bestIndividual;
	}
	
	public MethodStatisticModel getMethodStatisticModel(AgentDescription description) {
		
		String agentName = description.exportAgentName();
		
		for (MethodStatisticModel modelI : methods) {
			
			AgentDescription descriptionI = modelI.getAgentDescription();
			if (descriptionI.exportAgentName().equals(agentName)) {
				return modelI;
			}
		}
		
		return null;
	}
	
	public void addIndividualWrp(IndividualWrapper individualWrp) {
		
		AgentDescription agentDescInput =
				individualWrp.getAgentDescription();
		IndividualEvaluated individualEvalInput =
				individualWrp.getIndividualEvaluated();
		
		// check initialization of problem Class
		if (problemToSolveClass == null) {
			Class<?> problemToolClass =
					agentDescInput.exportProblemToolClass();
			Class<?> problemClass = ProblemToolEvaluation.
					getProblemClassFromProblemTool(problemToolClass);
			
			problemToSolveClass = problemClass;
		}

		
		// adding Individual to concrete method model
		MethodStatisticModel methodModel = getMethodStatisticModel(agentDescInput);
		if (methodModel == null) {
			methodModel = new MethodStatisticModel(agentDescInput);
			methods.add(methodModel);
		}
		methodModel.addIndividualEvaluated(individualEvalInput);
		
		
		// update best Individual
		updateBestIndividual(individualEvalInput, methodModel);
		
		
		// adding Individual to set of genetic material
		updateGeneticMaterial(individualEvalInput, methodModel);
	}
	
	private void updateBestIndividual(IndividualEvaluated newIndividual,
			MethodStatisticModel methodModel) {

		if (! isFistIndividualWBetterThanSecond(
				newIndividual, bestIndividualEvaluated)) {
			return;
		}
		
		if (bestGeneticMaterial.size() == MAX_SIZE_OF_MATERIAL) {
			bestGeneticMaterial.remove(bestGeneticMaterial.size() -1);
		}
		bestGeneticMaterial.add(0, bestIndividualEvaluated);
		bestIndividualEvaluated = newIndividual;
		
		methodModel.incrementBestCreatedIndividualsCount();
		methodModel.incrementBestCreatedGeneticMaterialCount();
	}
	
	private void updateGeneticMaterial(IndividualEvaluated newIndividual,
			MethodStatisticModel methodModel) {
		
		IndividualEvaluated theWorstIndividualInModel =
				bestGeneticMaterial.get(bestGeneticMaterial.size() -1);
		
		// is not better or contains the same
		if ( (! isFistIndividualWBetterThanSecond(
				newIndividual, theWorstIndividualInModel)) ||
				bestGeneticMaterial.contains(newIndividual) ) {
			return;
		}
		
		if (bestGeneticMaterial.size() == MAX_SIZE_OF_MATERIAL) {
			bestGeneticMaterial.remove(bestGeneticMaterial.size() -1);
		}
		
		for (int i = 0; i < bestGeneticMaterial.size(); i++) {
			
			IndividualEvaluated individualI =
					bestGeneticMaterial.get(i);
			if (isFistIndividualWBetterThanSecond(
					newIndividual, individualI)) {
				
				bestGeneticMaterial.add(i, newIndividual);
				break;
			}
		}

		methodModel.incrementBestCreatedGeneticMaterialCount();
	}
	
	private boolean isFistIndividualWBetterThanSecond(IndividualEvaluated individual1,
			IndividualEvaluated individual2) {
		
		return ProblemToolEvaluation
				.isFistIndividualWBetterThanSecond(individual1,
						individual2, problemToSolveClass);
	}
	
	public Statistic exportStatistic() {

		Statistic statistic = new Statistic(jobID);
		
		for (MethodStatisticModel methodStatModI : methods) {
			MethodStatistic methodStatI =
				methodStatModI.exportStatisticMethod();
			statistic.addMethodStatistic(methodStatI);
		}
		return statistic;
	}
	
}
