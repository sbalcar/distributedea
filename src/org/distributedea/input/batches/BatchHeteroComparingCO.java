package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowBestResult;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerInitialization;
import org.distributedea.input.InputBatch;
import org.distributedea.input.PostProcessing;
import org.distributedea.input.batches.jobs.InputContOpt;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class BatchHeteroComparingCO extends InputBatch {

	@Override
	public Batch batch() {

		Batch batch = new Batch();
		batch.setBatchID("heteroComparingCO");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : Cont.Opt. ");
		
		JobWrapper jobW0 = InputContOpt.test03();
		jobW0.setJobID("onlyInit");
		jobW0.setDescription("Only Initialization");
		jobW0.setScheduler(new SchedulerInitialization());
		
		JobWrapper jobW1 = InputContOpt.test03();
		jobW1.setJobID("followBestResult");
		jobW1.setDescription("Follow Best Result");
		jobW1.setScheduler(new SchedulerFollowBestResult());
		
		JobWrapper jobW2 = InputContOpt.test03();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setScheduler(new SchedulerFollowupHelpers());
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		
		
		PostProcessing ps0 = new PostProcBoxplot();

		PostProcessing ps1 = new PostProcComparing();
		
		
		batch.addPostProcessings(ps0);
		batch.addPostProcessings(ps1);
		
		return batch;
	}

}
