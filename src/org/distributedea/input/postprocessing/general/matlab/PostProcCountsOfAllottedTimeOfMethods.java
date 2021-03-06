package org.distributedea.input.postprocessing.general.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistory;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.tests.batches.BatchTestTSP;

/**
 * PostProcessing compares numbers of {@link Iteration}s which
 * computing agents got to computation. Creates one graph for each {@link JobRun}.
 * @author stepan
 *
 */
public class PostProcCountsOfAllottedTimeOfMethods extends PostProcessingMatlab {
	
	@Override
	public void run(Batch batch) throws Exception {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		
		for (Job jobI : batch.getJobs()) {
			
			String jobIDI = jobI.getJobID();
			
			for (int runNumberI = 0; runNumberI < jobI.getNumberOfRuns();
					runNumberI++) {
				
				JobID jobID = new JobID(BATCH_ID, jobIDI, runNumberI);
				processJobRun(jobID);
			}
		}
	}
	
	private void processJobRun(JobID jobID) throws Exception {
		
		String BATCH_ID = jobID.getBatchID();
		String JOB_ID = jobID.getJobID();
		
		
		String monitoringDirNameI = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDirI = new File(monitoringDirNameI);
		
		History history = History.importXML(monitoringDirI);
		MethodHistories methodHistories = history.getMethodHistories();
		
		methodHistories.sortMethodInstancesByName();

		String YLABEL = "CPU jádro-iterace";
		
		List<Long> iterationsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodHistory methodI :
			methodHistories.getMethods()) {
			
			iterationsList.add(methodI.exportNumberOfIteration());
			
			MethodInstanceDescription methodInstanceDescriptionI =
					methodI.getMethodInstanceDescription();
			labelsList.add(methodInstanceDescriptionI.exportInstanceName());
		}

		String iterations = MatlabTool.convertLongsToMatlamArray(iterationsList);
		String labels = MatlabTool.createLabels(labelsList);
		labels = labels.replaceAll("ProblemTool", "");
		labels = labels.replaceAll("Agent\\\\_", "");
		
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "") +
				JOB_ID + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());
		
		
		@SuppressWarnings("unused")
		String matlabCode_ =
			"h = figure" + NL +
			"hold on;" + NL +
			"title('Časová kvanta, která dostala metody k dispozici');" + NL +
			"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
			"bar(" + iterations + ");" + NL +
			"labels = " + labels + ";" + NL +
			"set(gca,'XTickLabel',labels);" + NL +
			"set(gca,'XTickLabelRotation', -40);" + NL +
			"hold off;" + NL +
			"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
			"exit;";
		
		String matlabCode =
			"h = figure" + NL +
			"barh(" + iterations + ");" + NL +
			"labels = " + labels + ";" + NL +
			"set(gca,'YTickLabel',labels);" + NL +
			"title('Časová kvanta, která dostala metody k dispozici');" + NL +
			"h.PaperPositionMode = 'auto'" + NL +
			"fig_pos = h.PaperPosition;" + NL +
			"h.PaperSize = [fig_pos(3) fig_pos(4)];" + NL +
			"saveas(h, '" + OUTPUT_FILE + "','bmp');" + NL +
			"print(h, '-fillpage', '" + OUTPUT_FILE + "','-dpdf');" + NL +
			"exit;";
		System.out.println(matlabCode);

		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}

	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcCountsOfAllottedTimeOfMethods();
		p.run(batch);
	}
	
}
