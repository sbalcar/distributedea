package org.distributedea.agents.systemagents.monitor.statisticmodel;

import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.systemagents.Agent_Monitor;
import org.distributedea.agents.systemagents.monitor.statisticmodel.individualhashesmodel.IndividualHashesModel;
import org.distributedea.agents.systemagents.monitor.statisticmodel.methodmodel.MethodStatisticModel;
import org.distributedea.agents.systemagents.monitor.statisticmodel.methodmodel.MethodStatisticsModel;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.problem.AProblem;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

/**
 * Internal data structure for {@link Agent_Monitor}.
 * Contains Statistic of each running Agents. 
 * @author stepan
 *
 */
public class MonitorStatisticModel {

	private JobID jobID;
	
	private IProblem problem;
	
	private IndividualHash bestIndividual;
	
	private IndividualHashesModel bestGeneticMaterial = new IndividualHashesModel();
	
	private MethodStatisticsModel methods = new MethodStatisticsModel();	

	
	
	/**
	 * Constructor - structure is initialized by set of
	 * received {@link IndividualWrapper}
	 * @param jobID
	 * @param problem
	 * @param resultOfComputing
	 */
	public MonitorStatisticModel(JobID jobID, IProblem problem,
			IndividualsWrappers resultOfComputing) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AProblem.class.getSimpleName() + " is not valid");
		}
		if (resultOfComputing == null || ! resultOfComputing.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualsWrappers.class.getSimpleName() + " is not valid");
		}
		
		this.jobID = jobID;
		this.problem = problem;
		
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
	
	/**
	 * Constructor
	 * @param jobID
	 * @param problem
	 */
	public MonitorStatisticModel(JobID jobID, IProblem problem) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		
		this.jobID = jobID;
		this.problem = problem;
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
	public IProblem getProblemToSolve() {
		return problem;
	}
	
	/**
	 * Returns the best {@link IndividualEvaluated}
	 * @return
	 */
	public IndividualHash getBestIndividualEvaluated() {
		return bestIndividual;
	}
	
	
	/**
	 * Adds {@link IndividualWrapper} to model
	 * @param individualWrp
	 */
	public void addIndividualWrp(IndividualWrapper individualWrp) {
		
		MethodDescription methDescI =
				individualWrp.getMethodDescription();
		IndividualEvaluated indivEval =
				individualWrp.getIndividualEvaluated();
		IndividualHash indivHash =
				new IndividualHash(indivEval);
		
		// check initialization of problem Class
		if (problem == null) {
			problem = methDescI.getProblem();
		}
		
		// adding Individual to concrete method model
		MethodStatisticModel methodModel = methods.getMethodStatisticModel(methDescI);
		if (methodModel == null) {
			methodModel = new MethodStatisticModel(methDescI);
			methods.add(methodModel);
		}
		methodModel.addIndividualHash(indivHash);
		
		
		// update best Individual
		updateBestIndividual(indivHash, methodModel);
		
		
		// adding Individual to set of genetic material
		updateGeneticMaterial(indivHash, methodModel);
	}
	
	private void updateBestIndividual(IndividualHash newIndividual,
			MethodStatisticModel methodModel) {

		if (bestIndividual == null ||
			FitnessTool.isFistFitnessBetterThanSecond(
				newIndividual.getFitness(),
				bestIndividual.getFitness(),
				problem)) {
			
			bestIndividual = newIndividual;			
			methodModel.incrementBestCreatedIndividualsCount();
		}
		
	}
	
	private void updateGeneticMaterial(IndividualHash individual,
			MethodStatisticModel methodModel) {
		
		if (bestGeneticMaterial.update(individual, problem)) {
			methodModel.incrementBestCreatedGeneticMaterialCount();
		}
	}
	
	
	/**
	 * Exports {@link Statistic}
	 * @return
	 */
	public Statistic exportStatistic() {
		return new Statistic(jobID,
				methods.exportMethodStatistics());
	}
	
}
