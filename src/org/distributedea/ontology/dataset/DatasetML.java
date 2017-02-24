package org.distributedea.ontology.dataset;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

public class DatasetML extends Dataset {

	private static final long serialVersionUID = 1L;

	private String arffFileName;

	@Deprecated
	public DatasetML() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param problemFile
	 */
	public DatasetML(File problemFile) {
		importProblemFile(problemFile);
	}

	/**
	 * Copy Constructor
	 * @param dataset
	 */
	public DatasetML(DatasetML dataset) {
		if (dataset == null || ! dataset.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetML.class.getSimpleName() + " is not valid");
		}
		importProblemFile(dataset.exportProblemFile());
	}

	
	@Override
	public File exportProblemFile() {
		return new File(arffFileName);
	}

	@Override
	public void importProblemFile(File problemFile) {

		if (problemFile == null || ! problemFile.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		this.arffFileName = problemFile.getPath();
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		
		File file = exportProblemFile();
		return (file != null) && file.isFile();
	}

	@Override
	public Dataset deepClone() {
		return new DatasetML(this);
	}

}
