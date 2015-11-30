package org.distributedea.ontology.problem.tsp;

import jade.content.Concept;

public abstract class Position implements Concept {

	private static final long serialVersionUID = 1L;

	public abstract int getNumber();
	public abstract void setNumber(int number);
}
