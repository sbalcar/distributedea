package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerFollowNaiveAskingForBestResult;
import org.distributedea.agents.systemagents.centralmanager.planners.historybased.PlannerTheGreatestQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.historybased.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.jobs.InputContOpt;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;

public class BatchHeteroComparingCO implements IInputBatch {

	@Override
	public Batch batch() {

		Batch batch = new Batch();
		batch.setBatchID("heteroComparingCO");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : Cont.Opt. ");
		
		Job jobW0 = InputContOpt.test03();
		jobW0.setJobID("onlyInit");
		jobW0.setDescription("Only Initialization");
		jobW0.setPlanner(new PlannerInitialisationOneMethodPerCore());
		
		Job jobW1 = InputContOpt.test03();
		jobW1.setJobID("followBestResult");
		jobW1.setDescription("Follow Best Result");
		jobW1.setPlanner(new PlannerFollowNaiveAskingForBestResult());
		
		Job jobW2 = InputContOpt.test03();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setPlanner(new PlannerTheBestHelper());
		
		Job jobW7 = InputContOpt.test03();
		jobW7.setJobID("theGreatestQuantityOfMaterial");
		jobW7.setDescription("The Greatest Quantity Of Genetic Material");
		jobW7.setPlanner(new PlannerTheGreatestQuantityOfMaterial());

		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW7);
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcBatchDiffTable();
		PostProcessing psLat1 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		return batch;
	}
	
}
