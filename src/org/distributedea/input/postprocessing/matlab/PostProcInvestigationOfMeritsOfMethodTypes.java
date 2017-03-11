package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.MatlabTool;
import org.distributedea.input.postprocessing.PostProcessingMatlab;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methodtype.MethodType;

public class PostProcInvestigationOfMeritsOfMethodTypes extends PostProcessingMatlab {

	private boolean legendContainsProblemTools;
	private boolean legendContainsArguments;
	
	/**
	 * Constructor
	 * @param legendContainsProblemTools
	 * @param legendContainsArguments
	 */
	public PostProcInvestigationOfMeritsOfMethodTypes(boolean legendContainsProblemTools,
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
			long periodMS = jobI.getIslandModelConfiguration().getReplanPeriodMS();
			
			for (int runNumberI = 0; runNumberI < jobI.getNumberOfRuns();
					runNumberI++) {
				
				JobID jobID = new JobID(BATCH_ID, jobIDI, runNumberI);
				processJobRun(jobID, periodMS);
			}
		}
	}
	
	private void processJobRun(JobID jobID, long periodMS) throws Exception {
		
		String BATCH_ID = jobID.getBatchID();
		String JOB_ID = jobID.getJobID();
		
		String monitoringDirNameI = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDirI = new File(monitoringDirNameI);
		
		History history = History.importXML(monitoringDirI);
		int numberOfIterations = history.getRePlans().size();
		MethodHistories methodHistories = history.getMethodHistories();
		
		methodHistories.sortMethodInstancesByName();

		List<String> investigationsList = new ArrayList<>();
		List<String> labelsList = new ArrayList<>();
		
		for (MethodType methodTypeI : methodHistories.exportMethodTypes()) {
			
			List<Integer> improvementsListI = new ArrayList<>();
			for (int iterationNumberI = 1; iterationNumberI <= numberOfIterations; iterationNumberI++) {

				long numberOfTheBestI = methodHistories
						.exportNumberOfTheBestCreatedIndividuals(methodTypeI, iterationNumberI);
				improvementsListI.add((int) numberOfTheBestI);
			}
			
			String matlabArrayI =
					MatlabTool.convertIntegersToMatlamArray(improvementsListI);
			investigationsList.add(matlabArrayI);
						
			String labelI = methodTypeI.exportString(
					legendContainsProblemTools, legendContainsArguments);
			labelsList.add(labelI);
		}
		
		String labels = MatlabTool.createLabels(labelsList);
		labels = labels.replaceAll("ProblemTool", "");
		labels = labels.replaceAll("Agent\\\\_", "");
		
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "") +
				JOB_ID + "R" + jobID.getRunNumber();
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(jobID.getBatchID());

		String TITLE = "Průběh počtů dosažených vylepšení typy metod";
		String XLABEL = "čas v iteracích plánovače(1x iterace = " +
				periodMS /1000 + "x sekund)";
		String YLABEL = "Počet vylepšení doposavaď nalezeného řešení";
		
		String matlabSourceCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"xlabel('x: " + XLABEL + "', 'FontSize', 10);" + NL +
		"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
		NL;
		
		List<String> lineTypes = Arrays.asList("-", "--", ":", "-.");
		
		for (int i = 0; i < investigationsList.size(); i++) {
			
			String investigationI = investigationsList.get(i);
						
			String lineTypeI = lineTypes.get(i % lineTypes.size());
			
			matlabSourceCode +=
				"M = " + investigationI + ";" + NL +
				"plot(M,' "+ lineTypeI + "','LineWidth',3);" + NL +
				NL;
		}
		
		matlabSourceCode +=
		"labels = " + labels + ";" + NL +
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
	
}
