package org.distributedea.ontology.dataset;

import java.io.File;

import org.distributedea.logging.IAgentLogger;

/**
 * Ontology represents electric vehicle charging {@link Dataset}
 * @author stepan
 *
 */
public class DatasetEVCharging extends Dataset {

	private static final long serialVersionUID = 1L;
	
	private String datasetFileName;

	@Deprecated
	public DatasetEVCharging() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param datasetFile
	 */
	public DatasetEVCharging(File datasetFile) {
		importDatasetFile(datasetFile); 
	}
	
	
	public String getDatasetFileName() {
		return datasetFileName;
	}
	@Deprecated
	public void setDatasetFileName(String datasetFileName) {
		this.datasetFileName = datasetFileName;
	}

	
	public File exportDatasetFile() {
		return new File(datasetFileName);
	}

	public void importDatasetFile(File datasetFile) {

		if (datasetFile == null || ! datasetFile.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		this.datasetFileName = datasetFile.getPath();
	}

	
	@Override
	public boolean valid(IAgentLogger logger) {
		return exportDatasetFile() != null;
	}

	@Override
	public Dataset deepClone() {
		return new DatasetEVCharging(exportDatasetFile());
	}
	
}
