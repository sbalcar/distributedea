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
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.job.JobID;

public class AgentComputingLogger extends AgentLogger {

	public AgentComputingLogger(Agent_DistributedEA agent) {
		super(agent);
	}
	
	public void logThrowable(String message, Throwable throwable) {
		
		String line = message + " " + throwable;
		//System.out.println(line);
		writeToFile(line);
	}
	
	public void log(Level logLevel, String message) {
		
		String line = logLevel + " " + message;
		//System.out.println(line);
		writeToFile(line);
	}
	
	public void log(Level logLevel, String source, String message) {
		
		String line = logLevel + " " + source + " " + message;
		//System.out.println(line);
		writeToFile(line);
	}
	
	public void logComputedResult(double fitness, long generationNumber, JobID jobID) {
		
		String fileName = Configuration.getComputingAgentResultDirectory(agent.getAID(), jobID);
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			if (generationNumber == -1) {
				writer.append(Configuration.COMMENT_CHAR + "Result of Individual distrubution\n");
			}
			writer.append(fitness + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}
		
	}
	
	public void logBestSolution(Individual individual, double fitness, JobID jobID) {

		String fileName = Configuration.getComputingAgentSolutionFile(agent.getAID(), jobID);
		
		File dir = new File(Configuration.getComputingAgentLogSolutionDirectory(jobID));
		if (! dir.exists()) {
			dir.mkdir();
		}
		
		String individualString = "null";
		if (individual != null) {
			individualString = individual.toLogString();
		}
		
		try {
			PrintWriter writer = new PrintWriter(new File(fileName));
			writer.append(Configuration.COMMENT_CHAR + " Fitness: " + fitness + "\n");
			writer.append(individualString);
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
	public void logDiffImprovementOfDistribution(double deltaFitness, long generationNumber,
			Individual individual, AgentDescription descriptionOfSolutionBuilder, JobID jobID) {

		String fileName = Configuration.getComputingAgentLogImprovementOfDistributionFile(agent.getAID(), jobID);
		
		File dir = new File(Configuration.getComputingAgentLogImprovementOfDistributionDirectory(jobID));
		if (! dir.exists()) {
			dir.mkdir();
		}
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			if (generationNumber == -1) {
				writer.append(Configuration.COMMENT_CHAR + "Delta improvement of Individual distrubution\n");
			}
			writer.append(Configuration.COMMENT_CHAR + "Generation: " + generationNumber + " - " + descriptionOfSolutionBuilder.getAgentConfiguration().getAgentName() + "\n");
			writer.append(deltaFitness + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}

	}
	
	private void writeToFile(String line) {
		
		String fileName = Configuration.getGeneralLogDirectoryForComputingAgent(agent.getAID());
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
