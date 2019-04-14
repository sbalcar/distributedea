package org.distributedea.ontology.dataset.matrixfactorization.objectrating;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.structures.comparators.CmpObjectRating;


/**
 * Structure represents the {@link List}t of Raiting objects
 * @author stepan
 *
 */
public class ObjectRatingList implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<ObjectRating> raitings;
	
	/**
	 * Constructor
	 */
	public ObjectRatingList() {
		this.raitings = new ArrayList<ObjectRating>();
	}

	/**
	 * Constructor
	 * @param raitings
	 */
	public ObjectRatingList(List<ObjectRating> raitings) {
		this.setRaitings(raitings);
	}
	
	public ObjectRatingList(int userID, List<Integer> itemIDs, List<Double> pradictedRatings) {
		
		List<ObjectRating> raitingsNew = new ArrayList<>();
		for (int i = 0; i < pradictedRatings.size(); i++) {
			
			double ratingI = pradictedRatings.get(i);
			int temIDI = itemIDs.get(i);
			
			raitingsNew.add(
					new ObjectRating(userID, temIDI, ratingI));
		}
		this.setRaitings(raitingsNew);
	}
	
	
	
	public List<ObjectRating> getRaitings() {
		return raitings;
	}
	@Deprecated
	public void setRaitings(List<ObjectRating> raitings) {
		if (raitings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRating raitingI : raitings) {
			if (raitingI == null || (! raitingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRating.class.getSimpleName() + " is not valid");
			}
		}
		this.raitings = raitings;
	}
	
	/**
	 * Add {@link ObjectRating}
	 * @param raiting
	 */
	public void add(ObjectRating raiting) {
		if (raiting == null || ! (raiting.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRating.class.getSimpleName() + " is not valid");			
		}
		this.raitings.add(raiting);
	}
	
	/**
	 * Add {@link ObjectRatingList}
	 * @param raitings
	 */
	public void addAll(ObjectRatingList raitings) {
		if (raitings == null || ! (raitings.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRatingList.class.getSimpleName() + " is not valid");			
		}
		this.raitings.addAll(raitings.getRaitings());
	}
	
	/**
	 * Returns number of {@link ObjectRating} inside
	 * @return
	 */
	public int size() {
		return this.raitings.size();
	}
	
	/**
	 * Count RMSE
	 * @param predictedRaiting
	 * @return
	 */
	public double countRMSEForFixedPredictedValue(double predictedRaiting) {
		if (size() == 0) {
			return 0;
		}
		
		double squares = 0;
		for (ObjectRating oRaitingI : getRaitings()) {
			
			squares += Math.pow(predictedRaiting -oRaitingI.getRaiting(), 2);
		}
		return Math.pow(squares / size(), 0.5);
	}
	
	/**
	 * Count RMSE
	 * @param predictedRaitings
	 * @return
	 */
	public double countRMSEForPredictedValues(ObjectRatingList predictedRaitings) {
		if (size() == 0) {
			return 0;
		}

		double squares = 0;
		for (ObjectRating oRaitingI : getRaitings()) {
			
			double predictedRaitingI = predictedRaitings.exportRaiting(
					oRaitingI.getUserID(), oRaitingI.getItemID());
			
			squares += Math.pow(predictedRaitingI -oRaitingI.getRaiting(), 2);
		}
		return Math.pow(squares / size(), 0.5);
	}
	
	/**
	 * Count RMSE
	 * @param foreignRaitings
	 * @return
	 */
	public double countRMSEForUserBaselineAvarageOfItemRatings(
			ObjectRatingList foreignRaitings) {
		if (size() == 0) {
			return 0;
		}
		
		double squares = 0;
		for (ObjectRating oRaitingI : getRaitings()) {
			
			ObjectRatingList raitingOfItemI = foreignRaitings
					.exportObjectRaitingOfItem(oRaitingI.getItemID());
			
			double avarageOverItems = raitingOfItemI.countMeanOfRaitings();
			
			squares += Math.pow(avarageOverItems -oRaitingI.getRaiting(), 2);
		}
		return Math.pow(squares / size(), 0.5);
	}
	
	/**
	 * Counts mean of raitings
	 * @return
	 */
	public double countMeanOfRaitings() {
		double sum = 0;
		for (ObjectRating oRaitingI : this.raitings) {
			sum += oRaitingI.getRaiting();
		}
		return sum / (double)size();
	}

	/**
	 * Counts variance of raitings
	 * @return
	 */
	public double countVarianceRaitings() {
		double mean = countMeanOfRaitings();
		
		double squares = 0;
		for (ObjectRating raitingObjI : this.raitings) {
			squares += Math.pow(raitingObjI.getRaiting() - mean, 2);
		}
		return squares / (double)this.raitings.size();

	}
	
	/**
	 * Removes one given object
	 * @param raitingToDelete
	 */
	public void remove(ObjectRating raitingToDelete) {
		this.raitings.remove(raitingToDelete);
	}
	/**
	 * Removes all given objects
	 * @param raitingsToDelete
	 */
	public void removeAll(List<ObjectRating> raitingsToDelete) {
		for (ObjectRating i : raitingsToDelete) {
			remove(i);
		}
	}
	/**
	 * Removes all given objects
	 * @param raitingsToDelete
	 */
	public void removeAll(ObjectRatingList raitingsToDelete) {
		this.removeAll(raitingsToDelete.getRaitings());
	}
	
	/**
	 * Exports userIDs
	 * @return
	 */
	public Set<Integer> exportUserIDs() {
		
		Set<Integer> userIDsSet = new HashSet<>();
		for (ObjectRating raitingI : this.raitings) {
			
			int userID = raitingI.getUserID();
			userIDsSet.add(userID);
		}
		return userIDsSet;
	}

	/**
	 * Exports min and max of userIDs
	 * @return
	 */
	public Pair<Integer, Integer> exportMinAndMaxUserID() {
		
		Set<Integer> userIDs = exportUserIDs();
		if (userIDs.isEmpty()) {
			return new Pair<Integer, Integer>(Integer.MAX_VALUE, Integer.MIN_VALUE);
		}
		
		int minUserID = Collections.min(userIDs);
		int maxUserID = Collections.max(userIDs);
		
		return new Pair<Integer, Integer>(minUserID, maxUserID);
	}
	
	/**
	 * Exports itemIDs
	 * @return
	 */
	public Set<Integer> exportItemIDs() {
		
		Set<Integer> itemIDsSet = new HashSet<>();
		for (ObjectRating raitingI : this.raitings) {
			
			int itemID = raitingI.getItemID();
			itemIDsSet.add(itemID);
		}
		return itemIDsSet;
	}

	/**
	 * Exports min and max of itemIDs
	 * @return
	 */
	public Pair<Integer, Integer> exportMinAndMaxItemID() {
		
		Set<Integer> itemIDs = exportItemIDs();
		if (itemIDs.isEmpty()) {
			return new Pair<Integer, Integer>(Integer.MAX_VALUE, Integer.MIN_VALUE);
		}
		
		int minItemID = Collections.min(itemIDs);
		int maxItemID = Collections.max(itemIDs);
		
		return new Pair<Integer, Integer>(minItemID, maxItemID);
	}
	
	
	/**
	 * Exports min and max of raitings
	 * @return
	 */
	public Pair<Double, Double> exportMinAndMaxRaiting() {
		
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		for (ObjectRating raitingI : this.raitings) {
			if (max < raitingI.getRaiting()) {
				max = raitingI.getRaiting();
			}
			if (min > raitingI.getRaiting()) {
				min = raitingI.getRaiting();
			}
		}
		return new Pair<Double, Double>(min, max);
	}

	/**
	 * Export raiting value of user and item
	 * @param userID
	 * @param itemID
	 * @return
	 */
	public double exportRaiting(int userID, int itemID) {
		
		for (ObjectRating raitingI : this.raitings) {
			if (raitingI.getUserID() == userID &&
					raitingI.getItemID() == itemID) {
				return raitingI.getRaiting();
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Exports all {@link ObjectRating}s of given user 
	 * @param userID
	 * @return
	 */
	public ObjectRatingList exportObjectRaitingOfUser(int userID) {
		
		List<ObjectRating> selectedRaitings = new ArrayList<>();
		
		for (ObjectRating oRaitingI : this.raitings) {
			if (oRaitingI.getUserID() == userID) {
				selectedRaitings.add(oRaitingI);
			}
		}
		return new ObjectRatingList(selectedRaitings);
	}

	/**
	 * Exports all {@link ObjectRating}s of given users
	 * @param userIDs
	 * @return
	 */
	public ObjectRatingList exportObjectRaitingOfUsers(Set<Integer> userIDs) {
	
		ObjectRatingList result = new ObjectRatingList();
		
		for (int userIdI : userIDs) {
			ObjectRatingList raitingsOfUserI =
					exportObjectRaitingOfUser(userIdI);
			result.addAll(raitingsOfUserI);
		}
		return result;
	}
	
	public int exportFirstObjectRaitingIndexOfItem(int itemID) {
		
		for (int i = 0; i < this.raitings.size(); i++) {
			ObjectRating oRaitingI = this.raitings.get(i);
			if (oRaitingI.getItemID() == itemID) {
				return i;
			}
		}
		return -1;
	}

	public List<Integer> exportFirstObjectRaitingIndexesOfItems(List<Integer> itemIDs) {
		
		List<Integer> indexesOfItems = new ArrayList<>();
		for (int itemIDI : itemIDs) {
			
			indexesOfItems.add(
					exportFirstObjectRaitingIndexOfItem(itemIDI));
		}
		return indexesOfItems;
	}

	
	/**
	 * Exports all {@link ObjectRating}s of given item
	 * @param itemID
	 * @return
	 */
	public ObjectRatingList exportObjectRaitingOfItem(int itemID) {
		
		List<ObjectRating> selectedRaitings = new ArrayList<>();
		
		for (ObjectRating oRaitingI : this.raitings) {
			if (oRaitingI.getItemID() == itemID) {
				selectedRaitings.add(oRaitingI);
			}
		}
		return new ObjectRatingList(selectedRaitings);
	}

	/**
	 * Exports {@link ObjectRatingList} of {@link ObjectRating} wit given raiting
	 * @param raiting
	 * @return
	 */
	public ObjectRatingList exportObjectWithRaiting(int raiting) {
		
		List<ObjectRating> selectedRaitings = new ArrayList<>();
		for (ObjectRating raitingObjI : raitings) {
			if (raitingObjI.getRaiting() == raiting) {
				selectedRaitings.add(raitingObjI);
			}
		}
		return new ObjectRatingList(selectedRaitings);
	}
	
	/**
	 * Exports random {@link ObjectRating}
	 * @return
	 */
	public ObjectRating exportRandomObjectRaiting() {
		Random r = new Random();
		int index = r.nextInt(size());
		
		return this.raitings.get(index);
	}
	
	public void removeObjectRaitingOfUser(int userID) {
		
		ObjectRatingList selectedRaitings = exportObjectRaitingOfUser(userID);
		removeAll(selectedRaitings);
	}
	
	public ObjectRatingList exportIntesectionOfObjectWithIdenticalRaiting(
			ObjectRatingList raitings) {
		
		ObjectRatingList intesection = new ObjectRatingList();
		for (ObjectRating oRaitingI : this.raitings) {
			
			boolean contains = raitings
					.containsObjectWithIdenticalRaiting(oRaitingI);
			if (contains) {
				intesection.add(oRaitingI);
			}
		}
		return intesection;
	}
	
	public boolean containsObjectWithIdenticalRaiting(ObjectRating oRaiting) {
		if (oRaiting == null || ! (oRaiting.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRating.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRating oRaitingI : this.raitings) {
			boolean equalI = (oRaitingI.getItemID() == oRaiting.getItemID())
					&& (oRaitingI.getRaiting() == oRaiting.getRaiting());
			if (equalI) {
				return true;
			}
		}
		return false;
	}
	
	public void sort() {
		Collections.sort(raitings, new CmpObjectRating());
	}
	
	public void print() {
		String string = "";
		for (ObjectRating raitingI : raitings) {
			double ratingValI = raitingI.getRaiting();
			string += " " + ratingValI;
		}
		System.out.println(string);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (this.raitings == null) {
			return false;
		}
		for(ObjectRating oRaitingI : this.raitings) {
			if (oRaitingI == null || (! oRaitingI.valid(logger))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Return clove of {@link ObjectRatingList} with the same items
	 * @return
	 */
	public ObjectRatingList noDeepClone() {
		
		List<ObjectRating> raitingClones = new ArrayList<>();
		for (ObjectRating oRaitingI : this.raitings) {
			raitingClones.add(oRaitingI);
		}
		return new ObjectRatingList(raitingClones);
	}
}
