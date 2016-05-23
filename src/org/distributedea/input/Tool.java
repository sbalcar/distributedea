package org.distributedea.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.Configuration;
import org.distributedea.ontology.job.JobID;

public class Tool {

	public static List<Double> readVectorFromFile(File file) {
		
		List<Double> vectors = new ArrayList<>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			return null;
		}
		
		try {
		    String line = br.readLine();

		    while (line != null && line != "") {
		    	
		    	double number = Double.valueOf(line);
		    	vectors.add(new Double(number));
		    	
		    	line = br.readLine();
		    }

		    
		} catch (IOException e) {
			return null;
		} finally {
		    try {
				br.close();
			} catch (IOException e) {
			}
		}
		
		return vectors;
	}
	

	
	public static Map<JobID, Double> getResultOfJobForAllRuns(String batchID, String jobID, int numberOfRuns) {
		
		Map<JobID, Double> results = new HashMap<>();
		
		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			JobID jobIDStructI = new JobID(batchID, jobID, runNumberI);
			Double lastValue = getResultOfJobRun(jobIDStructI);

			results.put(jobIDStructI, lastValue);
		}
		
		return results;
	}
	
	public static Double getResultOfJobRun(JobID JobIDStruct) {
		
		String fileName = Configuration.getResultFile(JobIDStruct);
		List<Double> vector = Tool.readVectorFromFile(new File(fileName));
		
		if (vector == null || fileName.isEmpty()) {
			return null;
		}
		
		return vector.get(vector.size() -1);
	}
	
	public static String convertToMatlamArray(List<Double> values) {
	
		List<String> valuesStr = new ArrayList<>();
		
		for (Double valueI : values) {
			
			valuesStr.add("" + valueI);
		}
		
		return "[" + convertToMatlab(valuesStr, ",") + "]";
	}

	public static String convertToMatlabMatrix(List<String> values) {

		return "[" + convertToMatlab(values, ";") + "]";
	}

	
	public static String convertToMatlabLegend(List<String> values) {
		
		return "{'" + convertToMatlab(values, "','") + "'}";
	}
	
	private static String convertToMatlab(List<String> values, String delimiter) {
		
		String matlab = "";
		for (int i = 0; i < values.size(); i++) {
			
			matlab += values.get(i); 
			
			if (i < values.size() -1) {
				matlab += delimiter;
			}
		}		
		return matlab;
	}
}
