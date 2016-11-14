package org.distributedea.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;

/**
 * Extended {@link AgentLogger} for {@link Agent_ComputingAgent}
 * @author stepan
 *
 */
public class AgentComputingLogger extends AgentLogger {

	/**
	 * Constructor
	 * @param agent
	 */
	public AgentComputingLogger(Agent_ComputingAgent agent) {
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
	
	/**
	 * Log computed {@link IndividualEvaluated} result
	 * @param individualEval
	 * @param generationNumber
	 * @param jobID
	 */
	public void logComputedResult(IndividualEvaluated individualEval,
			long generationNumber, JobID jobID) {
		
		double fitness = individualEval.getFitness();
		
		String fileName = FileNames.getComputingAgentRunLogDirectory(jobID) +
				File.separator + agent.getAID().getLocalName() + ".rslt";
		
		File file = new File(fileName);
		if (! file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			if (generationNumber == -1) {
				writer.append(Configuration.COMMENT_CHAR + "Result of Individual distrubution\n");
			}
			writer.append(fitness + "	" + generationNumber + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}
		
	}
	
	/**
	 * Log given {@link IndividualEvaluated} as best solution
	 * @param individualEval
	 * @param jobID
	 */
	public void logBestSolution(IndividualEvaluated individualEval, JobID jobID) {

		if (individualEval == null || ! individualEval.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		
		File dir = new File(FileNames.getComputingAgentLogSolutionDirectory(jobID));
		if (! dir.exists()) {
			dir.mkdir();
		}

		String fileName = FileNames.getComputingAgentLogSolutionDirectory(jobID) +
				File.separator + agent.getAID().getLocalName() + ".sol";

		String individualString = individualEval.getIndividual().toLogString();
		double fitness = individualEval.getFitness();
		
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
	 * Log the benefit of received fitness value and the best {@link Individual} 
	 * @param deltaFitness
	 */
	public void logDiffImprovementOfDistribution(double deltaFitness, long generationNumber,
			Individual individual, MethodDescription descriptionOfSolutionBuilder, JobID jobID) {
		
		File dir = new File(FileNames.getComputingAgentLogImprovementOfDistributionDirectory(jobID));
		if (! dir.exists()) {
			dir.mkdir();
		}

		String fileName = FileNames.getComputingAgentLogImprovementOfDistributionDirectory(jobID) +
				File.separator + agent.getAID().getLocalName() + ".impr";

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
		
		String fileName = FileNames.getGeneralLogDirectoryForComputingAgent(agent.getAID());
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
