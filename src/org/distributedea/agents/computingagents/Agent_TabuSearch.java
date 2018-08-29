package org.distributedea.agents.computingagents;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.universal.CompAgentState;
import org.distributedea.agents.computingagents.universal.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.arguments.Argument;
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
import org.distributedea.problemtools.IProblemTool;
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
		
		// set Tabu model max size
		Argument tabuModelSizeArg = arguments.exportArgument(TABU_MODEL_SIZE);
		this.tabuModelSize = tabuModelSizeArg.exportValueAsInteger();

	    // initial number of neighbors
		Argument numberOfNeighborsArg = arguments.exportArgument(NUMBER_OF_NEIGHBORS);
		this.numberOfNeighbors = numberOfNeighborsArg.exportValueAsInteger();
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
		
		IProblemTool problemTool = problemToolDef.exportProblemTool(getLogger());
		
		IDatasetDescription datasetDescr = problemWrp.getDatasetDescription();
		Dataset dataset = problemTool.readDataset(datasetDescr, problem, getLogger());
				
		
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
		readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);

		
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
			readyToSendIndividualsInserter.insertIndiv(individualEvalI, problem);
			
			// add actual individual in the Tabu Set
			tabu.offer(individualEvalI);
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID, localSaver);
			
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividualSelector.getIndividual(problem);
			
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