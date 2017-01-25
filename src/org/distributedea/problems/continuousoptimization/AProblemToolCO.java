package org.distributedea.problems.continuousoptimization;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemdefinition.ProblemContinuousOptDef;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.point.tools.ToolGenerateIndividualCO;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadProblemCO;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadSolutionCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolExitBbobCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolFitnessBbobCO;
import org.distributedea.problems.continuousoptimization.point.tools.bbob.ToolInitializationBbobCO;
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
	
	
	@Override
	public Class<?> datasetReprezentation() {
		return DatasetContinuousOpt.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemContinuousOptDef.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualPoint.class;
	}

	@Override
	public void initialization(Dataset dataset, AgentConfiguration agentConf,
			IAgentLogger logger) throws Exception {
////////////////////	
    	DatasetContinuousOpt problemContinousOpt = (DatasetContinuousOpt)dataset;

    	String functionID = problemContinousOpt.getFunctionID();
    	
    	if (functionID.equals("f2")) {
    		return;
    	}
    	
		ToolInitializationBbobCO.initialization(problemContinousOpt, agentConf,
				fgeneric, bbobTools, logger);
	}

	@Override
	public void exit() throws Exception {

		if (fgeneric != null && bbobTools != null) {
			ToolExitBbobCO.exit(fgeneric, bbobTools);
		}
	}
	
	@Override
	public double fitness(Individual individual, IProblemDefinition problemDef, Dataset dataset,
			IAgentLogger logger) {
////////////////////		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		DatasetContinuousOpt problemContinousOpt = (DatasetContinuousOpt) dataset;

    	String functionID = problemContinousOpt.getFunctionID();
    	
    	if (functionID.equals("f2")) {
    		return f2.evaluate(individualPoint);
    	}

		return ToolFitnessBbobCO.evaluate(individualPoint, problemDef, fgeneric, bbobTools, logger);		
	}
	
	@Override
	public Dataset readDataset(File fileOfProblem, IAgentLogger logger) {
		
		return ToolReadProblemCO.readProblem(fileOfProblem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) {

		DatasetContinuousOpt problemContinousOpt = (DatasetContinuousOpt) dataset;
		
		return ToolGenerateIndividualCO.generateIndividual(problemContinousOpt, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionCO.readSolution(fileOfSolution, logger);
	}

	public AProblemToolCO deepClone() {
		return this;
	}
}
