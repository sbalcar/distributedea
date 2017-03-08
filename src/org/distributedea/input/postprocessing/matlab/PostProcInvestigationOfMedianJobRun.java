package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.cities1083.BatchSingleMethodsTSP1083;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.IProblem;

/**
 * Shows results of all {@link Job}s of given {@link Batch} as course of function
 * depending on {@link Iteration}s. As a result of {@link Job} is selected
 * number of {@link JobRun} with median result.
 * @author stepan
 *
 */
public class PostProcInvestigationOfMedianJobRun extends PostProcessingMatlab {
	
	private String yLabel;
	
	public PostProcInvestigationOfMedianJobRun(String yLabel) {
		this.yLabel = yLabel;
	}
	
	public void run(Batch batch) throws Exception {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		String TITLE = batch.getDescription();
		String XLABEL1 = "čas v iteracích plánovače(1x iterace = " +
				batch.exportIslandModelConfiguration().getReplanPeriodMS() /1000 +
				"x sekund)";
		
		
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(batch.getBatchID());
		
		String matlabSourceCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"xlabel('x: " + XLABEL1 + "', 'FontSize', 10);" + NL +
		"ylabel('y: " + yLabel + "', 'FontSize', 10);" + NL +
		NL;
		
		List<String> lineTypes = Arrays.asList("-", "--", ":", "-.");
		
		String batchID = batch.getBatchID();
		List<Job> jobs = batch.getJobs();
		for (int i = 0; i < jobs.size(); i++) {
			
			Job jobI = jobs.get(i);
			
			JobID jobIDI = processJobIDOfTheBestResult(
					batchID, jobI.getJobID(), jobI.getNumberOfRuns(),
					jobI.getProblem());
			
			String fileNameI = FileNames.getResultFile(jobIDI);
			List<Double> valuesI = FilesystemTool.readVectorFromFile(
					new File(fileNameI));
			String matlabArrayI =
					MatlabTool.convertDoublesToMatlamArray(valuesI);
			
			String lineTypeI = lineTypes.get(i % lineTypes.size());
			
			matlabSourceCode +=
				//"M = dlmread('" + "../../../" + fileNameI + "')" + NL +
				"M = " + matlabArrayI + ";" + NL +
				"plot(M,' "+ lineTypeI + "','LineWidth',3);" + NL +
				NL;
		}
		
		String descriptions =
				MatlabTool.createLabels(batch.exportDescriptions());
		
		matlabSourceCode +=
		"labels = " + descriptions + ";" + NL +
		"legend(labels,'Location','best');" + NL +
		NL +
		"legend('show');" + NL +
		NL +
		"hold off" + NL +
		"h.PaperPositionMode = 'auto'" + NL +
		"fig_pos = h.PaperPosition;" + NL +
		"h.PaperSize = [fig_pos(3) fig_pos(4)];" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','bmp');" + NL +
		"print(h, '-fillpage', '" + OUTPUT_FILE + "','-dpdf');" + NL +
		"exit;";
		
	
		System.out.println(matlabSourceCode);
		
		saveAndProcessMatlab(matlabSourceCode, OUTPUT_PATH, OUTPUT_FILE);
	}
	

	@SuppressWarnings("unused")
	private JobID processJobIDOfTheMedianResult(String batchID, String jobID,
			int numberOfRuns, IProblem problem) throws IOException {

		Map<JobID, Double> resultsOfJobsMap =
				FilesystemTool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		List<Double> list = new ArrayList<Double>(resultsOfJobsMap.values());
		Collections.sort(list);
		
		Double medianValue = list.get(list.size() / 2);
		
		
		for (Map.Entry<JobID, Double> e : resultsOfJobsMap.entrySet()) {
			if (e.getValue() == medianValue) {
				return e.getKey();
			}
		}
		
		return null;
	}
	
	private JobID processJobIDOfTheBestResult(String batchID, String jobID,
			int numberOfRuns, IProblem problem) throws IOException {
		
		Map<JobID, Double> resultsOfJobsMap =
				FilesystemTool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		return getBestJobID(resultsOfJobsMap, problem);
	}
	
	protected JobID getBestJobID(Map<JobID, Double> resultsOfJobsMap,
			IProblem problem) {
		
		if (resultsOfJobsMap == null || resultsOfJobsMap.isEmpty()) {
			return null;
		}
		
		List<JobID> jobIDs = new ArrayList<>(resultsOfJobsMap.keySet());
		List<Double> fitnessValues = new ArrayList<>(resultsOfJobsMap.values());
		
		JobID bestJobID = jobIDs.get(0);
		Double bestFitnessValue = fitnessValues.get(0);
		
		for (Map.Entry<JobID, Double> entry : resultsOfJobsMap.entrySet()) {
			
			JobID jobIDI = entry.getKey();
			Double fitnessValueI = entry.getValue();
		    
			boolean isBetter = FitnessTool.isFistFitnessBetterThanSecond(
					fitnessValueI, bestFitnessValue, problem);
			
			if (isBetter) {
				bestJobID = jobIDI;
				bestFitnessValue = fitnessValueI;
			}
		}
		
		return bestJobID;
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchSingleMethodsTSP1083();
		Batch batch = batchCmp.batch();
		
		String YLABEL = "hodnota fitness v kilometrech";
		PostProcessing p = new PostProcInvestigationOfMedianJobRun(YLABEL);
		p.run(batch);
	}
}
