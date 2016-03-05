package org.distributedea.agents.computingagents.computingagent.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.individuals.Individual;

public class AgentComputingLogger extends AgentLogger {

	public AgentComputingLogger(Agent_DistributedEA agent) {
		super(agent);
	}
	
	public void logThrowable(String message, Throwable throwable) {
		
		String line = message + " " + throwable;
		writeToFile(line);
	}
	
	public void log(Level logLevel, String message) {
		
		String line = logLevel + " " + message;
		writeToFile(line);
	}
	
	public void log(Level logLevel, String source, String message) {
		
		String line = logLevel + " " + source + " " + message;
		writeToFile(line);
	}
	
	public void logBestResult(Individual individual, double fitness) {

		String fileName = Configuration.getComputingAgentLogResultFile(agent.getAID());
		
		File dir = new File(Configuration.getComputingAgentLogResultDirectory());
		if (! dir.exists()) {
			dir.mkdir();
		}
		
		try {
			PrintWriter writer = new PrintWriter(new File(fileName));
			writer.println("Fitness: " + fitness);
			writer.append(individual.toLogString());
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}

	}

	/**
	 * log the benefit of received fitness value and the best Individual 
	 * @param deltaFitness
	 */
	public void logDiffImprovementOfDistribution(double deltaFitness) {

		String fileName = Configuration.getComputingAgentLogResultFile(agent.getAID());
		
		File dir = new File(Configuration.getComputingAgentLogResultDirectory());
		if (! dir.exists()) {
			dir.mkdir();
		}
		

	}
	
	private void writeToFile(String line) {

		String fileName = Configuration.getComputingAgentLogFile(agent.getAID());
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(line + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}
	}

	
}
