package org.distributedea.input.postprocessing.general.latex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.IProblem;

public class PostProcTableSuccessOfPlanners extends PostProcessing {

	@Override
	public void run(Batch batch) throws Exception {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");

		
		List<JobID> jobRunsFromQuartile3 = getQuartile3OfBestJobRuns(batch);
		
		Map<String, Integer> aggrJobRunsFromQuartile3 = 
				aggregationByJobIDString(jobRunsFromQuartile3);
		
		String string = convertToString(aggrJobRunsFromQuartile3, batch);
		System.out.println(string);
		
		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".lat")  ){
		    out.println(string);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private String convertToString(Map<String, Integer> aggrBestJobRuns,
			Batch batch) {

		List<Entry<String, Integer>> allJobIDsWithBestOccurs =
				new ArrayList<Entry<String, Integer>>();
		
		for (Job jobI : batch.getJobs()) {
			
			String jobIDI = jobI.getJobID();
			Integer occursI = aggrBestJobRuns.get(jobIDI);
		
			if (occursI == null) {
				occursI = 0;
			}
			
			allJobIDsWithBestOccurs.add(new AbstractMap
					.SimpleEntry<String, Integer>(jobIDI, occursI));
		}
		
		String NL = "\n";
		
		String result =
			"\\begin{table} [hh]" + NL +
			"\\centering" + NL +
			"\\begin{tabular}{ |l|r| }" + NL +
			"\\hline" + NL +
			"\\textbf{Joby podle planovacu} & pocet \\\\" + NL +
			"\\hline\\hline" + NL;

		for (Entry<String, Integer> entryI : allJobIDsWithBestOccurs) {
			
			result += " " + entryI.getKey() + " & " + entryI.getValue() +  " \\\\" + NL;
		}

		result +=
			"\\hline" + NL +
			"\\end{tabular}" + NL +
			"\\caption{Pocet behu jednotlivych jobu lepsich nez treti kvartil}" + NL +
			"\\label{tab:" + batch.getBatchID() + "}" + NL +
			"\\end{table}";
		
		return result;
	}
	
	private Map<String, Integer> aggregationByJobIDString(List<JobID> bestJobIDs) {
				
		Map<String, Integer> mapJobIDAndOccurs = new HashMap<>();
						
		for (JobID jobIDI : bestJobIDs) {
			
			Integer numberOfOccursI = mapJobIDAndOccurs.get(jobIDI.getJobID());
			
			Integer newNumberOfOccursI = 1;
			if (numberOfOccursI != null) {
				newNumberOfOccursI = new Integer(numberOfOccursI +1);
			}
			
			mapJobIDAndOccurs.put(jobIDI.getJobID(), newNumberOfOccursI);
		}
		
		return mapJobIDAndOccurs;
	}
	
	private List<JobID> getQuartile3OfBestJobRuns(Batch batch) throws IOException {
		
		String batchID = batch.getBatchID();
		IProblem problem = null;
		
		Map<JobID, Double> resultsOfJobIDs = new HashMap<>();
		
		for (Job jobI : batch.getJobs()) {
			
			String jobID = jobI.getJobID();
			int numberOfRuns = jobI.getNumberOfRuns();
			problem = jobI.getProblem();
			
			Map<JobID, Double> resultsOfJobI = FilesystemTool
					.getTheBestPartResultOfJobForAllRuns(batchID, jobID, numberOfRuns, problem);
			resultsOfJobIDs.putAll(resultsOfJobI);
		}

		List<Double> values = new ArrayList<Double>(resultsOfJobIDs.values());
		
		if (values.size() < 4) {
			return new ArrayList<>();
		}
		
		//Collections.sort(values);
		Collections.sort(values, new FitnessComparator(problem));
		
		int quartil3Index = values.size() / 4;
		double quartil3Result = values.get(quartil3Index);
		
		List<Double> betterThanQuartile3List = values.subList(0, quartil3Index);
		

		Set<Double> resultsBetterThanQuartile3 =
				new HashSet<Double>(betterThanQuartile3List);
		resultsBetterThanQuartile3.remove(quartil3Result);

		
		List<JobID> jobIDsOfResultsBetterThanQuartile3 = new ArrayList<>();
		for (Entry<JobID, Double> entryI : resultsOfJobIDs.entrySet()) {
			
			if (resultsBetterThanQuartile3.contains(entryI.getValue())) {
				 jobIDsOfResultsBetterThanQuartile3.add(entryI.getKey());
			}
		}		
		
		return jobIDsOfResultsBetterThanQuartile3;
	}
	
}


class FitnessComparator implements Comparator<Double> {
	
	private IProblem problem;
	
	public FitnessComparator(IProblem problem) {
		this.problem = problem;
	}
	
    @Override
    public int compare(Double fitness1, Double fitness2) {
    	
    	boolean isFistBetter = FitnessTool.isFistFitnessBetterThanSecond(
    			fitness1, fitness2, problem);
    	
    	if (isFistBetter) {
    		return -1;
    	}
 
    	
    	boolean isFistWorst = FitnessTool.isFistFitnessWorseThanSecond(
    			fitness1, fitness2, problem);
 
    	if (isFistWorst) {
    		return 1;
    	}

    	
   		return 0;
    }
}

