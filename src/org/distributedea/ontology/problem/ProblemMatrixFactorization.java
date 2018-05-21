package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.problem.matrixfactorization.DatasetPartitioning;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.ILatFactDefinition;

/**
 * Ontology for definition Matrix Factorization problem
 * @author stepan
 *
 */
public class ProblemMatrixFactorization extends AProblem {

	private static final long serialVersionUID = 1L;

	private ILatFactDefinition latFactXDef;
	private ILatFactDefinition latFactYDef;
	
	/** Width of latent factor **/
	private int latentFactorWidth;

	private DatasetPartitioning datasetPartitioning;

	
	@Deprecated
	public ProblemMatrixFactorization() {} // only for JADE
	
	/**
	 * Constructor
	 * @param sizeOfBin
	 */
	public ProblemMatrixFactorization(ILatFactDefinition latFactXDef,
			ILatFactDefinition latFactYDef, int latentFactorWidth,
			DatasetPartitioning datasetPartitioning) {
		this.setLatFactXDef(latFactXDef);
		this.setLatFactYDef(latFactYDef);
		this.setLatentFactorWidth(latentFactorWidth);
		this.setDatasetPartitioning(datasetPartitioning);
	}

	/**
	 * Copy constructor
	 * @param problem
	 */
	public ProblemMatrixFactorization(ProblemMatrixFactorization problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemMatrixFactorization.class.getSimpleName() + " is not valid");			
		}
		this.setLatFactXDef(problem.getLatFactXDef().deepClone());
		this.setLatFactYDef(problem.getLatFactYDef().deepClone());		
		this.setLatentFactorWidth(problem.getLatentFactorWidth());
		this.setDatasetPartitioning(problem.getDatasetPartitioning().deepClone());
	}
	
	
	public ILatFactDefinition getLatFactXDef() {
		return latFactXDef;
	}
	@Deprecated
	public void setLatFactXDef(ILatFactDefinition latFactXDef) {
		if (latFactXDef == null || ! latFactXDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ILatFactDefinition.class.getSimpleName() + " is not valid");			
		}
		this.latFactXDef = latFactXDef;
	}

	
	public ILatFactDefinition getLatFactYDef() {
		return latFactYDef;
	}
	@Deprecated
	public void setLatFactYDef(ILatFactDefinition latFactYDef) {
		if (latFactYDef == null || ! latFactYDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ILatFactDefinition.class.getSimpleName() + " is not valid");			
		}
		this.latFactYDef = latFactYDef;
	}

	
	
	public int getLatentFactorWidth() {
		return latentFactorWidth;
	}
	@Deprecated
	public void setLatentFactorWidth(int latentFactorWidth) {
		if (latentFactorWidth < 1) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		
		this.latentFactorWidth = latentFactorWidth;
	}

	
	public DatasetPartitioning getDatasetPartitioning() {
		return datasetPartitioning;
	}
	@Deprecated
	public void setDatasetPartitioning(DatasetPartitioning datasetPartitioning) {
		if (datasetPartitioning == null ||
				! datasetPartitioning.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetPartitioning.class.getSimpleName() + " is not valid");
		}
		this.datasetPartitioning = datasetPartitioning;
	}

	
	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}

	@Override
	public Class<?> exportDatasetClass() {

		return DatasetMF.class;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
	
		return getLatentFactorWidth() > 0 &&
				getLatFactXDef() != null && getLatFactXDef().valid(logger) &&
				getLatFactYDef() != null && getLatFactYDef().valid(logger) &&
				getDatasetPartitioning() != null && getDatasetPartitioning().valid(logger);
	}

	@Override
	public AProblem deepClone() {
		
		return new ProblemMatrixFactorization(this);
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemMatrixFactorization)) {
	        return false;
	    }
	    
	    ProblemMatrixFactorization outherMF = (ProblemMatrixFactorization) other; 
	    
	    return getLatFactXDef().equals(outherMF.getLatFactXDef()) &&
	    		getLatFactYDef().equals(outherMF.getLatFactYDef()) &&
	    		getLatentFactorWidth() == outherMF.getLatentFactorWidth() &&
	    		getDatasetPartitioning().equals(outherMF.getDatasetPartitioning());
	}
	
}
