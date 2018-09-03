
package org.distributedea.input.batches.co.f2;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputContOpt;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJob;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.input.postprocessing.general.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;

public class BatchSingleMethodsCOf2 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {

		Batch batch = new Batch();
		batch.setBatchID("singleMethodsCOf2");
		batch.setDescription("Porovnání samostatných metod : COf2");
		
		Job jobI = InputContOpt.test04_();
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		jobI.getIslandModelConfiguration().setIndividualDistribution(false);
		
		InputMethodDescription methodHillClimbing = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_HillClimbing.class);
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.setMethods(new Methods(methodHillClimbing));

		
		InputMethodDescription methodRandomSearch = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_RandomSearch.class);

		Job job1 = jobI.deepClone();
		job1.setJobID("singleRandomsearch");
		job1.setDescription("Single-RandomSearch");
		job1.setMethods(new Methods(methodRandomSearch));

		
		InputMethodDescription methodEvolution = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_Evolution.class);
		
		Job job2 = jobI.deepClone();
		job2.setJobID("singleEvolution");
		job2.setDescription("Single-Evolution");
		job2.setMethods(new Methods(methodEvolution));
		

		InputMethodDescription methodBruteForce = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_BruteForce.class);
		
		Job job3 = jobI.deepClone();
		job3.setJobID("singleBruteforce");
		job3.setDescription("Single-BruteForce");
		job3.setMethods(new Methods(methodBruteForce));

		
		InputMethodDescription methodTabuSearch = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_TabuSearch.class);
		
		Job job4 = jobI.deepClone();
		job4.setJobID("singleTabusearch");
		job4.setDescription("Single-TabuSearch");
		job4.setMethods(new Methods(methodTabuSearch));

		
		InputMethodDescription methodSimulatedAnnealing = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_SimulatedAnnealing.class);
		
		Job job5 = jobI.deepClone();
		job5.setJobID("singleSimulatedannealing");
		job5.setDescription("Single-SimulatedAnnealing");
		job5.setMethods(new Methods(methodSimulatedAnnealing));
		
		
		InputMethodDescription methodDifferentialEvolution = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_DifferentialEvolution.class);
		
		Job job6 = jobI.deepClone();
		job6.setJobID("singleDifferentialevolution");
		job6.setDescription("Single-DifferentialEvolution");
		job6.setMethods(new Methods(methodDifferentialEvolution));
		
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
