package org.distributedea.ontology.individuals;

import jade.content.Concept;

public abstract class Individual implements Concept {

	private static final long serialVersionUID = 1L;

	public abstract boolean validation();
	
	public abstract String toLogString();
}
