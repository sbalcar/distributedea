package org.distributedea.ontology.dataset.vertexcover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

public class Graph implements Concept {

	private static final long serialVersionUID = 1L;

	private List<Vertex> vertices;
	
	/**
	 * Constructor
	 */
	public Graph() {
		setVertices(new ArrayList<Vertex>()); 
	}

	/**
	 * Copy Constructor
	 * @param graph
	 */
	public Graph(Graph graph) {
		if (graph == null || ! graph.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Graph.class.getSimpleName() + " is not valid");			
		}
		setVertices(new ArrayList<Vertex>());
		
		for (Vertex vertexI : graph.getVertices()) {
			addVertex(vertexI.deepClone());
		}
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	@Deprecated
	public void setVertices(List<Vertex> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.vertices = vertices;
	}

	public Vertex exportVertex(int vertexID) {
		
		for (Vertex vertexI : vertices) {
			if (vertexI.getVerexID() == vertexID) {
				return vertexI;
			}
		}
		return null;
	}

	public boolean contains(int vertexID) {
		
		if (exportVertex(vertexID) != null) {
			return true;
		}
		return false;
	}
	public void addVertex(Vertex vertex) {
		if (vertex == null || ! vertex.valid(new TrashLogger()) ||
				contains(vertex.getVerexID())) {
			vertex.valid(new TrashLogger());
			throw new IllegalArgumentException("Argument " +
					Vertex.class.getSimpleName() + " is not valid");			
		}
		
		vertices.add(vertex);
	}
	
	public void addEdge(int vertexFromID, int vertexToID) {
		
		Vertex vertexFrom = exportVertex(vertexFromID);
		if (vertexFrom == null) {
			vertexFrom = new Vertex(vertexFromID);
			addVertex(vertexFrom);
		}
		
		Vertex vertexTo = exportVertex(vertexToID);
		if (vertexTo == null) {
			vertexTo = new Vertex(vertexToID);
			addVertex(vertexTo);
		}
		
		vertexFrom.addVertex(vertexTo);
	}
	
	public int numberOfVertices() {
		return getVertices().size();
	}
	
	/**
	 * Exports IDs of vertices
	 * @return
	 */
	public List<Integer> exportVertices() {
		
		List<Integer> allVertices = new ArrayList<>();
		
		for (Vertex vertexI : getVertices()) {
			allVertices.add(vertexI.getVerexID());
		}
		
		return allVertices;
	}
	
	public void importVertices(List<Integer> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (int vertexID : vertices) {
			addVertex(new Vertex(vertexID));
		}
	}
	
	public List<Integer> exportVerticesInRandomOrder() {
		
		List<Integer> exportedVertices = exportVertices();
		Collections.shuffle(exportedVertices);
		
		return exportedVertices;
	}
	
	/**
	 * Exports edges as IDs pairs of edges
	 * @return
	 */
	public List<Pair<Integer, Integer>> exportEdges() {
		
		List<Pair<Integer, Integer>> allEdges = new ArrayList<>();
		for (Vertex vertexI : getVertices()) {
			
			for (int edgeJ : vertexI.getEdges()) {
				Pair<Integer, Integer> edge = new Pair<Integer, Integer>(
						vertexI.getVerexID(), edgeJ);
				allEdges.add(edge);
			}

		}
		
		return allEdges;
	}

	public List<Integer> exportNeighboursFrom(List<Integer> vertexIDsTo) {
		if (vertexIDsTo == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}

		List<Integer> neighboursFrom = new ArrayList<>();
		for (Vertex vertexI : getVertices()) {
			if (! Collections.disjoint(vertexI.getEdges(), vertexIDsTo)) {
				neighboursFrom.add(vertexI.getVerexID());
			}
		}

		return neighboursFrom;
	}
	
	public List<Integer> exportNeighboursFrom(Integer vertexIDTo) {
		if (vertexIDTo == null) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		return new ArrayList<Integer>(vertexIDTo);
	}
	
	public List<Integer> exportNeighboursTo(List<Integer> vertexIDsFrom) {
		if (vertexIDsFrom == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		Set<Integer> result = new HashSet<>();
		for (Integer vertexId : vertexIDsFrom) {
			result.addAll(
					exportNeighboursTo(vertexId));
		}
		
		return new ArrayList<>(result);
	}
	
	public List<Integer> exportNeighboursTo(int vertexIdFrom) {
		if (! contains(vertexIdFrom)) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		Vertex vertex = exportVertex(vertexIdFrom);
		
		return vertex.getEdges();
	}
	
	public Set<Integer> vertexCover(List<Integer> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Argument " +
				List.class.getSimpleName() + " is not valid");
		}
		
		Set<Integer> coveredVertices = new LinkedHashSet<>();
		for (Integer vertexID : vertices) {
			
			List<Integer> neighbours = exportNeighboursTo(vertexID);
			coveredVertices.addAll(neighbours);
		}
		
		return coveredVertices;
	}
	
	public Set<Integer> correctVertexCover(Set<Integer> vertexCover,
			List<Integer> candidates) {
		
		Set<Integer> vertexCoverNew = new HashSet<>(vertexCover);
		Set<Integer> coveredVertices = vertexCover(new ArrayList<>(vertexCover));
		
		for (int neighbourIdI : candidates) {
			if (coveredVertices.size() == numberOfVertices()) {
				break;
			}
			
			List<Integer> coveredNew = exportNeighboursTo(neighbourIdI);
			if (! coveredVertices.containsAll(coveredNew)) {
				
				vertexCoverNew.add(neighbourIdI);
				coveredVertices.addAll(coveredNew);
			}
		}
		
		return vertexCoverNew;
	}
	
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	public Graph deepClone() {
		return new Graph(this);
	}
}
