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
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
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
	
	private String yLabel;
	
	public PostProcBoxplot(String yLabel) {
		this.yLabel = yLabel; 
	}
	
	@Override
	public void run(Batch batch) throws Exception {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		String description = batch.getDescription();
		
		List<String> legends = new ArrayList<>();
		List<String> matrix = new ArrayList<>();
		
		for (Job jobI : batch.getJobs()) {
				
			String jobDescrI = jobI.getDescription();
			legends.add(jobDescrI);
			
			String columbI = processJob(jobI, BATCH_ID);
			matrix.add(columbI);
		}
		
		String legendStr = MatlabTool.convertToMatlabLegend(legends);
		String matrixStr = MatlabTool.convertToMatlabMatrix(matrix);
		
		
		String TITLE = batch.getDescription();
		
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		
		String matlabCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"ylabel('y: " + yLabel + "', 'FontSize', 10);" + NL +
		NL;
		
		matlabCode += 
		"M=" + matrixStr + NL +
		"Mtrans = M.'" + NL +
		"boxplot(Mtrans," + legendStr + ")" + NL +
		"title('" + description + "')" + NL +
		"set(gca,'XTickLabelRotation', -40);" + NL;
		
		matlabCode +=
		"hold off" + NL +
		"h.PaperPositionMode = 'auto'" + NL +
		"fig_pos = h.PaperPosition;" + NL +
		"h.PaperSize = [fig_pos(3) fig_pos(4)];" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','bmp');" + NL +
		"print(h, '-fillpage', '" + OUTPUT_FILE + "','-dpdf');" + NL +
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
//		IInputBatch batchCmp = new BatchSingleMethodsTSP1083();
		IInputBatch batchCmp = new BatchHomoMethodsTSP1083();
		Batch batch = batchCmp.batch();
		
		String YLABEL = "hodnota fitness v kilometrech";
		PostProcBoxplot p = new PostProcBoxplot(YLABEL);
		p.run(batch);
	}
}
