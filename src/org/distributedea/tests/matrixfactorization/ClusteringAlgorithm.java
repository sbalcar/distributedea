package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.tests.matrixfactorization.structures.ClusterSet;
import org.distributedea.tests.matrixfactorization.structures.MatrixOfIncidence;

/**
 * Wrapper for clustering algorithm
 * @author stepan
 *
 */
public class ClusteringAlgorithm {
	
	/**
	 * Creates cluster by generating centroids + k-mean
	 * @param datasetMF
	 * @param numberOfClusters
	 * @return
	 * @throws Exception
	 */
	public static ClusterSet createSpecifiedNumberOfUserBasedClusters(DatasetMF datasetMF,
			int numberOfClusters) throws Exception {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		// creating matrix of incidence
		MatrixOfIncidence matrix =
				new MatrixOfIncidence(datasetModel.exportUserIDs());
		matrix.createUserMatrix(datasetModel);
				
		List<Integer> userIDsList =
				new ArrayList<Integer>(datasetModel.exportUserIDs());
		Collections.shuffle(userIDsList);
		
		List<Integer> initClusterDefByUserIDs =
				userIDsList.subList(0, numberOfClusters);

		
		ClusterSet cluster =
				new ClusterSet(new HashSet<Integer>(initClusterDefByUserIDs));
		
		List<Integer> matrOfIncIndexes =
				datasetModel.exportIndexOfUsers(initClusterDefByUserIDs);
		
		for (int userIdI : datasetModel.exportUserIDs()) {
			if (initClusterDefByUserIDs.contains(userIdI)) {
				continue;
			}
			int matrOfIncIndexI = datasetModel.exportIndexOfUser(userIdI);
			
			int theNearstMatrOfIncIndex = matrix
					.getGivenIndexOfTheHighestIncidenceBetween(matrOfIncIndexI, matrOfIncIndexes);
			
			int theNearstUserIdI = initClusterDefByUserIDs.get(
					matrOfIncIndexes.indexOf(theNearstMatrOfIncIndex));
			
			cluster.addNewIdToCluster(userIdI, theNearstUserIdI);
		}
		
		return cluster;
	}
	
	/**
	 * Creates cluster by intersection of each clusters with specified similarity
	 * @param datasetMF
	 * @param sizeOfRatingIntersection
	 * @return
	 * @throws Exception
	 */
	public static ClusterSet createTransitivelySimilarUserBasedClusters(DatasetMF datasetMF,
			int sizeOfRatingIntersection) throws Exception {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();

		MatrixOfIncidence matrix =
				new MatrixOfIncidence(datasetModel.exportUserIDs());
		matrix.createUserMatrix(datasetModel);

		ClusterSet cluster =
				new ClusterSet(datasetModel.exportUserIDs());
		for (int userIdI : datasetModel.exportUserIDs()) {
			for (int userIdJ : datasetModel.exportUserIDs()) {
				
				if (userIdI >= userIdJ) {
					continue;
				}
				
				int indexI = datasetModel.exportIndexOfUser(userIdI);
				int indexJ = datasetModel.exportIndexOfUser(userIdJ);
				
				int incidenceI = matrix.get(indexI, indexJ);
				
				if (incidenceI >= sizeOfRatingIntersection) {
					cluster.uniteClusters(userIdI, userIdJ);
				}
			}
		}
		
		cluster.exportXML(new File("cluster40.ml"));
		
		return cluster;
	}
	
	
	
	
	//////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private static ClusterSet createClustersOfUsers2(DatasetMF datasetMF,
			DatasetDescriptionMF datasetDescrMF, int sizeOfRatingIntersection) throws Exception {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		if (datasetDescrMF.getTrainingSetDef().equals("u.data") &&
				sizeOfRatingIntersection == 8) {
			return ClusterSet.importXML(new File("cluster8.ml"));
		}
		
		List<Set<Integer>> clustersOfUsers = new ArrayList<>();
		for (int userIdI : datasetModel.exportUserIDs()) {
			
			Set<Integer> clusterI = new HashSet<Integer>();
			clusterI.add(userIdI);
			
			clustersOfUsers.add(clusterI);
		}
	
		while (tryJoinTwoClusters(clustersOfUsers, datasetMF, sizeOfRatingIntersection)) {
			System.out.println("ClustersOfUsers: " + clustersOfUsers.size());
		}
		

		ClusterSet cluster = new ClusterSet(clustersOfUsers);
		cluster.exportXML(new File("cluster15.ml"));
		
		return cluster;
	}
	
	private static boolean tryJoinTwoClusters(
			List<Set<Integer>> clustersOfUsers, DatasetMF datasetMF,
			int sizeOfRatingIntersection) {
		
		for (Set<Integer> clusterI : clustersOfUsers) {
			for (Set<Integer> clusterJ : clustersOfUsers) {
				if (clusterI == clusterJ) {
					continue;
				}
				
				boolean intersection = testsIntersectionOfSets(
						clusterI, clusterJ, datasetMF,
						sizeOfRatingIntersection);
				if (intersection) {
					clustersOfUsers.remove(clusterI);
					clusterJ.addAll(clusterI);
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean testsIntersectionOfSets(Set<Integer> cluster1,
			Set<Integer> cluster2, DatasetMF datasetMF,
			int sizeOfRatingIntersection) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		ObjectRatingList raitings = datasetModel.exportObjectRaitingList();

		for (int userIdI : cluster1) {
			for (int userIdJ : cluster2) {
				
				ObjectRatingList raitingsOfCluster1 =
						raitings.exportObjectRaitingOfUser(userIdI);
				ObjectRatingList raitingsOfCluster2 =
						raitings.exportObjectRaitingOfUser(userIdJ);

				ObjectRatingList intersectionI = raitingsOfCluster1
						.exportIntesectionOfObjectWithIdenticalRaiting(
								raitingsOfCluster2);
				if (intersectionI.size() >= sizeOfRatingIntersection) {
					return true;
				}
			}
		}
		return false;
	}
}

