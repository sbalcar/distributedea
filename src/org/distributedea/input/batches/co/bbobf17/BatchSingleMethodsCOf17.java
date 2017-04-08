package org.distributedea.input.batches.co.bbobf17;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputContOpt;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcTableOfJobRunResults;
import org.distributedea.input.postprocessing.latex.PostProcTableOfJob;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.method.Methods;
import org.distributedea.problems.continuousoptimization.ProblemToolCORandomMove;

public class BatchSingleMethodsCOf17 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("singleMethodsCOf17");
		batch.setDescription("Porovnání samostatných metod : COf17");
		
		Job jobI = InputContOpt.test17();
		jobI.setJobID("0");
		jobI.setDescription("clone");
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Methods methods0 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10")) )),
				new ProblemTools(ProblemToolCORandomMove.class));
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.setMethods(methods0);

		
		Methods methods1 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments())),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job1 = jobI.deepClone();
		job1.setJobID("singleRandomsearch");
		job1.setDescription("Single-RandomSearch");
		job1.setMethods(methods1);

		
		Methods methods2 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) ))),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job2 = jobI.deepClone();
		job2.setJobID("singleEvolution");
		job2.setDescription("Single-Evolution");
		job2.setMethods(methods2);
		

		Methods methods3 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments())),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job3 = jobI.deepClone();
		job3.setJobID("singleBruteforce");
		job3.setDescription("Single-BruteForce");
		job3.setMethods(methods3);
		
		Methods methods4 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10")) )),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job4 = jobI.deepClone();
		job4.setJobID("singleTabusearch");
		job4.setDescription("Single-TabuSearch");
		job4.setMethods(methods4);

		
		Methods methods5 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002")) )),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job5 = jobI.deepClone();
		job5.setJobID("singleSimulatedannealing");
		job5.setDescription("Single-SimulatedAnnealing");
		job5.setMethods(methods5);
		

		Methods methods6 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")) )),
				new ProblemTools(ProblemToolCORandomMove.class));

		Job job6 = jobI.deepClone();
		job6.setJobID("singleDifferentialevolution");
		job6.setDescription("Single-DifferentialEvolution");
		job6.setMethods(methods6);
		
		batch.addJob(job0);
		batch.addJob(job1);
		batch.addJob(job2);
		batch.addJob(job3);
		batch.addJob(job4);
		batch.addJob(job5);
		batch.addJob(job6);

		
		PostProcessing psLat0 = new PostProcTableOfJob();
		PostProcessing psLat1 = new PostProcTableOfJobRunResults(10);
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);

		
		String YLABEL0 = "fitness jako funkční hodnota vstupní funkce";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String YLABEL1 = "fitness jako funkční hodnota vstupní funkce";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);

		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		return batch;
	}

}
