package org.distributedea.agents.computingagents;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.universal.CompAgentState;
import org.distributedea.agents.computingagents.universal.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.problemtools.IProblemToolBruteForce;

/**
 * Agent represents Brute Force Algorithm Method
 * @author stepan
 *
 */
public class Agent_BruteForce extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean isAbleToSolve(ProblemWrapper problemWrp) {
		
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
	protected void startComputing(ProblemWrapper problemWrp,
			IslandModelConfiguration configuration, AgentConfiguration agentConf) throws Exception {
		
  		if (problemWrp == null || ! problemWrp.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		if (configuration == null || ! configuration.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		if (agentConf == null || ! agentConf.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		
		this.state = CompAgentState.INITIALIZATION;
		
		JobID jobID = problemWrp.getJobID();
		ProblemToolDefinition problemToolDef = problemWrp.getProblemToolDefinition();
		IProblem problem = problemWrp.getProblem();
		boolean individualDistribution = configuration.isIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemToolDef);
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemWrp.getPedigreeDefinition(), methodDescription);
		
		IProblemToolBruteForce problemTool = (IProblemToolBruteForce) problemToolDef.exportProblemTool(getLogger());
		
		IDatasetDescription datasetDescr = problemWrp.getDatasetDescription();
		Dataset dataset = problemTool.readDataset(datasetDescr, problem, getLogger());
				
		
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
		readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);
		
		
		while (individualEvalI != null && state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
			individualEvalI = problemTool.generateNextIndividualEval(problem,
					dataset, individualEvalI, generationNumberI,
					pedigreeParams, getLogger());
			
			// save, log and distribute new computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);
			
			// take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividualSelector.getIndividual(problem);
			
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
