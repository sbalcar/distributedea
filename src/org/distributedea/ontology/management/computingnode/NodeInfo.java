package org.distributedea.ontology.management.computingnode;

import jade.content.Concept;

public class NodeInfo implements Concept {

	private static final long serialVersionUID = 1L;
	
	private int numberCPU;

	public int getNumberCPU() {
		return numberCPU;
	}

	public void setNumberCPU(int numberCPU) {
		this.numberCPU = numberCPU;
	}
	
}
