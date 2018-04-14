package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;

/**
 * Tool as reader of the {@link DatasetMF} dataset
 * @author stepan
 *
 */
public class ToolReadDatasetMF {

	public static DatasetMF readDataset(File datasetFile, IAgentLogger logger) {

		String delimiter = "\t";;
		
		int ratingMult = 1; 
		if (datasetFile.getName().endsWith("ml-1m" + File.separator + "ratings.dat")) {
			ratingMult = 2; // ml-10M100K
		}
		
		return read(datasetFile, ratingMult, delimiter, logger);
	}
	
	private static DatasetMF read(File datasetFile, int ratingMult,
			String delimiter, IAgentLogger logger) {
		
		List<ObjectRaiting> raitings = new ArrayList<>();
		
		BufferedReader br = null;
				
		try {
			br = new BufferedReader(new FileReader(datasetFile.getAbsolutePath()));
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {

				if (sCurrentLine.contains("::")) {
					delimiter = "::";
				}
				StringTokenizer stI = new StringTokenizer(sCurrentLine, delimiter);
				int userID = Integer.parseInt(stI.nextToken());
				int itemID = Integer.parseInt(stI.nextToken());
				int raiting = (int) (ratingMult*Double.parseDouble(stI.nextToken()));
				
				ObjectRaiting orI = new ObjectRaiting(userID, itemID, raiting);
				raitings.add(orI);
			}
			
		} catch (Exception e) {
			logger.logThrowable("Problem with reading " +
					datasetFile.getName() + " file", e);
			return null;
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.logThrowable("Problem with closing the file: " +
							datasetFile.getName(), e);
				}
			}
		}
		
		return new DatasetMF(raitings, datasetFile);
	}
	
}
