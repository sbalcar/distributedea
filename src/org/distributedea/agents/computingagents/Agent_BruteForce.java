package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
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
			IslandModelConfiguration configuration, AgentConfiguration agentConf) throws Exception {
		
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = configuration.isIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(problem, dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;
		
		IndividualEvaluated individualEvalI = problemTool
				.generateFirstIndividualEval(problem, dataset,
				pedigreeParams, getLogger());

		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		// logs data
		distributeIndividualToNeighours(individualEvalI, problem, jobID);
		
		
		while (individualEvalI != null && state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			individualEvalI = problemTool.generateNextIndividualEval(problem,
					dataset, individualEvalI, pedigreeParams, getLogger());
			
			// save, log and distribute new computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(individualEvalI, problem, jobID);
			
			
			// take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem)) {
				
				// save and log received Individual
				processRecievedIndividual(individualEvalI, recievedIndividualW,
						generationNumberI, problem, localSaver);

				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();				
			}
				
		}
	
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}


}
