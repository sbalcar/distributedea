package org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQualitiOfFitnessAverage;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQualityOfBestIndividual;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQuantityOfGoodMaterial;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQuantityOfImprovement;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Structure represents statistics and results of one Iteration
 * @author stepan
 *
 */
public class MethodsStatistics {

	private JobID jobID;
	private Iteration iteration;
	
	private List<MethodStatistic> methodsStatistics;

	/**
	 * Constructor specifies {@link JobID} and {@link Iteration}
	 * @param jobID
	 * @param iteration
	 */
	public MethodsStatistics(JobID jobID, Iteration iteration) {
		this.jobID = jobID;
		this.iteration = iteration;
		this.methodsStatistics = new ArrayList<>();
	}
	
	/**
	 * Returns {@link JobID}
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	
	/**
	 * Returns {@link Iteration}
	 * @return
	 */
	public Iteration getIteration() {
		return iteration;
	}
	
	/**
	 * Returns instances of {@link MethodStatistic} saved inside
	 * @return
	 */
	public List<MethodStatistic> getMethodsStatistics() {
		return methodsStatistics;
	}
	private void setMethodStatistic(List<MethodStatistic> methodsStatistics) {
		this.methodsStatistics = methodsStatistics;
	}
	/**
	 * Returns clone of {@link AgentDescriptions}.
	 * @return
	 */
	public AgentDescriptions exportAgentDescriptions() {
		
		List<AgentDescription> descriptions = new ArrayList<>();
		for (MethodStatistic methodStatI : methodsStatistics) {
			AgentDescription agentDescriptionI =
					methodStatI.getAgentDescription();
			descriptions.add(agentDescriptionI.deepClone());
		}
		return new AgentDescriptions(descriptions);
	}
	/**
	 * Adds {@link MethodStatistic}
	 * @param methodStatistic
	 */
	public void addMethodStatistic(MethodStatistic methodStatistic) {
		this.methodsStatistics.add(methodStatistic);
	}
	
	/**
	 * Returns number of {@link MethodStatistic}
	 * @return
	 */
	public int getNumberOfMethodsStatistics() {
		return methodsStatistics.size();
	}

	/**
	 * Exports {@link MethodsStatistics} of method based on given {@link Agent_ComputingAgent} classes
	 * @param agentClasses
	 * @return
	 */
	public MethodsStatistics getMethodStatisticsOfAgentClasses(
			List<Class<?>> agentClasses) {
		
		List<MethodStatistic> methodInstances = getMethodInstancesOf(
				agentClasses);
		
		JobID cloneJobID = jobID.deepClone();
		Iteration cloneIteration = iteration.deepClone();
		
		MethodsStatistics results =
				new MethodsStatistics(cloneJobID, cloneIteration);
		results.setMethodStatistic(methodInstances);
		
		return results;
	}
	private List<MethodStatistic> getMethodInstancesOf(List<Class<?>> agentClasses) {
		
		List<MethodStatistic> selectedStatistic = new ArrayList<>();
		
		for (Class<?> agentClassI : agentClasses) {
			List<MethodStatistic> statisticI =
					getMethodInstancesOf(agentClassI);
			selectedStatistic.addAll(statisticI);
		}
		return selectedStatistic;
	}
	
	private List<MethodStatistic> getMethodInstancesOf(Class<?> agentClass) {
		if (methodsStatistics == null || methodsStatistics.isEmpty()) {
			return null;
		}
		
		List<MethodStatistic> selectedMethods = new ArrayList<>();
		for (MethodStatistic methodStatisticI : methodsStatistics) {
			AgentDescription agentDescriptionI =
					methodStatisticI.getAgentDescription();
			Class<?> agentClassI = agentDescriptionI.exportAgentClass();
			if (agentClassI == agentClass) {
				selectedMethods.add(methodStatisticI);
			}
		}
		
		return selectedMethods;
	}
	
	/**
	 * Exports one random selected {@link MethodStatistic}
	 * @return
	 */
	public MethodStatistic exportRandomMethodStatistic() {
		if (methodsStatistics == null || methodsStatistics.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int index = ran.nextInt(methodsStatistics.size());
		return methodsStatistics.get(index);
	}
	
	/**
	 * Returns total number of gradual improved created by all methods.
	 * @return
	 */
	public long exportNumberOfImprovementsAchievedByAllMethods() {
		
		long numberOfImprovement = 0;
		for (MethodStatistic statisticI : methodsStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 
			numberOfImprovement += statisticResultI
					.getNumberOfTheBestCreatedIndividuals();
		}
		return numberOfImprovement;
	}

	/**
	 * Returns total number of good material created by all methods.
	 * Good material does not mean only numbers of all improvement,
	 * but number of good Individuals
	 * @return
	 */
	public long exportNumberOfGoodMaterialAchievedByAllMethods() {
		
		long numberOfGoodMaterial = 0;
		for (MethodStatistic statisticI : methodsStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 			
			numberOfGoodMaterial += statisticResultI
					.getNumberOfGoodCreatedMaterial();
		}
		return numberOfGoodMaterial;
	}

	/**
	 * Exports {@link MethodStatistic} of method which achieved the greatest quantity of improvement
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfImprovement() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodsStatistics, new ComparatorQuantityOfImprovement());
		return methodsStatistics.get(methodsStatistics.size() -1);
	}
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the least quantity of improvement
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheLeastQuantityOfImprovement() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}

		Collections.sort(methodsStatistics, new ComparatorQuantityOfImprovement());
		return methodsStatistics.get(0);
	}
	
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the greatest
	 * quantity of good material
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfGoodMaterial() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodsStatistics, new ComparatorQuantityOfGoodMaterial());
		return methodsStatistics.get(methodsStatistics.size() -1);
	}
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the least
	 * quantity of good material
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheLeastQuantityOfGoodMaterial() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}

		Collections.sort(methodsStatistics, new ComparatorQuantityOfGoodMaterial());
		return methodsStatistics.get(0);
	}
	
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the greatest
	 * quantity of created type of individuals
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfType() {
		if (methodsStatistics == null | methodsStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodsStatistics, new ComparatorQuantityOfMaterial());
		return methodsStatistics.get(methodsStatistics.size() -1);
	}
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the least
	 * quantity of created type of individuals
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheLeastQuantityOfType() {
		if (methodsStatistics == null | methodsStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodsStatistics, new ComparatorQuantityOfMaterial());
		return methodsStatistics.get(0);
	}

	/**
	 * Exports {@link MethodStatistic} of method which achieved the best
	 * average of fitness of created individuals
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheBestAverageOfFitness() {
		if (methodsStatistics == null | methodsStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodsStatistics.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, new TrashLogger());	
		Class<?> problemClass = problemTool.problemWhichSolves();

		Collections.sort(methodsStatistics, new ComparatorQualitiOfFitnessAverage(problemClass));
		return methodsStatistics.get(methodsStatistics.size() -1);
	}
	
	/**
	 * Exports {@link MethodStatistic} of method which achieved the worst
	 * average of fitness of created individuals
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheWorstAverageOfFitness() {
		if (methodsStatistics == null | methodsStatistics.isEmpty()) {
			return null;
		}

		MethodStatistic method0 = this.methodsStatistics.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, new TrashLogger());	
		Class<?> problemClass = problemTool.problemWhichSolves();
		
		Collections.sort(methodsStatistics, new ComparatorQualitiOfFitnessAverage(problemClass));
		return methodsStatistics.get(0);
	}

	/**
	 * Exports {@link MethodStatistic} of method which the best created Individual
	 * is in comparison with other the worst of best.
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheWorstOfBestIndividuals() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodsStatistics.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, new TrashLogger());	
		Class<?> problemClass = problemTool.problemWhichSolves();
		
		Collections.sort(methodsStatistics,
				new ComparatorQualityOfBestIndividual(problemClass));
		
		return methodsStatistics.get(0);
	}
	
	/**
	 * Exports {@link MethodStatistic} of method which the best created Individual
	 * is in comparison with other the best of best.
	 * @return
	 */
	public MethodStatistic exportMethodAchievedTheBestOfBestIndividuals() {
		
		if (this.methodsStatistics == null || this.methodsStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodsStatistics.get(0);
		AgentDescription agentDescription = method0.getAgentDescription();
		
		Class<?> problemToolClass =
				agentDescription.exportProblemToolClass();
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, new TrashLogger());	
		Class<?> problemClass = problemTool.problemWhichSolves();
		
		Collections.sort(methodsStatistics,
				new ComparatorQualityOfBestIndividual(problemClass));
		MethodStatistic methodWithBestIdividual =
				methodsStatistics.get(methodsStatistics.size() -1);
		
		return methodWithBestIdividual;
	}
	
	/**
	 * Exports the best {@link IndividualWrapper}
	 * @return
	 */
	public IndividualWrapper exportTheBestIndividualWrp() {
		
		MethodStatistic bestMethod =
				exportMethodAchievedTheBestOfBestIndividuals();
		AgentDescription agentDescription =
				bestMethod.getAgentDescription();
		IndividualEvaluated bestIndividual =
				bestMethod.getMethodStatisticResult().getBestIndividual();
		
		return new IndividualWrapper(getJobID(), agentDescription,
				bestIndividual);
	}
}
