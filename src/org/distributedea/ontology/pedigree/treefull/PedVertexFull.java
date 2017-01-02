package org.distributedea.ontology.pedigree.treefull;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.pedigree.tree.PedVertex;

/**
 * Vertex in pedigree
 * @author stepan
 *
 */
public class PedVertexFull implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescription method;
	
	private List<PedVertexFull> ancestors;
	
	@Deprecated
	public PedVertexFull() {
		this.ancestors = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param method
	 * @param ancestors
	 */
	public PedVertexFull(MethodDescription method, List<PedVertexFull> ancestors) {
		if (method == null || ! method.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");			
		}
		if (ancestors == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		this.method = method;
		this.ancestors = ancestors;
	}
	
	/**
	 * Copy Constructor
	 * @param methodVertex
	 */
	public PedVertexFull(PedVertexFull methodVertex) {
		if (methodVertex == null || ! methodVertex.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedVertexFull.class.getSimpleName() + " is not valid");
		}
		
		this.method = methodVertex.getMethod().deepClone();
		
		this.ancestors = new ArrayList<>();
		for (PedVertexFull ancestorI : methodVertex.getAncestors()) {
			this.ancestors.add(ancestorI.deepClone());
		}
	}
	
	public MethodDescription getMethod() {
		return method;
	}
	public void setMethod(MethodDescription method) {
		this.method = method;
	}

	public List<PedVertexFull> getAncestors() {
		return ancestors;
	}
	public void setAncestors(List<PedVertexFull> ancestors) {
		if (ancestors == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.ancestors = ancestors;
	}

	
	public MethodDescriptionNumbers exportMethodDescriptionNumbers() {
		
		MethodDescriptionNumbers numbers = new MethodDescriptionNumbers();
		exportMethodDescriptionNumbers(numbers);
		
		return numbers;
	}

	private void exportMethodDescriptionNumbers(MethodDescriptionNumbers numbers) {
		
		numbers.incrementCounterOf(method);
		
		for (PedVertexFull vertexFullI : ancestors) {
			vertexFullI.exportMethodDescriptionNumbers(numbers);
		}
	}

	
	/**
	 * Exports {@link PedVertex} and {@link MethodDescriptionNumbers}
	 * @return
	 */
	public Pair<PedVertex, MethodDescriptionNumbers> exportPedVertex() {
		
		MethodDescriptionNumbers numbers = new MethodDescriptionNumbers();
		
		PedVertex pedVertex = exportPedVertex(numbers);
		
		return new Pair<PedVertex, MethodDescriptionNumbers>(pedVertex, numbers);
	}
	
	private PedVertex exportPedVertex(MethodDescriptionNumbers methodNumbers) {
		
		int newUniqueNumber = methodNumbers.addMethodDescriptionWithUniqueNumber(method);
		
		List<PedVertex> vertices = new ArrayList<>();
		for (PedVertexFull methodVertexI : ancestors) {
			
			PedVertex pedVertexI =
					methodVertexI.exportPedVertex(methodNumbers);
			vertices.add(pedVertexI);
		}
		
		return new PedVertex(newUniqueNumber, vertices);
	}
	
	/**
	 * Imports {@link PedVertex} and {@link MethodDescriptionNumbers}
	 * @param pedVertex
	 * @param numbers
	 * @return
	 */
	public static PedVertexFull importPedVertex(PedVertex pedVertex, MethodDescriptionNumbers numbers) {
		
		int methodID = pedVertex.getMethodID();
		
		MethodDescriptionNumber methodDescriptionNumber =
				numbers.exportMethodDescriptionNumberOfGivenNumberID(methodID);
		
		List<PedVertexFull> ancestorNewList = new ArrayList<>();
		for (PedVertex ancestorI : pedVertex.getAncestors()) {
			PedVertexFull ancestorNewI = PedVertexFull.importPedVertex(ancestorI, numbers);
			ancestorNewList.add(ancestorNewI);
		}
		
		return new PedVertexFull(methodDescriptionNumber.getDescription(), ancestorNewList);
		
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (method == null || ! method.valid(logger)) {
			return false;
		}
		
		if (ancestors == null) {
			return false;
		}
		for (PedVertexFull ancestorI : ancestors) {
			if (ancestorI == null || ! ancestorI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PedVertexFull deepClone() {
		return new PedVertexFull(this);
	}
}
