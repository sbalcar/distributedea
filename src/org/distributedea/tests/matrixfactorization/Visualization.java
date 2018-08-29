package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.javaextension.ListUtils;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsEmptySet;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsFullSet;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;
import org.distributedea.tests.matrixfactorization.structures.ClusterSet;
import org.distributedea.tests.matrixfactorization.structures.ClusterSetList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class Visualization {

	public static void main(String [ ] args) throws Exception {
				
		File file = new File(FileNames.getInputProblemFile(
//				"ml-100k" + File.separator + "u.data"
		        "ml-1m" + File.separator + "ratings.dat"
//		        "ml-10M100K" + File.separator + "ratings.dat"
				));
				
		DatasetDescriptionMF datasetDescr = new DatasetDescriptionMF(
				file, new RatingIDsFullSet(),
				file, new RatingIDsEmptySet(),
				null, null);
		
		ProblemMatrixFactorization problemMF =
				new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10);
		
		DatasetMF datasetMF = ToolReadDatasetMF.readDatasetWithoutContent(
				datasetDescr, problemMF, new TrashLogger());

		visualDataset(datasetMF);
		
//		visualClusters(datasetMF);
		
		System.out.println("OK");
	}
	
	public static void visualDataset(DatasetMF datasetMF) throws Exception {

		visualBarSizeOfDataset(datasetMF);
		visualDistributionOfRatings(datasetMF);
		
		visualHistoramOfUserRaitingCounts(datasetMF);
		visualHistoramOfItemRaitingCounts(datasetMF);

		visualHistoramOfUserRaitingE(datasetMF);
		visualHistoramOfItemRaitingE(datasetMF);
		
		visualHistoramOfUserRaitingVariance(datasetMF);
		visualHistoramOfItemRaitingVariance(datasetMF);
		
		visualHistoramOfUserRMSEBaselineRandomRating(datasetMF);
		visualHistoramOfUserRMSEBaselineTheMostCommonItemRating(datasetMF);
		visualHistoramOfUserRMSEBaselineAvarageOfAllRatings(datasetMF);
		visualHistoramOfUserRMSEBaselineAvarageOfRelevantItemRatings(datasetMF.deepClone());
	}
	
	public static void visualClusters(DatasetMF datasetMF) throws Exception {
		
		ClusterSetList clusterSets = new ClusterSetList();
		for (int i = 0; i < 10; i++) {
			
			ClusterSet clusterSetOfUsrI = ClusteringAlgorithm
					.createSpecifiedNumberOfUserBasedClusters(datasetMF, 5);
			clusterSets.addClusterSet(clusterSetOfUsrI);
		}
		
		visualHistoramOfClusterSizes(clusterSets, datasetMF);
		visualHistoramOfClusterSizesVariance(clusterSets, datasetMF);
		
		ClusterSet clusterSetWithMinVar =
				clusterSets.getClusterSetWithMinClusterSizeVariance();
		clusterSetWithMinVar.print();
		
		visualHistoramOfSubClusterSizes(clusterSetWithMinVar, datasetMF);
		
		
		visualHistoramOfUserRMSEBaselineUserKmeanClusteringMeanOfCluster(
				clusterSetWithMinVar, datasetMF);

		visualHistoramOfUserRMSEBaselineUserKmeanClusteringMeanOfRelevantItemRatings(
				clusterSetWithMinVar, datasetMF);

	}
	
	private static void visualBarSizeOfDataset(DatasetMF datasetMF) {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		int numberOfUsers = datasetModel.exportNumberOfUsers();
		System.out.println("NumberOfUsers: " + numberOfUsers);

		Set<Integer> userIDs = datasetModel.exportUserIDs();
		int maxUserId = Collections.max(userIDs);
		System.out.println("MaxUserId: " + maxUserId);
		
		int numberOfItems = datasetModel.exportNumberOfItems();
		System.out.println("NumberOfItems: " + numberOfItems);		

		Set<Integer> itemIDs = datasetModel.exportItemIDs();
		int maxItemId = Collections.max(itemIDs);
		System.out.println("MaxItemId: " + maxItemId);
		
		int numberOfRaitings = datasetModel.exportNumberOfRaitings();
		System.out.println("NumberOfRaitings: " + numberOfRaitings);
		
		double complOfRaitings = numberOfRaitings / (double)(numberOfUsers * numberOfItems);
		System.out.println("Completeness: " + complOfRaitings * 100 + " %");
		
		double minOfRaitings = datasetModel.exportMinOfRaitings();
		System.out.println("MinOfRaitings: " + minOfRaitings);
		
		double maxOfRaitings = datasetModel.exportMaxOfRaitings();
		System.out.println("MaxOfRaitings: " + maxOfRaitings);		
		
		int countOfItemsWithOneRaiting = 0;
		for (int itemIdI : datasetModel.exportItemIDs()) {
			if (datasetModel.exportRaitingsOfItem(itemIdI).size() == 1) {
				countOfItemsWithOneRaiting++;
			}
		}
		System.out.println("CountOfItemsWithOneRaiting: " + countOfItemsWithOneRaiting);
		
		
        // column keys...
        final String category1 = "uživatelé a filmy";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(numberOfUsers, "Počet uživatelů", category1);
        dataset.addValue(numberOfItems, "Počet filmů", category1);
        
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Velikost datasetu",       // chart title
            "entity",                  // domain axis label
            "počet záznamů",           // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,
            true,                      // include legend
            true,                      // tooltips?
            false                      // URLs?
        );

		int width = 500;
		int height = 300; 
		
		File f = new File("barSizeOfDataset.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static void visualDistributionOfRatings(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Pair<Integer, Integer>> raitingAndCounts = new ArrayList<>();
		for (int raitingValueI = (int) datasetModel.exportMinOfRaitings();
				raitingValueI <= datasetModel.exportMaxOfRaitings();
				raitingValueI++) {
		
			ObjectRatingList objectsWithRaitingI =
					datasetModel.exportObjectWithRaiting(raitingValueI);
			raitingAndCounts.add(new Pair<Integer, Integer>(
					raitingValueI, objectsWithRaitingI.size()));
		}
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Pair<Integer, Integer> raitingI : raitingAndCounts) {
			
			dataset.setValue("Raiting " + raitingI.first, new Double(raitingI.second));
		}
		
		JFreeChart chart = ChartFactory.createPieChart(
				"Rozložení raitingů",   // chart title
				dataset,          // data
				true,             // include legend
				true,
				false);

		int width = 500;
		int height = 300;
		
		File f = new File("pieChartDistributionOfRatings.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	
	private static void visualHistoramOfUserRaitingCounts(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> availableRaitingOfItemCounts = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfUser(userIdI);
			availableRaitingOfItemCounts.add((double) raitingsI.size());
		}
		
		Collections.sort(availableRaitingOfItemCounts);
		System.out.println("AvailableRaitingOfItemCounts: " +
				availableRaitingOfItemCounts.get(0));
		System.out.println("AvailableRaitingOfItemCounts: " +
				availableRaitingOfItemCounts.get(availableRaitingOfItemCounts.size()-1));
		
		double[] availableRaitingOfItemCountsArray =
				ListUtils.toArrayDblToDbl(availableRaitingOfItemCounts);
		
		int number = 50;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", availableRaitingOfItemCountsArray, number);
		String plotTitle = "Četnosti počtu vyplněných raitingů uživatelů"; 
		String xaxis = "počty vyplnených raitingů vyplněných jedním uživatelem";
		String yaxis = "četnost raitingů"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300; 
		
		File f = new File("historamOfUserRaitingCounts.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static void visualHistoramOfItemRaitingCounts(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> availableRaitingOfItemCounts = new ArrayList<>();
		for (int itemIdI : datasetModel.exportItemIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfItem(itemIdI);
			availableRaitingOfItemCounts.add((double) raitingsI.size());
		}
		
		Collections.sort(availableRaitingOfItemCounts);
		System.out.println("AvailableRaitingOfItemCounts: " +
				availableRaitingOfItemCounts.get(0));
		System.out.println("AvailableRaitingOfItemCounts: " +
				availableRaitingOfItemCounts.get(availableRaitingOfItemCounts.size()-1));
		
		double[] availableRaitingOfItemCountsArray =
				ListUtils.toArrayDblToDbl(availableRaitingOfItemCounts);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", availableRaitingOfItemCountsArray, number);
		String plotTitle = "Četnosti počtu vyplněných raitingů filmů"; 
		String xaxis = "počty vyplnených raitingů dostupných pro jeden film";
		String yaxis = "četnost raitingů"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfItemRaitingCounts.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	
	private static void visualHistoramOfUserRaitingE(DatasetMF datasetMF) {
		
		int LINE = 100;
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> meanesWitMoreThan100Raitings = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfUser(userIdI);
			if (raitingsI.size() > LINE) {
				ObjectRatingList raitingsOfUserI =
						datasetModel.exportRaitingsOfUser(userIdI);
				double eValueI = raitingsOfUserI.countMeanOfRaitings();
				meanesWitMoreThan100Raitings.add(eValueI);
			}
		}

		double[] meanesWitMoreThan100RaitingsArray =
				ListUtils.toArrayDblToDbl(meanesWitMoreThan100Raitings);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", meanesWitMoreThan100RaitingsArray, number);
		String plotTitle = "Histogram středních hodnot raitingů jednotlivých uživatelů"; 
		String xaxis = "střední hodnoty raitingů uživatelů obsahujících nad " + LINE + " záznamů";
		String yaxis = "četnosti E raitingů pro jednotlivé uživatele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRaitingE.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}


	private static void visualHistoramOfItemRaitingE(DatasetMF datasetMF) {
		
		int LINE = 100;
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> meanesWitMoreThan100Raitings = new ArrayList<>();
		
		for (int itemIdI : datasetModel.exportItemIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfItem(itemIdI);
			if (raitingsI.size() > LINE) {
				ObjectRatingList raitingOfItemI =
						datasetModel.exportRaitingsOfItem(itemIdI);
				double eValueI = raitingOfItemI.countMeanOfRaitings();
				meanesWitMoreThan100Raitings.add(eValueI);
			}
		}

		double[] meanesWitMoreThan100RaitingsArray =
				ListUtils.toArrayDblToDbl(meanesWitMoreThan100Raitings);

		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", meanesWitMoreThan100RaitingsArray, number);
		String plotTitle = "Histogram středních hodnot raitingů jednotlivých filmů"; 
		String xaxis = "střední hodnoty raitingů filmů obsahujících nad " + LINE + " záznamů";
		String yaxis = "četnosti E raitingů pro jednotlivé filmy"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfItemRaitingE.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static void visualHistoramOfUserRaitingVariance(DatasetMF datasetMF) {
		
		int LINE = 100;

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> meanesWitMoreThan100Raitings = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfUser(userIdI);
			if (raitingsI.size() > LINE) {
				ObjectRatingList raitingsOfUserI =
						datasetModel.exportRaitingsOfUser(userIdI);
				double varianceValueI =
						raitingsOfUserI.countVarianceRaitings();
				meanesWitMoreThan100Raitings.add(varianceValueI);
			}
		}

		double[] meanesWitMoreThan100RaitingsArray =
				ListUtils.toArrayDblToDbl(meanesWitMoreThan100Raitings);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", meanesWitMoreThan100RaitingsArray, number);
		String plotTitle = "Histogram rozptylů raitingů jednotlivých uživatelů"; 
		String xaxis = "rozptyly raitingů uživatelů obsahujících nad " + LINE + " záznamů";
		String yaxis = "četnosti rozptylů raitingů pro jednotlivé uživatele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRaitingVariance.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static void visualHistoramOfItemRaitingVariance(DatasetMF datasetMF) {
		
		int LINE = 100;
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> meanesWitMoreThan100Raitings = new ArrayList<>();
		
		for (int itemIdI : datasetModel.exportItemIDs()) {
			ObjectRatingList raitingsI =
					datasetModel.exportRaitingsOfItem(itemIdI);
			if (raitingsI.size() > LINE) {
				ObjectRatingList raitingOfItemI =
						datasetModel.exportRaitingsOfItem(itemIdI);
				double varianceValueI =
						raitingOfItemI.countVarianceRaitings();
				meanesWitMoreThan100Raitings.add(varianceValueI);
			}
		}

		double[] meanesWitMoreThan100RaitingsArray =
				ListUtils.toArrayDblToDbl(meanesWitMoreThan100Raitings);

		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", meanesWitMoreThan100RaitingsArray, number);
		String plotTitle = "Histogram rozptylů raitingů jednotlivých filmů"; 
		String xaxis = "rozpzyly raitingů filmů obsahujících nad " + LINE + " záznamů";
		String yaxis = "četnosti rozptylů raitingů pro jednotlivé filmy"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfItemRaitingVariance.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static void visualHistoramOfUserRMSEBaselineRandomRating(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		ObjectRatingList raitings =
				datasetModel.exportObjectRaitingList();

		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			ObjectRatingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			
			ObjectRating randomObject = raitings.exportRandomObjectRaiting();
			
			double rmseI = raitingsOfUserI.countRMSEForFixedPredictedValue(
					randomObject.getRaiting());
			rmses.add(rmseI);
		}
		
		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineRandomRating: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline ruleta raiting"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineRandomRating.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	
	private static void visualHistoramOfUserRMSEBaselineTheMostCommonItemRating(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			ObjectRatingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			
			double rmseI = raitingsOfUserI.countRMSEForFixedPredictedValue(4);
			rmses.add(rmseI);
		}
		
		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineTheMostCommonItemRating: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline nejčastější raiting"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineTheMostCommonItemRating.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}
	
	private static void visualHistoramOfUserRMSEBaselineAvarageOfAllRatings(DatasetMF datasetMF) {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		ObjectRatingList raitings = datasetModel.exportObjectRaitingList();
		double meanOfAll = raitings.countMeanOfRaitings();
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			ObjectRatingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			
			double rmseI = raitingsOfUserI.countRMSEForFixedPredictedValue(
					meanOfAll);
			rmses.add(rmseI);
		}

		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineAvarageOfAllRatings: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline průměr všech raitingů"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineAvarageOfAllRatings.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}
	
	private static void visualHistoramOfUserRMSEBaselineAvarageOfRelevantItemRatings(DatasetMF datasetMF) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			System.out.println("userIdI: " + userIdI);
			ObjectRatingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			
			ObjectRatingList foreignRaitings =
					datasetModel.exportObjectRaitingList().noDeepClone();
			foreignRaitings.removeObjectRaitingOfUser(userIdI);
			
			double rmseI = raitingsOfUserI
					.countRMSEForUserBaselineAvarageOfItemRatings(foreignRaitings);
			if (! Double.isNaN(rmseI)) {
			rmses.add(rmseI);
			}
		}

		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineAvarageOfItemRatings: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline průměr přes raitingy relevatních itemů"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineAvarageOfItemRatings.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	@SuppressWarnings("unused")
	private static void visualHistoramOfUserRMSEBaselineUserClustering8SameRaitings(
			ClusterSet clustersOfUsers, DatasetMF datasetMF) throws Exception {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		ObjectRatingList raitings =
				datasetModel.exportObjectRaitingList();
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			Set<Integer> clusterI =
					clustersOfUsers.exportClusterOfID(userIdI);

			ObjectRatingList raitingsOfUserI =
					raitings.exportObjectRaitingOfUser(userIdI);
			
			ObjectRatingList raitingOfClusterI =
					raitings.exportObjectRaitingOfUsers(clusterI);
			
			double rmseI = countRMSEForClusterOfUsers(
					userIdI, raitingsOfUserI, raitingOfClusterI, 4);
			rmses.add(rmseI);
		}
		
		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineUserClustering8SameRaitings: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline klastry uživatelů s 8 stejnými raitingy"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineUserClustering8SameRaitings.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}
	
	private static double countRMSEForClusterOfUsers(
			int userID, ObjectRatingList raitingsOfUserI,
			ObjectRatingList raitingOfClusterI, double defaultRaiting
			) throws Exception {
		
		raitingOfClusterI.removeObjectRaitingOfUser(userID);
		double mean = raitingOfClusterI.countMeanOfRaitings();
		if (Double.isNaN(mean)) {
			mean = defaultRaiting;
		}
		
		return raitingsOfUserI.countRMSEForFixedPredictedValue(mean);
	}

	private static void visualHistoramOfClusterSizes(
			ClusterSetList clusterSets, DatasetMF datasetMF) throws Exception {
		
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
		for (int clusterSetIndexI = 0; clusterSetIndexI < clusterSets.size();
				clusterSetIndexI++) {
			
			ClusterSet clusterSetI =
					clusterSets.getClusterSet(clusterSetIndexI);
			List<Integer> clusterSizes = clusterSetI.getClusterSizes();
			Collections.sort(clusterSizes);
			
			String clusterLabelI = "Cluster " + clusterSetIndexI;
			int clusterMinI = clusterSizes.get(0);
			int clusterMaxI = clusterSizes.get(clusterSizes.size() -1);
			int clusterMedianI = clusterSizes.get(clusterSizes.size() /2);
			
			categoryDataset.setValue(clusterMinI, "Min", clusterLabelI);
			categoryDataset.setValue(clusterMedianI, "Median", clusterLabelI);
			categoryDataset.setValue(clusterMaxI, "Max", clusterLabelI);
		}		

        JFreeChart chart = ChartFactory.createBarChart3D
                     ("Vizualizace velikosti klastrování - počet klastrů = 5",  // Title
                      "Klastry",                        // X-Axis label
                      "Velikosti klastrů",              // Y-Axis label
                      categoryDataset,                  // Dataset
                      PlotOrientation.VERTICAL,
                      true,                             // Show legend
                      true,
                      false
                     );
        
		int width = 500;
		int height = 300; 
		
		File f = new File("barChartOfClusterSizes.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }

	}
	
	private static void visualHistoramOfClusterSizesVariance(
			ClusterSetList clusterSets, DatasetMF datasetMF) throws Exception {
		
        // column keys...
        final String category1 = "generované k-mens centroidy";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int clsSetIndexI = 0; clsSetIndexI < clusterSets.size();
        		clsSetIndexI++) {
        	
        	ClusterSet clusterSetI = clusterSets.getClusterSet(clsSetIndexI);
        	dataset.addValue(clusterSetI.countVariance(),
        			"ClusterSet " + clsSetIndexI, category1);
        }
        
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Rozptyly velikostí klastrů",  // chart title
            "Klastry",                     // domain axis label
            "Rozptyly velikostí klastrů",  // range axis label
            dataset,                       // data
            PlotOrientation.VERTICAL,
            true,                      // include legend
            true,                      // tooltips?
            false                      // URLs?
        );

		int width = 500;
		int height = 300; 
		
		File f = new File("barOfClusterSizesVariance.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }

	}

	private static void visualHistoramOfSubClusterSizes(
			ClusterSet clusterSet, DatasetMF datasetMF) throws Exception {
		
		double[] clusterSizes =
				ListUtils.toArrayIntToDbl(clusterSet.getClusterSizes());
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", clusterSizes, number);
		String plotTitle = "Histogram velikostí klastrů"; 
		String xaxis = "velikosti jednotlivých klastrů";
		String yaxis = "četnosti velikostí jednotlivých klastrů"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfSubClusterSizes.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}
	
	
	private static void visualHistoramOfUserRMSEBaselineUserKmeanClusteringMeanOfCluster(
			ClusterSet clustersOfUsers, DatasetMF datasetMF) throws Exception {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		int numOfCls = clustersOfUsers.getCountOfClusters();
		
		ObjectRatingList raitings =
				datasetModel.exportObjectRaitingList();
		
		double meanOfAll = raitings.countMeanOfRaitings();
		
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			Set<Integer> clusterI =
					clustersOfUsers.exportClusterOfID(userIdI);

			ObjectRatingList raitingsOfUserI =
					raitings.exportObjectRaitingOfUser(userIdI);
			
			ObjectRatingList raitingOfClusterI =
					raitings.exportObjectRaitingOfUsers(clusterI);
			
			double rmseI = countRMSEForClusterOfUsers(
					userIdI, raitingsOfUserI, raitingOfClusterI, meanOfAll);
			rmses.add(rmseI);
		}
		
		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineUserK" + numOfCls + "meanClusteringMeanOfCluster: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline " + numOfCls + " průměr klastrů uživatelů"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineUserK" + numOfCls + "meanClusteringMeanOfCluster.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}
	
	private static void visualHistoramOfUserRMSEBaselineUserKmeanClusteringMeanOfRelevantItemRatings(
			ClusterSet clustersOfUsers, DatasetMF datasetMF) throws Exception {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		int numOfCls = clustersOfUsers.getCountOfClusters();
		
		ObjectRatingList raitings =
				datasetModel.exportObjectRaitingList();
		
		double meanOfAll = raitings.countMeanOfRaitings();
		
		
		List<Double> rmses = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			Set<Integer> clusterI =
					clustersOfUsers.exportClusterOfID(userIdI);

			ObjectRatingList raitingsOfUserI =
					raitings.exportObjectRaitingOfUser(userIdI);
			
			ObjectRatingList raitingOfClusterI =
					raitings.exportObjectRaitingOfUsers(clusterI);
			
			double rmseI = countRMSEForClusterOfUsers_(
					userIdI, raitingsOfUserI, raitingOfClusterI, meanOfAll);
			rmses.add(rmseI);
		}
		
		double rmseMean = ListUtils.sumDoubles(rmses) / rmses.size();
		System.out.println("RMSEBaselineUserK" + numOfCls + "meanClusteringMeanOfRelevantItemRatings: " + rmseMean);
		
		double[] rmsesArray = ListUtils.toArrayDblToDbl(rmses);
		
		int number = 100;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", rmsesArray, number);
		String plotTitle = "Histogram RMSE baseline " + numOfCls + " klastrů uživatelů"; 
		String xaxis = "hodnoty RMSE jednotlivých uživatelů";
		String yaxis = "četnosti míry RMSE raitingů pro jednotlivé uživetele"; 
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createHistogram(plotTitle,
				xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		
		File f = new File("historamOfUserRMSEBaselineUserK" + numOfCls +"meanClusteringMeanOfRelevantItemRatings.png");
		try {
	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
	    } catch (IOException e) {
	    }
	}

	private static double countRMSEForClusterOfUsers_(
			int userID, ObjectRatingList raitingsOfUserI,
			ObjectRatingList raitingOfCluster, double defaultRaiting
			) throws Exception {
		
		ObjectRatingList raitingsOfClusterI = raitingOfCluster.noDeepClone();
		raitingsOfClusterI.removeObjectRaitingOfUser(userID);
	
		ObjectRatingList predictedRaitings = new ObjectRatingList();
		
		for (ObjectRating oOfUserI : raitingsOfUserI.getRaitings()) {
			
			ObjectRatingList oOfItemsOfUserI = raitingsOfClusterI
					.exportObjectRaitingOfItem(oOfUserI.getItemID());
			double meanOfItemsOfUserI = oOfItemsOfUserI.countMeanOfRaitings();
			if (Double.isNaN(meanOfItemsOfUserI)) {
				meanOfItemsOfUserI = defaultRaiting;
			}
			
			ObjectRating predictedRaiting = new ObjectRating(
					oOfUserI.getUserID(), oOfUserI.getItemID(),
					meanOfItemsOfUserI);
			predictedRaitings.add(predictedRaiting);
		}
		
		return raitingsOfUserI.countRMSEForPredictedValues(predictedRaitings);
	}
	
}
