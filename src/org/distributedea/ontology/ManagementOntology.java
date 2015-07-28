package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.agent.Arguments;

public class ManagementOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private ManagementOntology() {
        super("ManagementOntology");

        try {
            add(CreateAgent.class);
            add(KillContainer.class);
            add(Arguments.class);
            add(Argument.class);


        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ManagementOntology theInstance = new ManagementOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
