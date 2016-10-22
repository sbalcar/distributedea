package org.distributedea.input.postprocessing.matlab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.batches.BatchTestTSP;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;

/**
 * Postprocessing creating boxplots for given {@link Batch}.  In the graph is one
 * boxplot for one {@link Job}. As one value takes final result of one {@link JobRun}.
 * One boxplot represents differences between runs of one {@link Job}.
 * @author stepan
 *
 */
public class PostProcBoxplot extends PostProcessingMatlab {
	
	@Override
	public void run(Batch batch) throws Exception {
		
		List<Job> jobWrps = batch.getJobs();
		String batchID = batch.getBatchID();
		String description = batch.getDescription();
		
		List<String> legends = new ArrayList<>();
		List<String> matrix = new ArrayList<>();
		
		for (Job jobI : jobWrps) {
				
			String jobIDI = jobI.getJobID();
			legends.add(jobIDI);
			
			String columbI = processJob(jobI, batchID);
			matrix.add(columbI);
		}
		
		String legendStr = MatlabTool.convertToMatlabLegend(legends);
		String matrixStr = MatlabTool.convertToMatlabMatrix(matrix);
		
		
		String TITLE = batch.getDescription();
		String YLABEL = "hodnota fitnes v kilometrech";
		
		String OUTPUT_FILE = batch.getBatchID() + "BoxPlot";
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(batchID);
		
		String matlabCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
		NL;
		
		matlabCode += 
		"M=" + matrixStr + NL +
		"Mtrans = M.'" + NL +
		"boxplot(Mtrans," + legendStr + ")" + NL +
		"title('" + description + "')" + NL;
		
		matlabCode +=
		"hold off" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
		"exit;";
		
		System.out.println(matlabCode);
		
		saveAndProcessMatlab(matlabCode, OUTPUT_PATH, OUTPUT_FILE);
	}
	


	private String processJob(Job job, String batchID) throws IOException {
		
		String jobID = job.getJobID();	
		int numberOfRuns = job.getNumberOfRuns();
		
		Map<JobID, Double> resultsOfJobsMap = FilesystemTool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		List<Double> resultsOfJobs = new ArrayList<>(resultsOfJobsMap.values());
		
		return MatlabTool.convertDoublesToMatlamArray(resultsOfJobs);
		
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchTestTSP();
		Batch batch = batchCmp.batch();
		
		PostProcBoxplot p = new PostProcBoxplot();
		p.run(batch);
	}
}
