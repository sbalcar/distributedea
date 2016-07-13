package org.distributedea.agents.systemagents.centralmanager.planner.history;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methodinstancedescription.MethodsResults;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;

public class History {

	private final JobID jobID;
	
	private final List<Plan> plans;
	private final List<RePlan> replans;
	
	private final List<MethodHistory> methods;
	
	public History(JobID jobID) {
		this.jobID = jobID;
		this.methods = new ArrayList<>();
		this.plans = new ArrayList<>();
		this.replans = new ArrayList<>();
	}
	
	private History(JobID jobID, List<MethodHistory> methods,  List<Plan> plans, List<RePlan> replans) {
		this.jobID = jobID;
		this.methods = methods;
		this.plans = plans;
		this.replans = replans;
	}
	
	public JobID getJobID() {
		return jobID;
	}

	public List<MethodHistory> getMethods() {
		return methods;
	}

	public List<RePlan> getRePlans() {
		return replans;
	}
	
	public void addNewPlan(Plan plan) {
		if (plan == null) {
			return;
		}
		
		this.plans.add(plan);
		
		for (AgentDescription agentDescriptionI : plan.getNewAgents()) {
			processAgentToCreate(agentDescriptionI);
		}
	}
	public void addNewRePlan(RePlan replan) {
		if (replan == null) {
			return;
		}
		
		this.replans.add(replan);
		
		if (replan.getAgentsToKill() != null) {
			for (AgentDescription agentToKillI : replan.getAgentsToKill()) {
				MethodHistory methodHistory = getMethodHistoryOfCurentlyRunning(agentToKillI);
				if (methodHistory != null) {
					methodHistory.setCurrentAgent(null);
				}
			}
		}

		for (AgentDescription agentToCreateI : replan.getAgentsToCreate()) {
			 processAgentToCreate(agentToCreateI);
		}
		
	}
	
	private void processAgentToCreate(AgentDescription agentToCreateI) {
		
		MethodInstanceDescription methodInstanceDescrittion =
				agentToCreateI.exportMethodInstanceDescription(-1);
		
		MethodHistory methodHistory = getFirstDeadMethodHistoryOfSameType(methodInstanceDescrittion);
		if (methodHistory != null) {
			methodHistory.setCurrentAgent(agentToCreateI);
			return;
		}
		
		int max = maxNumberOfAvailableInstance(methodInstanceDescrittion);

		methodInstanceDescrittion.setInstanceNumber(max +1);
		
		MethodHistory methodHistoryI = new MethodHistory();
		methodHistoryI.setMethodInstanceDescription(methodInstanceDescrittion);
		methodHistoryI.setCurrentAgent(agentToCreateI);
		
		this.methods.add(methodHistoryI);
	}
	
	public int maxNumberOfAvailableInstance(MethodInstanceDescription instance) {

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
			
			if (agentI != null) {
				
				String agentNameI = agentI.getAgentConfiguration().exportAgentname();
				
				if (agentNameI.equals(agentName)) {
					return methodHistoryI;
				}
			}
		}
		return null;
	}
	
	private MethodHistory getFirstDeadMethodHistoryOfSameType(MethodInstanceDescription methodInstanceDescription) {
		
		for (MethodHistory methodHistoryI : methods) {
			
			if (methodHistoryI.getCurrentAgent() != null) {
				continue;
			}
			
			MethodInstanceDescription methodInstDescI =
					methodHistoryI.getMethodInstanceDescription();
			
			if (methodInstDescI.exportAreTheSameType(methodInstanceDescription)) {
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
			
			} else if (agentDescriptionI.exportAgentName().equals(
					agentDescription.exportAgentName())) {
				return methodHistoryI;
			}
		}
		return null;
	}

	private RePlan getRePlan(Iteration iteration) {
		if (this.replans == null || this.replans.isEmpty()) {
			return null;
		}
		
		return this.replans.get(this.replans.size() -1);
	}
	
	public void addStatictic(Statistic statistic, Iteration iteration) {

		RePlan currentRePlan = getRePlan(iteration);
		
		for (MethodStatistic methodStatisticI : statistic.getStatistics()) {
		
			MethodStatisticResult statisticI =
					methodStatisticI.getMethodStatisticResult();
			AgentDescription agentDescriptionI =
					methodStatisticI.getAgentDescription();
			
			if (currentRePlan != null) {
				boolean wasThisAgentKilledI = currentRePlan
						.containsAgentToKill(agentDescriptionI);
				if (wasThisAgentKilledI) {
					continue;
				}
			}
			
			MethodHistory methodHistoryI =
					getMethodHistoryOfRunningMethod(agentDescriptionI);
			if (methodHistoryI != null) {
				methodHistoryI.addStatistic(statisticI, iteration);
			}
		}
	}
	
	public MethodsResults exportMethodsResults(Iteration iteration) {
				
		List<MethodStatistic> methodStatistic = new ArrayList<>();
		for (MethodHistory methodI : methods) {
			
			MethodStatisticResultWrapper resultWrpI = methodI.exportStatistic(iteration);
			
			if (resultWrpI == null) {
				continue;
			}
			
			MethodStatisticResult methodStatisticResultI = resultWrpI.getMethodStatisticResult();
			AgentDescription agentDescriptionI = resultWrpI.getAgentDescription();
			
			MethodStatistic methodStatisticI = new MethodStatistic();
			methodStatisticI.setMethodStatisticResult(methodStatisticResultI);
			methodStatisticI.setAgentDescription(agentDescriptionI);
			
			methodStatistic.add(methodStatisticI);
			
		}
		
		MethodsResults result = new MethodsResults();
		result.setJobID(jobID);
		result.setIteration(iteration);
		result.setMethodStatistic(methodStatistic);
		
		return result;
	}
	
	public History exportHistoryOfRunningMethods(Iteration iteration) {
		
		List<MethodHistory> currentlyRunningMethods = new ArrayList<>();
		for (MethodHistory methodHistoryI : getMethods()) {
			
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

	public History exportHistoryOfRunningMethods(Iteration iteration, long minimumLengthOfHistory) {
		
		History historyOfCurrentlyRunningMethods =
				exportHistoryOfRunningMethods(iteration);
		
		List<MethodHistory> amethodsWithHisotry = new ArrayList<>();
		for (MethodHistory methodHistoryI :
			historyOfCurrentlyRunningMethods.getMethods()) {
			
			if (methodHistoryI.getStatistics().size() >=
					minimumLengthOfHistory) {
				amethodsWithHisotry.add(methodHistoryI);
			}
		}
		
		return new History(getJobID(), amethodsWithHisotry, plans, replans);
	}
	
	public ResultOfIteration exportResultOfIteration(Iteration iteration) {
		
		List<ResultOfMethodInstanceIteration> results = new ArrayList<>();
		for (MethodHistory methodHistoryI : methods) {
			
			ResultOfMethodInstanceIteration statisticI =
					methodHistoryI.exportResultOfMethodInstanceIteration(iteration);
			if (statisticI != null) {
				results.add(statisticI);
			}
		}
		
		return new ResultOfIteration(jobID, iteration, results);
	}
	
	public void exportToXML(File dir) throws FileNotFoundException, JAXBException {
		 
		if (dir == null) {
			return;
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
	
	public static History importXML(JobID jobID) throws FileNotFoundException {
		
		String monitoringDirName = Configuration.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDir = new File(monitoringDirName);
		
		if ((! monitoringDir.exists()) || (! monitoringDir.isDirectory())) {
			return null;
		}
	
		String planDirName = monitoringDir.getAbsolutePath() + File.separator + "plan";
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
		
		String replanDirName = monitoringDir.getAbsolutePath() + File.separator + "replan";
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
		for (File fileI : monitoringDir.listFiles()) {
		    if (fileI.isDirectory() && fileI.getName().startsWith("Agent")) {
		    	
		    	MethodHistory methodI = MethodHistory.importXML(fileI);
		    	importedMethods.add(methodI);
		    }
		}
				
		return new History(jobID, importedMethods, importedPlans, importedRePlans);
	}
}
