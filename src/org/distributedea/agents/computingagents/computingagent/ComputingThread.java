package org.distributedea.agents.computingagents.computingagent;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.services.CentralLogerService;

/**
 * Thread that is running computation in {@link Agent_ComputingAgent},
 * long-term computation can not be Behavior because
 * it would block another Jade Behavior
 * @author stepan
 *
 */
public class ComputingThread extends Thread {

	private Agent_ComputingAgent agent;

	private IslandModelConfiguration configuration;
	
	private ProblemStruct problemStruct;
	
	private AgentConfiguration requiredAgentConfiguration;
	
	public ComputingThread(Agent_ComputingAgent agent, ProblemStruct problemStruct,
			IslandModelConfiguration configuration, AgentConfiguration requiredAgentConfiguration) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " can not be null");
		}
		
		IAgentLogger logger = agent.getCALogger();
		
		if (problemStruct == null || ! problemStruct.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		if (requiredAgentConfiguration == null || ! requiredAgentConfiguration.valid(logger)) {
			requiredAgentConfiguration.valid(logger);
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		
		this.agent = agent;
		
		this.problemStruct = problemStruct;
		
		this.configuration = configuration;
		
		this.requiredAgentConfiguration = requiredAgentConfiguration;
		
	}

	public IProblemTool getProblemTool() {
		
		return problemStruct.exportProblemTool(agent.getLogger());
	}

	public IProblem getProblemDefinition() {
		return problemStruct.getProblem().deepClone();
	}
	
	public Dataset getDataset() {
		return problemStruct.getDataset().deepClone();
	}

	public void isIndividualDistribution() {
		configuration.isIndividualDistribution();
	}
	
	@Override
	public void run() {

		try {
			agent.startComputing(problemStruct.deepClone(),
					configuration.deepClone(), requiredAgentConfiguration);
			
		} catch (Exception e) {
			System.out.println("Error by computing");
			e.printStackTrace();
			CentralLogerService.logMessage(agent, "Error by computing", agent.getLogger());
			this.agent.getLogger().logThrowable("Error by computing", e);
			this.agent.commitSuicide();
		}
		
	}
	
}

