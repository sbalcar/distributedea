package org.distributedea.agents.systemagents.centralmanager.structures.pedigree;

import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;

/**
 * Structure represents Parameters for creating/updating Pedigree
 * @author stepan
 *
 */
public class PedigreeParameters {
	
	public PedigreeDefinition pedigreeDefinition;
	public MethodDescription methodDescription;

	/**
	 * Constructor
	 * @param pedigreeClass
	 * @param methodDescription
	 */
	public PedigreeParameters(PedigreeDefinition pedigreeDefinition, MethodDescription methodDescription) {
		
		this.pedigreeDefinition = pedigreeDefinition;
		this.methodDescription = methodDescription;
	}
}
