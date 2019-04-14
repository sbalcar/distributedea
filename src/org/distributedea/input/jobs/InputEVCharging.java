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
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerDummy;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.ProblemEVCharging;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problems.evcharging.point.ProblemToolBruteForceEVCharging;
import org.distributedea.problems.evcharging.point.ProblemToolDifferentialEvolutionEVCharging;
import org.distributedea.problems.evcharging.point.ProblemToolEvolutionEVCharging;
import org.distributedea.problems.evcharging.point.ProblemToolHillClimbingEVChargingRandomMove;
import org.distributedea.problems.evcharging.point.ProblemToolRandomSearchEVCharging;
import org.distributedea.problems.evcharging.point.ProblemToolSimulatedAnnealingEVChargingRandomMove;
import org.distributedea.problems.evcharging.point.ProblemToolTabuSearchEVChargingRandomMove;

public class InputEVCharging {

	public static Job test01() throws IOException {
		
		InputMethodDescription methodHillClimbing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "3"))),
				new ProblemToolDefinition(new ProblemToolHillClimbingEVChargingRandomMove(0.1, 0.1))
				);

		InputMethodDescription methodRandomSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolRandomSearchEVCharging(0.1))
				);

		InputMethodDescription methodEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "4"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) )),
				new ProblemToolDefinition(new ProblemToolEvolutionEVCharging(0.1, 0.05))
				);

		InputMethodDescription methodBruteForce = new InputMethodDescription(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolBruteForceEVCharging(0.1, 0.005))
				);

		InputMethodDescription methodTabuSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "500"), new Argument("numberOfNeighbors", "3") )),
				new ProblemToolDefinition(new ProblemToolTabuSearchEVChargingRandomMove(0.1, 0.05))
				);

		InputMethodDescription methodSimulatedAnnealing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002") )),
				new ProblemToolDefinition(new ProblemToolSimulatedAnnealingEVChargingRandomMove(0.1, 0.2))
				);

		InputMethodDescription methodDifferentialEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "10"), new Argument("crossRate", "1.0")) ),
				new ProblemToolDefinition(new ProblemToolDifferentialEvolutionEVCharging(0.1, 1))
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
		islandModelConf.setReplanPeriodMS(60000);
		islandModelConf.setIndividualBroadcastPeriodMS(20000);
		islandModelConf.importReadyToSendIndividualInserterClass(
				ReadyToSendIndividualsOnlyOneInserter.class);
		islandModelConf.importReceivedIndividualSelectorClass(
				ReceivedIndivRemoveOneSelector.class);
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsTwoQueuesModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsOneQueueModel.class);
		
		Job job = new Job();
		job.setJobID("evchAdvnn");
		job.setDescription("electric vehicle chrarging advnn");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemEVCharging("127.0.0.1", 8080));
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("evcharging" + File.separator + "config_advnn.json"))));
		job.setMethods(methods);
		job.setPlanner(new PlannerDummy());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(150));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));

		return job;
	}

	public static Job test02() throws IOException {

		Job job = test01();
		job.setJobID("evchAdvnnhh");
		job.setDescription("electric vehicle chrarging advnnhh");
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("evcharging" + File.separator + "config_advnn_hh.json"))));
		
		return job;
	}
	
	public static Job test03() throws IOException {

		Job job = test01();
		job.setJobID("evchEsn");
		job.setDescription("electric vehicle chrarging esn");
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("evcharging" + File.separator + "config_esn.json"))));
		
		return job;
	}

	public static Job test04() throws IOException {

		Job job = test01();
		job.setJobID("evchEsnhh");
		job.setDescription("electric vehicle chrarging esnhh");
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("evcharging" + File.separator + "config_esn_hh.json"))));
		
		return job;
	}

}
