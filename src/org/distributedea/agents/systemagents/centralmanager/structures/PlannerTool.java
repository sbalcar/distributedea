package org.distributedea.agents.systemagents.centralmanager.structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerTool {
	
	/**
	 * Creates and runs given given {@link Agent_ComputingAgent}s in plan
	 * @param centralManager
	 * @param job
	 * @param plan
	 * @param logger
	 * @return
	 */
	public static Plan createAndRunAgents(Agent_CentralManager centralManager,
			JobRun job, InputPlan plan, IAgentLogger logger) {
		
		Plan createdAgents =
				createAgents(centralManager, plan, logger);
		runAgents(centralManager, createdAgents.getNewAgents(), job, logger);
		
		return createdAgents;
	}

	public static Plan createAgents(Agent_CentralManager centralManager,
			InputPlan inputPlan, IAgentLogger logger) {
		
		List<AgentDescription> createdAgents = new ArrayList<>();
		//create computing agents
		for (int cpuIndex = 0; cpuIndex < inputPlan.getSchedule().size(); cpuIndex++) {	
		
			Pair<AID,InputAgentDescription> pairI = inputPlan.getSchedule().get(cpuIndex);
			
			AID managerAgentOfEmptyCoreAIDI = pairI.first;
			
			InputAgentDescription agentDescriptionI = pairI.second;
			Class<?> problemToolClass = agentDescriptionI.exportProblemToolClass();
			InputAgentConfiguration agentConfigurationI = agentDescriptionI.getInputAgentConfiguration();
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, agentConfigurationI, logger);
			
			AgentDescription createdAgentDescriptionI =
					new AgentDescription(createdAgentI, problemToolClass);
						
			createdAgents.add(createdAgentDescriptionI);
		}
		
		return new Plan(inputPlan.getIteration(), new AgentDescriptions(createdAgents));
	}
	
	private static void runAgents(Agent_CentralManager centralManager,
			AgentDescriptions createdAgents,  JobRun jobRun, IAgentLogger logger) {
		
		for (AgentDescription agentDescriptionI :
			createdAgents.getAgentDescriptions()) {

			AgentConfiguration agentConfigurationI =
					agentDescriptionI.getAgentConfiguration();

			Class<?> problemToolI = agentDescriptionI.exportProblemToolClass();
			
			ProblemStruct problemStructI =
					jobRun.exportProblemStruct(problemToolI);
					
			ComputingAgentService.sendStartComputing(centralManager,
					agentConfigurationI.exportAgentAID(), problemStructI, logger);
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
			InputRePlan replan, JobRun jobRun, IAgentLogger logger) throws Exception {
		
		List<AgentDescription> agentsToKill =
				replan.getAgentsToKill().getAgentDescriptions();
		List<InputAgentDescription> agentsToCreate =
				replan.getAgentsToCreate().getInputAgentDescriptions();
		
		List<AgentDescription> agentsCreated = new ArrayList<>();
		
		for (int i = 0; i < agentsToKill.size(); i++) {
			
			AgentDescription agentToKillI = agentsToKill.get(i);
			InputAgentDescription agentToCreateI = agentsToCreate.get(i);
			
			AID agentTokillAID = agentToKillI.exportAgentAID();
			InputAgentConfiguration newConfiguration =
					agentToCreateI.getInputAgentConfiguration();
			
			Class<?> problemToolClass =
					agentToCreateI.exportProblemToolClass();
			ProblemStruct problemStruct =
					jobRun.exportProblemStruct(problemToolClass);
			
			
			AgentConfiguration createdAC = killAndCreateAgent(centralManager,
					agentTokillAID, newConfiguration, problemStruct, logger);
			
			AgentDescription createdAD =
					new AgentDescription(createdAC, problemToolClass);
			
			agentsCreated.add(createdAD);
		}
		
		return new RePlan(replan.getIteration().deepClone(),
				new AgentDescriptions(agentsToKill),
				new AgentDescriptions(agentsCreated));		
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
			AID agentTokillAID, InputAgentConfiguration newConfiguration,
			ProblemStruct problemStruct, IAgentLogger logger) throws Exception {

		
		// kill agent
		ManagerAgentService.sendKillAgent(centralManager, agentTokillAID, logger);

		logger.log(Level.INFO, "Killed: " + agentTokillAID.getLocalName());
		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, agentTokillAID);
		
		
		// create new agent
		AgentConfiguration newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, newConfiguration, logger);
		
		logger.log(Level.INFO, "Created: " + newAgent.exportAgentname());
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent.exportAgentAID(), problemStruct, logger);
		return newAgent;
	}
}
