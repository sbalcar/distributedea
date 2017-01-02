package org.distributedea.problems.binpacking.permutation.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolReadSolutionBinPacking {

	public static IndividualPermutation readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		List<Integer> objectsOfBinPack = new ArrayList<>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(fileOfSolution));
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
		
				int valueI = Integer.parseInt(sCurrentLine);
				objectsOfBinPack.add(valueI);
			}
			
		} catch (Exception e) {
			logger.logThrowable("Problem with reading " +
					fileOfSolution.getName() + " file", e);
			return null;
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.logThrowable("Problem with closing the file: " +
							fileOfSolution.getName(), e);
				}
			}
		}
		
		return new IndividualPermutation(objectsOfBinPack);
	}
	
}
