package org.distributedea.agents.systemagents.centralmanager.structures.pedigree;

import org.distributedea.ontology.methoddescription.MethodDescription;

/**
 * Structure represents Parameters for creating/updating Pedigree
 * @author stepan
 *
 */
public class PedigreeParameters {
	
	public Class<?> pedigreeClass;
	public MethodDescription methodDescription;

	/**
	 * Constructor
	 * @param pedigreeClass
	 * @param methodDescription
	 */
	public PedigreeParameters(Class<?> pedigreeClass, MethodDescription methodDescription) {
		
		this.pedigreeClass = pedigreeClass;
		this.methodDescription = methodDescription;
	}
}
