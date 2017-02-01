package org.distributedea.input.postprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class PostProcessingMatlab extends PostProcessing {

	protected String NL = "\n";
	
	
	public void saveAndProcessMatlab(String matlabCode, String OUTPUT_PATH, String OUTPUT_FILE) {
		
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
		
		executeMatlabScript(bashScriptFileName);
	
	}
	
	private void executeMatlabScript(String bashScriptFileName) {
	
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
}
