package org.distributedea.ontology.dataset;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.vertexcover.Graph;

public class DatasetVertexCover extends Dataset {

	private static final long serialVersionUID = 1L;

	private Graph graph;
	
	
	@Deprecated
	public DatasetVertexCover() {
		graph = new Graph();
	}

	/**
	 * Constructor
	 * @param graph
	 */
	public DatasetVertexCover(Graph graph, File datasetFile) {
		setGraph(graph);
	}

	/**
	 * Copy constructor
	 * @param datasetVertexCover
	 */
	public DatasetVertexCover(DatasetVertexCover datasetVertexCover) {
		if (datasetVertexCover == null ||
				! datasetVertexCover.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				DatasetVertexCover.class.getSimpleName() + " is not valid");
		}
		
		Graph graphClone = datasetVertexCover.getGraph().deepClone();
		
		setGraph(graphClone);
	}

	
	public Graph getGraph() {
		return graph;
	}
	@Deprecated
	public void setGraph(Graph graph) {
		if (graph == null ||
				! graph.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Graph.class.getSimpleName() + " is not valid");
		}
		this.graph = graph;
	}


	@Override
	public boolean valid(IAgentLogger logger) {
		if (graph == null || ! graph.valid(logger)) {
			return false;
		}
		return true;
	}

	@Override
	public Dataset deepClone() {
		return new DatasetVertexCover(this);
	}

}
