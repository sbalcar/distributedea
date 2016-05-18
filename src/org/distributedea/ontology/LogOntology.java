package org.distributedea.ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.logger.LogMessage;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan
 */
public class LogOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;

	private LogOntology() {
        super("LogOntology");

        try {
            add(LogMessage.class);
            add(JobID.class);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static LogOntology theInstance = new LogOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}