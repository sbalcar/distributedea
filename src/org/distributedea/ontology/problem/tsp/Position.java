package org.distributedea.ontology.problem.tsp;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Abstract ontology represents one TSP position
 * @author balcs7am
 *
 */
public abstract class Position implements Concept {

	private static final long serialVersionUID = 1L;

	public abstract int getNumber();
	public abstract void setNumber(int number);
	
	public abstract boolean valid(IAgentLogger logger);
}
