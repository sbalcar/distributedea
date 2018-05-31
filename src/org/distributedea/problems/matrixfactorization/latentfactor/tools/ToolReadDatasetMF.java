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
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.IRatingIDs;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

/**
 * Tool as reader of the {@link DatasetMF} dataset
 * @author stepan
 *
 */
public class ToolReadDatasetMF {

	public static DatasetMF readDataset(DatasetDescriptionMF datasetDescription,
			ProblemMatrixFactorization problem, IAgentLogger logger) {
		
		File trainingFile = datasetDescription.exportDatasetTrainingFile();
		IRatingIDs trainingSetDef = datasetDescription.getTrainingSetDef();
		
		File testingFile = datasetDescription.exportDatasetTestingFile();
		IRatingIDs testingSetDef = datasetDescription.getTestingSetDef();
		
		
		List<ObjectRaiting> trainingDataset = readSelectedIDsOfDataset(
				trainingFile, trainingSetDef, logger);
		
		List<ObjectRaiting> testingDataset = readSelectedIDsOfDataset(
				testingFile, testingSetDef, logger);
		
		return new DatasetMF(trainingDataset, testingDataset);
	}
	
		
	private static List<ObjectRaiting> readSelectedIDsOfDataset(File datasetFile,
			IRatingIDs selectedIDs, IAgentLogger logger) {
		
		String delimiter = "\t";;
		
		int ratingMult = 1; 
		if (datasetFile.getName().endsWith("ml-1m" + File.separator + "ratings.dat")) {
			ratingMult = 2; // ml-10M100K
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(datasetFile.getAbsolutePath()));
			
			List<ObjectRaiting> raitings = read(br, selectedIDs,
					ratingMult, delimiter, logger);
			
			return raitings;
			
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
	}
	
	private static List<ObjectRaiting> read(BufferedReader br,
			IRatingIDs selectedIDs, int ratingMult, String delimiter,
			IAgentLogger logger) throws NumberFormatException, IOException {
		
		List<ObjectRaiting> raitings = new ArrayList<>();
					
		String sCurrentLine;
		int lineNumber = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			lineNumber++;

			if (! selectedIDs.containsRatingID(lineNumber)) {
				continue;
			}
				
			if (sCurrentLine.contains("::")) {
				delimiter = "::";
			}
			StringTokenizer stI = new StringTokenizer(sCurrentLine, delimiter);
			int userID = Integer.parseInt(stI.nextToken());
			int itemID = Integer.parseInt(stI.nextToken());
			int raiting = (int) (ratingMult*Double.parseDouble(stI.nextToken()));
			
			raitings.add(
					new ObjectRaiting(userID, itemID, raiting));
		}
		
		return raitings;
	}
	
}
