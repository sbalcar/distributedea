package org.distributedea.ontology.pedigree;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.pedigree.tree.PedVertex;
import org.distributedea.ontology.pedigree.treefull.PedVertexFull;

/**
 * Ontology represents pedigree of one {@link Individual}.
 * Pedigree is designed to avoid unnecessary repetition of
 * {@link MethodDescription} structure.
 * @author stepan
 *
 */
public class PedigreeTree extends Pedigree {
	
	private static final long serialVersionUID = 1L;

	private MethodDescriptionNumbers numbers;
	
	private PedVertex tree;

	
	@Deprecated
	public PedigreeTree() {} // Only for Jade
	
	/**
	 * Constructor
	 * @param numbers
	 * @param tree
	 */
	public PedigreeTree(MethodDescriptionNumbers numbers, PedVertex tree) {
		if (numbers == null) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionNumbers.class.getSimpleName() + "is not valid");
		}
		if (tree == null) {
			throw new IllegalArgumentException("Argument " +
					PedVertexFull.class.getSimpleName() + "is not valid");
		}
		this.numbers = numbers;
		this.tree = tree;
	}	
	
	PedigreeTree(PedigreeParameters pedParams) {
		this.numbers = new MethodDescriptionNumbers();
		int methodID = this.numbers.addMethodDescriptionWithUniqueNumber(
				pedParams.methodDescription);
		this.tree = new PedVertex(methodID, new ArrayList<PedVertex>());
	}
	
	PedigreeTree(List<Pedigree> pedigrees, PedigreeParameters pedParams) {
		if (pedigrees == null || pedigrees.isEmpty() || pedigrees.size() > 3) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<Pedigree> ancestors = new ArrayList<>();
		for (Pedigree pedigreeI : pedigrees) {
			PedigreeTree pedigreeTreeI = (PedigreeTree) pedigreeI;
			
			PedigreeTreeFull pedigreeTreeFullI =
					PedigreeTreeFull.importPedigreeTree(pedigreeTreeI);
			ancestors.add(pedigreeTreeFullI);
		}

		PedigreeTreeFull pedigreeTreeFullNew = new PedigreeTreeFull(
				ancestors, pedParams);
		PedigreeTree pedigreeTreeNew = pedigreeTreeFullNew.exportPedigreeTree();


		this.numbers = pedigreeTreeNew.getNumbers();
		this.tree = pedigreeTreeNew.getTree();
		
	}
	
	/**
	 * Copy Constructor
	 * @param pedigreeTree
	 */
	public PedigreeTree(PedigreeTree pedigreeTree) {
		if (pedigreeTree == null || ! pedigreeTree.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeTree.class.getSimpleName() + "is not valid");			
		}
		
		this.numbers = pedigreeTree.getNumbers().deepClone();
		this.tree = pedigreeTree.getTree().deepClone();
	}
	
	public MethodDescriptionNumbers getNumbers() {
		return numbers;
	}
	public void setNumbers(MethodDescriptionNumbers numbers) {
		this.numbers = numbers;
	}

	public PedVertex getTree() {
		return tree;
	}
	public void setTree(PedVertex tree) {
		this.tree = tree;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		//if (numbers == null || ! numbers.valid(logger)) {
		//	return false;
		//}
		
		//List<Integer> ids = this.getTree().exportMethodIDs();
		//if (! this.numbers.containsMethodDescriptionsWithNumbers(ids)) {
		//	return false;
		//}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PedigreeTree deepClone() {
		return new PedigreeTree(this);
	}

	@Override
	public MethodDescriptionNumbers exportCreditsOfMethodDescriptions() {
		
		PedigreeTreeFull pedTreeFull = PedigreeTreeFull.importPedigreeTree(this);
		return pedTreeFull.exportCreditsOfMethodDescriptions();
	}
	
}
