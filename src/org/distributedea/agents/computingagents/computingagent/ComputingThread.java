package org.distributedea.agents.computingagents.computingagent;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;
import org.distributedea.problems.exceptions.ProblemToolException;

public class ComputingThread extends Thread {

	private Agent_ComputingAgent agent;
	
	private JobID jobID;
	private boolean individualDistribution;
	private Class<?> problemToolClass;
	private ProblemTool problemTool;
	private Problem problem;
	private AgentConfiguration requiredAgentConfiguration;
	
	/** best result of computing (Individual and fitness) **/
	private ResultOfComputing bestResultOfComputing = null;
	
	public ComputingThread(Agent_ComputingAgent agent, ProblemStruct problemStruct,
			AgentConfiguration requiredAgentConfiguration) {
		this.agent = agent;
		this.individualDistribution = problemStruct.getIndividualDistribution();
		this.jobID = problemStruct.getJobID();
		this.problemToolClass = problemStruct.exportProblemToolClass(agent.getLogger());
		this.problem = problemStruct.getProblem();
		this.requiredAgentConfiguration = requiredAgentConfiguration;
	}
	
	public JobID getJobID() {
		return this.jobID;
	}
	public boolean isIndividualDistribution() {
		return this.individualDistribution;
	}
	public ProblemTool getProblemTool() {
		return this.problemTool;
	}
	public Problem getProblem() {
		return this.problem;
	}

	public ResultOfComputing getBestresultOfComputing() {
		
		return bestResultOfComputing;
	}
	public void setBestresultOfComputing(ResultOfComputing resultOfComputing) {
		
		if (resultOfComputing == null) {
			return;
		}
		
		AgentDescription agentDescription = new AgentDescription();
		agentDescription.importProblemTool(problemTool);
		agentDescription.setAgentConfiguration(agent.requiredAgentConfiguration);
		
		resultOfComputing.setAgentDescription(agentDescription);
		resultOfComputing.setJobID(jobID);
		
		this.bestResultOfComputing = resultOfComputing;
	}
	
	
	@Override
	public void run() {
		
		problemTool = ProblemToolEvaluation.getProblemToolFromClass(problemToolClass);
		
		try {
			agent.startComputing(problem, problemTool, jobID, requiredAgentConfiguration);
			
		} catch (ProblemToolException e) {
			this.agent.getLogger().logThrowable("Error in the ProblemTool", e);
			this.agent.commitSuicide();
		}
		
	}}

