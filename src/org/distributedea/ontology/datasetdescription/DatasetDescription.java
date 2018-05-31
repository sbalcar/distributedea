package org.distributedea.ontology.datasetdescription;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;

/**
 * Ontology which represents description of {@link Dataset}
 * @author stepan
 */
public class DatasetDescription implements IDatasetDescription {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Defines the filename with the input {@link Dataset}
	 */
	private String datasetFileName;
	
	@Deprecated
	public DatasetDescription() {} // only for Jade
	
	/**
	 * Constructor
	 */
	public DatasetDescription(File datasetFile) {
		importDatasetFile(datasetFile);
	}

	/**
	 * Copy Constructor
	 * @param datasetDescription
	 */
	public DatasetDescription(DatasetDescription datasetDescription) {
		if (datasetDescription == null ||
				(! datasetDescription.valid(new TrashLogger()))) {
			throw new IllegalArgumentException("Argument " +
					DatasetDescription.class.getSimpleName() + " is not valid");
		}

		importDatasetFile(datasetDescription.exportDatasetFile());
	}

	
	public String getDatasetFileName() {
		return datasetFileName;
	}
	@Deprecated
	public void setDatasetFileName(String datasetFileName) {
		this.datasetFileName = datasetFileName;
	}

	
	/**
	 * Exports {@link File} with {@link Dataset} assignment
	 */
	public File exportDatasetFile() {
		if (datasetFileName == null) {
			return null;
		}
		return new File(datasetFileName);
	}
	
	/**
	 * Imports {@link File} with {@link Dataset} assignment
	 */
	public void importDatasetFile(File datasetFile) {
//		if (datasetFile == null || ! datasetFile.isFile()) {
//			System.out.println(datasetFile);
//			throw new IllegalArgumentException("Argument " +
//					File.class.getSimpleName() + " is not valid");
//		}
		this.datasetFileName = datasetFile.getPath();
	}
	
	@Override
	public String toLogString() {
		return getDatasetFileName();
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IDatasetDescription deepClone() {
		return new DatasetDescription(this);
	}

	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return getDatasetFileName() != null;
	}
	
}
