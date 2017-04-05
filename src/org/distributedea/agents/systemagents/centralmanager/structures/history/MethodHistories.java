package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodhistory.ComparatorLexicographical;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;

/**
 * Structure represents {@link List} of {@link MethodHistory}
 * @author stepan
 *
 */
public class MethodHistories {

	private final List<MethodHistory> methods;
	
	/**
	 * Constructor
	 */
	MethodHistories() {
		this.methods = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 */
	MethodHistories(List<MethodHistory> methods) {
		if (methods == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + "is not null");
		}
		this.methods = new ArrayList<>();
		for (MethodHistory mehodHistoryI : methods) {
			this.add(mehodHistoryI);
		}
	}
	
	
	private List<MethodHistory> exportMethodsOfAgent(Class<?> agentClass) {

		List<MethodHistory> selected = new ArrayList<>();
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstaneI =
					methodI.getMethodInstanceDescription();
						
			if (methodInstaneI.exportAgentClass() == agentClass) {
				selected.add(methodI);
			}
		}
		
		return selected;
	}

	private List<MethodHistory> exportMethodsOfAgent(List<Class<?>> agentClasses) {

		if (agentClasses == null) {
			throw new IllegalArgumentException();
		}
		
		List<MethodHistory> selected = new ArrayList<>();
		
		for (Class<?> agentClassI : agentClasses) {
			
			List<MethodHistory> methodsI = exportMethodsOfAgent(agentClassI);
			selected.addAll(methodsI);
		}

		return selected;
	}
	
	public MethodHistories exportMethodHistoriesOfAgent(List<Class<?>> agentClasses) {
		
		List<MethodHistory> methodsSelected =
				exportMethodsOfAgent(agentClasses);
		
		return new MethodHistories(methodsSelected);
	}
	
	/**
	 * Returns {@link List} of {@link MethodHistory}s
	 * @return
	 */
	public List<MethodHistory> getMethods() {
		return this.methods;
	}
	
	
	public int getNumberOfMethodInstances() {
		if (methods == null) {
			return 0;
		}
		return methods.size();
	}

	public boolean isEmpty() {
		return methods.isEmpty();
	}
	
	/**
	 * Adds {@link MethodHistory} into the structure
	 * @param methodHistory
	 */
	public void add(MethodHistory methodHistory) {
		if (methodHistory == null || ! methodHistory.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodHistory.class.getSimpleName() + " is not valid");
		}
		this.methods.add(methodHistory);
	}
	
	public int maxNumberOfAvailableInstance(MethodType instance) {
		if (instance == null || ! instance.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + "is not valid");
		}
		
		int number = -1;
		for (MethodHistory methodHistoryI : methods) {
			
			MethodInstanceDescription instanceI =
					methodHistoryI.getMethodInstanceDescription();
			if (instanceI.exportAreTheSameType(instance)) {
				int instanceNumberI = instanceI.getInstanceNumber();
				if (instanceNumberI > number) {
					number = instanceNumberI;
				}
			}
		}
		
		return number;
	}

	public MethodHistory getMethodHistoryOfCurentlyRunning(MethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				MethodDescription.class.getSimpleName() + " is not valid");
		}
				
		for (MethodHistory methodHistoryI : methods) {
			
			MethodDescription agentI = methodHistoryI.getCurrentAgent();
			
			if (agentI == null) {
				continue;
			}
						
			if (agentI.equals(agentDescription)) {
				return methodHistoryI;
			}
		}
		return null;
	}
	
	public MethodHistory getFirstDeadMethodHistoryOfSameType(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is nt valid");
		}
		
		for (MethodHistory methodHistoryI : methods) {
			
			// skip if Method is not dead
			if (methodHistoryI.getCurrentAgent() != null) {
				continue;
			}
			
			MethodInstanceDescription methodInstDescI =
					methodHistoryI.getMethodInstanceDescription();
			
			if (methodInstDescI.exportAreTheSameType(methodType)) {
				return methodHistoryI;
			}
		}
		return null;
	}

	/**
	 * Returns {@link MethodHistory} of given {@link MethodDescription}
	 * @param agentDescription
	 * @return
	 */
	public MethodHistory getMethodHistoryOfRunningMethod(MethodDescription agentDescription) {
		if (agentDescription == null || ! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argumet " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		for (MethodHistory methodHistoryI : methods) {

			MethodDescription agentDescriptionI =
					methodHistoryI.getCurrentAgent();
			
			if (agentDescriptionI == null) {
				continue;
			
			} else if (agentDescriptionI.exportMethodName().equals(
					agentDescription.exportMethodName())) {
				return methodHistoryI;
			}
		}
		return null;
	}

	public Iteration getLastIterationOfMethods() {
		if (methods == null || methods.isEmpty()) {
			return null;
		}
		
		MethodHistory selectedMethodHistory = methods.get(0);
		for (MethodHistory methodHistoryI : methods) {
			
			MethodStatisticResultWrapper resultWrpI =
					methodHistoryI.exportLastStatistic();
			Iteration iterationI = resultWrpI.getIteration();
			
			MethodStatisticResultWrapper selectedResultWrp =
					selectedMethodHistory.exportLastStatistic();
			Iteration selectedIteration = selectedResultWrp.getIteration();
			
			if (iterationI.getIterationNumber() >
					selectedIteration.getIterationNumber()) {
				selectedMethodHistory = methodHistoryI;
			}
		}
		
		MethodStatisticResultWrapper lastResultWrp =
				selectedMethodHistory.exportLastStatistic();
		return lastResultWrp.getIteration();
	}

	/**
	 * Exports all {@link MethodHistory} of given {@link MethodType}
	 * @param methodType
	 * @return
	 */
	public List<MethodHistory> exportMethodsOfInstance(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalStateException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		
		List<MethodHistory> selected = new ArrayList<>();
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstaneI =
					methodI.getMethodInstanceDescription();
						
			if (methodInstaneI.exportAreTheSameType(methodType)) {
				selected.add(methodI);
			}
		}
		
		return selected;
	}

	/**
	 * Exports {@link MethodsStatistics} for given {@link Iteration}
	 * @param iteration
	 * @return
	 */
	public MethodsStatistics exportMethodsResults(Iteration iteration, JobID jobID) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		
		MethodsStatistics results = new MethodsStatistics(jobID, iteration);
		
		for (MethodHistory methodI : methods) {
			
			MethodStatisticResultWrapper resultWrpI =
					methodI.exportStatistic(iteration);
			
			if (resultWrpI == null) {
				continue;
			}
			
			MethodDescription agentDescriptionI =
					resultWrpI.getAgentDescription();
			MethodStatisticResult methodStatisticResultI =
					resultWrpI.getMethodStatisticResult();
			
			MethodStatistic methodStatisticI = new MethodStatistic(
					agentDescriptionI, methodStatisticResultI);
			
			results.addMethodStatistic(methodStatisticI);			
		}
		
		return results;
	}

	public MethodHistories exportHistoryOfRunningMethods(Iteration iteration) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		List<MethodHistory> currentlyRunningMethods = new ArrayList<>();
		for (MethodHistory methodHistoryI : methods) {
			
			MethodStatisticResultWrapper statisticI =
					methodHistoryI.exportStatistic(iteration);
			if (statisticI == null) {
				continue;
			}
			
			Iteration iterationI = statisticI.getIteration();
			
			if (iterationI.getIterationNumber() ==
					iteration.getIterationNumber()) {
				currentlyRunningMethods.add(methodHistoryI);
			}
		}
		
		return new MethodHistories(currentlyRunningMethods);
	}
	
	public MethodHistories exportHistoryOfRunningMethods(Iteration iteration, long minimumLengthOfHistory) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		MethodHistories currentlyRunningMethods =
				exportHistoryOfRunningMethods(iteration);
		
		List<MethodHistory> amethodsWithHisotry = new ArrayList<>();

		for (MethodHistory methodHistoryI :
			currentlyRunningMethods.getMethods()) {
			
			if (methodHistoryI.isRunningLastNIteration(iteration,
					minimumLengthOfHistory)) {
				amethodsWithHisotry.add(methodHistoryI);
			}
		}
		
		return new MethodHistories(amethodsWithHisotry);
	}

	public MethodHistories exportHistoryWithoutImprovement(Iteration iteration, long minimumLengthOfHistory) {

		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}

		MethodHistories currentlyRunningMethods =
				exportHistoryOfRunningMethods(iteration);
		
		List<MethodHistory> amethodsWithHisotry = new ArrayList<>();

		for (MethodHistory methodHistoryI :
			currentlyRunningMethods.getMethods()) {
			
			if (methodHistoryI.reachedNtimesZeroTheBestCreatedIndividuals(iteration,
					minimumLengthOfHistory)) {
				amethodsWithHisotry.add(methodHistoryI);
			}
		}
		
		return new MethodHistories(amethodsWithHisotry);

	}
	
	/**
	 * Exports currently running methods
	 * @return
	 */
	public MethodDescriptions exportRunningMethods() {
		
		List<MethodDescription> agentDescriptions = new ArrayList<>();
		for (MethodHistory methodHistoryI : methods) {
			MethodDescription currentAgentI = methodHistoryI.getCurrentAgent();
			if (currentAgentI != null) {
				agentDescriptions.add(currentAgentI);
			}
		}
		return new MethodDescriptions(agentDescriptions);
	}

	/**
	 * Exports {@link MethodDescription} of random running method
	 * @return
	 */
	public MethodDescription exportRandomRunningMethod() {
		
		return exportRunningMethods().exportRandomAgentDescription();
	}
	
	/**
	 * Exports {@link List} of {@link ResultOfMethodInstanceIteration}s
	 * @param iteration
	 * @return
	 */
	public List<ResultOfMethodInstanceIteration> exportResultOfIterations(
			Iteration iteration) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		List<ResultOfMethodInstanceIteration> results = new ArrayList<>();
		for (MethodHistory methodHistoryI : methods) {
			
			ResultOfMethodInstanceIteration statisticI =
					methodHistoryI.exportResultOfMethodInstanceIteration(iteration);
			if (statisticI != null) {
				results.add(statisticI);
			}
		}
		return results;
	}
	
	/**
	 * Export number of the last {@link Iteration} which was running
	 * @return
	 */
	public long exportNumberOfLastIteration() {
		
		long number = -1;
		
		for (MethodHistory mathodI : methods) {
			long numberI =
					mathodI.exportNumberOfLastIteration();
			if (numberI > number) {
				number = numberI;
			}
		}
		
		return number;
	}
	
	/**
	 * Exports number of {@link Iteration} of given {@link MethodType}
	 * @param methodType
	 * @return
	 */
	public long exportNumberOfIterationOf(MethodType methodType) {
		
		long number = 0;
		
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstanceI =
					methodI.getMethodInstanceDescription();
			MethodType methodTypeI =
					methodInstanceI.getMethodType();
			
			if (! methodTypeI.equals(methodType)) {
				continue;
			}
			
			number += methodI.exportNumberOfIteration();
		}
		
		return number;
	}

	public long exportNumberOfTheBestCreatedIndividuals(MethodType methodType) {
		
		long number = 0;
		
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstanceI =
					methodI.getMethodInstanceDescription();
			MethodType methodTypeI =
					methodInstanceI.getMethodType();
			
			if (! methodTypeI.equals(methodType)) {
				continue;
			}
			
			number += methodI.exportNumberOfTheBestCreatedIndividuals();
		}
		
		return number;
	}
	
	public long exportNumberOfTheBestCreatedIndividuals(MethodType methodType, int iterationNumber) {
		
		long number = 0;
		
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstanceI =
					methodI.getMethodInstanceDescription();
			MethodType methodTypeI =
					methodInstanceI.getMethodType();
			
			if (! methodTypeI.equals(methodType)) {
				continue;
			}
			
			number += methodI.exportNumberOfTheBestCreatedIndividuals(iterationNumber);
		}

		return number;
	}
	
	/**
	 * Exports {@link List} of {@link MethodType}s in structure 
	 * @return
	 */
	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (MethodHistory methodI : methods) {
			MethodInstanceDescription instanceI =
					methodI.getMethodInstanceDescription();
			MethodType methodTypeI = instanceI.getMethodType();
			
			if (! methodTypes.contains(methodTypeI)) {
				methodTypes.add(methodTypeI.deepClone());
			}
		}
		
		return methodTypes;
	}

	/**
	 * Exports methods which has never run
	 * @param inputDescriptions
	 * @return
	 */
	public Methods exportsMethodsWhichHaveNeverRun(
			Methods inputDescriptions) {
		if (inputDescriptions == null ||
				! inputDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Methods.class.getSimpleName() + " is not valid");
		}
		
		List<MethodType> availableMethodTypes = exportMethodTypes();
		
		List<InputMethodDescription> methodsWhichHaveNeverRun = new ArrayList<>();
		
		for (InputMethodDescription inputAgentDescriptionI :
				inputDescriptions.getInputMethodDescriptions()) {
			MethodType methodTypeI =
					inputAgentDescriptionI.exportMethodType();
			
			if (! availableMethodTypes.contains(methodTypeI)) {
				methodsWhichHaveNeverRun.add(inputAgentDescriptionI);
			}
		}
		
		return new Methods(methodsWhichHaveNeverRun);
	}
	
	private List<MethodTypeHistory> getMethodTypeHistories() {
		
		List<MethodTypeHistory> methodTypeHistories = new ArrayList<>();
		
		for (MethodType methodTypeI : exportMethodTypes()) {
			
			List<MethodHistory> methodHistoryList =
					exportMethodsOfInstance(methodTypeI);
			
			methodTypeHistories.add(new MethodTypeHistory(
					methodTypeI, methodHistoryList));
		}
		
		return methodTypeHistories;
	}

	/**
	 * Exports {@link MethodHistories} as {@link MethodTypeHistories}
	 * @return
	 */
	public MethodTypeHistories getMethodTypeHistory() {
		
		return new MethodTypeHistories(getMethodTypeHistories());
	}
	
	/**
	 * Sorts methods by {@link MethodInstanceDescription} name
	 */
	public void sortMethodInstancesByName() {
		
		Collections.sort(methods, new ComparatorLexicographical());
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		for (MethodHistory methodI : methods) {
			if (methodI == null || ! methodI.valid(new TrashLogger())) {
				return false;
			}
		}
		return true;
	}
}
