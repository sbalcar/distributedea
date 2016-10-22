package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
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
	
	private final MethodHistories methods;
	
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
		this.methods = new MethodHistories();
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
	private History(JobID jobID, MethodHistories methods, List<Plan> plans,
			List<RePlan> replans) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (methods == null || ! methods.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodHistories.class.getSimpleName() + " is not valid");
		}
		if (plans == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		if (replans == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
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

	/**
	 * Returns the {@link MethodHistories}
	 * @return
	 */
	public MethodHistories getMethodHistories() {
		return methods;
	}

	/**
	 * Returns the {@link List} of {@link RePlan}s
	 * @return
	 */
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
	 * Inserts the re-planning result of Planner into the history,
	 * results contains methods which were killed and created 
	 * @param replan
	 */
	public void addNewRePlan(RePlan replan) {
		if (replan == null || ! replan.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					RePlan.class.getSimpleName() + " is not valid");
		}
		
		this.replans.add(replan.deepClone());
		
		AgentDescriptions agentsToKill = replan.getAgentsToKill();
		for (AgentDescription agentToKillI : agentsToKill.getAgentDescriptions()) {
			MethodHistory methodHistory =
					methods.getMethodHistoryOfCurentlyRunning(agentToKillI);
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
		
		MethodHistory methodHistory = methods.getFirstDeadMethodHistoryOfSameType(methodType);
		if (methodHistory != null) {
			methodHistory.setCurrentAgent(agentToCreateI.deepClone());
			return;
		}
		
		int max = methods.maxNumberOfAvailableInstance(methodType);

		MethodInstanceDescription methodInstanceDescription =
				new MethodInstanceDescription(methodType, max +1);
				
		MethodHistory methodHistoryI = new MethodHistory(
				methodInstanceDescription, agentToCreateI.deepClone());
		
		this.methods.add(methodHistoryI);
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
	
	
	/**
	 * Adds to history {@link Statistic} of current {@link Iteration} number,
	 * for inserting Statistic is required to have added {@link Plan}
	 * and {@link RePlan} of the belonging {@link Iteration}
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
		
		MethodHistory methodHistory = methods
				.getMethodHistoryOfRunningMethod(agentDescription);
		if (methodHistory != null) {
			methodHistory.addStatistic(statistic, iteration);
		} else {
			methods.getMethodHistoryOfRunningMethod(agentDescription);
			throw new IllegalStateException("Not avaliable last " +
					MethodHistory.class.getSimpleName() + " " +
					agentDescription.exportMethodName());
		}

	}
	
	/**
	 * Exports {@link InputAgentDescriptions} from {@link JobRun} which
	 * have never run
	 * @param jobRun
	 * @return
	 */
	public InputAgentDescriptions exportsMethodsWhichHaveNeverRun(JobRun jobRun) {
		
		InputAgentDescriptions agentDescriptions =
				jobRun.exportInputAgentDescriptions();
		
		return getMethodHistories().exportsMethodsWhichHaveNeverRun(
				agentDescriptions);
	}
	
	/**
	 * Exports {@link MethodsStatistics} for given {@link Iteration}
	 * @param agentClasses
	 * @param iteration
	 * @return
	 */
	public MethodsStatistics exportMethodsStatisticsOfAgent(List<Class<?>> agentClasses,
			Iteration iteration) {
		if (agentClasses == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		MethodHistories selectedMethods =
				methods.exportMethodHistoriesOfAgent(agentClasses);
		
		return selectedMethods.exportMethodsResults(iteration, jobID);
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
		
		List<MethodType> methodInstancesToSelectCopy =
				new ArrayList<MethodType>(methodInstancesToSelect);
		methodInstancesToSelectCopy.removeAll(methods.exportMethodTypes());
	
		
		MethodTypeHistories methodTypeHistories =
				methods.getMethodTypeHistory();
		
		MethodTypeHistories methodTypeHistoriesSelected =
				methodTypeHistories.exportMethodTypeHistoriesOf(methodInstancesToSelect);
		methodTypeHistoriesSelected.addMethodWhichHaveNeverRun(methodInstancesToSelectCopy);
		
		
		return methodTypeHistoriesSelected.methodTypeWhichDidntRunForTheLongestTime();
	}
	
	/**
	 * Returns {@link MethodType}, one of given Agent Class, which didn't run for the longest time
	 * @param methodInstancesToSelect
	 * @return
	 */
	public MethodType agentClassWhichDidntRunForTheLongestTime(List<Class<?>> agentClasses) {

		if (agentClasses == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can't be null");
		}
		if (agentClasses.isEmpty()) {
			return null;
		}
		
		MethodHistories methodHistories =
				methods.exportMethodHistoriesOfAgent(agentClasses);
		
		MethodTypeHistories methodTypeHistories =
				methodHistories.getMethodTypeHistory();

		return methodTypeHistories.methodTypeWhichDidntRunForTheLongestTime();
	}
	
	/**
	 * Exports {@link MethodsStatistics} for given {@link Iteration}
	 * @param iteration
	 * @return
	 */
	public MethodsStatistics exportMethodsResults(Iteration iteration) {
		
		return methods.exportMethodsResults(iteration, jobID);
	}
	
	/**
	 * Exports currently running methods
	 * @return
	 */
	public AgentDescriptions exportRunningMethods() {
		
		return methods.exportRunningMethods();
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
	
	public boolean wereLastKRePlanEmpty(Iteration iteration, int k) {
		
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalStateException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		
		Iteration iterationI = iteration;
		
		for (int i = 0; i < k; i++) {
			
			iterationI = iterationI.exportPreviousIteration();
			
			RePlan e = exportRePlan(iterationI);
			if (e == null || ! e.isEmpty()) {
				return false;
			}
		}
		
		return true;
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
		
		List<ResultOfMethodInstanceIteration> results =
				methods.exportResultOfIterations(iteration);
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
		for ( MethodHistory methodI : methods.getMethods()) {
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
		
		return new History(jobID, new MethodHistories(importedMethods),
				importedPlans, importedRePlans);
	}


}
