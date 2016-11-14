package org.distributedea.ontology.pedigree;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

public abstract class Pedigree implements Concept {

	private static final long serialVersionUID = 1L;
	
	public abstract boolean valid(IAgentLogger logger);
	public abstract Pedigree deepClone();
	
	
	public static Pedigree create(PedigreeParameters pedParams) {
		
		if (pedParams == null) {
			return null;
		}
		
		PedigreeCounter p = new PedigreeCounter();
		p.incrementCounterOf(pedParams.methodDescription);
		
		return p;
	}

	public static Pedigree update(Pedigree pedigree1,
			PedigreeParameters pedParams) {
		
		if (pedParams == null) {
			return null;
		}
		
		PedigreeCounter pedigCounter = (PedigreeCounter) pedigree1.deepClone();
		pedigCounter.incrementCounterOf(pedParams.methodDescription);
		return pedigCounter;
		
	}
	public static Pedigree update(Pedigree pedigree1, Pedigree pedigree2,
			PedigreeParameters pedParams) {
		
		if (pedParams == null) {
			return null;
		}
		
		PedigreeCounter pedigCounter = (PedigreeCounter) pedigree1.deepClone();
		pedigCounter.incrementCounterOf(pedParams.methodDescription);
		return pedigCounter;
	}
	
	public static Pedigree update(Pedigree pedigree1, Pedigree pedigree2,
			Pedigree pedigree3, PedigreeParameters pedParams) {
		
		if (pedParams == null) {
			return null;
		}
		
		PedigreeCounter pedigCounter = (PedigreeCounter) pedigree1.deepClone();
		pedigCounter.incrementCounterOf(pedParams.methodDescription);
		return pedigCounter;
	}
	
}
