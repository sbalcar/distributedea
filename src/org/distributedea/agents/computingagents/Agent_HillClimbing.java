package org.distributedea.agents.computingagents;

import java.util.logging.Level;

import org.distributedea.agents.FitnessTool;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.CompAgentState;
import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolException;

/**
 * Agent represents Hill Climbing Algorithm Method
 * @author stepan
 *
 */
public class Agent_HillClimbing extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;
	

	@Override
	protected boolean isAbleToSolve(ProblemStruct problemStruct) {

		Class<?> problemToolClass =
				problemStruct.exportProblemToolClass(getLogger());
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				problemToolClass, getLogger());
		
		Class<?> problem = problemStruct.getProblem().getClass();
		Class<?> representation = problemTool.reprezentationWhichUses();
		
		boolean isAble = false;
		
		if (problem == ProblemTSPGPS.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}	
		} else if (problem == ProblemTSPPoint.class) {
			if (representation == IndividualPermutation.class) {
				isAble = true;
			}
		} else if (problem == ProblemContinousOpt.class) {
			if (representation == IndividualPoint.class) {
				isAble = true;
			}			
		}
		
		return isAble;
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(false);
		description.setExploration(true);
		
		return description;
	}
	
	protected void processArguments(Arguments args) throws Exception {
	}
	
	@Override
	protected void startComputing(ProblemStruct problemStruct,
			AgentConfiguration agentConf) throws Exception {
		
		if (problemStruct == null || ! problemStruct.valid(getCALogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = problemStruct.getJobID();
		IProblemTool problemTool = problemStruct.exportProblemTool(getLogger());
		Problem problem = problemStruct.getProblem();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		MethodDescription methodDescription = new MethodDescription(agentConf, problemTool.getClass());
		PedigreeParameters pedigreeParams = new PedigreeParameters(null, methodDescription);
		
		
		problemTool.initialization(problem, agentConf, getLogger());
		state = CompAgentState.COMPUTING;
		
		long generationNumberI = -1;

		// save, log and distribute computed Individual
		IndividualEvaluated individualEvalI = problemTool
				.generateIndividualEval(problem, pedigreeParams, getCALogger());

		processIndividualFromInitGeneration(individualEvalI,
				generationNumberI, problem, jobID);
		
		
		while (state == CompAgentState.COMPUTING) {
			
			// increment next number of generation
			generationNumberI++;
						
			IndividualEvaluated individualEvalNew = getNewIndividual(
					individualEvalI, problem, problemTool, pedigreeParams);
			
			boolean isNewIndividualBetter =
					FitnessTool.isFirstIndividualEBetterThanSecond(
							individualEvalNew, individualEvalI, problem);

			if (isNewIndividualBetter) {
				getCALogger().log(Level.INFO, "JUMP");
				individualEvalI = individualEvalNew;
			}
			
			// save, log and distribute computed Individual
			processComputedIndividual(individualEvalI,
					generationNumberI, problem, jobID);
			
			// send new Individual to distributed neighbors
			if (individualDistribution) {
				distributeIndividualToNeighours(individualEvalI, problem, jobID);
			}
			
			//take received individual to new generation
			IndividualWrapper recievedIndividualW = receivedIndividuals.getBestIndividual(problem);
			
			if (individualDistribution &&
					FitnessTool.isFistIndividualWBetterThanSecond(
							recievedIndividualW, individualEvalI, problem)) {
				
				// update if better that actual
				individualEvalI = recievedIndividualW.getIndividualEvaluated();
				
				// save and log received Individual
				processRecievedIndividual(recievedIndividualW, generationNumberI, problem);
			}
			
		}
		
		problemTool.exit();
	}

	protected IndividualEvaluated getNewIndividual(IndividualEvaluated individualEval,
			Problem problem, IProblemTool problemTool,
			PedigreeParameters pedigreeParams) throws ProblemToolException {
		
		return problemTool.improveIndividualEval(individualEval, problem, pedigreeParams, getCALogger());
	}
	
}
