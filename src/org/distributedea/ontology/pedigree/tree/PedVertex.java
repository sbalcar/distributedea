package org.distributedea.ontology.pedigree.tree;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Vertex in pedigree which contains only ID of method
 * @author stepan
 *
 */
public class PedVertex implements Concept {

	private static final long serialVersionUID = 1L;

	private int methodID;
	
	private List<PedVertex> ancestors;
	
	@Deprecated
	public PedVertex() { // only for Jade
		this.ancestors = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param methodID
	 * @param ancestors
	 */
	public PedVertex(int methodID, List<PedVertex> ancestors) {
		
		this.methodID = methodID;
		this.ancestors = ancestors;
	}

	/**
	 * Copy constructor
	 * @param pedVertex
	 */
	public PedVertex(PedVertex pedVertex) {
		if (pedVertex == null || ! pedVertex.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedVertex.class.getSimpleName() + "is not valid");
		}
		
		this.methodID = pedVertex.getMethodID();
		
		List<PedVertex> ancestorsClone = new ArrayList<>();
		for (PedVertex pedVertexI : pedVertex.getAncestors()) {
			ancestorsClone.add(pedVertexI.deepClone());
		}
		this.ancestors = ancestorsClone;
	}
	
	public int getMethodID() {
		return methodID;
	}
	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}

	public List<PedVertex> getAncestors() {
		return ancestors;
	}
	public void setAncestors(List<PedVertex> ancestors) {
		this.ancestors = ancestors;
	}
	
	public List<Integer> exportMethodIDs() {
		List<Integer> ids = new ArrayList<>();
		exportMethodIDs(ids);
		return ids;
	}
	private void exportMethodIDs(List<Integer> ids) {
		ids.add(this.getMethodID());
		for (PedVertex vertexI : getAncestors()) {
			vertexI.exportMethodIDs(ids);
		}
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PedVertex deepClone() {
		return new PedVertex(this);
	}
}
