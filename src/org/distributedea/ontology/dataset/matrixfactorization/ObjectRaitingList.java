package org.distributedea.ontology.dataset.matrixfactorization;

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


/**
 * Structure represents the {@link List}t of Raiting objects
 * @author stepan
 *
 */
public class ObjectRaitingList implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<ObjectRaiting> raitings;
	
	/**
	 * Constructor
	 */
	public ObjectRaitingList() {
		this.raitings = new ArrayList<ObjectRaiting>();
	}

	/**
	 * Constructor
	 * @param raitings
	 */
	public ObjectRaitingList(List<ObjectRaiting> raitings) {
		this.setRaitings(raitings);
	}
	
	
	public List<ObjectRaiting> getRaitings() {
		return raitings;
	}
	@Deprecated
	public void setRaitings(List<ObjectRaiting> raitings) {
		if (raitings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRaiting raitingI : raitings) {
			if (raitingI == null || (! raitingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRaiting.class.getSimpleName() + " is not valid");
			}
		}
		this.raitings = raitings;
	}
	
	/**
	 * Add {@link ObjectRaiting}
	 * @param raiting
	 */
	public void add(ObjectRaiting raiting) {
		if (raiting == null || ! (raiting.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRaiting.class.getSimpleName() + " is not valid");			
		}
		this.raitings.add(raiting);
	}
	
	/**
	 * Add {@link ObjectRaitingList}
	 * @param raitings
	 */
	public void addAll(ObjectRaitingList raitings) {
		if (raitings == null || ! (raitings.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRaitingList.class.getSimpleName() + " is not valid");			
		}
		this.raitings.addAll(raitings.getRaitings());
	}
	
	/**
	 * Returns number of {@link ObjectRaiting} inside
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
		for (ObjectRaiting oRaitingI : getRaitings()) {
			
			squares += Math.pow(predictedRaiting -oRaitingI.getRaiting(), 2);
		}
		return Math.pow(squares / size(), 0.5);
	}
	
	/**
	 * Count RMSE
	 * @param predictedRaitings
	 * @return
	 */
	public double countRMSEForPredictedValues(ObjectRaitingList predictedRaitings) {
		if (size() == 0) {
			return 0;
		}

		double squares = 0;
		for (ObjectRaiting oRaitingI : getRaitings()) {
			
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
			ObjectRaitingList foreignRaitings) {
		if (size() == 0) {
			return 0;
		}
		
		double squares = 0;
		for (ObjectRaiting oRaitingI : getRaitings()) {
			
			ObjectRaitingList raitingOfItemI = foreignRaitings
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
		for (ObjectRaiting oRaitingI : this.raitings) {
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
		for (ObjectRaiting raitingObjI : this.raitings) {
			squares += Math.pow(raitingObjI.getRaiting() - mean, 2);
		}
		return squares / (double)this.raitings.size();

	}
	
	/**
	 * Removes one given object
	 * @param raitingToDelete
	 */
	public void remove(ObjectRaiting raitingToDelete) {
		this.raitings.remove(raitingToDelete);
	}
	/**
	 * Removes all given objects
	 * @param raitingsToDelete
	 */
	public void removeAll(List<ObjectRaiting> raitingsToDelete) {
		for (ObjectRaiting i : raitingsToDelete) {
			remove(i);
		}
	}
	/**
	 * Removes all given objects
	 * @param raitingsToDelete
	 */
	public void removeAll(ObjectRaitingList raitingsToDelete) {
		this.removeAll(raitingsToDelete.getRaitings());
	}
	
	/**
	 * Exports userIDs
	 * @return
	 */
	public Set<Integer> exportUserIDs() {
		
		Set<Integer> userIDsSet = new HashSet<>();
		for (ObjectRaiting raitingI : this.raitings) {
			
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
		for (ObjectRaiting raitingI : this.raitings) {
			
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
		for (ObjectRaiting raitingI : this.raitings) {
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
		
		for (ObjectRaiting raitingI : this.raitings) {
			if (raitingI.getUserID() == userID &&
					raitingI.getItemID() == itemID) {
				return raitingI.getRaiting();
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Exports all {@link ObjectRaiting}s of given user 
	 * @param userID
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingOfUser(int userID) {
		
		List<ObjectRaiting> selectedRaitings = new ArrayList<>();
		
		for (ObjectRaiting oRaitingI : this.raitings) {
			if (oRaitingI.getUserID() == userID) {
				selectedRaitings.add(oRaitingI);
			}
		}
		return new ObjectRaitingList(selectedRaitings);
	}

	/**
	 * Exports all {@link ObjectRaiting}s of given users
	 * @param userIDs
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingOfUsers(Set<Integer> userIDs) {
	
		ObjectRaitingList result = new ObjectRaitingList();
		
		for (int userIdI : userIDs) {
			ObjectRaitingList raitingsOfUserI =
					exportObjectRaitingOfUser(userIdI);
			result.addAll(raitingsOfUserI);
		}
		return result;
	}
	
	/**
	 * Exports all {@link ObjectRaiting}s of given item
	 * @param itemID
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingOfItem(int itemID) {
		
		List<ObjectRaiting> selectedRaitings = new ArrayList<>();
		
		for (ObjectRaiting oRaitingI : this.raitings) {
			if (oRaitingI.getItemID() == itemID) {
				selectedRaitings.add(oRaitingI);
			}
		}
		return new ObjectRaitingList(selectedRaitings);
	}

	/**
	 * Exports {@link ObjectRaitingList} of {@link ObjectRaiting} wit given raiting
	 * @param raiting
	 * @return
	 */
	public ObjectRaitingList exportObjectWithRaiting(int raiting) {
		
		List<ObjectRaiting> selectedRaitings = new ArrayList<>();
		for (ObjectRaiting raitingObjI : raitings) {
			if (raitingObjI.getRaiting() == raiting) {
				selectedRaitings.add(raitingObjI);
			}
		}
		return new ObjectRaitingList(selectedRaitings);
	}
	
	/**
	 * Exports random {@link ObjectRaiting}
	 * @return
	 */
	public ObjectRaiting exportRandomObjectRaiting() {
		Random r = new Random();
		int index = r.nextInt(size());
		
		return this.raitings.get(index);
	}
	
	public void removeObjectRaitingOfUser(int userID) {
		
		ObjectRaitingList selectedRaitings = exportObjectRaitingOfUser(userID);
		removeAll(selectedRaitings);
	}
	
	public ObjectRaitingList exportIntesectionOfObjectWithIdenticalRaiting(
			ObjectRaitingList raitings) {
		
		ObjectRaitingList intesection = new ObjectRaitingList();
		for (ObjectRaiting oRaitingI : this.raitings) {
			
			boolean contains = raitings
					.containsObjectWithIdenticalRaiting(oRaitingI);
			if (contains) {
				intesection.add(oRaitingI);
			}
		}
		return intesection;
	}
	
	public boolean containsObjectWithIdenticalRaiting(ObjectRaiting oRaiting) {
		if (oRaiting == null || ! (oRaiting.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					ObjectRaiting.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRaiting oRaitingI : this.raitings) {
			boolean equalI = (oRaitingI.getItemID() == oRaiting.getItemID())
					&& (oRaitingI.getRaiting() == oRaiting.getRaiting());
			if (equalI) {
				return true;
			}
		}
		return false;
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
		for(ObjectRaiting oRaitingI : this.raitings) {
			if (oRaitingI == null || (! oRaitingI.valid(logger))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Return clove of {@link ObjectRaitingList} with the same items
	 * @return
	 */
	public ObjectRaitingList noDeepClone() {
		
		List<ObjectRaiting> raitingClones = new ArrayList<>();
		for (ObjectRaiting oRaitingI : this.raitings) {
			raitingClones.add(oRaitingI);
		}
		return new ObjectRaitingList(raitingClones);
	}
}
