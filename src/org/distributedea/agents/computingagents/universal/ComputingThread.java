package org.distributedea.agents.computingagents.universal;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
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

	private IslandModelConfiguration islandModelConf;
	
	private ProblemWrapper problemWrp;
	
	private AgentConfiguration requiredAgentConf;
	
	private  MethodIDs methodIDs;
	
	private Dataset dataset;
	
	
	public ComputingThread(Agent_ComputingAgent agent, ProblemWrapper problemWrp,
			IslandModelConfiguration islandModelConf, AgentConfiguration requiredAgentConf,
			MethodIDs methodIDs) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " can not be null");
		}
		
		IAgentLogger logger = agent.getCALogger();
		
		if (problemWrp == null || ! problemWrp.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		if (islandModelConf == null || ! islandModelConf.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		if (requiredAgentConf == null || ! requiredAgentConf.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		if (methodIDs == null || ! methodIDs.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " is not valid");
		}
				
		
		this.agent = agent;
		
		this.problemWrp = problemWrp;
		
		this.islandModelConf = islandModelConf;
		
		this.requiredAgentConf = requiredAgentConf;

		this.methodIDs = methodIDs;
		
		ProblemToolDefinition problemToolDef = problemWrp.getProblemToolDefinition();
		IProblemTool problemTool = problemToolDef.exportProblemTool(logger);
		IDatasetDescription fileOfProblem = problemWrp.getDatasetDescription();
		this.dataset = problemTool.readDataset(fileOfProblem, problemWrp.getProblem(), logger);
	}

	public IProblemTool getProblemTool() {
		
		return problemWrp.getProblemToolDefinition().exportProblemTool(agent.getLogger());
	}

	public IProblem getProblemDefinition() {
		return problemWrp.getProblem().deepClone();
	}
	
	public ProblemToolDefinition getProblemToolDefinition() {
		return problemWrp.getProblemToolDefinition().deepClone();
	}
	
	
	public Dataset getDataset() {
		return dataset;
	}

	public void isIndividualDistribution() {
		islandModelConf.isIndividualDistribution();
	}
	
	@Override
	public void run() {
		
		try {
			agent.startComputing(problemWrp.deepClone(),
					islandModelConf.deepClone(), requiredAgentConf, methodIDs.deepClone());
			
		} catch (Exception e) {
			System.out.println("Error by computing");
			e.printStackTrace();
			CentralLogerService.logMessage(agent, "Error by computing", agent.getLogger());
			this.agent.getLogger().logThrowable("Error by computing", e);
			this.agent.commitSuicide();
		}
		
	}
	
}

