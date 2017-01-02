package org.distributedea.ontology.problemdefinition;

import jade.content.Concept;

/**
 * Ontology for Problem definition
 * @author stepan
 *
 */
public abstract class AProblemDefinition implements IProblemDefinition, Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates {@link AProblemDefinition} instance from Class
	 * @param problemDefClass
	 * @return
	 */
	public static AProblemDefinition createProblem(Class<?> problemDefClass) {
		
		AProblemDefinition problemDef = null;
		try {
			problemDef = (AProblemDefinition)problemDefClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		
		return problemDef;
	}
}
