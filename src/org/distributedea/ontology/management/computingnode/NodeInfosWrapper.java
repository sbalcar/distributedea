package org.distributedea.ontology.management.computingnode;

import jade.content.Concept;
import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

/**
 * Ontology represents list of {@link NodeInfo}.
 * @author stepan
 *
 */
public class NodeInfosWrapper implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<NodeInfo> nodeInfos;
	
	/**
	 * Constructor
	 */
	public NodeInfosWrapper() {
		this.nodeInfos = new ArrayList<>();
	}

	/**
	 * Constructor
	 */
	public NodeInfosWrapper(List<NodeInfo> nodeInfos) {
		importNodeInfos(nodeInfos); 
	}
	/**
	 * Copy constructor
	 * @param nodeInfosWrp
	 */
	public NodeInfosWrapper(NodeInfosWrapper nodeInfosWrp) {
		if (nodeInfosWrp == null || ! nodeInfosWrp.valid()) {
			throw new IllegalArgumentException();
		}
		importNodeInfos(nodeInfosWrp.deepClone().getNodeInfos());
	}
	
	/**
	 * Returns list of {@link NodeInfo}
	 * @return
	 */
	public List<NodeInfo> getNodeInfos() {
		return nodeInfos;
	}
	@Deprecated
	public void setNodeInfos(List<NodeInfo> nodeInfos) {
		importNodeInfos(nodeInfos);
	}
	private void importNodeInfos(List<NodeInfo> nodeInfos) {
		if (nodeInfos == null) {
			throw new IllegalArgumentException();
		}
		for (NodeInfo nodeInfoI : nodeInfos) {
			if (! nodeInfoI.valid()) {
				throw new IllegalArgumentException();
			}
		}
		this.nodeInfos = nodeInfos;
	}
	
	/**
	 * Export number of cores
	 * @return
	 */
	public int exportNumberOfAllCores() {
		
		int numberOfCPU = 0;
		for (NodeInfo nodeInfoI : nodeInfos) {
			
			int numberOfCPUI = nodeInfoI.getTotalCPUNumber();
			numberOfCPU += numberOfCPUI;
		}
		
		return numberOfCPU;
	}
	
	public List<AID> exportManagersAID() {
		
		List<AID> aids = new ArrayList<>();
		
		for (NodeInfo nodeInfoI : nodeInfos) {
			AID managerOfEmptyCore = nodeInfoI.getManagerAgentAID();
			aids.add(managerOfEmptyCore);
		}
		
		return aids;
	}
	
	public List<AID> exportManagerAIDOfEachEmptyCore() {
		
		List<AID> aids = new ArrayList<>();
		
		for (NodeInfo nodeInfoI : nodeInfos) {
			
			int freeNodesI = nodeInfoI.getFreeCPUnumber();
			for (int freeCoreIndex = 0; freeCoreIndex < freeNodesI; freeCoreIndex++) {
				AID managerOfEmptyCore = nodeInfoI.getManagerAgentAID();
				aids.add(managerOfEmptyCore);
			}
		}
		
		return aids;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid() {
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public NodeInfosWrapper deepClone() {
		return new NodeInfosWrapper(this);
	}
}
