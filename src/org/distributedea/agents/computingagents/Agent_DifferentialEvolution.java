package org.distributedea.agents.computingagents;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.computingagents.computingagent.localsaver.LocalSaver;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Agent represents Differential Evolution Algorithm Method
 * @author stepan
 *
 */
public class Agent_DifferentialEvolution extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	private String POP_SIZE = "popSize";
	private int popSize = 10;
	
	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		IProblem problem = problemStruct.getProblem();
		
		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		//Class<?> dataset = problemStruct.getDataset().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		
		if (problem instanceof ProblemTSPGPS) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemTSPPoint) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemBinPacking) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem instanceof ProblemContinuousOpt) {
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
		
		// set population size
		Argument popSizeArg = arguments.exportArgument(POP_SIZE);
		this.popSize = popSizeArg.exportValueAsInteger();
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
		
		problemTool.initialization(dataset, agentConf, getLogger());
		this.state = CompAgentState.COMPUTING;
		
		
		long generationNumberI = -1;
		
		
		// generates Individuals
		Vector<IndividualEvaluated> population = new Vector<>();
		for (int i = 0; i < popSize; i++) {
			IndividualEvaluated individualI = problemTool.
					generateIndividualEval(problem, dataset, pedigreeParams, getCALogger());
			population.add(individualI);
		}
		
		DifferentialModel model = new DifferentialModel(population, popSize);
		
		final IndividualEvaluated individualEvalI =
				model.getBestIndividual(problem);
		
		// save, log and distribute computed Individual
		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
			
						
			//pick random point from population			
			DifferentialQuaternion quaternion = model.getQuaternion();
			
			IndividualEvaluated individualCandidateI = quaternion.individualCandidateI;
			
			IndividualEvaluated individual1 = quaternion.individual1;
			IndividualEvaluated individual2 = quaternion.individual2;
			IndividualEvaluated individual3 = quaternion.individual3;
			
									
			IndividualEvaluated[] individualsNew = problemTool.
					createNewIndividualEval(individual1, individual2,
					individual3, problem, dataset, pedigreeParams, getCALogger());
			IndividualEvaluated individualNew = individualsNew[0];

			IndividualEvaluated betterFromCandidateAndNewIndiv = individualCandidateI;
			if (FitnessTool.isFirstIndividualEBetterThanSecond(
							individualNew, individualCandidateI, problem)) {
				// switching Individuals
				model.replaceIndividual(individualCandidateI, individualNew);
				betterFromCandidateAndNewIndiv = individualNew;
			}
			
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualNew, generationNumberI,
					problem, jobID, localSaver);
			
			// send new Individual to distributed neighbors
			IndividualEvaluated theBestOfPopulation = model.getBestIndividual(problem);
			distributeIndividualToNeighours(theBestOfPopulation, problem, jobID);
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.removeTheBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, betterFromCandidateAndNewIndiv, problem)) {
				
				// save and log received Individual
				processRecievedIndividual(betterFromCandidateAndNewIndiv,
						recievedIndividualW, generationNumberI, problem, localSaver);

				model.replaceIndividual(betterFromCandidateAndNewIndiv,
						recievedIndividualW.getIndividualEvaluated());				
			}

		}
		
		problemTool.exit();
		
		this.localSaver.closeFiles();
	}

}


class DifferentialModel {
	
	private IndividualEvaluated[] population;
	
	private Random random = new Random();
	
	public DifferentialModel(Vector<IndividualEvaluated> population0, int popSize) {
		if (population0 == null || population0.size() != popSize) {
			throw new IllegalArgumentException();
		}
		
		this.population = new IndividualEvaluated[popSize];
		
		for (int i = 0; i < popSize; i++) {
			
			IndividualEvaluated indivI = population0.get(i);
			this.population[i] = indivI;
		}
	}

	public DifferentialQuaternion getQuaternion() {
	
		int popSize = population.length;
		
		//pick random point from population
		int candidateIndex = random.nextInt(popSize);
		
		int index1;
		do {
			index1 = random.nextInt(popSize);
		} while (index1 == candidateIndex);

		int index2;
		do {
			index2 = random.nextInt(popSize);
		} while (index2 == candidateIndex || index2 == index1);
		
		int index3;
		do {
			index3 = random.nextInt(popSize);
		} while (index3 == candidateIndex || index3 == index1 || index3 == index2);

		
		DifferentialQuaternion quaternion = new DifferentialQuaternion();
		quaternion.individualCandidateI = population[candidateIndex];
		
		quaternion.individual1 = population[index1];
		quaternion.individual2 = population[index2];
		quaternion.individual3 = population[index3];
		
		return quaternion;
	}

	public void replaceIndividual(IndividualEvaluated indivToDel,
			IndividualEvaluated indivToAdd) {
		
		int index = indexOf(indivToDel);
/*		
		if (index == -1) {
			System.out.println(indivToDel);
			System.out.println("----------------");
			
			for (int i = 0; i < population.length; i++) {
				IndividualEvaluated a = population[i];
				if (a.getFitness() == indivToDel.getFitness()) {
					int todo = 5;
					todo++;
					a.getIndividual().equals(indivToDel.getIndividual());
					System.out.println(a);
				}
			}

		}
*/		
		population[index] = indivToAdd;
	}
	
	private int indexOf(IndividualEvaluated indiv) {
		if (indiv == null) {
			new IllegalArgumentException("Argument is not valid");
		}
		
		for (int i = 0; i < population.length; i++) {
			if (indiv.equals(population[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public IndividualEvaluated getBestIndividual(IProblem problem) {

		Arrays.sort(population, new CmpIndividualEvaluated(problem));
		return population[0];
	}
}

class DifferentialQuaternion {
	
	IndividualEvaluated individualCandidateI;
	
	IndividualEvaluated individual1;
	IndividualEvaluated individual2;
	IndividualEvaluated individual3;
	
}
