package org.distributedea.agents.computingagents;

import java.util.Random;
import java.util.Vector;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Agent represents Differential Evolution Algorithm Method
 * @author stepan
 *
 */
public class Agent_DifferentialEvolution extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private int popSize = 50;
	
	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		Class<?> problem = problemStruct.getProblem().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		
		if (problem == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemContinousOpt.class) {
			if (representation == IndividualPoint.class) {
				isAble = true;
			}			
		}
		
		return isAble;
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(popSize);
		description.setExploitation(true);
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
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(null, methodDescription);
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		Vector<IndividualEvaluated> population = new Vector<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualI = problemTool.
					generateIndividualEval(problem, pedigreeParams, getCALogger());
			population.add(individualI);
		}
		
		DifferentialModel model = new DifferentialModel(population);
		
		final IndividualEvaluated individualEvalI =
				model.getBestIndividual(problem, problemTool, getLogger());
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
						
			//pick random point from population			
			DifferentialQuaternion quaternion = model.getQuaternion();
			
			IndividualEvaluated individualEvalCandidateI = quaternion.individualCandidateI;
			
			IndividualEvaluated individual1 = quaternion.individual1;
			IndividualEvaluated individual2 = quaternion.individual2;
			IndividualEvaluated individual3 = quaternion.individual3;
			
									
			IndividualEvaluated[] individualsNew =
					problemTool.createNewIndividualEval(individual1,
							individual2, individual3, problem, pedigreeParams, getCALogger());
			IndividualEvaluated individualEvalNew = individualsNew[0];

			if (FitnessTool.isFirstIndividualEBetterThanSecond(
							individualEvalNew, individualEvalCandidateI, problem)) {
				// switching Individuals
				model.replaceIndividual(individualEvalCandidateI, individualEvalNew);
			}
			
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalNew, generationNumberI,
					problem, jobID);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalNew, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalCandidateI, problem) &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalNew, problem)) {
				
				model.replaceIndividual(individualEvalCandidateI,
						recievedIndividualW.getIndividualEvaluated());
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			}
		}
		
		problemTool.exit();
		
	}

}


class DifferentialModel {
	
	private Vector<IndividualEvaluated> population = new Vector<>();
	
	private Random random = new Random();
	
	public DifferentialModel(Vector<IndividualEvaluated> population) {
		if (population == null) {
			throw new IllegalArgumentException();
		}	
		this.population = population;
	}

	public DifferentialQuaternion getQuaternion() {
	
		int popSize = population.size();
		
		//pick random point from population
		int candidateIndex = Math.abs(random.nextInt()) % (popSize-1);
		
		int index1;
		do {
			index1 = Math.abs(random.nextInt()) % (popSize-1);
		} while (index1 == candidateIndex);

		int index2;
		do {
			index2 = Math.abs(random.nextInt()) % (popSize-1);
		} while (index2 == candidateIndex || index2 == index1);
		
		int index3;
		do {
			index3 = Math.abs(random.nextInt()) % (popSize-1);
		} while (index3 == candidateIndex || index3 == index1 || index3 == index2);
		
		DifferentialQuaternion quaternion = new DifferentialQuaternion();
		quaternion.individualCandidateI = population.get(candidateIndex);
		
		quaternion.individual1 = population.get(index1);
		quaternion.individual2 = population.get(index2);
		quaternion.individual3 = population.get(index3);
		
		return quaternion;
	}

	public void replaceIndividual(IndividualEvaluated indivToDel,
			IndividualEvaluated indivToAdd) {
		population.remove(indivToDel);
		population.add(indivToAdd);
	}
			
	public IndividualEvaluated getBestIndividual(Problem problem,
			IProblemTool problemTool, IAgentLogger logger) {
		
		if (population.isEmpty()) {
			return null;
		}
		
		IndividualEvaluated bestIndividual = population.get(0);
		
		for (IndividualEvaluated individualI : population) {
			
			boolean isIndividualBetter =
					FitnessTool.isFirstIndividualEBetterThanSecond(
							individualI, bestIndividual, problem);
			if (isIndividualBetter) {
				bestIndividual = individualI;
			}
		}
		
		return bestIndividual;		
	}
}

class DifferentialQuaternion {
	
	IndividualEvaluated individualCandidateI;
	
	IndividualEvaluated individual1;
	IndividualEvaluated individual2;
	IndividualEvaluated individual3;
	
}
