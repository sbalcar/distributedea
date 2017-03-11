package org.distributedea.input.batches.co.f2;

import java.io.IOException;

import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerFollowNaiveAskingForBestResult;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputContOpt;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcAllottedTimeOfMethodTypes;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMeritsOfMethodTypes;

public class BatchHeteroMethodsCOf2 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {

		Batch batch = new Batch();
		batch.setBatchID("heteroComparingCOf2");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : Cont.Opt. ");
		
		Job job0 = InputContOpt.test03();
		job0.setJobID("onlyInit");
		job0.setDescription("Only Initialization");
		job0.setPlanner(new PlannerInitialisationOneMethodPerCore());
		
		Job job1 = InputContOpt.test03();
		job1.setJobID("followBestResult");
		job1.setDescription("Follow Best Result");
		job1.setPlanner(new PlannerFollowNaiveAskingForBestResult());
		
		Job job2 = InputContOpt.test03();
		job2.setJobID("followupHelpers");
		job2.setDescription("Follow up Helpers");
		job2.setPlanner(new PlannerTheBestHelper());
		
		Job job7 = InputContOpt.test03();
		job7.setJobID("theGreatestQuantityOfMaterial");
		job7.setDescription("The Greatest Quantity Of Genetic Material");
		job7.setPlanner(new PlannerTheGreatestQuantityOfMaterial());

		
		batch.addJob(job0);
		batch.addJob(job1);
		batch.addJob(job2);
		batch.addJob(job7);
		
		
		PostProcessing psLat0 = new PostProcJobTable();
		PostProcessing psLat1 = new PostProcJobRunsResultTable(10);
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		
		String YLABEL = "fitness jako funkční hodnota vstupní funkce";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL);
		
		String YLABEL1 = "fitness jako funkční hodnota vstupní funkce";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);

		PostProcessing psMat2 = new PostProcAllottedTimeOfMethodTypes(false, false);

		PostProcessing psMat3 = new PostProcInvestigationOfMeritsOfMethodTypes(false, false);
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		batch.addPostProcessings(psMat2);
		batch.addPostProcessings(psMat3);
		
		return batch;
	}
	
}
