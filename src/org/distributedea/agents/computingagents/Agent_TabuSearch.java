package org.distributedea.agents.computingagents;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Agent represents TabuSearch Algorithm Method
 * @author stepan
 *
 */
public class Agent_TabuSearch extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	
	
	private String TABU_MODEL_SIZE = "tabuModelSize";
	private int tabuModelSize = 50;
	
    // initial number of neighbors
	private String NUMBER_OF_NEIGHBORS = "numberOfNeighbors";
	private int numberOfNeighbors = 10;
	
	
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

	    // initial number of neighbors
		Argument numberOfNeighborsArg = arguments.exportArgument(NUMBER_OF_NEIGHBORS);
		this.numberOfNeighbors = numberOfNeighborsArg.exportValueAsInteger();
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
		IProblem problem = problemStruct.getProblem();
		Dataset dataset = problemStruct.getDataset();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problem, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(
				problemStruct.exportPedigreeOfIndividual(getCALogger()), methodDescription);
		
		this.localSaver = new LocalSaver(this, jobID);
		
		problemTool.initialization(problem, dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
        
		long generationNumberI = -1;
		TabuModel tabu = new TabuModel(tabuModelSize);
		
		IndividualEvaluated individualEvalI = problemTool
				.generateIndividualEval(problem, dataset, pedigreeParams, getCALogger());

		
		// add actual individual in the Tabu Set
		tabu.offer(individualEvalI);
		
		// logs data
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		// send new Individual to distributed neighbors
		distributeIndividualToNeighours(individualEvalI, problem, jobID);

		
		while (state == CompAgentState.COMPUTING) {

			// increment next number of generation
			generationNumberI++;

			IndividualEvaluated[] neighbours = getNeighbours(
					individualEvalI, problem, dataset, problemTool,
					numberOfNeighbors, pedigreeParams); 

			Arrays.sort(neighbours, new CmpIndividualEvaluated(problem));
			IndividualEvaluated neighbor = neighbours[0];
			
			boolean isNeighborlBetter =
					FitnessTool.isFirstIndividualEBetterThanSecond(
							neighbor, individualEvalI, problem);


			if (isNeighborlBetter && (! tabu.contains(neighbor)) ) {
				getCALogger().log(Level.INFO, "JUMP " + neighbor.getFitness());
				individualEvalI = neighbor;
			}
			
			// send new Individual to distributed neighbors
			distributeIndividualToNeighours(neighbor, problem, jobID);
			
			
			// add actual individual in the Tabu Set
			tabu.offer(individualEvalI);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			
			boolean isReceivedBetter =
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem);

			if (individualDistribution &&
					isReceivedBetter &&
					(! tabu.contains(recievedIndividualW))) {
				
				// save and log received Individual
				processRecievedIndividual(individualEvalI, recievedIndividualW,
						generationNumberI, problem, localSaver);
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				tabu.offer(individualEvalI);
			}
			
		}
		
		problemTool.exit();

		this.localSaver.closeFiles();
	}

	
	
	protected IndividualEvaluated[] getNeighbours(IndividualEvaluated individualEval,
			IProblem problem, Dataset dataset, IProblemTool problemTool,
			int numberOfNeighbors, PedigreeParameters pedigreeParams) throws Exception {
		
		IndividualEvaluated[] neighbours = new IndividualEvaluated[numberOfNeighbors];
				
		for (int i = 0; i < numberOfNeighbors; i++) {
			
			IndividualEvaluated indivI = problemTool.getNeighborEval(individualEval,
					problem, dataset, 0, pedigreeParams, getCALogger());
			neighbours[i] = indivI;
			
			if (state != CompAgentState.COMPUTING) {
				IndividualEvaluated [] shortedNeighbours = new IndividualEvaluated[i+1];
				System.arraycopy(neighbours, 0, shortedNeighbours, 0, i+1);
				return shortedNeighbours;
			}
		}
		
		return neighbours;
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