package org.distributedea.input.batches.tsp.cities2036;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJob;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.input.postprocessing.general.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;

public class BatchHomoMethodsTSP2036 implements IInputBatch {

	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("homoMethodsTSP2036");
		batch.setDescription("Porovnání homogenních modelů : TSP2036");
		
		Job jobI = InputTSP.test06();
		jobI.getIslandModelConfiguration().setIndividualDistribution(true);
		
		InputMethodDescription methodHillClimbing = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_HillClimbing.class);

		Job job0 = jobI.deepClone();
		job0.setJobID("homoHillclimbing");
		job0.setDescription("Homo-HillClimbing");
		job0.setMethods(new Methods(methodHillClimbing));

		
		InputMethodDescription methodRandomSearch = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_RandomSearch.class);
		
		Job job1 = jobI.deepClone();
		job1.setJobID("homoRandomsearch");
		job1.setDescription("Homo-RandomSearch");
		job1.setMethods(new Methods(methodRandomSearch));

		
		InputMethodDescription methodEvolution = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_Evolution.class);

		Job job2 = jobI.deepClone();
		job2.setJobID("homoEvolution");
		job2.setDescription("Homo-Evolution");
		job2.setMethods(new Methods(methodEvolution));

		
		InputMethodDescription methodBruteForce = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_BruteForce.class);

		Job job3 = jobI.deepClone();
		job3.setJobID("homoBruteforce");
		job3.setDescription("Homo-BruteForce");
		job3.setMethods(new Methods(methodBruteForce));
		
		
		InputMethodDescription methodTabuSearch = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_TabuSearch.class);
		
		Job job4 = jobI.deepClone();
		job4.setJobID("homoTabusearch");
		job4.setDescription("Homo-TabuSearch");
		job4.setMethods(new Methods(methodTabuSearch));
		
		
		InputMethodDescription methodSimulatedAnnealing = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_SimulatedAnnealing.class);
		
		Job job5 = jobI.deepClone();
		job5.setJobID("homoSimulatedannealing");
		job5.setDescription("Homo-SimulatedAnnealing");
		job5.setMethods(new Methods(methodSimulatedAnnealing));

		
		InputMethodDescription methodDifferentialEvolution = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_DifferentialEvolution.class);
		
		Job job6 = jobI.deepClone();
		job6.setJobID("homoDifferentialevolution");
		job6.setDescription("Homo-DifferentialEvolution");
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
		
		
		String YLABEL0 = "hodnota fitness v kilometrech";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String YLABEL1 = "hodnota fitness v kilometrech";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		return batch;
	}
	
}
