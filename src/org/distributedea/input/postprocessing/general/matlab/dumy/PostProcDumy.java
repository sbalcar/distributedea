package org.distributedea.input.postprocessing.general.matlab.dumy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


public class PostProcDumy {

	private String NL = "\n";
	
	public void run() {
		
		String TITLE = "Fitness TSP ostrovů v závislosti na čase";
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
		NL +
		"t=0:0.1:6.3;" + NL +
		"plot(t,cos(t),'LineWidth',3);" + NL +
		"plot(t,sin(t),'--','LineWidth',3);" + NL +
		"plot(t,sin(2*t),':','LineWidth',3);" + NL +
		NL +
		"legend('y','first derivative','second derivative');" + NL +
		NL +
		"legend(gca,'off');" + NL +
		"legend('show');" + NL +
		NL +
		"hold off" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
		"exit;";
		
		//M = dlmread('input.txt')
		System.out.println(matlabSourceCode);
		
		try(  PrintWriter out = new PrintWriter(INPUT_PATH + File.separator + "filename.m")  ){
		    out.println(matlabSourceCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String bashSourceCode = "cd matlab;" + NL + 
		"matlab -nodisplay -r filename";
		
		String bashScriptFileName = INPUT_PATH + File.separator +"run.sh";
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
	
	public static void main(String [] args) {
		
		PostProcDumy p = new PostProcDumy();
		p.run();
	}
}
