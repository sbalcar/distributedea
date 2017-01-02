package org.distributedea.agents.computingagents;

import java.util.LinkedList;
import java.util.Queue;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
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
 * Agent represents TabuSearch Algorithm Method
 * @author stepan
 *
 */
public class Agent_TabuSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	
	
	private String TABU_MODEL_SIZE = "tabuModelSize";
	private int tabuModelSize = 500;
	
	
	private TabuModel tabu = new TabuModel(tabuModelSize);
	
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
		
		// set Tabu model max size
		Argument tabuModelSizeArg = arguments.exportArgument(TABU_MODEL_SIZE);
		this.tabuModelSize = tabuModelSizeArg.exportValueAsInteger();
		
		// update model
		tabu = new TabuModel(tabuModelSize);
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
				.generateFirstIndividualEval(problemDefinition, problem, pedigreeParams, getCALogger());

		
		// add actual individual in the Tabu Set
		tabu.offer(individualEvalI);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problemDefinition, jobID);
		
		while (state == CompAgentState.COMPUTING) {
			
			// going through neighbors
			IndividualEvaluated neighborJ = null;
			
			long neighborIndex = 0;
			while (state == CompAgentState.COMPUTING) {
				// increment next number of generation
				generationNumberI++;
				
				neighborJ = problemTool.getNeighborEval(individualEvalI,
						problemDefinition, problem, neighborIndex, pedigreeParams, getCALogger());
				
				// not available next better neighbor 
				if (neighborJ == null) {
					break;
				}
				
				// send new Individual to distributed neighbors
				if (individualDistribution) {
					distributeIndividualToNeighours(neighborJ, problemDefinition, jobID);
				}
				
				boolean isNeighborlBetter =
						FitnessTool.isFirstIndividualEBetterThanSecond(
								neighborJ, individualEvalI, problemDefinition);
				
				// new better individual found
				if (isNeighborlBetter && (! tabu.contains(neighborJ)) ) {
					break;
				}
				
				neighborIndex++;
			}
			individualEvalI = neighborJ;

			// generate new individual in local extreme
			if (individualEvalI == null) {
				individualEvalI = problemTool.generateFirstIndividualEval(
						problemDefinition, problem, pedigreeParams, getCALogger());
			}
			
			// add actual individual in the Tabu Set
			tabu.offer(individualEvalI);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problemDefinition, jobID);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problemDefinition, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problemDefinition);
			
			boolean isReceivedBetter =
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problemDefinition);

			if (individualDistribution &&
					(! tabu.contains(recievedIndividualW)) &&
					isReceivedBetter) {
								
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				tabu.offer(individualEvalI);
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW,
						generationNumberI, problemDefinition);
			}
			
		}
		
		problemTool.exit();

	}

}


class TabuModel {
	
	private int tabuListSize;
	
	private Queue<IndividualEvaluated> tabuList = new LinkedList<>();
	
	/**
	 * Constructor
	 * @param tabuListSize
	 */
	public TabuModel(int tabuListSize) {
		if (tabuListSize <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		this.tabuListSize = tabuListSize;
	}
	
	public boolean contains(IndividualWrapper individualWrapper) {
		
		if (individualWrapper == null) {
			return false;
		}
		
		IndividualEvaluated individual = individualWrapper.getIndividualEvaluated();
		
		return tabuList.contains(individual);
	}

	public boolean contains(IndividualEvaluated individual) {
		
		if (individual == null) {
			return false;
		}
		
		return tabuList.contains(individual);
	}
	
	public void offer(IndividualEvaluated individual) {
		
		resize();
		
		tabuList.offer(individual);
	}
	
	
	private void resize() {
		
		//adjust the size of the taboo on the acceptable limit
		while (tabuList.size() >= tabuListSize) {
			tabuList.poll();
		}
	}
}