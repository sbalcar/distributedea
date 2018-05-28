package org.distributedea.agents.computingagents;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.universal.CompAgentState;
import org.distributedea.agents.computingagents.universal.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;

/**
 * Agent represents Random Search Algorithm Method
 * @author stepan
 *
 */
public class Agent_RandomSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(true);
		description.setExploration(false);
		
		return description;
	}

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		return true;
	}

	@Override
	protected void processArguments(Arguments arguments) throws Exception {
	}

	@Override
	protected void startComputing(ProblemStruct problemStruct,
			IslandModelConfiguration configuration, AgentConfiguration requiredAgentConfiguration) throws Exception {
		
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();

		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(problem, dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			IndividualEvaluated individualEvalI = problemTool.generateIndividualEval(
					problem, dataset, pedigreeParams, getLogger());
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(individualEvalI, problem, jobID);
		}
	
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}
	
}
