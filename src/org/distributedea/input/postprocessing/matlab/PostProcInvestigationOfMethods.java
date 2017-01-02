package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistory;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodStatisticResultWrapper;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.BatchTestTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;

/**
 * Creates for each {@link JobRun} graph of {@link MethodInstanceDescription}
 * which was running through the {@link Iteration}s. 
 * @author stepan
 *
 */
public class PostProcInvestigationOfMethods extends PostProcessingMatlab {
	
	@Override
	public void run(Batch batch) throws Exception {
		
		String batchID = batch.getBatchID();
		
		for (Job jobI : batch.getJobs()) {
			
			String jobIDI = jobI.getJobID();
			
			for (int runNumberI = 0; runNumberI < jobI.getNumberOfRuns();
					runNumberI++) {
				
				JobID jobID = new JobID(batchID, jobIDI, runNumberI);
				processJobRun(batch, jobID);
			}
		}
	}
	
	private void processJobRun(Batch batch, JobID jobID) throws Exception {
		
		String historyDir = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		History history = History.importXML(new File(historyDir));
		
		MethodHistories methodHistories = history.getMethodHistories();
		methodHistories.sortMethodInstancesByName();
		
		String TITLE = "Průběh jednotlivých metod"; // batch.getDescription();
		String YLABEL = "jádra systému a jejich vytížení instancemi metod";
		
		String OUTPUT_FILE = "investigarionOfMethods" + jobID.getJobID() + "" + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(batch.getBatchID());
		

		int numOfIter = (int) history.getMethodHistories().exportNumberOfLastIteration();
		
		String matlabCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
		NL;
		

		List<String> labelsList = new ArrayList<>();
		List<String> hsList = new ArrayList<>();
		for (int i = 0; i < methodHistories.getMethods().size(); i++) {
		
			MethodHistory methodHistoryI = methodHistories.getMethods().get(i);
			
			MethodInstanceDescription methodInstanceI =
					methodHistoryI.getMethodInstanceDescription();
			List<MethodStatisticResultWrapper> statisticsI =
					methodHistoryI.getStatistics();
						
			List<Double> valuesI =
					getMethodValues(methodInstanceI, statisticsI, numOfIter, i+1);
			String valuesStringI =
					MatlabTool.convertDoublesToMatlamArray(valuesI);
			
			matlabCode += "h" + i + " = " +
			"plot([1:" + (numOfIter+1) + "], " + valuesStringI + ", '-o');" + NL;
			
			labelsList.add(methodInstanceI.exportInstanceName());
			
			hsList.add("h" + i);
		}

		Collections.reverse(labelsList);
		String descriptions = MatlabTool.createLabels(labelsList);
		
		Collections.reverse(hsList);
		String hs = MatlabTool.convertStringsToMatlamArray(hsList);
		
		matlabCode +=
		"labels = " + descriptions + ";" + NL +
		"legend(" + hs + ", labels,'Location','southoutside','Orientation','vertical');" + NL +
		"x1=xlim;" + NL +
		"y1=ylim;" + NL +
		"xlim([x1(1)-1, x1(2)+1]);" + NL +
		"ylim([y1(1)-1, y1(2)+1]);" + NL +
		"hold off" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
		"exit;";
		
		System.out.println(matlabCode);
		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}

	public List<Double> getMethodValues(MethodInstanceDescription methodInstance,
			List<MethodStatisticResultWrapper> statistics, int numberOfIterations, double value) {

		List<Double> values = new ArrayList<>();
		for (int i = 0; i <= numberOfIterations; i++) {
			values.add(Double.NaN);
		}

		for (MethodStatisticResultWrapper statRsltI : statistics) {
			
			Iteration iterationI = statRsltI.getIteration();
			int iterationNumber = (int) iterationI.getIterationNumber();
			
			values.set(iterationNumber, value);
		}

		return values;
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcInvestigationOfMethods();
		p.run(batch);
	}
	
}
