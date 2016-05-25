package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.distributedea.input.PostProcessing;
import org.distributedea.input.Tool;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;
public class PostProcBoxplot extends PostProcessing {

	String NL = "\n";
	
	@Override
	public void run(Batch batch) {
		
		List<JobWrapper> jobWrps = batch.getJobWrappers();
		String batchID = batch.getBatchID();
		String description = batch.getDescription();
		
		List<String> legends = new ArrayList<>();
		List<String> matrix = new ArrayList<>();
		
		for (JobWrapper jobWrpI : jobWrps) {
				
			String jobIDI = jobWrpI.getJobID();
			legends.add(jobIDI);
			
			String columbI = processJobWrapper(jobWrpI, batchID);
			matrix.add(columbI);
		}
		
		String legendStr = Tool.convertToMatlabLegend(legends);
		String matrixStr = Tool.convertToMatlabMatrix(matrix);
		
		
		String TITLE = batch.getDescription();
		String YLABEL = "hodnota fitnes v kilometrech";
		
		String OUTPUT_FILE = batch.getBatchID() + "BoxPlot";
		String OUTPUT_PATH = "matlab";
		
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

		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".m")  ){
		    out.println(matlabCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String bashSourceCode = "cd " + OUTPUT_PATH + ";" + NL + 
		"matlab -nodisplay -r " + OUTPUT_FILE;
		
		String bashScriptFileName = OUTPUT_PATH + File.separator + "run.sh";
		try(  PrintWriter out = new PrintWriter(bashScriptFileName)  ){
		    out.println(bashSourceCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Runtime rt = Runtime.getRuntime();
		try {
			Process pr0 = rt.exec("chmod +x " + bashScriptFileName);
			pr0.waitFor();
			Process pr1 = rt.exec("./" + bashScriptFileName);
			pr1.waitFor();
			Process pr2 = rt.exec("rm " + bashScriptFileName);
			pr2.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Export OK");
	}

	public String processJobWrapper(JobWrapper jobWrp, String batchID) {
		
		String jobID = jobWrp.getJobID();	
		int numberOfRuns = jobWrp.getNumberOfRuns();
		
		Map<JobID, Double> resultsOfJobsMap = Tool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		List<Double> resultsOfJobs = new ArrayList<>(resultsOfJobsMap.values());
		
		return Tool.convertToMatlamArray(resultsOfJobs);
		
	}
	
	public static void main(String [] args) {
		
		BatchHeteroComparingTSP batchCmp = new BatchHeteroComparingTSP(); 
		Batch batch = batchCmp.batch();
		
		PostProcBoxplot p = new PostProcBoxplot();
		p.run(batch);
	}
}
