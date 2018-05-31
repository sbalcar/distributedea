package org.distributedea.problems.continuousoptimization;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.point.tools.ToolGenerateIndividualCO;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadProblemCO;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadSolutionCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolExitBbobCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolFitnessBbobCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolInitializationBbobCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.fBbob;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.IFuncitonCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f02;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f04;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f08;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f10;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f14;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.f17;
import org.distributedea.problems.continuousoptimization.point.tools.ownfunction.f2;

/**
 * Abstract {@link ProblemTool} for Continuous Optimization {@link Problem}
 * for general {@link Individual} representation
 * @author stepan
 *
 */
public abstract class AProblemToolCO extends ProblemTool {

	protected IJNIfgeneric fgeneric;
	protected BbobTools bbobTools;
	
	protected IFuncitonCO function;
	
	@Override
	public Class<?> datasetReprezentation() {
		return DatasetContinuousOpt.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemContinuousOpt.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualPoint.class;
	}

	@Override
	public void initialization(IProblem problem, Dataset dataset, AgentConfiguration agentConf,
			IAgentLogger logger) throws Exception {

    	ProblemContinuousOpt problemCO = (ProblemContinuousOpt) problem;
		DatasetContinuousOpt datasetCO = (DatasetContinuousOpt) dataset;

    	String functionID = problemCO.getFunctionID();
    	
    	if (functionID.equals("f2")) {
    		function = new f2();
    	}
    	if (functionID.equals("f02")) {
    		function = new f02();
    	}
    	if (functionID.equals("f04")) {
    		function = new f04();
    	}
    	if (functionID.equals("f08")) {
    		function = new f08();
    	}
    	if (functionID.equals("f10")) {
    		function = new f10();
    	}
     	if (functionID.equals("f14")) {
    		function = new f14();
    	}
     	if (functionID.equals("f17")) {
    		function = new f17();
    	}
     	
    	if (functionID.equals("Bbob")) {
    		function = new fBbob();
			ToolInitializationBbobCO.initialization(problemCO, datasetCO,
					agentConf, fgeneric, bbobTools, logger);
    	}
    	
		function.initialisation(problemCO.getDimension());
	}

	@Override
	public void exit() throws Exception {

		if (fgeneric != null && bbobTools != null) {
			ToolExitBbobCO.exit(fgeneric, bbobTools);
		}
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;

		if (function instanceof fBbob) {
			return ToolFitnessBbobCO.evaluate(individualPoint, problem,
					fgeneric, bbobTools, logger);
		}
		return function.evaluate(individualPoint);		
	}
	
	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {
		
		DatasetDescription datasetDescr = (DatasetDescription) datasetDescription;
		return ToolReadProblemCO.readProblem(datasetDescr, problem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		ProblemContinuousOpt problemCO = (ProblemContinuousOpt) problem;
		DatasetContinuousOpt datasetCO = (DatasetContinuousOpt) dataset;
		
		return ToolGenerateIndividualCO.generateIndividual(problemCO, datasetCO, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionCO.readSolution(fileOfSolution, logger);
	}

	/**
	 * Returns clone
	 * @return
	 */
	public AProblemToolCO deepClone() {
		return this;
	}
}
