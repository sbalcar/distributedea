package org.distributedea.ontology.management.computingnode;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

public class NodeInfosWrapper {

	private List<NodeInfo> nodeInfos;
	
	public List<NodeInfo> getNodeInfos() {
		return nodeInfos;
	}
	public void setNodeInfos(List<NodeInfo> nodeInfos) {
		this.nodeInfos = nodeInfos;
	}

	
	public int exportNumberOfCores() {
		
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
	
}
