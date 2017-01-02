package org.distributedea.ontology.problemdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.ProblemBinPacking;

/**
 * Ontology for definition Bin Packing problem
 * @author stepan
 *
 */
public class ProblemBinPackingDef extends AProblemDefinition {

	private static final long serialVersionUID = 1L;
	
	private int sizeOfBin;
	
	@Deprecated
	public ProblemBinPackingDef() {} // only for JADE
	
	/**
	 * Constructor
	 * @param sizeOfBin
	 */
	public ProblemBinPackingDef(int sizeOfBin) {
		setSizeOfBin(sizeOfBin);
	}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public ProblemBinPackingDef(ProblemBinPackingDef problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemBinPackingDef.class.getSimpleName() + " is not valid");			
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

		return ProblemBinPacking.class;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		if (sizeOfBin <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public ProblemBinPackingDef deepClone() {
		
		return new ProblemBinPackingDef(this);
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof ProblemBinPackingDef)) {
	        return false;
	    }
	    
	    ProblemBinPackingDef otherBP = (ProblemBinPackingDef)other;
	    
	    return getSizeOfBin() == otherBP.getSizeOfBin();
	}

}
