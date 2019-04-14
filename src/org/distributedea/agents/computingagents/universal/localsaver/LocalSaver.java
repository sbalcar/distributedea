package org.distributedea.agents.computingagents.universal.localsaver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.distributedea.Configuration;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.agentconfiguration.AgentName;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;

public class LocalSaver {
	
	private PrintWriter writerOfFitness;
	private PrintWriter writerOfSolution;
	private PrintWriter writerOfImprovementOfDist;
	 
	/**
	 * Constructor
	 * @param computinAgent
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public LocalSaver(Agent_ComputingAgent computinAgent, JobID jobID) throws IOException {
		
		if (computinAgent == null || ! (computinAgent instanceof Agent_ComputingAgent)) {
			throw new IllegalArgumentException("Argument " +
					Agent_ComputingAgent.class.getSimpleName() + " is not valid");
		}
		
		String fileNameOfFitness = FileNames.getComputingAgentRunLogDirectory(jobID) +
				File.separator + computinAgent.getAID().getLocalName() + ".rslt";
		File fileOfFitness = new File(fileNameOfFitness);
		fileOfFitness.getParentFile().mkdirs(); 
		fileOfFitness.createNewFile();
		this.writerOfFitness = new PrintWriter(new FileOutputStream(fileOfFitness, true));
		
		
		
		String fileNameOfSolution = FileNames.getComputingAgentLogSolutionDirectory(jobID) +
				File.separator + computinAgent.getAID().getLocalName() + ".sol";
		File fileOfSolution = new File(fileNameOfSolution);
		fileOfSolution.getParentFile().mkdirs(); 
		fileOfSolution.createNewFile();
		this.writerOfSolution = new PrintWriter(new FileOutputStream(fileNameOfSolution, false));
		
		
		
		String fileNameOfImprovementOfDist = FileNames.getComputingAgentLogImprovementOfDistributionDirectory(jobID) +
				File.separator + computinAgent.getAID().getLocalName() + ".impr";
		File fileOfImprovementOfDist = new File(fileNameOfImprovementOfDist);
		fileOfImprovementOfDist.getParentFile().mkdirs(); 
		fileOfImprovementOfDist.createNewFile();
		this.writerOfImprovementOfDist = new PrintWriter(new FileOutputStream(fileNameOfImprovementOfDist, true));

	}
	
	/**
	 * Log computed {@link IndividualEvaluated} result
	 * @param individualEval
	 * @param generationNumber
	 * @param jobID
	 */
	public void logComputedFitnessResult(IndividualEvaluated individualEval,
			long generationNumber) {

		this.writerOfFitness.print(
				individualEval.getFitness() + "	" + generationNumber + "\n");
		this.writerOfFitness.flush();
	}
	
	/**
	 * Log given {@link IndividualEvaluated} as best solution
	 * @param individualEval
	 * @param jobID
	 */
	public void logSolution_(IndividualEvaluated individualEval) {
		
		String individualString = individualEval.exportXML();
		
		this.writerOfSolution.write((individualString + "\n"));
		this.writerOfSolution.flush();
	}
	
	/**
	 * Log the benefit of received fitness value and the best {@link Individual} 
	 * @param deltaFitness
	 */
	public void logDiffImprovementOfDistribution(IndividualWrapper receivedIndiv,
			long generationNumber, IndividualEvaluated currentIndiv) {
		
		MethodDescription description =
				receivedIndiv.getMethodDescription();
		AgentName agentName = description.getAgentConfiguration().getAgentName();
		
		double receivedFitness = receivedIndiv.getIndividualEvaluated().getFitness();
		double currentFitness = currentIndiv.getFitness(); 
		
		double deltaFitness = Math.abs(
				receivedFitness - currentFitness);
		
		this.writerOfImprovementOfDist.write(
				Configuration.COMMENT_CHAR + "Generation: " + generationNumber + " - " + agentName + "\n");
		
		this.writerOfImprovementOfDist.write("" + deltaFitness  + " = |" + receivedFitness + " - " + currentFitness + "|\n");
		this.writerOfImprovementOfDist.flush();
	}
	
	/**
	 * Close files
	 * @throws IOException 
	 */
	public void closeFiles() throws IOException {

		this.writerOfFitness.flush();
		this.writerOfFitness.close();
		
		this.writerOfSolution.flush();		
		this.writerOfSolution.close();
		
		this.writerOfImprovementOfDist.flush();
		this.writerOfImprovementOfDist.close();
	}
	
	public static void main(String [ ] args) {
//		Agent_DataManager computinAgent = new Agent_DataManager();
//		computinAgent.run();
		
	}
}
