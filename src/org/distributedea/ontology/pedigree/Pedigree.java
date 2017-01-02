package org.distributedea.ontology.pedigree;

import java.util.ArrayList;
import java.util.Arrays;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;

import jade.content.Concept;

public abstract class Pedigree implements Concept {

	private static final long serialVersionUID = 1L;
	

	/**
	 * Exports number of credit of each {@link MethodDescription}
	 * @return
	 */
	public abstract MethodDescriptionNumbers exportCreditsOfMethodDescriptions();
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public abstract boolean valid(IAgentLogger logger);
	
	/**
	 * Returns deep Clone
	 * @return
	 */
	public abstract Pedigree deepClone();	
	
	
	public static final Pedigree create(PedigreeParameters pedParams) {
		
		if (pedParams == null || pedParams.pedigreeClass == null) {
			return null;
		}

		if (pedParams.pedigreeClass == PedigreeCounter.class) {
			return new PedigreeCounter(pedParams);
		}
		
		if (pedParams.pedigreeClass == PedigreeTreeFull.class) {			
			return new PedigreeTreeFull(pedParams);
		}
		
		if (pedParams.pedigreeClass == PedigreeTree.class) {			
			return new PedigreeTree(pedParams);
		}
		
		throw new IllegalStateException();
	}
	


	public static final Pedigree update(Pedigree pedigree1,
			PedigreeParameters pedParams) {
		
		if (pedParams == null || pedParams.pedigreeClass == null) {
			return null;
		}
		
		if (pedigree1 instanceof PedigreeCounter &&
				pedParams.pedigreeClass == PedigreeCounter.class) {
			return new PedigreeCounter(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTreeFull &&
				pedParams.pedigreeClass == PedigreeTreeFull.class) {
			return new PedigreeTreeFull(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTree &&
				pedParams.pedigreeClass == PedigreeTree.class) {
			return new PedigreeTree(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1)), pedParams);
		}

		throw new IllegalStateException();
	}
	
	public static final Pedigree update(Pedigree pedigree1, Pedigree pedigree2,
			PedigreeParameters pedParams) {
		
		if (pedParams == null || pedParams.pedigreeClass == null) {
			return null;
		}
		
		if (pedigree1 instanceof PedigreeCounter &&
				pedigree2 instanceof PedigreeCounter &&
				pedParams.pedigreeClass == PedigreeCounter.class) {
			return new PedigreeCounter(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTreeFull &&
				pedigree2 instanceof PedigreeTreeFull &&
				pedParams.pedigreeClass == PedigreeTreeFull.class) {
			return new PedigreeTreeFull(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTree &&
				pedigree2 instanceof PedigreeTree &&
				pedParams.pedigreeClass == PedigreeTree.class) {
			return new PedigreeTree(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2)), pedParams);
		}
		
		throw new IllegalStateException();
	}
	
	public static final Pedigree update(Pedigree pedigree1, Pedigree pedigree2,
			Pedigree pedigree3, PedigreeParameters pedParams) {
		
		if (pedParams == null || pedParams.pedigreeClass == null) {
			return null;
		}

		if (pedigree1 instanceof PedigreeCounter &&
				pedigree2 instanceof PedigreeCounter &&
				pedigree3 instanceof PedigreeCounter &&
				pedParams.pedigreeClass == PedigreeCounter.class) {
			return new PedigreeCounter(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2, pedigree3)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTreeFull &&
				pedigree2 instanceof PedigreeTreeFull &&
				pedigree3 instanceof PedigreeTreeFull &&
				pedParams.pedigreeClass == PedigreeTreeFull.class) {
			return new PedigreeTreeFull(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2, pedigree3)), pedParams);
		}

		if (pedigree1 instanceof PedigreeTree &&
				pedigree2 instanceof PedigreeTree &&
				pedigree3 instanceof PedigreeTree &&
				pedParams.pedigreeClass == PedigreeTree.class) {
			return new PedigreeTree(new ArrayList<Pedigree>(
				    Arrays.asList(pedigree1, pedigree2, pedigree3)), pedParams);
		}

		throw new IllegalStateException();
	}
	
}
