package org.distributedea.ontology.individuals.latentfactors;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Ontology represents one latent factor vector
 * @author stepan
 *
 */
public class LatentFactorVector implements Concept {

	private static final long serialVersionUID = 1L;

	private double [] vector;
	
	
	@Deprecated
	public LatentFactorVector() {} // Only for JADE

	/**
	 * Constructor
	 * @param permutation
	 */
	public LatentFactorVector(double[] vector) {
		setVector(vector);
	}
	
	/**
	 * Copy constructor
	 * @param individual
	 */
	public LatentFactorVector(LatentFactorVector vector) {
		if (vector == null || ! vector.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorVector.class.getSimpleName() +
					" is not valid");
		}
		
		this.vector = vector.getVector().clone();
	}
	
	
	
	public double[] getVector() {
		return vector;
	}

	@Deprecated
	public void setVector(double[] vector) {
		if (vector == null) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.vector = vector;
	}
	
	/**
	 * Exports value on index
	 * @param index
	 * @return
	 */
	public double exportValue(int index) {
		return getVector()[index];
	}
	
	/**
	 * Export width of latent factor
	 * @return
	 */
	public int width() {
		if (getVector() == null) {
			return 0;
		}
		return getVector().length;
	}
	
	
	/**
	 * Operator vector plus 
	 * @param latFactVector
	 * @return
	 */
	public LatentFactorVector plus(LatentFactorVector latFactVector) {
		
		double [] vectorNew = new double [latFactVector.width()];
		
		for (int i = 0; i < latFactVector.width(); i++) {
			
			vectorNew[i] = exportValue(i) + latFactVector.exportValue(i);
		}
		
		return new LatentFactorVector(vectorNew);
	}
	
	/**
	 * Operator vector minus 
	 * @param latFactVector
	 * @return
	 */
	public LatentFactorVector minus(LatentFactorVector latFactVector) {
		
		LatentFactorVector product = latFactVector.multiply(-1);
		return plus(product);
	}

	/**
	 * Operator multiplies the vector by a scalar
	 * @param factor
	 * @return
	 */
	public LatentFactorVector multiply(double factor) {

		double [] vectorNew = new double [width()];
		
		for (int i = 0; i < width(); i++) {
			vectorNew[i] = exportValue(i) * factor;
		}
		
		return new LatentFactorVector(vectorNew);
	}
	
	/**
	 * Exports scalar product of two {@link LatentFactorVector}
	 * @param latFactVector
	 * @return
	 */
	public double exportScalarProduct(LatentFactorVector latFactVector) {
		if (latFactVector == null || ! latFactVector.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorVector.class.getSimpleName() +
					" is not valid");
		}
		if (width() != latFactVector.width()) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorVector.class.getSimpleName() +
					" is not valid");			
		}

		double product = 0;
		for (int i = 0; i < latFactVector.width(); i++) {
			product += exportValue(i) * latFactVector.exportValue(i);
		}
		return product;
	}
	
	/**
	 * Validation
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		return vector != null;
	}

	/**
	 * Returns clone
	 * @return
	 */
	public LatentFactorVector deepClone() {
	
		return new LatentFactorVector(this);
	}

}
