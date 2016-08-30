package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodhistory.ComparatorLexicographical;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;

/**
 * Data structure preserving the history of one run of the {@link Job}
 * @author stepan
 *
 */
public class History {

	private final JobID jobID;

	private final List<Plan> plans;
	private final List<RePlan> replans;
	
	private final List<MethodHistory> methods;
	
	/**
	 * Constructor
	 * @param jobID
	 */
	public History(JobID jobID) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		this.jobID = jobID;
		this.methods = new ArrayList<>();
		this.plans = new ArrayList<>();
		this.replans = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param jobID
	 * @param methods
	 * @param plans
	 * @param replans
	 */
	private History(JobID jobID, List<MethodHistory> methods,  List<Plan> plans, List<RePlan> replans) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		this.jobID = jobID;
		this.methods = methods;
		this.plans = plans;
		this.replans = replans;
	}
	
	/**
	 * Returns the @link{Job} specification 
	 * @return
	 */
	public JobID getJobID() {
		return jobID.deepClone();
	}

	public List<MethodHistory> getMethodInstances() {
		return methods;
	}
	public int getNumberOfMethodInstances() {
		if (methods == null) {
			return 0;
		}
		return methods.size();
	}

	public List<RePlan> getRePlans() {
		return replans;
	}
	
	/**
	 * Inserts the initialization result of Planner into the history,
	 * results contains methods which were created
	 * @param plan
	 */
	public void addNewPlan(Plan plan) {
		if (plan == null) {
			return;
		}
		
		this.plans.add(plan);
		
		for (AgentDescription agentDescriptionI :
				plan.getNewAgents().getAgentDescriptions()) {
			processAgentToCreate(agentDescriptionI);
		}
	}
	/**
	 * Inserts the replanning result of Planner into the history,
	 * results contains methods which were killed and created 
	 * @param replan
	 */
	public void addNewRePlan(RePlan replan) {
		if (replan == null || ! replan.valid(new TrashLogger())) {
			replan.valid(new TrashLogger());
			throw new IllegalArgumentException("Argument " +
					RePlan.class.getSimpleName() + " is not valid");
		}
		
		this.replans.add(replan.deepClone());
		
		AgentDescriptions agentsToKill = replan.getAgentsToKill();
		for (AgentDescription agentToKillI : agentsToKill.getAgentDescriptions()) {
			MethodHistory methodHistory =
					getMethodHistoryOfCurentlyRunning(agentToKillI);
			if (methodHistory != null) {
				methodHistory.setCurrentAgent(null);
			}
		}

		AgentDescriptions agentsToCreate = replan.getAgentsToCreate();
		for (AgentDescription agentToCreateI : agentsToCreate.getAgentDescriptions()) {
			 processAgentToCreate(agentToCreateI);
		}
		
	}
	
	private void processAgentToCreate(AgentDescription agentToCreateI) {
		
		MethodType methodType =
				agentToCreateI.exportMethodType();
		
		MethodHistory methodHistory = getFirstDeadMethodHistoryOfSameType(methodType);
		if (methodHistory != null) {
			methodHistory.setCurrentAgent(agentToCreateI.deepClone());
			return;
		}
		
		int max = maxNumberOfAvailableInstance(methodType);

		MethodInstanceDescription methodInstanceDescription = new MethodInstanceDescription(methodType, max +1);
				
		MethodHistory methodHistoryI = new MethodHistory(
				methodInstanceDescription, agentToCreateI.deepClone());
		
		this.methods.add(methodHistoryI);
	}
	
	private int maxNumberOfAvailableInstance(MethodType instance) {

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
	
	private MethodHistory getMethodHistoryOfCurentlyRunning(AgentDescription agentDescription) {
		
		String agentName = agentDescription.getAgentConfiguration().exportAgentname();
		
		for (MethodHistory methodHistoryI : methods) {
			
			AgentDescription agentI = methodHistoryI.getCurrentAgent();
			
			if (agentI == null) {
				continue;
			}
			
			String agentNameI = agentI.getAgentConfiguration().exportAgentname();
			
			if (agentNameI.equals(agentName)) {
				return methodHistoryI;
			}
		}
		return null;
	}
	
	private MethodHistory getFirstDeadMethodHistoryOfSameType(MethodType methodType) {
		
		for (MethodHistory methodHistoryI : methods) {
			
			// skip if Method is dead
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
	
	private MethodHistory getMethodHistoryOfRunningMethod(AgentDescription agentDescription) {

		for (MethodHistory methodHistoryI : methods) {

			AgentDescription agentDescriptionI =
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

	private RePlan getRePlan(Iteration iteration) {
		
		if (this.replans == null) {
			return null;
		}
		
		for (RePlan rePlanI : replans) {
			
			Iteration iterationI = rePlanI.getIteration();
			
			if (iterationI.getIterationNumber() ==
					iteration.getIterationNumber()) {
				return rePlanI;
			}
		}
		return null;
	}
	
	
	private Iteration getLastIterationOfMethods() {
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
	 * Adds to history Statistic of current Iteration number,
	 * for inserting Statistic is required to have added Plan and RePlan
	 * of the belonging iteration
	 * @param statistic
	 * @param iteration
	 */
	public void addStatictic(Statistic statistic, Iteration iteration) {

		if (statistic == null || ! statistic.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Statistic.class.getSimpleName() + " is not valid");
		}
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		if (iteration.getIterationNumber() == 1) {
			
			for (MethodStatistic methodStatI : statistic.getStatistics()) {
				addMethodStatistic(methodStatI, iteration);
			}
			
		} else {
		
			Iteration previousIteration = iteration.exportPreviousIteration();
			RePlan currentRePlan = getRePlan(previousIteration);
			if (currentRePlan == null) {
				throw new IllegalStateException();
			}
			
			for (MethodStatistic methodStatI : statistic.getStatistics()) {
			
				AgentDescription agentDescriptionI =
						methodStatI.getAgentDescription();
				
				boolean wasThisAgentKilledI = currentRePlan
						.containsAgentToKill(agentDescriptionI);
				boolean wasThisAgentCreatedI = currentRePlan
						.containsAgentToCreate(agentDescriptionI);
				if (wasThisAgentKilledI && ! wasThisAgentCreatedI) {
					continue;
				}
				
				addMethodStatistic(methodStatI, iteration);
			}
		}
		
	}
	
	private void addMethodStatistic(MethodStatistic methodStatistic, Iteration iteration) {

		if (methodStatistic == null || ! methodStatistic.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodStatistic.class.getSimpleName() + " is not valid");
		}
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}

		MethodStatisticResult statistic =
				methodStatistic.getMethodStatisticResult();
		AgentDescription agentDescription =
				methodStatistic.getAgentDescription();
		
		MethodHistory methodHistory =
				getMethodHistoryOfRunningMethod(agentDescription);
		if (methodHistory != null) {
			methodHistory.addStatistic(statistic, iteration);
		} else {
			getMethodHistoryOfRunningMethod(agentDescription);
			throw new IllegalStateException("Not avaliable last " +
					MethodHistory.class.getSimpleName() + " " +
					agentDescription.exportMethodName());
		}

	}
	
	public History exportHistoryOfAgents(List<Class<?>> agentClasses) {
		if (agentClasses == null) {
			throw new IllegalArgumentException();
		}

		List<MethodHistory> selectedMethods = exportMethodsOfAgent(agentClasses);
		
		return new History(jobID, selectedMethods, plans, replans);
	}
	private List<MethodHistory> exportMethodsOfAgent(List<Class<?>> agentClasses) {
		
		List<MethodHistory> selectedMethods = new ArrayList<>();
		for (Class<?> agentClassI : agentClasses) {
			
			List<MethodHistory> selectedMethodsI =
					exportMethodsOfAgent(agentClassI);
			selectedMethods.addAll(selectedMethodsI);
		}
		return selectedMethods;
	}
	private List<MethodHistory> exportMethodsOfAgent(Class<?> agentClass) {

		List<MethodHistory> selected = new ArrayList<>();
		for (MethodHistory methodI : methods) {
			
			MethodInstanceDescription methodInstaneI =
					methodI.getMethodInstanceDescription();
						
			if (methodInstaneI.exportAgentClass() != agentClass) {
				selected.add(methodI);
			}
		}
		
		return selected;
	}
	
	/**
	 * Exports History Of given type of MethodIstacess
	 * @param methodInstanes
	 * @return
	 */
	public History exportHistoryOfMethodInstances(List<MethodType> methodInstanes) {
		if (methodInstanes == null) {
			throw new IllegalArgumentException();
		}
		
		List<MethodHistory> selectedMethods = exportMethodsOfInstance(methodInstanes);
		
		return new History(jobID, selectedMethods, plans, replans);
	}
	private List<MethodHistory> exportMethodsOfInstance(List<MethodType> methodInstanes) {
		
		List<MethodHistory> selectedMethods = new ArrayList<>();
		for (MethodType methodTypeI : methodInstanes) {
			
			List<MethodHistory> selectedMethodsI =
					exportMethodsOfInstance(methodTypeI);
			selectedMethods.addAll(selectedMethodsI);
		}
		return selectedMethods;
	}
	private List<MethodHistory> exportMethodsOfInstance(MethodType methodType) {

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
	 * Returns Methods Type, one of Given, which didn't run for the longest time
	 * @param methodInstancesToSelect
	 * @return
	 */
	public MethodType methodsWhichDidntRunForTheLongestTime(
			List<MethodType> methodInstancesToSelect) {
	
		if (methodInstancesToSelect == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		if (methodInstancesToSelect.isEmpty()) {
			return null;
		}

		MethodType methodDescription = methodInstancesToSelect.get(0);
		History historySelected =
				exportHistoryOfMethodInstances(Arrays.asList(methodDescription));
		Iteration iteration = historySelected.getLastIterationOfMethods();
		// first methodInstance has never been running
		if (iteration == null) {
			return methodDescription;
		}
		
		for (MethodType methodDescriptionI : methodInstancesToSelect) {

			History historySelectedI =
					exportHistoryOfMethodInstances(Arrays.asList(methodDescriptionI));
			Iteration iterationI =
					historySelectedI.getLastIterationOfMethods();
			// methodInstance has never been running
			if (iterationI == null) {
				return methodDescriptionI.exportClone();
			}
			
			if (iterationI.getIterationNumber() <
				iteration.getIterationNumber()) {
				methodDescription = methodDescriptionI;
				iteration = iterationI;
			}

		}
		
		return methodDescription.exportClone();
	}
	
	/**
	 * Returns Methods Type, one of Given, which didn't run for the longest time
	 * @param methodInstancesToSelect
	 * @return
	 */
	public Class<?> agentClassWhichDidntRunForTheLongestTime(List<Class<?>> agentClasses) {

		if (agentClasses == null) {
			throw new IllegalArgumentException();
		}
		if (agentClasses.isEmpty()) {
			return null;
		}
		
		Class<?> agentClass = agentClasses.get(0);
		
		List<Class<?>> agentClassList = new ArrayList<>();
		agentClassList.add(agentClass);
		
		History historySelected =
				exportHistoryOfAgents(agentClassList);
		Iteration iteration =
				historySelected.getLastIterationOfMethods();
		// first agentClass has never been running
		if (iteration == null) {
			return agentClass;
		}
		
		for (Class<?> agentClassI : agentClasses) {
			
			List<Class<?>> agentClassListI = new ArrayList<>();
			agentClassListI.add(agentClassI);
			
			History historySelectedI =
					exportHistoryOfAgents(agentClassListI);
			Iteration iterationI =
					historySelectedI.getLastIterationOfMethods();
			// agentClass has never been running
			if (iterationI == null) {
				return agentClassI;
			}

			if (iterationI.getIterationNumber() <
					iteration.getIterationNumber()) {
				agentClass = agentClassI;
				iteration = iterationI;
			}
			
		}
		return agentClass;
	}
	public boolean containsMethodInstanceType(
			MethodType methodType) {
		
		MethodType methodTypeTheLongestTime =
				methodsWhichDidntRunForTheLongestTime(Arrays.asList(methodType));
		if (methodTypeTheLongestTime == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Exports {@link MethodsStatistics} for given {@link Iteration}
	 * @param iteration
	 * @return
	 */
	public MethodsStatistics exportMethodsResults(Iteration iteration) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		MethodsStatistics results = new MethodsStatistics(jobID, iteration);
		
		for (MethodHistory methodI : methods) {
			
			MethodStatisticResultWrapper resultWrpI =
					methodI.exportStatistic(iteration);
			
			if (resultWrpI == null) {
				continue;
			}
			
			AgentDescription agentDescriptionI =
					resultWrpI.getAgentDescription();
			MethodStatisticResult methodStatisticResultI =
					resultWrpI.getMethodStatisticResult();
			
			MethodStatistic methodStatisticI = new MethodStatistic(
					agentDescriptionI, methodStatisticResultI);
			
			results.addMethodStatistic(methodStatisticI);			
		}
		
		return results;
	}
	
	/**
	 * Exports {@link History} of method instances which were running in given {@link Iteration}
	 * @param iteration
	 * @return
	 */
	public History exportHistoryOfRunningMethods(Iteration iteration) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		List<MethodHistory> currentlyRunningMethods = new ArrayList<>();
		for (MethodHistory methodHistoryI : getMethodInstances()) {
			
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
		
		return new History(getJobID(), currentlyRunningMethods, plans, replans);
	}

	/**
	 * Exports {@link History} of currently running methods with minimal history duration
	 * @param iteration
	 * @param minimumLengthOfHistory
	 * @return
	 */
	public History exportHistoryOfRunningMethods(Iteration iteration, long minimumLengthOfHistory) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		History historyOfCurrentlyRunningMethods =
				exportHistoryOfRunningMethods(iteration);
		
		List<MethodHistory> amethodsWithHisotry = new ArrayList<>();
		for (MethodHistory methodHistoryI :
			historyOfCurrentlyRunningMethods.getMethodInstances()) {
			
			if (methodHistoryI.isRunningLastNIteration(iteration,
					minimumLengthOfHistory)) {
				amethodsWithHisotry.add(methodHistoryI);
			}
		}
		
		return new History(getJobID(), amethodsWithHisotry, plans, replans);
	}
	
	/**
	 * Exports currently running methods
	 * @return
	 */
	public AgentDescriptions exportRunningMethods() {
		
		List<AgentDescription> agentDescriptions = new ArrayList<>();
		for (MethodHistory methodHistoryI : methods) {
			AgentDescription currentAgentI = methodHistoryI.getCurrentAgent();
			if (currentAgentI != null) {
				agentDescriptions.add(currentAgentI);
			}
		}
		return new AgentDescriptions(agentDescriptions);
	}
	
	private Plan exportPlan(Iteration iteration) {
		
		for (Plan planI : plans) {
			Iteration iterationI = planI.getIteration();
			if (iterationI.equals(iteration)) {
				return planI;
			}
		}
		return null;
	}
	private RePlan exportRePlan(Iteration iteration) {
		
		for (RePlan rePlanI : replans) {
			Iteration iterationI = rePlanI.getIteration();
			if (iterationI.equals(iteration)) {
				return rePlanI;
			}
		}
		return null;
	}
	
	/**
	 * Exports results of given {@link Iteration}
	 * @param iteration
	 * @return
	 */
	public ResultOfIteration exportResultOfIteration(Iteration iteration, IAgentLogger logger) {
		
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
		
		Plan plan = exportPlan(iteration);
		RePlan rePlan = exportRePlan(iteration);
		
		ResultOfIteration resultOfIteration = new ResultOfIteration();
		resultOfIteration.setJobID(jobID.deepClone());
		resultOfIteration.setIteration(iteration.deepClone());
		resultOfIteration.setMethodInstanceIterations(results);
		resultOfIteration.setPlan(plan);
		resultOfIteration.setRePlan(rePlan);
		
		if (resultOfIteration.valid(logger)) {
			return resultOfIteration;
		}
		
		return null;
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

	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (MethodHistory methodI : methods) {
			MethodInstanceDescription instanceI =
					methodI.getMethodInstanceDescription();
			MethodType methodTypeI = instanceI.getMethodType();
			
			if (! methodTypes.contains(methodTypeI)) {
				methodTypes.add(methodTypeI.exportClone());
			}
		}
		
		return methodTypes;
	}
	
	/**
	 * Sorts methods by {@link MethodInstanceDescription} name
	 */
	public void sortMethodInstancesByName() {
		
		Collections.sort(methods, new ComparatorLexicographical());
	}
	
	/**
	 * Exports {@link History} to XML
	 * @param dir
	 * @throws IOException
	 */
	public void exportToXML(File dir) throws IOException {
		 
		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		jobID.exportXML(dir);
		
		// export history of methods
		for ( MethodHistory methodI : methods) {
			 methodI.exportXML(dir);
		}

		// export list of plans
		String planDirName = dir.getAbsolutePath() + File.separator + "plan";
		
		File planDir = new File(planDirName);
		if (! planDir.exists()) {
			planDir.mkdir();
		}
		
		for (Plan planI : plans) {
			planI.exportXML(new File(planDirName));
		}
		
		// export list of replans
		String replanDirName = dir.getAbsolutePath() + File.separator + "replan";
		
		File replanDir = new File(replanDirName);
		if (! replanDir.exists()) {
			replanDir.mkdir();
		}
		
		for (RePlan rePlanI : replans) {			
			rePlanI.exportXML(new File(replanDirName));
		}
	}
	
	/**
	 * Imports {@link History} from XML
	 * @param jobID
	 * @return
	 * @throws IOException
	 */
	public static History importXML(File dir) throws IOException {
		
		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}

		String jobIDFileName = dir.getAbsolutePath() + File.separator + "jobID.txt";
		JobID jobID = JobID.importXML(new File(jobIDFileName));
		
		
		String planDirName = dir.getAbsolutePath() + File.separator + "plan";
		File planDir = new File(planDirName);
		
		List<Plan> importedPlans = new ArrayList<>();
		if (planDir.exists() && planDir.isDirectory()) {
			for (File file : planDir.listFiles()) {
				
			    if (! file.isFile()) {
			    	continue;
			    }
		        
		    	Plan planI = Plan.importXML(file);
		    	importedPlans.add(planI);
			}
		}
		
		String replanDirName = dir.getAbsolutePath() + File.separator + "replan";
		File replanDir = new File(replanDirName);
		
		List<RePlan> importedRePlans = new ArrayList<>();
		if (replanDir.exists() && replanDir.isDirectory()) {
			for (File file : replanDir.listFiles()) {
				
			    if (! file.isFile()) {
			    	continue;
			    }
		        
		    	RePlan replanI = RePlan.importXML(file);
		    	importedRePlans.add(replanI);
			}
		}
		
		List<MethodHistory> importedMethods = new ArrayList<>();
		for (File fileI : dir.listFiles()) {
		    if (fileI.isDirectory() && fileI.getName().startsWith("Agent")) {
		    	
		    	MethodHistory methodI = MethodHistory.importXML(fileI);
		    	importedMethods.add(methodI);
		    }
		}
		
		return new History(jobID, importedMethods, importedPlans, importedRePlans);
	}
}
