package org.distributedea.ontology.dataset;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.vertexcover.Graph;

public class DatasetVertexCover extends Dataset {

	private static final long serialVersionUID = 1L;

	private Graph graph;
	
	/** Dataset File */
	private String datasetFileName;
	
	
	@Deprecated
	public DatasetVertexCover() {
		graph = new Graph();
	}

	/**
	 * Constructor
	 * @param graph
	 */
	public DatasetVertexCover(Graph graph, File datasetFile) {
		importDatasetFile(datasetFile);
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
		
		importDatasetFile(datasetVertexCover.exportDatasetFile());
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

	
	@Deprecated
	public String getDatasetFileName() {
		return datasetFileName;
	}
	@Deprecated
	public void setDatasetFileName(String datasetFileName) {
		this.datasetFileName = datasetFileName;
	}

	/**
	 * Exports File with {@link Dataset} assignment
	 */
	@Override
	public File exportDatasetFile() {
		if (datasetFileName == null) {
			return null;
		}
		return new File(datasetFileName);
	}
	/**
	 * Imports File with {@link Dataset} assignment
	 */
	@Override
	public void importDatasetFile(File datasetFile) {
		if (datasetFile == null) {
			throw new IllegalArgumentException();
		}
		if (! datasetFile.exists() || ! datasetFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.datasetFileName = datasetFile.getAbsolutePath();
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
