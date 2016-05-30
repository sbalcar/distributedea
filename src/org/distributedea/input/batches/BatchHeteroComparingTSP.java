package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowBestResult;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowup3Helpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitializationState;
import org.distributedea.input.batches.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

public class BatchHeteroComparingTSP extends InputBatch {

	@Override
	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroComparingTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");
		
		Job jobW0 = InputTSP.test06();
		jobW0.setJobID("onlyInit");
		jobW0.setDescription("Only Initialization");
		SchedulerInitializationState state = SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE;
		jobW0.setScheduler(new SchedulerInitialization(state, true));
		
		Job jobW1 = InputTSP.test06();
		jobW1.setJobID("followBestResult");
		jobW1.setDescription("Follow Best Result");
		jobW1.setScheduler(new SchedulerFollowBestResult());
		
		Job jobW2 = InputTSP.test06();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setScheduler(new SchedulerFollowupHelpers());

		Job jobW3 = InputTSP.test06();
		jobW3.setJobID("followup3Helpers");
		jobW3.setDescription("Follow up three Helpers");
		jobW3.setScheduler(new SchedulerFollowup3Helpers());
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcComparing();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcBatchDiffTable();
		PostProcessing psLat1 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		return batch;
	}
	
}
