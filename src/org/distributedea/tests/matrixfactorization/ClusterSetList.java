package org.distributedea.tests.matrixfactorization;

import java.util.ArrayList;
import java.util.List;

public class ClusterSetList {

	private List<ClusterSet> clusterSets;
	
	/**
	 * Constructor
	 */
	public ClusterSetList() {
		this.clusterSets = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param clusterSets
	 */
	public ClusterSetList(List<ClusterSet> clusterSets) {
		if (clusterSets == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (ClusterSet clusterSetI : clusterSets) {
			if (clusterSetI == null) {
				throw new IllegalArgumentException("Argument " +
						ClusterSet.class.getSimpleName() + " is not valid");
			}			
		}
		this.clusterSets = clusterSets;
	}
	
	/**
	 * Get cluster sets
	 * @return
	 */
	public List<ClusterSet> getClusterSets() {
		return this.clusterSets;
	}

	/**
	 * Get cluster set
	 * @return
	 */
	public ClusterSet getClusterSet(int index) {
		return this.clusterSets.get(index);
	}
	
	/**
	 * Add new {@link ClusterSet}
	 * @param newClusterSet
	 */
	public void addClusterSet(ClusterSet newClusterSet) {
		this.clusterSets.add(newClusterSet);
	}

	/**
	 * Size - count of cluster sets
	 * @return
	 */
	public int size() {
		return this.clusterSets.size();
	}	
	
	/**
	 * Returns cluster with minimal variance of 
	 * @return
	 */
	public ClusterSet getClusterSetWithMinClusterSizeVariance() {
		if (this.clusterSets.isEmpty()) {
			return null;
		}
		
		ClusterSet clusterSetWithMinVar = this.clusterSets.get(0);
		for (ClusterSet clusterSetI : this.clusterSets) {
			if (clusterSetI.countVariance() <
					clusterSetWithMinVar.countVariance()) {
				clusterSetWithMinVar = clusterSetI;
			}
		}
		return clusterSetWithMinVar;
	}
}
