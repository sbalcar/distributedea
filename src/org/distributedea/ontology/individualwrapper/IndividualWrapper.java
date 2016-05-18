package org.distributedea.ontology.individualwrapper;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

import jade.content.Concept;

/**
 * Ontology for solution + description representation
 */
public class IndividualWrapper implements Concept {

	private static final long serialVersionUID = -3883073526379640439L;

	private IndividualEvaluated individualEvaluated;
	
	private AgentDescription agentDescription;

	private JobID jobID;
	
	
	public IndividualEvaluated getIndividualEvaluated() {
		return individualEvaluated;
	}
	public void setIndividualEvaluated(IndividualEvaluated individualEvaluated) {
		this.individualEvaluated = individualEvaluated;
	}
	
	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}

	public boolean validation(Problem problem, AgentLogger logger) {
		
		if (problem == null) {
			return false;
		}
		
		try {
			return validEvaluatedIndividual(problem, logger);
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("", e);
			return false;
		}
		
	}
	
	private boolean validEvaluatedIndividual(Problem problem, AgentLogger logger) throws InstantiationException, IllegalAccessException {
		
		Class<?> problemToolClass = agentDescription.exportProblemToolClass();
		ProblemTool problemTool = (ProblemTool) problemToolClass.newInstance();
		
		return individualEvaluated.validation(problem, problemTool, logger);
	}
}
