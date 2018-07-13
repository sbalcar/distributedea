package org.distributedea.ontology.dataset.matrixfactorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.distributedea.javaextension.Pair;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;

 /**
 * Optimized data model for {@link Dataset}
 * Contains duplicated data optimized to the speed of operations
 * @author stepan
 *
 */
public class RatingModel {

	private ObjectRatingList raitings;

	private Map<Integer, ObjectRatingList> userIDMapToRaiting;
	private Map<Integer, ObjectRatingList> itemIDMapToRaiting;
	
	private int numberOfUsers;
	private int numberOfItems;

	private Set<Integer> userIDS;
	private Set<Integer> itemIDS;
	
	private int minUserID;
	private int maxUserID;
	
	private int minItemID;
	private int maxItemID;
	
	private double minRaiting;
	private double maxRaiting;
	
	/**
	 * Constructor
	 * @param raitings
	 */
	public RatingModel(List<ObjectRating> raitings) {
		
		this.raitings = new ObjectRatingList(raitings);
		
		this.userIDS = this.raitings.exportUserIDs();
		this.itemIDS = this.raitings.exportItemIDs();
		
		this.numberOfUsers = userIDS.size();
		this.numberOfItems = itemIDS.size();
		
		Pair<Integer, Integer> minMaxUserIDs = this.raitings.exportMinAndMaxUserID();
		this.minUserID = minMaxUserIDs.first;
		this.maxUserID = minMaxUserIDs.second;

		Pair<Integer, Integer> minMaxItemIDs = this.raitings.exportMinAndMaxItemID();
		this.minItemID = minMaxItemIDs.first;
		this.maxItemID = minMaxItemIDs.second;
		
		Pair<Double, Double> minMaxRaiting = this.raitings.exportMinAndMaxRaiting();
		this.minRaiting = minMaxRaiting.first;
		this.maxRaiting = minMaxRaiting.second;
		
		this.userIDMapToRaiting = createUserIDMap(userIDS);
		this.itemIDMapToRaiting = createItemIDMap(itemIDS);	
	}
	
	private Map<Integer, ObjectRatingList> createUserIDMap(Set<Integer> userIDS) {
		
		Map<Integer, ObjectRatingList> userIDMap = new HashMap<>();
		for (Integer userIDI : userIDS) {
			userIDMap.put(userIDI, new ObjectRatingList());
		}
		
		for (ObjectRating raitingI : this.raitings.getRaitings()) {
			int userID = raitingI.getUserID();
			userIDMap.get(userID).add(raitingI);
		}

		return userIDMap;
	}

	private Map<Integer, ObjectRatingList> createItemIDMap(Set<Integer> itemIDs) {
		
		Map<Integer, ObjectRatingList> itemIDMap = new HashMap<>();
		for (Integer itemIDI : itemIDs) {
			itemIDMap.put(itemIDI, new ObjectRatingList());
		}
		
		for (ObjectRating raitingI : this.raitings.getRaitings()) {
			
			int itemID = raitingI.getItemID();
			itemIDMap.get(itemID).add(raitingI);
		}

		return itemIDMap;
	}

	public List<ObjectRating> exportRatingsClone() {
		
		List<ObjectRating> raitingCloneList = new ArrayList<>();
		
		for (ObjectRating oRaitingI : this.raitings.getRaitings()) {
			raitingCloneList.add(oRaitingI.deepClone());
		}
		
		return raitingCloneList;
	}

	/**
	 * Exports {@link ObjectRating} of all raitings
	 * @return
	 */
	public ObjectRatingList exportObjectRaitingList() {
		return this.raitings;
	}
	
	/**
	 * Exports the number of raitings in the dataset
	 * @return
	 */	
	public int exportNumberOfRaitings() {
		return this.raitings.size();
	}

	/**
	 * Exports the number of users in the dataset
	 * @return
	 */	
	public int exportNumberOfUsers() {
		return this.numberOfUsers;
	}

	/**
	 * Exports the number of items in the dataset
	 * @return
	 */
	public int exportNumberOfItems() {
		return this.numberOfItems;
	}
	
	/**
	 * Exports the min userID in the dataset
	 * @return
	 */
	public int exportMinUserID() {
		return this.minUserID;
	}

	/**
	 * Exports the max userID in the dataset
	 * @return
	 */
	public int exportMaxUserID() {
		return this.maxUserID;
	}
	
	/**
	 * Exports the min itemID in the dataset
	 * @return
	 */
	public int exportMinItemID() {
		return this.minItemID;
	}

	/**
	 * Exports the max itemID in the dataset
	 * @return
	 */
	public int exportMaxItemID() {
		return this.maxItemID;
	}
	
	/**
	 * Exports the min value of raitings in the dataset
	 * @return
	 */
	public double exportMinOfRaitings() {
		return this.minRaiting;
	}

	/**
	 * Exports the max value of raitings in the dataset
	 * @return
	 */
	public double exportMaxOfRaitings() {
		return this.maxRaiting;
	}

	
	/**
	 * Export all User-IDs in the dataset
	 * @return
	 */
	public Set<Integer> exportUserIDs() {
		return this.userIDS;
	}

	/**
	 * Export all Item-IDs in the dataset
	 * @return
	 */
	public Set<Integer> exportItemIDs() {
		return this.itemIDS;
	}
	
	/**
	 * Exports {@link List} of {@link ObjectRating} corresponds to given userID 
	 * @param userID
	 * @return
	 */	
	public ObjectRatingList exportRaitingsOfUser(int userID) {
		return this.userIDMapToRaiting.get(userID);
	}

	/**
	 * Exports {@link List} of {@link ObjectRating} corresponds to given itemID
	 * @param itemID
	 * @return
	 */
	public ObjectRatingList exportRaitingsOfItem(int itemID) {
		return this.itemIDMapToRaiting.get(itemID);
	}
	
	/**
	 * Exports {@link ObjectRatingList} of {@link ObjectRating} wit given raiting
	 * @param raiting
	 * @return
	 */
	public ObjectRatingList exportObjectWithRaiting(int raiting) {
		return this.raitings.exportObjectWithRaiting(raiting);
	}
	
	
	public int exportIndexOfUser(int userID) {
		return userID -1;
	}
	
	public int exportIndexOfItem(int itemID) {
		return itemID -1;
	}

	public List<Integer> exportIndexOfUsers(List<Integer> userIDs) {
		List<Integer> indexes = new ArrayList<>();
		for (int userIdI : userIDs) {
			indexes.add(exportIndexOfUser(userIdI));
		}
		return indexes;
	}
	
	public List<Integer> exportIndexOfItems(List<Integer> itemIDs) {
		List<Integer> indexes = new ArrayList<>();
		for (int  itemIdI : itemIDs) {
			indexes.add(exportIndexOfItem(itemIdI));
		}
		return indexes;
	}
}
