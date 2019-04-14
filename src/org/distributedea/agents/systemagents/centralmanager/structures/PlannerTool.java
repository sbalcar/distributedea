package org.distributedea.agents.systemagents.centralmanager.structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.methoddesriptionsplanned.PlannedMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerTool {
	
	/**
	 * Creates and runs given given {@link Agent_ComputingAgent}s in plan
	 * @param centralManager
	 * @param jobRun
	 * @param plan
	 * @param logger
	 * @return
	 */
	public static Plan createAndRunAgents(Agent_CentralManager centralManager,
			JobRun jobRun, InputPlan plan,IslandModelConfiguration configuration,
			IAgentLogger logger) {
		
		Plan createdAgents =
				createAgents(centralManager, plan, jobRun.getProblem(), logger);
		runAgents(centralManager, createdAgents.getNewAgents(), jobRun,
				configuration, logger);
		
		return createdAgents;
	}

	public static Plan createAgents(Agent_CentralManager centralManager,
			InputPlan inputPlan, IProblem problem, IAgentLogger logger) {
		
		List<MethodDescription> createdAgents = new ArrayList<>();
		//create computing agents
		for (int cpuIndex = 0; cpuIndex < inputPlan.getSchedule().size(); cpuIndex++) {	
		
			Pair<AID, PlannedMethodDescription> pairI = inputPlan.getSchedule().get(cpuIndex);
			
			AID managerAgentOfEmptyCoreAIDI = pairI.first;
			
			PlannedMethodDescription plannedMethodDescrI = pairI.second;
			ProblemToolDefinition problemToolDef = plannedMethodDescrI.getProblemToolDefinition();
			InputAgentConfiguration inputAgentConfI = plannedMethodDescrI.getInputAgentConfiguration();
			MethodIDs methodIDsI = plannedMethodDescrI.getMethodIDs();
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, inputAgentConfI, methodIDsI, logger);
			MethodIDs methodIDs = plannedMethodDescrI.getMethodIDs();
			
			MethodDescription createdAgentDescriptionI =
					new MethodDescription(createdAgentI, methodIDs, problem, problemToolDef);
						
			createdAgents.add(createdAgentDescriptionI);
		}
		
		return new Plan(inputPlan.getIteration(), new MethodDescriptions(createdAgents));
	}
	
	private static void runAgents(Agent_CentralManager centralManager,
			MethodDescriptions createdAgents,  JobRun jobRun,
			IslandModelConfiguration configuration, IAgentLogger logger) {
		
		for (MethodDescription agentDescriptionI :
				createdAgents.getAgentDescriptions()) {

			AgentConfiguration agentConfigurationI =
					agentDescriptionI.getAgentConfiguration();

			ProblemToolDefinition problemToolI =
					agentDescriptionI.getProblemToolDefinition();
			
			ProblemWrapper problemStructI =
					jobRun.exportProblemWrapper(problemToolI);
					
			boolean startOK = ComputingAgentService.sendStartComputing(centralManager,
					agentConfigurationI.exportAgentAID(), problemStructI, configuration, logger);
			if (! startOK) {
				centralManager.exit();
			}
		}
		
	}
	
	
	/**
	 * Process Replan
	 * @param centralManager
	 * @param replan
	 * @param jobRun
	 * @param logger
	 * @throws PlannerException
	 */
	public static RePlan processReplanning(Agent_CentralManager centralManager,
			InputRePlan replan, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		List<MethodDescription> agentsToKill =
				replan.getAgentsToKill().getAgentDescriptions();
		List<InputMethodDescription> agentsToCreate =
				replan.getAgentsToCreate().getInputMethodDescriptions();
		
		List<MethodDescription> agentsCreated = new ArrayList<>();
		
		for (int i = 0; i < agentsToKill.size(); i++) {
			
			MethodDescription agentToKillI = agentsToKill.get(i);
			
			InputMethodDescription agentToCreateI = agentsToCreate.get(i);
			
			InputAgentConfiguration newConfiguration =
					agentToCreateI.getInputAgentConfiguration();
			
			ProblemToolDefinition problemToolDefI =
					agentToCreateI.getProblemToolDefinition();
			ProblemWrapper problemWrpI =
					jobRun.exportProblemWrapper(problemToolDefI);
			
			
			AgentConfiguration createdAC = killAndCreateAgent(centralManager,
					agentToKillI, newConfiguration, problemWrpI, configuration, logger);
			
			MethodDescription createdAD = new MethodDescription(createdAC, agentToKillI.getMethodIDs(),
					jobRun.getProblem(), problemToolDefI);
			
			agentsCreated.add(createdAD);
		}
		
		return new RePlan(replan.getIteration().deepClone(),
				new MethodDescriptions(agentsToKill),
				new MethodDescriptions(agentsCreated));		
	}
	
	/**
	 * Kills Agent and creates the new Agent
	 * @param centralManager
	 * @param agentTokillAID
	 * @param bestConfiguration
	 * @param problemStruct
	 * @param logger
	 * @throws PlannerException
	 */
	private static AgentConfiguration killAndCreateAgent(Agent_CentralManager centralManager,
			MethodDescription agentToKillI , InputAgentConfiguration newConfiguration,
			ProblemWrapper problemStruct, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {

		AID agentTokillAID = agentToKillI.exportAgentAID();
		MethodIDs methodIDs = agentToKillI.getMethodIDs();
		
		// kill agent
		ManagerAgentService.sendKillAgent(centralManager, agentTokillAID, logger);

		logger.log(Level.INFO, "Killed: " + agentTokillAID.getLocalName());
		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, agentTokillAID);
		
		
		// create new agent
		AgentConfiguration newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, newConfiguration, methodIDs, logger);
		
		logger.log(Level.INFO, "Created: " + newAgent.exportAgentname());
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent.exportAgentAID(), problemStruct, configuration, logger);
		return newAgent;
	}
}
