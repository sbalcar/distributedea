package org.distributedea.input.batches;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.distributedea.InputTSP;
import org.distributedea.agents.systemagents.centralmanager.InputJobQueue;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowBestResult;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerInitialization;
import org.distributedea.input.InputBatch;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class BatchHeteroComparing extends InputBatch {

	@Override
	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroComparingTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");
		
		JobWrapper jobW0 = InputTSP.test06();
		jobW0.setJobID("onlyInit");
		jobW0.setDescription("Only Initialization");
		jobW0.setScheduler(new SchedulerInitialization());
		
		JobWrapper jobW1 = InputTSP.test06();
		jobW1.setJobID("followBestResult");
		jobW1.setDescription("Follow Best Result");
		jobW1.setScheduler(new SchedulerFollowBestResult());
		
		JobWrapper jobW2 = InputTSP.test06();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setScheduler(new SchedulerFollowupHelpers());
		
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		
		
		PostProcBoxplot ps0 = new PostProcBoxplot();

		PostProcComparing ps1 = new PostProcComparing();
		
		
		batch.addPostProcessings(ps0);
		batch.addPostProcessings(ps1);
		
		return batch;
	}

	public static void main(String [] args) throws FileNotFoundException, JAXBException {
		
		BatchHeteroComparing batchCmp = new BatchHeteroComparing(); 
		Batch batch = batchCmp.batch();
		
		//InputJobQueue.cleanJobsInQueueDirectory();
		InputJobQueue.exportBatchToJobQueueDirectory(batch);
	}
}
