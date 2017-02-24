package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;

/**
 * Ontology for definiton of Machine Learning problem
 * @author stepan
 *
 */
public class ProblemMachineLearning extends AProblem implements IProblem {

	private static final long serialVersionUID = 1L;

	private String classifierClassname;

	private String filterClassName;
	
	private ArgumentsDef argumentsDef;
	
	/**
	 * Constructor
	 */
	@Deprecated
	public ProblemMachineLearning() { // Only for Jade
		this.argumentsDef = new ArgumentsDef();
	}

	/**
	 * Constructor
	 * @param classifier
	 * @param filter
	 * @param argumentsDef
	 */
	public ProblemMachineLearning(Class<?> classifier, Class<?> filter,
			ArgumentsDef argumentsDef) {
		importClassifierClass(classifier);
		importFilterClass(filter);
		setArgumentsDef(argumentsDef);
	}

	/**
	 * Copy constructor
	 * @param problem
	 */
	public ProblemMachineLearning(ProblemMachineLearning problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemMachineLearning.class.getSimpleName() + " is not valid");
		}
		importClassifierClass(problem.exportClassifierClass());
		importFilterClass(problem.exportFilterClass());
		setArgumentsDef(problem.getArgumentsDef().deepClone());
	}
	
	public ArgumentsDef getArgumentsDef() {
		return argumentsDef;
	}
	public void setArgumentsDef(ArgumentsDef argumentsDef) {
		if (argumentsDef == null || ! argumentsDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ArgumentsDef.class.getSimpleName() + " is not valid");
		}
		this.argumentsDef = argumentsDef;
	}

	public Class<?> exportClassifierClass() {
		try {
			return Class.forName(classifierClassname);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	public void importClassifierClass(Class<?> classifier) {
		if (classifier == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		this.classifierClassname = classifier.getName();
	}
	
	@Deprecated
	public String getClassifierClassname() {
		return classifierClassname;
	}
	@Deprecated
	public void setClassifierClassname(String classifierClassname) {
		this.classifierClassname = classifierClassname;
	}

	public Class<?> exportFilterClass() {
		try {
			return Class.forName(filterClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	public void importFilterClass(Class<?> filter) {
		if (filter == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		this.filterClassName = filter.getName();
	}

	@Deprecated
	public String getFilterClassName() {
		return filterClassName;
	}
	@Deprecated
	public void setFilterClassName(String filterClassName) {
		this.filterClassName = filterClassName;
	}

	
	@Override
	public boolean exportIsMaximizationProblem() {
		return false; // minimalisation of error
	}

	@Override
	public Class<?> exportDatasetClass() {
		return DatasetML.class;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (this.argumentsDef == null) {
			return false;
		}
		 
		return this.argumentsDef.valid(logger);
	}

	@Override
	public AProblem deepClone() {
		return new ProblemMachineLearning(this);
	}

}
