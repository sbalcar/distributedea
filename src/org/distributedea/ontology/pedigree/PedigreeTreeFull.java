package org.distributedea.ontology.pedigree;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.pedigree.tree.PedVertex;
import org.distributedea.ontology.pedigree.treefull.PedVertexFull;

/**
 * Ontology represents pedigree of one {@link Individual}..
 * Pedigree represents information about parents in tree of
 * {@link MethodDescription}. 
 * @author stepan
 *
 */
public class PedigreeTreeFull extends Pedigree {

	private static final long serialVersionUID = 1L;

	private PedVertexFull rootOfTree;


	@Deprecated
	public PedigreeTreeFull() { // only for Jade
	}
	
	/**
	 * Constructor
	 * @param rootOfTree
	 */
	public PedigreeTreeFull(PedVertexFull rootOfTree) {
		if (rootOfTree == null || ! rootOfTree.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeTreeFull.class.getSimpleName() + "is not valid");
		}
		this.rootOfTree = rootOfTree;
	}

	PedigreeTreeFull(PedigreeParameters parameters) {
		
		this.rootOfTree = new PedVertexFull(
				parameters.methodDescription, new ArrayList<PedVertexFull>());
	}
	
	PedigreeTreeFull(List<Pedigree> pedigrees, PedigreeParameters pedParams) {
		if (pedigrees == null || pedigrees.isEmpty() || pedigrees.size() > 3) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<PedVertexFull> ancestors = new ArrayList<>();
		for (Pedigree pedigreeI : pedigrees) {
			PedigreeTreeFull pedigreeTreeFull =
					(PedigreeTreeFull) pedigreeI;		
			PedVertexFull rootCloneI =
					pedigreeTreeFull.getRootOfTree().deepClone();
			ancestors.add(rootCloneI);
		}
		
		this.rootOfTree = new PedVertexFull(pedParams.methodDescription,
				ancestors);
	}
	
	/**
	 * Copy Constructor
	 * @param pedigreeTreeFull
	 */
	public PedigreeTreeFull(PedigreeTreeFull pedigreeTreeFull) {
		if (pedigreeTreeFull == null || ! pedigreeTreeFull.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeTreeFull.class.getSimpleName() + "is not valid");
		}
		this.rootOfTree = pedigreeTreeFull.getRootOfTree().deepClone();
	}
	
	
	public PedVertexFull getRootOfTree() {
		return rootOfTree;
	}
	public void setRootOfTree(PedVertexFull rootOfTree) {
		this.rootOfTree = rootOfTree;
	}
	
	
	/**
	 * Exports {@link PedigreeTree}
	 * @return
	 */
	public PedigreeTree exportPedigreeTree() {
		
		Pair<PedVertex, MethodDescriptionNumbers> pair =
				rootOfTree.exportPedVertex();
				
		return new PedigreeTree(pair.second, pair.first);
	}
	
	public static PedigreeTreeFull importPedigreeTree(PedigreeTree pedigreeTree) {
		if (pedigreeTree == null || ! pedigreeTree.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		MethodDescriptionNumbers numbers = pedigreeTree.getNumbers();
		PedVertex root = pedigreeTree.getTree();
		
		PedVertexFull vertex = PedVertexFull.importPedVertex(root, numbers);
		
		return new PedigreeTreeFull(vertex);
	}

	/**
	 * 
	 * @return
	 */
	public PedigreeCounter exportPedigreeCounter() {

		MethodDescriptionNumbers numbers =
				rootOfTree.exportMethodDescriptionNumbers();
		
		return new PedigreeCounter(numbers);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (rootOfTree == null || ! rootOfTree.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PedigreeTreeFull deepClone() {
		return new PedigreeTreeFull(this);
	}

	@Override
	public MethodDescriptionNumbers exportCreditsOfMethodDescriptions() {

		return this.exportPedigreeCounter().exportCreditsOfMethodDescriptions();
	}
	
}
