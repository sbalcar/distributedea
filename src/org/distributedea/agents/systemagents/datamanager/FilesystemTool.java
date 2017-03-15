package org.distributedea.agents.systemagents.datamanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.IProblem;

public class FilesystemTool {

	public static List<Double> readVectorFromFile(File file) throws IOException {
		
		if (file == null || ! file.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		List<Double> vectors = new ArrayList<>();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		try {
		    String line = br.readLine();

		    while (line != null && line != "") {
		    	
		    	double number = Double.valueOf(line);
		    	vectors.add(number);
		    	
		    	line = br.readLine();
		    }
		    
		} catch (IOException e) {
			throw e;
		} finally {
		    br.close();
		}
		
		return vectors;
	}
	
	public static Map<JobID, Double> getTheBestPartResultOfJobForAllRuns(String batchID, String jobID,
			int numberOfRuns, IProblem problem) throws IOException {
		
		Map<JobID, Double> results = new HashMap<>();
		
		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			JobID jobIDStructI = new JobID(batchID, jobID, runNumberI);
			Double lastValue = getTheBestPartResultOfJobRun(jobIDStructI, problem);

			results.put(jobIDStructI, lastValue);
		}
		
		return results;
	}
	
	public static Double getLastPartResultOfJobRun_(JobID JobID) throws IOException {
		
		if (JobID == null || ! JobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		
		String fileName = FileNames.getResultFile(JobID);
		List<Double> listOfFitnessValues = FilesystemTool.readVectorFromFile(new File(fileName));
		
		if (listOfFitnessValues == null || listOfFitnessValues.isEmpty()) {
			return null;
		}
		
		return listOfFitnessValues.get(listOfFitnessValues.size() -1);
	}
	
	public static Double getTheBestPartResultOfJobRun(JobID JobID, IProblem problem) throws IOException {
		
		if (JobID == null || ! JobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		
		String fileName = FileNames.getResultFile(JobID);
		List<Double> listOfFitnessValues = FilesystemTool.readVectorFromFile(new File(fileName));

		if (listOfFitnessValues == null || listOfFitnessValues.isEmpty()) {
			return null;
		}
		
		return FitnessTool.getTheBestFitnessValueFrom(listOfFitnessValues, problem);
	}

}
