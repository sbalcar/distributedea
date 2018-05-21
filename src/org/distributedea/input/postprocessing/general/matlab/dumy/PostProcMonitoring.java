package org.distributedea.input.postprocessing.general.matlab.dumy;

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
import org.distributedea.input.batches.tsp.BatchTestTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;

public class PostProcMonitoring extends PostProcessingMatlab {
	
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

		
		List<Long> valuesList = new ArrayList<>();
		List<Long> iterationsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodHistory methodI : methodHistories.getMethods()) {
			
			MethodInstanceDescription methodInstanceDescriptionI =
					methodI.getMethodInstanceDescription();
			
			valuesList.add(methodI.exportNumberOfTheBestCreatedIndividuals());
			
			iterationsList.add(methodI.exportNumberOfIteration());
			
			labelsList.add(methodInstanceDescriptionI.exportInstanceName());
		}

		String values = MatlabTool.convertLongsToMatlamArray(valuesList);
		String iterations = MatlabTool.convertLongsToMatlamArray(iterationsList);
		String descriptions = MatlabTool.createLabels(labelsList);
		
		
		String OUTPUT_FILE = "pie" + jobID.getJobID() + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());
		
		
		String matlabCode =
			"h = figure" + NL +
			"ax1 = subplot(3,1,1);" + NL +
			"pie(" + values + ");" + NL +
			"title(ax1,'Přínos metod pro celkový výpočet');" + NL +
			"ax2 = subplot(3,1,2)" + NL +
			"pie(" + iterations + ");" + NL +
			"title(ax2,'Časová kvanta, která dostaly k dispozici');" + NL +
			"labels = " + descriptions + ";" + NL +
			"legend(labels,'Location','southoutside','Orientation','vertical');" + NL +
			"set(gca,'Visible','off');" + NL +
			"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
			"exit;";
		
		System.out.println(matlabCode);

		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}
	
	
	public static void main(String [] args) throws Exception {
		
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcMonitoring();
		p.run(batch);
	}
}
