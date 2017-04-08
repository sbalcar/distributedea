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
import org.distributedea.input.batches.binpacking.objects1000.BatchHeteroMethodsBPP1000;
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
public class PostProcCountsOfMeritsOfMethodTypes extends PostProcessingMatlab {
	
	private boolean legendContainsProblemTools;
	private boolean legendContainsArguments;
	
	/**
	 * Constructor
	 * @param legendContainsProblemTools
	 * @param legendContainsArguments
	 */
	public PostProcCountsOfMeritsOfMethodTypes(boolean legendContainsProblemTools,
			boolean legendContainsArguments) {
		this.legendContainsProblemTools = legendContainsProblemTools;
		this.legendContainsArguments = legendContainsArguments;
	}
	
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

		List<Long> improvementsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodType methodTypeI : methodHistories.exportMethodTypes()) {
			
			long numberOfTheBestI = methodHistories.exportNumberOfTheBestCreatedIndividuals(methodTypeI);
			improvementsList.add(numberOfTheBestI);
			
			String labelI = methodTypeI.exportString(
					legendContainsProblemTools, legendContainsArguments);
			labelsList.add(labelI);
		}
		
		String improvements = MatlabTool.convertLongsToMatlamArray(improvementsList);
		String labels = MatlabTool.createLabels(labelsList);
		labels = labels.replaceAll("ProblemTool", "");
		labels = labels.replaceAll("Agent\\\\_", "");
		
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "") +
				JOB_ID + "" + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());
		
		String matlabCode =
			"h = figure" + NL +
			"barh(" + improvements + ");" + NL +
			"labels = " + labels + ";" + NL +
			"set(gca,'YTickLabel',labels);" + NL +
			"title('Počty dosažených vylepšení metodami');" + NL +
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
		IInputBatch batchCmp = new BatchHeteroMethodsBPP1000();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcCountsOfMeritsOfMethodTypes(false, false);
		p.run(batch);
		System.out.println(p.exportXML());
	}
	
}
