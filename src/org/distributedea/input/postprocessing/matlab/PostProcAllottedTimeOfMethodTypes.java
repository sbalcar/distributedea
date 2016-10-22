package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.BatchTestTSP;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * PostProcessing compares numbers of {@link Iteration}s which
 * methods of {@link MethodType} got to computation. Creates one
 * graph for each {@link JobRun}.
 * @author stepan
 *
 */
public class PostProcAllottedTimeOfMethodTypes extends PostProcessingMatlab {
	
	@Override
	public void run(Batch batch) throws Exception {
		
		String batchID = batch.getBatchID();
		
		for (Job jobI : batch.getJobs()) {
			
			String jobIDI = jobI.getJobID();
			
			for (int runNumberI = 0; runNumberI < jobI.getNumberOfRuns();
					runNumberI++) {
				
				JobID jobID = new JobID(batchID, jobIDI, runNumberI);
				processJobRun(jobID);
			}
		}
	}
	
	private void processJobRun(JobID jobID) throws IOException {
		
		String monitoringDirNameI = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDirI = new File(monitoringDirNameI);
		
		History history = History.importXML(monitoringDirI);
		MethodHistories methodHistories = history.getMethodHistories();
		
		methodHistories.sortMethodInstancesByName();

		List<Long> iterationsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodType methodTypeI : methodHistories.exportMethodTypes()) {
			
			long iterationI = methodHistories.exportNumberOfIterationOf(methodTypeI);
			iterationsList.add(iterationI);
			
			labelsList.add(methodTypeI.exportString());
		}
		
		String iterations = MatlabTool.convertLongsToMatlamArray(iterationsList);
		String labels = MatlabTool.createLabels(labelsList);
		labels = labels.replaceAll("ProblemTool", "");
		labels = labels.replaceAll("Agent\\\\_", "");
		
		String OUTPUT_FILE = "allottedTime" + jobID.getJobID() + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());
		
		String matlabCode =
				"h = figure" + NL +
				"barh(" + iterations + ");" + NL +
				"labels = " + labels + ";" + NL +
				"set(gca,'YTickLabel',labels);" + NL +
				"title('Časová kvanta, která dostala metody k dispozici');" + NL +
				"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
				"exit;";
		System.out.println(matlabCode);

		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}

	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcAllottedTimeOfMethodTypes();
		p.run(batch);
	}
	
}
