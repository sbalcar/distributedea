package org.distributedea.ontology.dataset.vertexcover;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

public class Vertex implements Concept {

	private static final long serialVersionUID = 1L;

	private int verexID;
	
	private List<Integer> edges;
	
	@Deprecated
	public Vertex() {
		setEdges(new ArrayList<Integer>());
	}

	/**
	 * Constructor
	 * @param verexID
	 */
	public Vertex(int verexID) {
		setVerexID(verexID);
		setEdges(new ArrayList<Integer>());
	}
	
	/**
	 * Copy constructor
	 * @param vertex
	 */
	public Vertex(Vertex vertex) {
		if (vertex == null || ! vertex.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Vertex.class.getSimpleName() + " is not valid");
		}
		setVerexID(vertex.getVerexID());
		setEdges(new ArrayList<Integer>());
		
		for (int edgeI : vertex.getEdges()) {
			edges.add(edgeI);
		}
	}
	

	public int getVerexID() {
		return verexID;
	}
	@Deprecated
	public void setVerexID(int verexID) {
		this.verexID = verexID;
	}
	
	public List<Integer> getEdges() {
		return edges;
	}
	@Deprecated
	public void setEdges(List<Integer> edges) {
		if (edges == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}

		this.edges = edges;
	}
	
	public void addVertex(Vertex vertex) {
		if (vertex == null || ! vertex.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Vertex.class.getSimpleName() + " is not valid");
		}
		
		this.edges.add(vertex.getVerexID());
	}
	
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	public Vertex deepClone() {
		return new Vertex(this);
	}
}
