package org.distributedea.input.jobs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.method.MethodsTwoSets;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.DatasetPartitioning;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsArithmeticSequence;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsComplement;
import org.distributedea.problems.matrixfactorization.ProblemToolMatrixFactorization;

/**
 * Defines a set of Matrix Factorization {@link Job}
 * @author stepan
 *
 */
public class InputMatrixFactorization {

	public static Job test01() throws IOException {
		
		InputAgentConfigurations algorithms = new InputAgentConfigurations(Arrays.asList(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) )),
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10") )),
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002") )),
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")) )
			));
		
		DatasetPartitioning datasetPartitioning = new DatasetPartitioning(
				new RatingIDsComplement(new RatingIDsArithmeticSequence(5, 5)),
				new RatingIDsArithmeticSequence(5, 5));
		
		Job job = new Job();
		job.setJobID("jobID");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(true, 60000, 5000));
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10, datasetPartitioning));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("ml-100k" + File.separator + "u.data")));
		job.setMethods(new MethodsTwoSets(
				algorithms,new ProblemTools(ProblemToolMatrixFactorization.class)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}
	
	public static Job test02() throws IOException {

		DatasetPartitioning datasetPartitioning = new DatasetPartitioning(
				new RatingIDsComplement(new RatingIDsArithmeticSequence(5, 5)),
				new RatingIDsArithmeticSequence(5, 5));

		Job job = test01();
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10, datasetPartitioning));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("ml-1m" + File.separator + "ratings.dat")));
		
		return job;
	}
	
	public static Job test03() throws IOException {

		DatasetPartitioning datasetPartitioning = new DatasetPartitioning(
				new RatingIDsComplement(new RatingIDsArithmeticSequence(5, 5)),
				new RatingIDsArithmeticSequence(5, 5));

		Job job = test01();
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10, datasetPartitioning));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("ml-10M100K" + File.separator + "ratings.dat")));
		
		return job;
	}
}
