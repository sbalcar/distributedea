package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.input.PostProcessing;
import org.distributedea.input.batches.BatchHomoComparing;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;

public class PostProcComparing extends PostProcessing {
	
	private String NL = "\n";
	
	public void run(Batch batch) {	
		
		
		String TITLE = batch.getDescription();
		String XLABEL = "čas v sekundách";
		String YLABEL = "hodnota fitnes v kilometrech";
		
		String INPUT_PATH = "matlab";
		
		String OUTPUT_FILE = "graph";
		
		String matlabSourceCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "')" + NL +
		"xlabel('x: " + XLABEL + "', 'FontSize', 10);" + NL +
		"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
		NL;
		
		List<JobID> jobIDs = batch.exportJobIDs();
		List<String> lineTypes = Arrays.asList("-", "--", ":", "-.");
		
		for (int i = 0; i < jobIDs.size(); i++) {
			
			JobID jobIDI = jobIDs.get(i);
			
			String fileNameI = Configuration.getResultFile(jobIDI);
			String lineTypeI = lineTypes.get(i % lineTypes.size());
			
			matlabSourceCode +=
				"M = dlmread('" + "../" + fileNameI + "')" + NL +
				"plot(M,' "+ lineTypeI + "','LineWidth',3);" + NL +
				NL;
		}
		
		String legend = createLegend(batch.exportDescriptions());
		
		matlabSourceCode +=
		"legend(" + legend + ",'Location','best');" + NL +
		NL +
		"legend('show');" + NL +
		NL +
		"hold off" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
		"exit;";
		
	
		System.out.println(matlabSourceCode);
		
		try(  PrintWriter out = new PrintWriter(INPUT_PATH + File.separator + "filename.m")  ){
		    out.println(matlabSourceCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String bashSourceCode = "cd matlab;" + NL + 
		"matlab -nodisplay -r filename";
		
		String bashScriptFileName = INPUT_PATH + File.separator + "run.sh";
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
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Export OK");
	}

	protected String createLegend(List<String> descriptions) {
		
		String legend = "";
		for (int i = 0; i < descriptions.size(); i++) {
			
			legend += "'" + descriptions.get(i) + "'";
			
			if (i < descriptions.size() -1) {
				legend += ",";
			}
		}
		
		return legend;
	}
	
	public static void main(String [] args) {
		
		BatchHomoComparing batchCmp = new BatchHomoComparing(); 
		Batch batch = batchCmp.batch();
		
		PostProcComparing p = new PostProcComparing();
		p.run(batch);
	}
}
