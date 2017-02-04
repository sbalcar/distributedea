package org.distributedea.input.batches.tsp.cities1083;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.method.Methods;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

public class BatchHomoMethodsTSP1083 implements IInputBatch {

	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("homoMethodsTSP1083");
		batch.setDescription("Porovnání homogeních modelů : TSP1083");
		
		Methods methods0 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));

		Job job0 = InputTSP.test05();
		job0.setJobID("homoHillclimbing");
		job0.setDescription("Homo-HillClimbing");
		job0.setMethods(methods0);

		
		Methods methods1 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments())),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		Job job1 = InputTSP.test05();
		job1.setJobID("homoRandomsearch");
		job1.setDescription("Homo-RandomSearch");
		job1.setMethods(methods1);

		
		Methods methods2 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) ))),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));

		Job jobW2 = InputTSP.test05();
		jobW2.setJobID("homoEvolution");
		jobW2.setDescription("Homo-Evolution");
		jobW2.setMethods(methods2);

		
		Methods methods3 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments())),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));

		Job job3 = InputTSP.test05();
		job3.setJobID("homoBruteforce");
		job3.setDescription("Homo-BruteForce");
		job3.setMethods(methods3);
		
		
		Methods methods4 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		Job job4 = InputTSP.test05();
		job4.setJobID("homoTabusearch");
		job4.setDescription("Homo-TabuSearch");
		job4.setMethods(methods4);
		
		
		Methods methods5 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002")) )),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		Job job5 = InputTSP.test05();
		job5.setJobID("homoSimulatedannealing");
		job5.setDescription("Homo-SimulatedAnnealing");
		job5.setMethods(methods5);

		
		Methods methods6 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")) )),
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));

		Job job6 = InputTSP.test05();
		job6.setJobID("homoDifferentialevolution");
		job6.setDescription("Homo-DifferentialEvolution");
		job6.setMethods(methods6);
		
		batch.addJobWrapper(job0);
		batch.addJobWrapper(job1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(job3);
		batch.addJobWrapper(job4);
		batch.addJobWrapper(job5);
		batch.addJobWrapper(job6);
		
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcJobRunsResultTable();
		PostProcessing psLat1 = new PostProcBatchDiffTable();
		PostProcessing psLat2 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		batch.addPostProcessings(psLat2);
		
		return batch;
	}
	
}
