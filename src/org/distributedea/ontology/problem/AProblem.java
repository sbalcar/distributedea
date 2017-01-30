package org.distributedea.ontology.problem;

import jade.content.Concept;

/**
 * Ontology for Problem definition
 * @author stepan
 *
 */
public abstract class AProblem implements IProblem, Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates {@link AProblemDef} instance from Class
	 * @param problemClass
	 * @return
	 */
	public static AProblem createProblem(Class<?> problemClass) {
		
		AProblem problemDef = null;
		try {
			problemDef = (AProblem)problemClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		
		return problemDef;
	}
}
