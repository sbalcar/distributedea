package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;

/**
 * Agent represents Brute Force Algorithm Method
 * @author stepan
 *
 */
public class Agent_BruteForce extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {
		
		return true;
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(false);
		description.setExploration(true);
		
		return description;
	}

	protected void processArguments(Arguments arguments) throws Exception {
	}
	
	@Override
	protected void startComputing(ProblemStruct problemStruct,
			AgentConfiguration agentConf) throws Exception {
		
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		IProblemDefinition problemDefinition = problemStruct.getProblemDefinition();
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemDefinition, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;
		
		IndividualEvaluated individualEvalI = problemTool
				.generateFirstIndividualEval(problemDefinition, problem,
				pedigreeParams, getLogger());

		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problemDefinition, jobID);
		
		while (individualEvalI != null && state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			individualEvalI = problemTool.generateNextIndividualEval(problemDefinition,
					problem, individualEvalI, pedigreeParams, getLogger());
			
			// save, log and distribute new computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problemDefinition, jobID);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problemDefinition, jobID);
			}
			
			// take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problemDefinition);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problemDefinition)) {
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problemDefinition);
			}
				
		}
	
		problemTool.exit();
	}


}
