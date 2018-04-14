package org.distributedea.ontology.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaitingList;

/**
 * Ontology represents matrix factorization {@link Dataset}
 * @author stepan
 *
 */
public class DatasetMF extends Dataset {

	private static final long serialVersionUID = 1L;

	private String ratingFileName;

	/** Dataset optimized Model - no ontology **/
	private DatasetModel datasetModel;
	
	
	@Deprecated
	public DatasetMF() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param problemFile
	 */
	public DatasetMF(List<ObjectRaiting> raitings, File problemFile) {
		setRaitings(raitings);
		importDatasetFile(problemFile);
	}

	/**
	 * Copy Constructor
	 * @param dataset
	 */
	public DatasetMF(DatasetMF dataset) {
		if (dataset == null || ! dataset.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetMF.class.getSimpleName() + " is not valid");
		}
		List<ObjectRaiting> raitingsCloneList = new ArrayList<>();
		for (ObjectRaiting raitingI : dataset.getRaitings()) {
			raitingsCloneList.add(raitingI.deepClone());
		}
		
		setRaitings(raitingsCloneList);
		importDatasetFile(dataset.exportDatasetFile());
	}
	
	/**
	 * Exports {@link ObjectRaiting} containing all raitings
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingList() {
		return this.datasetModel.exportObjectRaitingList();
	}

	/**
	 * Exports clone of {@link ObjectRaiting} containing all raitings
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingListClone() {
		return this.datasetModel.exportObjectRaitingList().noDeepClone();
	}
	
	
	public String getRatingFileName() {
		return ratingFileName;
	}
	
	@Deprecated
	public void setRatingFileName(String ratingFileName) {
		this.ratingFileName = ratingFileName;
	}

	public List<ObjectRaiting> getRaitings() {
		return datasetModel.exportRaitingsClone();
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
		
		this.datasetModel = new DatasetModel(raitings);
	}

	
	@Override
	public File exportDatasetFile() {
		return new File(ratingFileName);
	}

	@Override
	public void importDatasetFile(File problemFile) {

		if (problemFile == null || ! problemFile.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		this.ratingFileName = problemFile.getPath();
	}
	

	/**
	 * Exports the number of raitings in the dataset
	 * @return
	 */	
	public int exportNumberOfRaitings() {
		return this.datasetModel.exportNumberOfRaitings();
	}
	
	/**
	 * Exports the number of users in the dataset
	 * @return
	 */
	public int exportNumberOfUsers() {
		return this.datasetModel.exportNumberOfUsers();
	}

	/**
	 * Exports the number of items in the dataset
	 * @return
	 */
	public int exportNumberOfItems() {
		return this.datasetModel.exportNumberOfItems();
	}

	
	/**
	 * Exports the min userID in the dataset
	 * @return
	 */
	public int exportMinUserID() {
		return this.datasetModel.exportMinUserID();
	}

	/**
	 * Exports the max userID in the dataset
	 * @return
	 */
	public int exportMaxUserID() {
		return this.datasetModel.exportMaxUserID();
	}

	
	/**
	 * Exports the min itemID in the dataset
	 * @return
	 */
	public int exportMinItemID() {
		return this.datasetModel.exportMinItemID();
	}

	/**
	 * Exports the max itemID in the dataset
	 * @return
	 */
	public int exportMaxItemID() {
		return this.datasetModel.exportMaxItemID();
	}
	
	
	/**
	 * Exports the min value of raitings in the dataset
	 * @return
	 */
	public double exportMinOfRaitings() {
		return this.datasetModel.exportMinOfRaitings();
	}

	/**
	 * Exports the max value of raitings in the dataset
	 * @return
	 */
	public double exportMaxOfRaitings() {
		return this.datasetModel.exportMaxOfRaitings();
	}

	
	/**
	 * Exports {@link ObjectRaitingList} of {@link ObjectRaiting} corresponds to given userID 
	 * @param userID
	 * @return
	 */
	public ObjectRaitingList exportRaitingsOfUser(int userID) {
		return this.datasetModel.exportRaitingsOfUser(userID);
	}
	
	/**
	 * Exports {@link List} of {@link ObjectRaiting} corresponds to given itemID
	 * @param itemID
	 * @return
	 */
	public ObjectRaitingList exportRaitingsOfItem(int itemID) {
		return this.datasetModel.exportRaitingsOfItem(itemID);
	}
	
	/**
	 * Exports {@link ObjectRaitingList} of {@link ObjectRaiting} wit given raiting
	 * @param raiting
	 * @return
	 */
	public ObjectRaitingList exportObjectWithRaiting(int raiting) {
		return this.datasetModel.exportObjectWithRaiting(raiting);
	}
	
	/**
	 * Export all User-IDs in the dataset
	 * @return
	 */
	public Set<Integer> exportUserIDs() {
		return this.datasetModel.exportUserIDs();
	}

	/**
	 * Export all Item-IDs in the dataset
	 * @return
	 */
	public Set<Integer> exportItemIDs() {
		return this.datasetModel.exportItemIDs();
	}	
	
	public int exportIndexOfUser(int userID) { //, ProblemMatrixFactorization problem) {
		return userID -1;
	}
	
	public int exportIndexOfItem(int itemID) { //, ProblemMatrixFactorization problem) {
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
	
	
	@Override
	public boolean valid(IAgentLogger logger) {

		File file = exportDatasetFile();
		return (file != null) && file.isFile();
	}

	@Override
	public DatasetMF deepClone() {
		
		return new DatasetMF(this);
	}

}


/**
 * Optimized data model for {@link Dataset}
 * Contains duplicated data optimized to the speed of operations

 * @author stepan
 *
 */
class DatasetModel {

	private ObjectRaitingList raitings;

	private Map<Integer, ObjectRaitingList> userIDMapToRaiting;
	private Map<Integer, ObjectRaitingList> itemIDMapToRaiting;
	
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
	DatasetModel(List<ObjectRaiting> raitings) {
		
		this.raitings = new ObjectRaitingList(raitings);
		
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
	
	private Map<Integer, ObjectRaitingList> createUserIDMap(Set<Integer> userIDS) {
		
		Map<Integer, ObjectRaitingList> userIDMap = new HashMap<>();
		for (Integer userIDI : userIDS) {
			userIDMap.put(userIDI, new ObjectRaitingList());
		}
		
		for (ObjectRaiting raitingI : this.raitings.getRaitings()) {
			int userID = raitingI.getUserID();
			userIDMap.get(userID).add(raitingI);
		}

		return userIDMap;
	}

	private Map<Integer, ObjectRaitingList> createItemIDMap(Set<Integer> itemIDs) {
		
		Map<Integer, ObjectRaitingList> itemIDMap = new HashMap<>();
		for (Integer itemIDI : itemIDs) {
			itemIDMap.put(itemIDI, new ObjectRaitingList());
		}
		
		for (ObjectRaiting raitingI : this.raitings.getRaitings()) {
			
			int itemID = raitingI.getItemID();
			itemIDMap.get(itemID).add(raitingI);
		}

		return itemIDMap;
	}

	public List<ObjectRaiting> exportRaitingsClone() {
		
		List<ObjectRaiting> raitingCloneList = new ArrayList<>();
		
		for (ObjectRaiting oRaitingI : this.raitings.getRaitings()) {
			raitingCloneList.add(oRaitingI.deepClone());
		}
		
		return raitingCloneList;
	}

	/**
	 * Exports {@link ObjectRaiting} of all raitings
	 * @return
	 */
	public ObjectRaitingList exportObjectRaitingList() {
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
	 * Exports {@link List} of {@link ObjectRaiting} corresponds to given userID 
	 * @param userID
	 * @return
	 */	
	public ObjectRaitingList exportRaitingsOfUser(int userID) {
		return this.userIDMapToRaiting.get(userID);
	}

	/**
	 * Exports {@link List} of {@link ObjectRaiting} corresponds to given itemID
	 * @param itemID
	 * @return
	 */
	public ObjectRaitingList exportRaitingsOfItem(int itemID) {
		return this.itemIDMapToRaiting.get(itemID);
	}
	
	/**
	 * Exports {@link ObjectRaitingList} of {@link ObjectRaiting} wit given raiting
	 * @param raiting
	 * @return
	 */
	public ObjectRaitingList exportObjectWithRaiting(int raiting) {
		return this.raitings.exportObjectWithRaiting(raiting);
	}
	
}
