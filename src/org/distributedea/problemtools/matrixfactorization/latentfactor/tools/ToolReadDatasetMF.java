package org.distributedea.problemtools.matrixfactorization.latentfactor.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.IRatingIDs;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.readingdataset.IReadDatasetML;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.readingdataset.ReadDatasetML100k;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.readingdataset.ReadDatasetML10M100K;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.readingdataset.ReadDatasetML1M;

/**
 * Tool as reader of the {@link DatasetMF} dataset
 * @author stepan
 *
 */
public class ToolReadDatasetMF {

	public static DatasetMF readDatasetWithoutContent(DatasetDescriptionMF datasetDescription,
			ProblemMatrixFactorization problem, IAgentLogger logger) {
		
		File trainingFile = datasetDescription.exportDatasetTrainingFile();
		IRatingIDs trainingSetDef = datasetDescription.getTrainingSetDef();
		
		File testingFile = datasetDescription.exportDatasetTestingFile();
		IRatingIDs testingSetDef = datasetDescription.getTestingSetDef();
		
		
		List<ObjectRating> trainingDataset = readSelectedIDsOfDataset(
				trainingFile, trainingSetDef, logger);
		
		List<ObjectRating> testingDataset = readSelectedIDsOfDataset(
				testingFile, testingSetDef, logger);
				
		return new DatasetMF(trainingDataset, testingDataset, null);
	}

	public static DatasetMF readDatasetWithContent(DatasetDescriptionMF datasetDescription,
			ProblemMatrixFactorization problem, IAgentLogger logger) {
		
		DatasetMF datasetMF = readDatasetWithoutContent(datasetDescription, problem, logger);
		
		File itemsContentFile = datasetDescription.exportItemsContentFile();
		//File usersContentFile = datasetDescription.exportUsersContentFile();
		
		List<IItemContent> itemsContent =
				 readItemsContent(itemsContentFile, logger);
		
		return new DatasetMF(datasetMF.getTrainingRatings(),
				datasetMF.getTestingRatings(), itemsContent);
	}

	
	private static List<IItemContent> readItemsContent(File itemsContentFile,
			IAgentLogger logger) {

		IReadDatasetML readDatasetML = null;
//		System.out.print(itemsContentFile.getParent());
		
		if (itemsContentFile.getParent().contains("ml-100k")) {
			readDatasetML = new ReadDatasetML100k();
			
		} else if (itemsContentFile.getParent().contains("ml-1m")) {
			readDatasetML = new ReadDatasetML1M();
			
		} else if (itemsContentFile.getParent().contains("ml-10M100K")) {
			readDatasetML = new ReadDatasetML10M100K();
		}

		List<IItemContent> itemContents = null;
		
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(itemsContentFile.getAbsolutePath()));

			itemContents = readItemsContentFromBR(br, readDatasetML, logger);
		} catch (IOException e) {
			return null;
		}
		
		return itemContents;
	}
	
	private static List<IItemContent> readItemsContentFromBR(BufferedReader br,
			IReadDatasetML readDatasetML, IAgentLogger logger) throws IOException {
		
		List<IItemContent> itemContents = new ArrayList<>();
		
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {

			IItemContent itemContentI =
					readDatasetML.parseFilmContentLine(sCurrentLine);
			
			itemContents.add(itemContentI);
		}
		
		return itemContents;
		
	}
		
	private static List<ObjectRating> readSelectedIDsOfDataset(File datasetFile,
			IRatingIDs selectedIDs, IAgentLogger logger) {
		
		String delimiter = "\t";;
		
		int ratingMult = 1; 
		if (datasetFile.getName().endsWith("ml-1m" + File.separator + "ratings.dat")) {
			ratingMult = 2; // ml-10M100K
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(datasetFile.getAbsolutePath()));
			
			List<ObjectRating> raitings = read(br, selectedIDs,
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
	
	private static List<ObjectRating> read(BufferedReader br,
			IRatingIDs selectedIDs, int ratingMult, String delimiter,
			IAgentLogger logger) throws NumberFormatException, IOException {
		
		List<ObjectRating> raitings = new ArrayList<>();
					
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
					new ObjectRating(userID, itemID, raiting));
		}
		
		return raitings;
	}
	
}
