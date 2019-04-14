package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorModel;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ParallelSGDFactorizerWrp;

/**
 * Ontology represents one latent factors based {@link Individual}
 * @author stepan
 *
 */
public class IndividualLatentFactors extends Individual {

	private static final long serialVersionUID = 1L;

	/** Latent factor model **/
	private LatentFactorModel latentFactorModel;
	
	private ParallelSGDFactorizerWrp mahoutModel;
	
	
	@Deprecated
	public IndividualLatentFactors() {} // Only for JADE
	
	/**
	 * Constructor
	 * @param latentFaktorModel
	 */
	public IndividualLatentFactors(
			LatentFactorModel latentFaktorModel) {
		setLatentFactorModel(latentFaktorModel);
	}

	/**
	 * Constructor
	 * @param latentFaktorModel
	 */
	public IndividualLatentFactors(
			ParallelSGDFactorizerWrp factorizer) {
		this.mahoutModel = factorizer;
	}
	
	/**
	 * Copy constructor
	 * @param individual
	 */
	public IndividualLatentFactors(IndividualLatentFactors individual) {
		if (individual == null || ! individual.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualLatentFactors.class.getSimpleName() +
					" is not valid");
		}
		
		LatentFactorModel latVectModelClone = null;
		if (individual.getLatentFactorModel() != null) {
			latVectModelClone = individual.getLatentFactorModel().deepClone();
		}
		//setLatentFactorModel(latVectModelClone);
		this.latentFactorModel = latVectModelClone;
		
		this.mahoutModel = individual.exportMahoutModel();
	}
	
	public LatentFactorModel getLatentFactorModel() {
		return latentFactorModel;
	}
	public void setLatentFactorModel(LatentFactorModel latentFactorModel) {
		if (latentFactorModel == null || ! latentFactorModel.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorModel.class.getSimpleName() + " is not valid");
		}
		this.latentFactorModel = latentFactorModel;
	}
	
	private ParallelSGDFactorizerWrp exportMahoutModel() {
		return this.mahoutModel;
	}
	
	/**
	 * Export value counted from latent factors
	 * @param userIDIndex
	 * @param itemIDIndex
	 * @return
	 */
	public double exportValue(int userIDIndex, int itemIDIndex) {

		if (this.latentFactorModel != null) {
			return this.latentFactorModel.exportValue(userIDIndex, itemIDIndex);
		}
	
		if (this.mahoutModel != null) {
			return this.mahoutModel.rating(userIDIndex, itemIDIndex);
		}
		
		throw new IllegalStateException();
	}
	
	
	public List<Double> exportValues(int userIDIndex, List<Integer> itemIDIndexes) {
		
		List<Double> values = new ArrayList<>(); 
		for (int itemIDIndex : itemIDIndexes) {
			values.add(exportValue(userIDIndex, itemIDIndex));
		}
		
		return values;
	}
	
	public void convert() {
		
		List<LatentFactorVector> latFactVectorsX = new ArrayList<LatentFactorVector>();
		for (int i = 0; i < this.mahoutModel.numberOfItems(); i++) {
			
			double[] vectI = this.mahoutModel.getItemVector(i);
			latFactVectorsX.add(new LatentFactorVector(vectI));
		}
		
		List<LatentFactorVector> latFactVectorsY = new ArrayList<LatentFactorVector>();
		for (int i = 0; i < this.mahoutModel.numberOfUsers(); i++) {
			
			double[] vectI = this.mahoutModel.getUserVector(i);
			latFactVectorsY.add(new LatentFactorVector(vectI));
		}
		
		LatentFactor latFactX = new LatentFactor(latFactVectorsX);
		
		LatentFactor latFactY = new LatentFactor(latFactVectorsY);
		
		this.latentFactorModel = new LatentFactorModel(latFactX, latFactY);

	}
	
	@Override
	public boolean equals(Object other) {

	    if (!(other instanceof IndividualLatentFactors)) {
	        return false;
	    }

	    IndividualLatentFactors that = (IndividualLatentFactors) other;
	    
	    return getLatentFactorModel().equals(
	    		that.getLatentFactorModel());
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {

		LatentFactorModel latFaktModel =
				getLatentFactorModel();
		
		return latFaktModel.toString();
	}
	
	
	@Override
	public String toLogString() {
		return toString();
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		
		return true;
	}

	@Override
	public Individual deepClone() {
		
		return new IndividualLatentFactors(this);
	}
	
}
