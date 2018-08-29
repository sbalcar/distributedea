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
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsTwoQueuesModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsOneQueueModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual.ReadyToSendIndividualsOnlyOneInserter;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual.ReceivedIndivRemoveOneSelector;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problemtools.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

/**
 * Defines a set of TSP {@link Job}
 * @author stepan
 *
 */
public class InputTSP {
	
	public static Job test01() throws IOException {

		InputMethodDescription methodHillClimbing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodRandomSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()))),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodBruteForce = new InputMethodDescription(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodTabuSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodSimulatedAnnealing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002"))),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		InputMethodDescription methodDifferentialEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50"))),
				new ProblemToolDefinition(new ProblemToolGPSEuc2D2opt())
				);

		Methods methods = new Methods();
		methods.addInputMethodDescr(methodHillClimbing);
		methods.addInputMethodDescr(methodRandomSearch);
		methods.addInputMethodDescr(methodEvolution);
		methods.addInputMethodDescr(methodBruteForce);
		methods.addInputMethodDescr(methodTabuSearch);
		methods.addInputMethodDescr(methodSimulatedAnnealing);
		methods.addInputMethodDescr(methodDifferentialEvolution);
		
		
		IslandModelConfiguration islandModelConf = new IslandModelConfiguration();
		islandModelConf.setIndividualDistribution(true);
		islandModelConf.setReplanPeriodMS(60000);
		islandModelConf.setIndividualBroadcastPeriodMS(5000);
		islandModelConf.importReadyToSendIndividualInserterClass(
				ReadyToSendIndividualsOnlyOneInserter.class);
		islandModelConf.importReceivedIndividualSelectorClass(
				ReceivedIndivRemoveOneSelector.class);
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsTwoQueuesModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsOneQueueModel.class);
		
		Job job = new Job();
		job.setJobID("tsp01");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("simpleTest.tsp"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		Methods methods = test01().getMethods();
		
		IslandModelConfiguration islandModelConf =
				test01().getIslandModelConfiguration().deepClone();
		
		Job job = new Job();
		job.setJobID("tsp02");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("wi29.tsp"))));
		job.setMethods(methods);
				
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}

	
	public static Job test04() throws IOException {

		Methods methods = test01().getMethods();
		
		IslandModelConfiguration islandModelConf =
				test01().getIslandModelConfiguration().deepClone();		
		
		Job job = new Job();
		job.setJobID("tsp04");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("xit1083.tsp"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}

	public static Job test05() throws IOException {

		Methods methods = test01().getMethods();
		
		IslandModelConfiguration islandModelConf =
				test01().getIslandModelConfiguration().deepClone();
		
		Job job = new Job();
		job.setJobID("jobID");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("xit1083.tsp"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}

	public static Job test06() throws IOException {

		Methods methods = test01().getMethods();
		
		IslandModelConfiguration islandModelConf =
				test01().getIslandModelConfiguration().deepClone();
		
		Job job = new Job();
		job.setJobID("id");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("djb2036.tsp"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}

	public static Job test07() throws IOException {

		Methods methods = test01().getMethods();
		
		IslandModelConfiguration islandModelConf =
				test01().getIslandModelConfiguration().deepClone();
		
		Job job = new Job();
		job.setJobID("id");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemTSPGPS());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("xql662.tsp"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}
}
