package org.distributedea.ontology.management.computingnode;

import jade.content.Concept;
import jade.core.AID;

public class NodeInfo implements Concept {

	private static final long serialVersionUID = 1L;
	
	private AID managerAgentAID;
	private int totalCPUnumber;
	private int freeCPUnumber;

	
	public AID getManagerAgentAID() {
		return managerAgentAID;
	}
	public void setManagerAgentAID(AID managerAgentAID) {
		this.managerAgentAID = managerAgentAID;
	}
	
	public int getTotalCPUNumber() {
		return totalCPUnumber;
	}
	public void setTotalCPUNumber(int totalCPUnumber) {
		this.totalCPUnumber = totalCPUnumber;
	}

	public int getFreeCPUnumber() {
		return freeCPUnumber;
	}
	public void setFreeCPUnumber(int freeCPUnumber) {
		this.freeCPUnumber = freeCPUnumber;
	}
	
}
