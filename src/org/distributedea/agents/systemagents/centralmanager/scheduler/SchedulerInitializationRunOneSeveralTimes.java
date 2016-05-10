package org.distributedea.agents.systemagents.centralmanager.scheduler;


import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class SchedulerInitializationRunOneSeveralTimes implements Scheduler {
	
	/** index on the method(agent)*/
	private int methodIndex = -1;
	
	/** index on the Problem Tool in the list of tolls */
	private int problemToolIndex = -1;
	
	public SchedulerInitializationRunOneSeveralTimes() {}

	public SchedulerInitializationRunOneSeveralTimes(int methodIndex, int problemToolIndex) {
		
		this.methodIndex = methodIndex;
		this.problemToolIndex = problemToolIndex;
	}
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException {
		
		AgentConfiguration agentConfigurationI = configurations.getAgentConfigurations().get(methodIndex);

		AgentConfigurations configurationsSelected = new AgentConfigurations();
		configurationsSelected.addAgentConfigurations(agentConfigurationI);

		// chooses ProblemTool by index
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(problemToolIndex);

		
		ProblemTools problemToolsSelected = new ProblemTools();
		problemToolsSelected.addProblemTool(problemToolI);
		
		Scheduler scheduler = new SchedulerInitialization();
		scheduler.agentInitialization(centralManager, job,
				configurationsSelected, logger);
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, Job job,
			AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException {		
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
