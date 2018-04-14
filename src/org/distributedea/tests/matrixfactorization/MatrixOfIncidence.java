package org.distributedea.tests.matrixfactorization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaitingList;

/**
 * Structure represents matrix of incidence
 * @author stepan
 *
 */
public class MatrixOfIncidence {

	private int[][] matrix;
	
	/**
	 * Constructor
	 * @param identifiers
	 */
	public MatrixOfIncidence(Set<Integer> identifiers) {
		
		this.matrix = new int[identifiers.size()][identifiers.size()];
	}
	
	public void increment(int x, int y) {
		this.matrix[x][y]++;
	}
	public void increment(int x, int y, int value) {
		this.matrix[x][y] += value;
	}
	
	public int get(int x, int y) {
		return this.matrix[x][y];
	}
	
	/**
	 * Returns size (width and high) of matrix
	 * @return
	 */
	public int size() {
		return matrix.length;
	}
	
	public List<Integer> getIncidencesBetween(int identifier,
			List<Integer> identifiers) {
		
		List<Integer> incidences = new ArrayList<>();
		for (int identifierI : identifiers) {
			incidences.add(get(identifier, identifierI));
		}
		return incidences;
	}
	
	public int getGivenIndexOfTheHighestIncidenceBetween(int index,
			List<Integer> indexes) {
		
		List<Integer> incidences =
				getIncidencesBetween(index, indexes);
		
		int maxIncidence = Collections.max(incidences);
		int maxIncidenceIndex = incidences.indexOf(maxIncidence);
		
		return indexes.get(maxIncidenceIndex);
	}
	
	/**
	 * Creates Incidence Matrix for {@link DatasetMF}
	 * @param datasetMF
	 */
	public void createUserMatrix(DatasetMF datasetMF) {
		
		Set<Integer> userIDsSet = datasetMF.exportUserIDs();
		
		for (int userIdI : userIDsSet) {
			for (int userIdJ : userIDsSet) {
				if (userIdI < userIdJ) {
					continue;
				}
				
				int indexI = datasetMF.exportIndexOfUser(userIdI);
				int indexJ = datasetMF.exportIndexOfUser(userIdJ);
				
				int incidenceI = countIncidence(userIdI, userIdJ, datasetMF);
				
				increment(indexI, indexJ, incidenceI);
				if (userIdI != userIdJ) {
					increment(indexJ, indexI, incidenceI);
				}
				
			}
		}
	}
	
	private int countIncidence(int userId1, int userId2,
			DatasetMF datasetMF) {
		
		ObjectRaitingList objects1 = datasetMF.exportRaitingsOfUser(userId1);
		ObjectRaitingList objects2 = datasetMF.exportRaitingsOfUser(userId2);
		
		ObjectRaitingList intersectionI = objects1
				.exportIntesectionOfObjectWithIdenticalRaiting(objects2);
		
		return intersectionI.size();
	}
	
}
