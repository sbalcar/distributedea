package org.distributedea.ontology.dataset;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.binpacking.ObjectBinPack;

/**
 * Ontology prepresents Bin packing problem {@link Dataset}
 * @author stepan
 *
 */
public class DatasetBinPacking extends Dataset {

	private static final long serialVersionUID = 1L;

	/** Input objects **/
	private List<ObjectBinPack> objects;
	
	
	@Deprecated
	public DatasetBinPacking() {
		this.objects = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param objects
	 * @param datasetFileName
	 */
	public DatasetBinPacking(List<ObjectBinPack> objects) {
		setObjects(objects);
	}

	/**
	 * Copy constructor
	 * @param problemBinPacking
	 */
	public DatasetBinPacking(DatasetBinPacking problemBinPacking) {
		if (problemBinPacking == null || ! problemBinPacking.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetBinPacking.class.getSimpleName() + " is not valid");
		}
		
		this.objects = new ArrayList<>();
		for (ObjectBinPack objectI : problemBinPacking.getObjects()) {
			objects.add(objectI.deepClone());
		}
	}

	
	public List<ObjectBinPack> getObjects() {
		return objects;
	}
	@Deprecated
	public void setObjects(List<ObjectBinPack> objects) {
		this.objects = objects;
	}

	/**
	 * Exports {@link ObjectBinPack} by identification number
	 * @param objectNumber
	 * @return
	 */
	public ObjectBinPack exportObjectBinPackBy(int objectNumber) {
		
		for (ObjectBinPack objectBinPackI : objects) {
			if (objectBinPackI.getNumberOfObject() == objectNumber) {
				return objectBinPackI;
			}
		}
		return null;
	}
	

	@Override
	public boolean valid(IAgentLogger logger) {
		if (getObjects() == null) {
			return false;
		}
		for (ObjectBinPack objectBinPackI : getObjects()) {
			if (objectBinPackI == null || ! objectBinPackI.valid(logger)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Dataset deepClone() {
		return new DatasetBinPacking(this);
	}

}
