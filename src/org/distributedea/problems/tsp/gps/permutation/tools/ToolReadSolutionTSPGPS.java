package org.distributedea.problems.tsp.gps.permutation.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolReadSolutionTSPGPS {

	public static IndividualPermutation readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		List<Integer> permutation = new ArrayList<Integer>();
		
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(fileOfSolution.getAbsolutePath()));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("NAME") ||
					sCurrentLine.startsWith("COMMENT") ||
					sCurrentLine.startsWith("TYPE") ||
					sCurrentLine.startsWith("DIMENSION") ||
					sCurrentLine.startsWith("TOUR_SECTION") ||
					sCurrentLine.startsWith("EOF") ) {
					
					logger.log(Level.INFO, sCurrentLine);

				} else {
					String delims = "[ ]+";
					String[] tokens = sCurrentLine.split(delims);
					
					int number = Integer.parseInt(tokens[0]);
					
					permutation.add(number);
				}
				
			}
 
		} catch (IOException exception) {
			logger.logThrowable("Problem with reading " + fileOfSolution.getName() + " file", exception);
			return null;
			
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				logger.logThrowable("Problem with closing the file: " + fileOfSolution.getName(), ex);
			}
		}
		
		//remove -1
		permutation.remove(permutation.size() -1);
				
		return new IndividualPermutation(permutation);
	}

}
