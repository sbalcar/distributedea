package org.distributedea.input.jobs;

import java.io.File;
import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.specific.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsOneLastIndivModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsThreeLastIndivModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsTwoQueuesModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsOneLastIndivModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsOneQueueModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsThreeLastIndivModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual.ReadyToSendIndividualsOnlyOneInserter;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual.ReceivedIndivRemoveOneEach5sSelector;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsArithmeticSequence;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsComplement;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolBruteForceMFSGDist1ByIndex;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolDifferentialEvolutionMF;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolHillClimbingMFMahout;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolHillClimbingMFSGDist1RandomInEachRow;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolRandomSearchMF;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolSimulatedAnnealingMFSGDist1RandomInEachRow;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolTabuSearchMFSGDist1RandomInEachRow;

/**
 * Defines a set of Matrix Factorization {@link Job}
 * @author stepan
 *
 */
public class InputMatrixFactorization {

	public static Job test01() throws IOException {
		
		InputMethodDescription methodHillClimbing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolHillClimbingMFSGDist1RandomInEachRow())
				);

		InputMethodDescription methodRandomSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolRandomSearchMF())
				);

		InputMethodDescription methodEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()))),
				new ProblemToolDefinition(new ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation())
				);

		InputMethodDescription methodBruteForce = new InputMethodDescription(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolBruteForceMFSGDist1ByIndex())
				);

		InputMethodDescription methodTabuSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolTabuSearchMFSGDist1RandomInEachRow())
				);

		InputMethodDescription methodSimulatedAnnealing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002"))),
				new ProblemToolDefinition(new ProblemToolSimulatedAnnealingMFSGDist1RandomInEachRow())
				);

		InputMethodDescription methodDifferentialEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50"), new Argument("crossRate", "0.0"))),
				new ProblemToolDefinition(new ProblemToolDifferentialEvolutionMF())
				);

		InputMethodDescriptions methods = new InputMethodDescriptions();
		methods.addInputMethodDescr(methodHillClimbing);
		methods.addInputMethodDescr(methodRandomSearch);
		methods.addInputMethodDescr(methodEvolution);
		methods.addInputMethodDescr(methodBruteForce);
		methods.addInputMethodDescr(methodTabuSearch);
		methods.addInputMethodDescr(methodSimulatedAnnealing);
		methods.addInputMethodDescr(methodDifferentialEvolution);
		
		
		IslandModelConfiguration islandModelConf = new IslandModelConfiguration();
		islandModelConf.setIndividualDistribution(true);
		islandModelConf.setNeighbourCount(3);
		islandModelConf.setReplanPeriodMS(60000);
		islandModelConf.setIndividualBroadcastPeriodMS(5000);
		islandModelConf.importReadyToSendIndividualInserterClass(
				ReadyToSendIndividualsOnlyOneInserter.class);
		islandModelConf.importReceivedIndividualSelectorClass(
				ReceivedIndivRemoveOneEach5sSelector.class);
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsTwoQueuesModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsOneQueueModel.class);
		
		File fileRatings = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.data"));
		File fileItems = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.item"));
		File fileUsers = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.user"));
		RatingIDsArithmeticSequence sequence =
				new RatingIDsArithmeticSequence(5, 5);
		
		IDatasetDescription datasetDescr = new DatasetDescriptionMF(
				fileRatings, new RatingIDsComplement(sequence),
				fileRatings, sequence,
				fileItems, fileUsers);
		
		Job job = new Job();
		job.setJobID("jobID");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10));
		job.setDatasetDescription(datasetDescr);
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		File fileRatings = new File(FileNames.getInputProblemFile(
				"ml-1m" + File.separator + "ratings.dat"));
		File fileItems = new File(FileNames.getInputProblemFile(
				"ml-1m" + File.separator + "movies.dat"));
		File fileUsers = new File(FileNames.getInputProblemFile(
				"ml-1m" + File.separator + "users.dat"));
		RatingIDsArithmeticSequence sequence =
				new RatingIDsArithmeticSequence(5, 5);
		
		IDatasetDescription datasetDescr = new DatasetDescriptionMF(
				fileRatings, new RatingIDsComplement(sequence),
				fileRatings, sequence,
				fileItems, fileUsers);
		
		Job job = test01();
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10));
		job.setDatasetDescription(datasetDescr);
		
		IslandModelConfiguration islandModelConf =
				job.getIslandModelConfiguration();
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsThreeLastIndivModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsThreeLastIndivModel.class);
		
		
		return job;
	}
	
	public static Job test03() throws IOException {

		File file = new File(FileNames.getInputProblemFile(
				"ml-10M100K" + File.separator + "ratings.dat"));
		File fileItems = new File(FileNames.getInputProblemFile(
				"ml-10M100K" + File.separator + "movies.dat"));
		File fileUsers = new File(FileNames.getInputProblemFile(
				"ml-10M100K" + File.separator + "tags.dat"));
		RatingIDsArithmeticSequence sequence =
				new RatingIDsArithmeticSequence(5, 5);
		
		IDatasetDescription datasetDescr = new DatasetDescriptionMF(
				file, new RatingIDsComplement(sequence),
				file, sequence,
				fileItems, fileUsers);

		Job job = test01();
		job.setProblem(new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10));
		job.setDatasetDescription(datasetDescr);
		
		IslandModelConfiguration islandModelConf =
				job.getIslandModelConfiguration();
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsOneLastIndivModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsOneLastIndivModel.class);
		
		return job;
	}
	
	public static Job test04() throws IOException {
		
		Job job = test01();
		
		InputMethodDescription methodHillClimbingOld = job.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_HillClimbing.class);
				
		InputMethodDescription methodHillClimbing = new InputMethodDescription(
				methodHillClimbingOld.getInputAgentConfiguration(),
				new ProblemToolDefinition(new ProblemToolHillClimbingMFMahout()));
		
		job.setMethods(new InputMethodDescriptions(methodHillClimbing));
		
		return job;
	}
}
