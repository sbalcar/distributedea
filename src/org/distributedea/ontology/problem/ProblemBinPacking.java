package org.distributedea.ontology.problem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.binpacking.ObjectBinPack;

/**
 * Ontology prepresents Bin packing problem
 * @author stepan
 *
 */
public class ProblemBinPacking extends Problem {

	private static final long serialVersionUID = 1L;

	/** Input objects **/
	private List<ObjectBinPack> objects;
	
	/** Problem File */
	private String problemFileName;
	
	
	@Deprecated
	public ProblemBinPacking() {
		this.objects = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param objects
	 * @param problemFileName
	 */
	public ProblemBinPacking(List<ObjectBinPack> objects, File problemFile) {
		setObjects(objects);
		importProblemFile(problemFile);
	}

	/**
	 * Copy constructor
	 * @param problemBinPacking
	 */
	public ProblemBinPacking(ProblemBinPacking problemBinPacking) {
		if (problemBinPacking == null || ! problemBinPacking.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemBinPacking.class.getSimpleName() + " is not valid");
		}
		
		this.objects = new ArrayList<>();
		for (ObjectBinPack objectI : problemBinPacking.getObjects()) {
			objects.add(objectI.deepClone());
		}
		
		importProblemFile(problemBinPacking.exportProblemFile());
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
	
	public String getProblemFileName() {
		return problemFileName;
	}
	@Deprecated
	public void setProblemFileName(String problemFileName) {
		this.problemFileName = problemFileName;
	}

	
	/**
	 * Exports File with {@link Problem} assignment
	 */
	@Override
	public File exportProblemFile() {
		if (problemFileName == null) {
			return null;
		}
		return new File(problemFileName);
	}
	
	/**
	 * Imports File with {@link Problem} assignment
	 */
	@Override
	public void importProblemFile(File problemFile) {
		if (problemFile == null) {
			throw new IllegalArgumentException();
		}
		if ((! problemFile.exists()) || (! problemFile.isFile())) {
			throw new IllegalArgumentException();
		}
		this.problemFileName = problemFile.getAbsolutePath();
	}

	@Override
	public boolean testIsIGivenIndividualSolutionOfTheProblem(
			Individual individual, IAgentLogger logger) {
		return true;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		if (exportProblemFile() == null) {
			return false;
		}

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
	public Problem deepClone() {
		return new ProblemBinPacking(this);
	}

}
