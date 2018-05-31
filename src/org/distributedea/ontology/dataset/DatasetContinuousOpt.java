package org.distributedea.ontology.dataset;

import java.io.File;
import java.io.FileNotFoundException;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.continuousoptimization.DomainDefinition;

/**
 * Ontology represents Continuous Optimization Dataset
 * @author stepan
 * 
 */
public class DatasetContinuousOpt extends Dataset {

	private static final long serialVersionUID = 1L;
		
	/** Limiting the space intervals */
	private DomainDefinition domain;
	
	@Deprecated
	public DatasetContinuousOpt() {
	}

	public DatasetContinuousOpt(DomainDefinition domain,
			File datasetFile) {
		setDomain(domain);
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public DatasetContinuousOpt(DatasetContinuousOpt problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetContinuousOpt.class.getSimpleName() + " is not valid");
		}
		
		setDomain(problem.getDomain().deepClone());
	}

	
	public DomainDefinition getDomain() {
		return domain;
	}
	public void setDomain(DomainDefinition domain) {
		this.domain = domain;
	}
	
	
	/**
	 * Import the {@link DatasetContinuousOpt} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static Dataset importXML(File file)
			throws Exception {
		
		return new DatasetContinuousOpt(
				DomainDefinition.importXML(file),
				file);
	}
	
	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {

		if (domain == null || ! domain.valid(logger)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Dataset deepClone() {
		return new DatasetContinuousOpt(this);
	}
	
}
