package org.distributedea.agents.systemagents.monitor.model;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.systemagents.Agent_Monitor;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Internal data structure for {@link Agent_Monitor}.
 * Contains Statistic of each running Agents. 
 * @author stepan
 *
 */
public class MonitorStatisticModel {

	private JobID jobID;
	
	private Class<?> problemToSolveClass;
	
	private IndividualEvaluated bestIndividualEvaluated;
	
	private List<IndividualEvaluated> bestGeneticMaterial = new ArrayList<>();

	private int MAX_SIZE_OF_MATERIAL = 5;
	private List<MethodStatisticModel> methods = new ArrayList<>();	
	
	
	/**
	 * Constructor - structure is initialized by set of
	 * received {@link IndividualWrapper}
	 * @param jobID
	 * @param problemToSolveClass
	 * @param resultOfComputing
	 */
	public MonitorStatisticModel(JobID jobID, Class<?> problemToSolveClass,
			IndividualsWrappers resultOfComputing) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (problemToSolveClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		if (resultOfComputing == null || ! resultOfComputing.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualsWrappers.class.getSimpleName() + " is not valid");
		}
		
		this.jobID = jobID;
		this.problemToSolveClass = problemToSolveClass;
		
		List<IndividualWrapper> bestResultsFromPrevIteration =
				resultOfComputing.getIndividualsWrappers();
		if (bestResultsFromPrevIteration == null) {
			return;
		}
		
		for (IndividualWrapper resultOfCompI : bestResultsFromPrevIteration) {
			if (resultOfCompI.getIndividualEvaluated() != null) {
				addIndividualWrp(resultOfCompI);
			}
		}
	}
	
	public MonitorStatisticModel(JobID jobID, Class<?> problemToSolveClass) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (problemToSolveClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		
		this.jobID = jobID;
		this.problemToSolveClass = problemToSolveClass;
	}
	
	/**
	 * Returns identification of {@link JobRun}
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	
	/**
	 * Returns {@link IProblemTool} class
	 * @return
	 */
	public Class<?> getProblemToSolveClass() {
		return problemToSolveClass;
	}
	
	/**
	 * Returns the best {@link IndividualEvaluated}
	 * @return
	 */
	public IndividualEvaluated getBestIndividualEvaluated() {
		return bestIndividualEvaluated;
	}
	
	/**
	 * Returns the best {@link IndividualEvaluated} from mgenetic material
	 * @return
	 */
	public IndividualEvaluated getTheBestIndividualFromMaterial() {
		
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
	
	private MethodStatisticModel getMethodStatisticModel(AgentDescription description) {
		
		String agentName = description.exportMethodName();
		
		for (MethodStatisticModel modelI : methods) {
			
			AgentDescription descriptionI = modelI.getAgentDescription();
			if (descriptionI.exportMethodName().equals(agentName)) {
				return modelI;
			}
		}
		
		return null;
	}
	
	/**
	 * Adds {@link IndividualWrapper} to model
	 * @param individualWrp
	 */
	public void addIndividualWrp(IndividualWrapper individualWrp) {
		
		AgentDescription agentDescInput =
				individualWrp.getAgentDescription();
		IndividualEvaluated individualEvalInput =
				individualWrp.getIndividualEvaluated();
		
		// check initialization of problem Class
		if (problemToSolveClass == null) {
			Class<?> problemToolClass =
					agentDescInput.exportProblemToolClass();
			IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
					problemToolClass, new TrashLogger());
			Class<?> problemClass = problemTool.problemWhichSolves();
			
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
		
		return FitnessTool
				.isFistIndividualEBetterThanSecond(individual1,
						individual2, problemToSolveClass);
	}
	
	/**
	 * Exports {@link Statistic}
	 * @return
	 */
	public Statistic exportStatistic() {

		Statistic statistic = new Statistic(jobID);
		
		for (MethodStatisticModel methodStatModI : methods) {
			MethodStatistic methodStatI =
				methodStatModI.exportMethodStatistic();
			statistic.addMethodStatistic(methodStatI);
		}
		return statistic;
	}
	
}
