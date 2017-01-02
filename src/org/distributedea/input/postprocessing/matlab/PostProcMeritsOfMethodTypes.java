package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.BatchTestTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * PostProcessing shows for each {@link JobRun} merits on the result
 * of {@link MethodType}s.
 * @author stepan
 *
 */
public class PostProcMeritsOfMethodTypes extends PostProcessingMatlab {
	
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
	
	private void processJobRun(JobID jobID) throws Exception {
		
		String monitoringDirNameI = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDirI = new File(monitoringDirNameI);
		
		History history = History.importXML(monitoringDirI);
		MethodHistories methodHistories = history.getMethodHistories();
		
		methodHistories.sortMethodInstancesByName();

		List<Long> improvementsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodType methodTypeI : methodHistories.exportMethodTypes()) {
			
			long numberOfTheBestI = methodHistories.exportNumberOfTheBestCreatedIndividuals(methodTypeI);
			improvementsList.add(numberOfTheBestI);
			
			labelsList.add(methodTypeI.exportString());
		}
		
		String improvements = MatlabTool.convertLongsToMatlamArray(improvementsList);
		String labels = MatlabTool.createLabels(labelsList);
		labels = labels.replaceAll("ProblemTool", "");
		labels = labels.replaceAll("Agent\\\\_", "");
		
		String OUTPUT_FILE = "improvements" + jobID.getJobID() + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());
		
		String matlabCode =
				"h = figure" + NL +
				"barh(" + improvements + ");" + NL +
				"labels = " + labels + ";" + NL +
				"set(gca,'YTickLabel',labels);" + NL +
				"title('Počty dosažených vylepšení metodami');" + NL +
				"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
				"exit;";
		System.out.println(matlabCode);

		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}

	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcMeritsOfMethodTypes();
		p.run(batch);
	}
	
}
