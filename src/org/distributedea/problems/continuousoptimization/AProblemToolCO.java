package org.distributedea.problems.continuousoptimization;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
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
	public Class<?> problemWhichSolves() {
		return ProblemContinuousOpt.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualPoint.class;
	}

	@Override
	public void initialization(Problem problem, AgentConfiguration agentConf,
			IAgentLogger logger) throws Exception {
////////////////////	
    	ProblemContinuousOpt problemContinousOpt = (ProblemContinuousOpt)problem;

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
	public double fitness(Individual individual, IProblemDefinition problemDef, Problem problem,
			IAgentLogger logger) {
////////////////////		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		ProblemContinuousOpt problemContinousOpt = (ProblemContinuousOpt) problem;

    	String functionID = problemContinousOpt.getFunctionID();
    	
    	if (functionID.equals("f2")) {
    		return f2.evaluate(individualPoint);
    	}

		return ToolFitnessBbobCO.evaluate(individualPoint, problemDef, fgeneric, bbobTools, logger);		
	}
	
	@Override
	public Problem readProblem(File fileOfProblem, IAgentLogger logger) {
		
		return ToolReadProblemCO.readProblem(fileOfProblem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {

		ProblemContinuousOpt problemContinousOpt = (ProblemContinuousOpt) problem;
		
		return ToolGenerateIndividualCO.generateIndividual(problemContinousOpt, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Problem problem,
			IAgentLogger logger) {
		
		return ToolReadSolutionCO.readSolution(fileOfSolution, logger);
	}

	public AProblemToolCO deepClone() {
		return this;
	}
}
