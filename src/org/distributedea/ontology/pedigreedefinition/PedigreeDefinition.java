package org.distributedea.ontology.pedigreedefinition;

import java.lang.reflect.InvocationTargetException;

import jade.content.Concept;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.problemtools.IProblemTool;

/**
 * Ontology represents pedigree definition
 * @author stepan
 *
 */
public class PedigreeDefinition implements Concept {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Specify about type of {@link Pedigree}
	 */
	private String pedigreeOfIndividualClassName;
	

	@Deprecated
	public PedigreeDefinition() {}  // only for Jade

	/**
	 * Constructor
	 */
	public PedigreeDefinition(Class<?> pedigreeOfIndividualClass) {
		
		importPedigreeOfIndividualClassName(pedigreeOfIndividualClass);
	}
	
	/**
	 * Copy constructor
	 */
	public PedigreeDefinition(PedigreeDefinition pedigreeDefinition) {
		
		if (pedigreeDefinition == null || ! pedigreeDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Given " +
					PedigreeDefinition.class.getSimpleName() + " is null or invalid");
		}
		
		setPedigreeOfIndividualClassName(
				pedigreeDefinition.getPedigreeOfIndividualClassName());
	}
	
	
	
	@Deprecated	
	public String getPedigreeOfIndividualClassName() {
		return pedigreeOfIndividualClassName;
	}
	@Deprecated
	public void setPedigreeOfIndividualClassName(String hisotyOfIndividualClassName) {
		this.pedigreeOfIndividualClassName = hisotyOfIndividualClassName;
	}

	/**
	 * Exports {@link IProblemTool} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportPedigreeOfIndividual(IAgentLogger logger) {
		
		try {
			return Class.forName(pedigreeOfIndividualClassName);
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToSolve
	 */
	public void importPedigreeOfIndividualClassName(Class<?> pedigreeOfIndividual) {
		
		if (pedigreeOfIndividual == null) {
			this.pedigreeOfIndividualClassName = null;
			return;
		}
		this.pedigreeOfIndividualClassName = pedigreeOfIndividual.getName();
	}	

	public Pedigree create(PedigreeParameters pedParams, IAgentLogger logger) {
		
		Class<?> pedigreeclass = exportPedigreeOfIndividual(logger);
		Class<?> cls[] = new Class[] { PedigreeParameters.class };
		
		if (pedigreeclass == null) {
			return null;
		}
		
		Pedigree pedigreeNew = null;
		try {
			pedigreeNew = (Pedigree) pedigreeclass.getDeclaredConstructor(cls).newInstance(pedParams);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.logThrowable("Can't create Pedigree", e);
		}
		
		return pedigreeNew;
	}

	public Pedigree create(Pedigree pedigree1,
			PedigreeParameters pedParams, IAgentLogger logger) {
		
		Class<?> pedigreeclass = exportPedigreeOfIndividual(logger);
		Class<?> cls[] = new Class[] { Pedigree.class, PedigreeParameters.class };
		 
		if (pedigreeclass == null) {
			return null;
		}

		Pedigree pedigreeNew = null;
		try {
			pedigreeNew = (Pedigree) pedigreeclass.getDeclaredConstructor(cls).newInstance(pedigree1, pedParams);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.logThrowable("Can't create Pedigree", e);
		}
		
		return pedigreeNew;
	}

	public Pedigree create(Pedigree pedigree1, Pedigree pedigree2,
			PedigreeParameters pedParams, IAgentLogger logger) {
		
		Class<?> pedigreeclass = exportPedigreeOfIndividual(logger);
		Class<?> cls[] = new Class[] { Pedigree.class, Pedigree.class, PedigreeParameters.class };

		if (pedigreeclass == null) {
			return null;
		}
		
		Pedigree pedigreeNew = null;
		try {
			pedigreeNew = (Pedigree) pedigreeclass.getDeclaredConstructor(cls).newInstance(pedigree1, pedigree2, pedParams);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.logThrowable("Can't create Pedigree", e);
		}
		
		return pedigreeNew;
	}

	public Pedigree create(Pedigree pedigree1, Pedigree pedigree2,
			Pedigree pedigree3, PedigreeParameters pedParams,
			IAgentLogger logger) {
		
		Class<?> pedigreeclass = exportPedigreeOfIndividual(logger);
		Class<?> cls[] = new Class[] { Pedigree.class, Pedigree.class, Pedigree.class, PedigreeParameters.class };
		
		if (pedigreeclass == null) {
			return null;
		}

		Pedigree pedigreeNew = null;
		try {
			pedigreeNew = (Pedigree) pedigreeclass.getDeclaredConstructor(cls).newInstance(pedigree1, pedigree2, pedigree3, pedParams);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.logThrowable("Can't create Pedigree", e);
		}
		
		return pedigreeNew;
	}	
	
	
	/**
	 * Tests validation
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
				
		return true;
	}
	
	/**
	 * Returns clone
	 */
	public PedigreeDefinition deepClone() {
		return new PedigreeDefinition(this);
	}
	
}
