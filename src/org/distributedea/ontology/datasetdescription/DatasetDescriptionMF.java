package org.distributedea.ontology.datasetdescription;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.IRatingIDs;

/**
 * Ontology which represents description of {@link DatasetMF}
 * @author stepan
 */
public class DatasetDescriptionMF implements IDatasetDescription {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Defines the filename of the training {@link DatasetMF}
	 */
	private String datasetTrainingFileName;
	
	private IRatingIDs trainingSetDef;
	
	
	/**
	 * Defines the filename of the testing {@link DatasetMF}
	 */
	private String datasetTestingFileName;
	
	private IRatingIDs testingSetDef;
	
	
	@Deprecated
	public DatasetDescriptionMF() {} // only for JADE
	
	/**
	 * Constructor
	 */
	public DatasetDescriptionMF(File datasetTrainingFile, IRatingIDs trainingSetDef,
			File datasetTestingFile, IRatingIDs testingSetDef) {
		
		importDatasetTrainingFile(datasetTrainingFile);
		setTrainingSetDef(trainingSetDef);
		
		importDatasetTestingFile(datasetTestingFile);
		setTestingSetDef(testingSetDef);
	}
	
	/**
	 * Copy Constructor
	 * @param datasetDescrMF
	 */
	public DatasetDescriptionMF(DatasetDescriptionMF datasetDescrMF) {
		if (datasetDescrMF == null ||
				! datasetDescrMF.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetDescriptionMF.class.getSimpleName() + " is not valid");
		}
		
		importDatasetTrainingFile(datasetDescrMF.exportDatasetTrainingFile());
		setTrainingSetDef(datasetDescrMF.getTrainingSetDef().deepClone());
		
		importDatasetTestingFile(datasetDescrMF.exportDatasetTestingFile());
		setTestingSetDef(datasetDescrMF.getTestingSetDef().deepClone());
	}
	
	
	public String getDatasetTrainingFileName() {
		return datasetTrainingFileName;
	}
	@Deprecated
	public void setDatasetTrainingFileName(String datasetTrainingFileName) {
		this.datasetTrainingFileName = datasetTrainingFileName;
	}

	
	public IRatingIDs getTrainingSetDef() {
		return trainingSetDef;
	}
	public void setTrainingSetDef(IRatingIDs trainingSetDef) {
		if (trainingSetDef == null ||
				! trainingSetDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IRatingIDs.class.getSimpleName() + " is not valid");
		}
		this.trainingSetDef = trainingSetDef;
	}
	

	public String getDatasetTestingFileName() {
		return datasetTestingFileName;
	}
	@Deprecated
	public void setDatasetTestingFileName(String datasetTestingFileName) {
		this.datasetTestingFileName = datasetTestingFileName;
	}
	
	
	public IRatingIDs getTestingSetDef() {
		return testingSetDef;
	}
	public void setTestingSetDef(IRatingIDs testingSetDef) {
		if (testingSetDef == null ||
				! testingSetDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IRatingIDs.class.getSimpleName() + " is not valid");
		}
		this.testingSetDef = testingSetDef;
	}

	
	/**
	 * Exports {@link File} with training {@link Dataset} assignment
	 */
	public File exportDatasetTrainingFile() {
		if (this.datasetTrainingFileName == null) {
			return null;
		}
		return new File(this.datasetTrainingFileName);
	}
	
	/**
	 * Imports {@link File} with training {@link Dataset} assignment
	 */
	public void importDatasetTrainingFile(File datasetFile) {
//		if (datasetFile == null || ! datasetFile.isFile()) {
//			System.out.println(datasetFile);
//			throw new IllegalArgumentException("Argument " +
//					File.class.getSimpleName() + " is not valid");
//		}
		this.datasetTrainingFileName = datasetFile.getPath();
	}
	
	/**
	 * Exports {@link File} with testing {@link Dataset} assignment
	 */
	public File exportDatasetTestingFile() {
		if (this.datasetTestingFileName == null) {
			return null;
		}
		return new File(this.datasetTestingFileName);
	}
	
	/**
	 * Imports {@link File} with testing {@link Dataset} assignment
	 */
	public void importDatasetTestingFile(File datasetFile) {
//		if (datasetFile == null || ! datasetFile.isFile()) {
//			System.out.println(datasetFile);
//			throw new IllegalArgumentException("Argument " +
//					File.class.getSimpleName() + " is not valid");
//		}
		this.datasetTestingFileName = datasetFile.getPath();
	}
	
	
	@Override
	public String toLogString() {
		return getDatasetTrainingFileName();
	}

	@Override
	public IDatasetDescription deepClone() {
		return new DatasetDescriptionMF(this);
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (exportDatasetTrainingFile() == null) {
			return false;
		}
		if (getTrainingSetDef() == null ||
				! getTrainingSetDef().valid(logger)) {
			return false;
		}
			
		if (exportDatasetTestingFile() == null) {
			return false;
		}
		if (getTestingSetDef() == null ||
				! getTestingSetDef().valid(logger)) {
			return false;
		}
		
		return true;
	}

}
