package org.distributedea.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.jobs.InputMatrixFactorization;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problemtools.IProblemTool;

public class VisualisationMFtopK {

	private static List<Integer> firstHitIndexes(File dir) throws IOException {
		
		IAgentLogger logger = new TrashLogger();
		
		Job jobI = InputMatrixFactorization.test03();
		IProblem problem = jobI.getProblem();
		
		IDatasetDescription datasetDesr = jobI.getDatasetDescription();
		Methods methods = jobI.getMethods();
		
		ProblemToolDefinition problemToolDef =
				methods.exportRandomMethodDescription().getProblemToolDefinition();
		IProblemTool problemTool = problemToolDef.exportProblemTool(new TrashLogger());
		
		DatasetMF dataset = (DatasetMF) problemTool.readDataset(datasetDesr, problem, logger);
		
		File fileOfSolution = new File(dir.getAbsolutePath() + File.separator + "solution.txt");
		IndividualLatentFactors model = (IndividualLatentFactors) problemTool.readSolution(fileOfSolution, dataset, logger);
	
		
		RatingModel datasetModelTest = dataset.exportTestingRatingModel();
		
		Set<Integer> userIDs = datasetModelTest.exportUserIDs();


		List<Integer> indexes = new ArrayList<>();
		for (int userIDI : userIDs) {
			
			indexes.add(
					visualisation(userIDI, model, datasetModelTest));
		}
		
		Collections.sort(indexes);
		
		System.out.println("Index " + indexes.get(0) + " " + indexes.get(indexes.size() -1));
		
		return indexes;
	}
	
	private static int visualisation(int userID, IndividualLatentFactors model, RatingModel datasetModelTest) {

		System.out.println("userIDI: " + userID);
		
		List<Integer> itemIDs = new ArrayList<Integer>(datasetModelTest.exportItemIDs());

		int userIDIndex = datasetModelTest.exportIndexOfUser(userID);
		List<Integer> itemIDIndexes = datasetModelTest.exportIndexOfItems(itemIDs);
		
		List<Double> pradictedRatings = model.exportValues(userIDIndex, itemIDIndexes);
		
		ObjectRatingList predictedORL = new ObjectRatingList(userID, itemIDs, pradictedRatings);
		
		
		ObjectRatingList givenORL = datasetModelTest.exportRaitingsOfUser(userID);
		double maxRatingValue = givenORL.exportMinAndMaxRaiting().second;
		
		ObjectRatingList topOfGivenORL = givenORL.exportObjectWithRaiting((int)maxRatingValue);
		List<Integer> topItemIDs = new ArrayList<>(topOfGivenORL.exportItemIDs());
		
		predictedORL.sort();
		//predictedORL.print();
		List<Integer> indexes = predictedORL.exportFirstObjectRaitingIndexesOfItems(topItemIDs);
		
		Collections.sort(indexes);
		//System.out.println("Index " + indexes.get(0) + " " + indexes.get(indexes.size() -1));
		
		return indexes.get(0);
	}
	
	private static void firstHitHistogram(File dir, String FILENAME) throws IOException {

		File file = new File(dir.getAbsolutePath() + File.separator + FILENAME + ".m");
		
		List<Integer> indexes = firstHitIndexes(dir);
		
		String NL = "\n";
		
		String string =
		"h = figure" + NL +
		"hold on" + NL +
		"title('Ranking predicted MF on 10M ML');" + NL +
		"xlabel('x: index of the first successful forecast', 'FontSize', 10);" + NL +
		"ylabel('y: number of users', 'FontSize', 10);" + NL + NL;

		
		string += "x = [";
		for (int indexI : indexes) {
			string += indexI + ", ";
		}
		string = string.substring(0, string.length() -3);
		string += "]";

		string += NL +
		"nbins = 50;" + NL +
		"hist(x,nbins)" + NL +  NL +

		"set(gca,'XTickLabelRotation', -40);" + NL +
		"hold off" + NL +
		"h.PaperPositionMode = 'auto'" + NL +
		"fig_pos = h.PaperPosition;" + NL +
		"h.PaperSize = [fig_pos(3) fig_pos(4)];" + NL +
		"saveas(h, '" + FILENAME + "','bmp');" + NL +
		"print(h, '-fillpage', '" + FILENAME + "','-dpdf');" + NL +
		"exit;" + NL;

		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(file), "utf-8"))) {
			writer.write(string);
		}

		System.out.println(string);
	}
	
	public static void main(String [] args) throws IOException {
		
		String FILENAME = "histogramFirstHitML10M";
		File dir = new File("result/recsys2018");
		
		firstHitHistogram(dir, FILENAME);
		
		System.out.println("OK");
	}
	
}
