package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Structure represents a set of clusters
 * @author stepan
 *
 */
public class ClusterSet {
	
	private List<Set<Integer>> clustersOfIDs;
	
	/**
	 * Constructor
	 * @param clustersOfIDs
	 */
	public ClusterSet(List<Set<Integer>> clustersOfIDs) {
		if (clustersOfIDs == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.clustersOfIDs = clustersOfIDs;
	}
	
	/**
	 * Constructor - creates one cluster for each item in given set
	 * @param identifiers
	 */
	public ClusterSet(Set<Integer> identifiers) {
		if (identifiers == null) {
			throw new IllegalArgumentException("Argument " +
					Set.class.getSimpleName() + " is not valid");
		}
		for (Integer identifierI : identifiers) {
			if (identifierI == null || identifierI < 0) {
				throw new IllegalArgumentException("Argument " +
						Integer.class.getSimpleName() + " is not valid");
			}
		}
		
		List<Set<Integer>> clusters = new ArrayList<>();
		for (int idI : identifiers) {
			
			Set<Integer> clusterI = new HashSet<Integer>();
			clusterI.add(idI);
			
			clusters.add(clusterI);
		}
		
		this.clustersOfIDs = clusters;
	}
	
	/**
	 * Get clusters of IDs
	 * @return
	 */
	public List<Set<Integer>> getClustersOfIDs() {
		return this.clustersOfIDs;
	}
		
	
	/**
	 * Exports cluster of given id
	 * @param id
	 * @return
	 */
	public Set<Integer> exportClusterOfID(int id) {
		
		Set<Integer> set = getClusterOfID(id);
		return new HashSet<>(set);		
	}
	
	private Set<Integer> getClusterOfID(int id) {
		
		for (Set<Integer> setI : this.clustersOfIDs) {
			if (setI.contains(id)) {
				return setI;
			}
		}
		return null;
	}
	
	/**
	 * Add new identifier to contained cluster
	 * @param newId
	 * @param clusterId
	 */
	public void addNewIdToCluster(int newId, int clusterId) {
		Set<Integer> cluster = getClusterOfID(clusterId);
		if (cluster == null) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		getClusterOfID(clusterId).add(newId);
	}
	
	/**
	 * Unite two contained clusters
	 * @param id1
	 * @param id2
	 */
	public void uniteClusters(int id1, int id2) {
		
		Set<Integer> cluster1 = getClusterOfID(id1);
		if (cluster1 == null) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		Set<Integer> cluster2 = getClusterOfID(id2);
		if (cluster2 == null) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		cluster2.addAll(cluster1);
		this.clustersOfIDs.remove(cluster1);
		this.clustersOfIDs.remove(cluster2);
		
		Set<Integer> clustersUnited = new HashSet<>();
		clustersUnited.addAll(cluster1);
		clustersUnited.addAll(cluster2);
		
		this.clustersOfIDs.add(clustersUnited);
	}

	/**
	 * Returns count of clusters
	 * @return
	 */
	public int getCountOfClusters() {
		return getClustersOfIDs().size();
	}
	
	/**
	 * Returns cluster sizes
	 * @return
	 */
	public List<Integer> getClusterSizes() {
		List<Integer> sizes = new ArrayList<>();
		for (Set<Integer> clusterI : getClustersOfIDs()) {
			sizes.add(clusterI.size());
		}
		return sizes;
	}
	
	/**
	 * Returns count of IDs
	 * @return
	 */
	public int getCountOfIDs() {
		int numberOfIDs = 0;
		for (Set<Integer> clusterI : getClustersOfIDs()) {
			numberOfIDs += clusterI.size();
		}
		return numberOfIDs;
	}

	/**
	 * Count variance of cluster sizes
	 * @return
	 */
	public double countVariance() {
		int numberOfIDs = getCountOfIDs();
		int numberOfClusters = getCountOfClusters();
		
		double sizeOfClusterE = numberOfIDs / numberOfClusters;
		
		double squares = 0;
		for (Set<Integer> clusterI : getClustersOfIDs()) {
			squares += Math.pow(clusterI.size() -sizeOfClusterE, 2);
		}
		
		return squares / numberOfClusters;
	}
	
	/**
	 * Prints cluster structure
	 */
	public void print() {
		String clusterSizesStr = "";
		for (Set<Integer> clusterI : clustersOfIDs) {
			clusterSizesStr += clusterI.size() + ", ";
		}
		clusterSizesStr = "[" + clusterSizesStr + "]";
		clusterSizesStr = clusterSizesStr.replaceFirst(", ]", "]");
		
		System.out.println("ClusterSizes: " + clusterSizesStr);
		System.out.println("VarianceOfClusterSizes: " + countVariance());
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File jobFile) throws Exception {

		String xml = exportXML();
		
		PrintWriter file = new PrintWriter(jobFile.getAbsolutePath());
		file.println(xml);
		file.close();
		
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static ClusterSet importXML(File file)
			throws Exception {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		return importXML(xml);
	}
	
	/**
	 * Import the {@link Job} from the String
	 */
	public static ClusterSet importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
				
		return (ClusterSet) xstream.fromXML(xml);
	}
}

