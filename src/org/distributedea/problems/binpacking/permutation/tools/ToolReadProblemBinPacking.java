package org.distributedea.problems.binpacking.permutation.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.dataset.binpacking.ObjectBinPack;

public class ToolReadProblemBinPacking {

	public static DatasetBinPacking readProblem(File problemFile, IAgentLogger logger) {
		
		List<ObjectBinPack> objectsOfBinPack = new ArrayList<>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(problemFile.getAbsolutePath()));

			int currentObjectID = 0;
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("#")) {
					continue;
				}
				double valueI = Double.parseDouble(sCurrentLine);
				objectsOfBinPack.add(new ObjectBinPack(currentObjectID++, valueI));
			}
			
		} catch (Exception e) {
			logger.logThrowable("Problem with reading " +
					problemFile.getName() + " file", e);
			return null;
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.logThrowable("Problem with closing the file: " +
							problemFile.getName(), e);
				}
			}
		}
		
		return new DatasetBinPacking(objectsOfBinPack, problemFile);
	}
	
}
