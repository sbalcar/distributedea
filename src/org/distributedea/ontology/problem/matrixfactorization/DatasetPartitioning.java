package org.distributedea.ontology.problem.matrixfactorization;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.problem.matrixfactorization.traintest.IRatingIDs;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsEmptySet;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsFullSet;

/**
 * Ontology represents partition of {@link DatasetMF}
 * @author stepan
 *
 */
public class DatasetPartitioning implements Concept {

	private static final long serialVersionUID = 1L;
	
	private IRatingIDs trainingSetDef;
	private IRatingIDs testingSetDef;
	
	@Deprecated
	public DatasetPartitioning() {  // Only for Jade
		
		this.setTrainingSetDef(new RatingIDsFullSet());
		this.setTestingSetDef(new RatingIDsEmptySet());
	}
	
	/**
	 * Constructor
	 * @param trainingSetDef
	 * @param testingSetDef
	 */
	public DatasetPartitioning(IRatingIDs trainingSetDef,
			IRatingIDs testingSetDef) {
		
		this.setTrainingSetDef(trainingSetDef);
		this.setTestingSetDef(testingSetDef);
	}

	/**
	 * Copy Constructor
	 * @param dPartitioning
	 */
	public DatasetPartitioning(DatasetPartitioning dPartitioning) {
		if (dPartitioning == null || ! dPartitioning.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetPartitioning.class.getSimpleName() + " is not valid");
		}
		this.setTrainingSetDef(
				dPartitioning.getTrainingSetDef().deepClone());
		this.setTestingSetDef(
				dPartitioning.getTestingSetDef().deepClone());
	}
	
	
	public IRatingIDs getTrainingSetDef() {
		return trainingSetDef;
	}
	@Deprecated
	public void setTrainingSetDef(IRatingIDs trainingSetDef) {
		this.trainingSetDef = trainingSetDef;
	}

	
	public IRatingIDs getTestingSetDef() {
		return testingSetDef;
	}
	@Deprecated
	public void setTestingSetDef(IRatingIDs testingSetDef) {
		this.testingSetDef = testingSetDef;
	}

	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return getTrainingSetDef() != null && getTrainingSetDef().valid(logger) &&
				getTestingSetDef() != null && getTestingSetDef().valid(logger);
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public DatasetPartitioning deepClone() {
		return new DatasetPartitioning(this);
	}
	
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof DatasetPartitioning)) {
	        return false;
	    }
	    
	    DatasetPartitioning otherDatPart = (DatasetPartitioning)other;
	    
	    boolean equalTrainingS = false;
	    boolean equalTestingS = false;
	    
	    if (getTrainingSetDef() == null && otherDatPart.getTrainingSetDef() == null) {
	    	equalTrainingS = true;
	    }
	    if (getTestingSetDef() == null && otherDatPart.getTestingSetDef() == null) {
	    	equalTestingS = true;
	    }
	    	    
	    if (getTrainingSetDef() != null &&
	    		getTrainingSetDef().equals(otherDatPart.getTrainingSetDef())) {
	    	equalTrainingS = true;
	    }
	    if (getTestingSetDef() != null &&
	    		! getTestingSetDef().equals(otherDatPart.getTestingSetDef())) {
	    	equalTestingS = true;
	    }
	    
	    return equalTrainingS && equalTestingS;
	}
}
