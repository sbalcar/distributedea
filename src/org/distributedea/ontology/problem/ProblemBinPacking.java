package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;

/**
 * Ontology for definition Bin Packing problem
 * @author stepan
 *
 */
public class ProblemBinPacking extends AProblem {

	private static final long serialVersionUID = 1L;
	
	private int sizeOfBin;
	
	@Deprecated
	public ProblemBinPacking() {} // only for JADE
	
	/**
	 * Constructor
	 * @param sizeOfBin
	 */
	public ProblemBinPacking(int sizeOfBin) {
		setSizeOfBin(sizeOfBin);
	}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public ProblemBinPacking(ProblemBinPacking problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemBinPacking.class.getSimpleName() + " is not valid");			
		}
		setSizeOfBin(sizeOfBin);
	}
	
	public int getSizeOfBin() {
		return sizeOfBin;
	}
	@Deprecated
	public void setSizeOfBin(int sizeOfBin) {
		if (sizeOfBin <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.sizeOfBin = sizeOfBin;
	}

	@Override
	public boolean exportIsMaximizationProblem() {
		
		return false;
	}

	@Override
	public Class<?> exportDatasetClass() {

		return DatasetBinPacking.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		if (sizeOfBin <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public ProblemBinPacking deepClone() {
		
		return new ProblemBinPacking(this);
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemBinPacking)) {
	        return false;
	    }
	    
	    ProblemBinPacking otherBP = (ProblemBinPacking)other;
	    
	    return getSizeOfBin() == otherBP.getSizeOfBin();
	}

}
