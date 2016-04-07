package org.distributedea;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerDummy;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerRunEachOnce;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.problems.continuousoptimization.ProblemTolRandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

	public void test01() {
		
		InputConfiguration.automaticStart = true;
		InputConfiguration.individualDistribution = false;
		
		InputConfiguration.inputProblemFileName = "f01.co";
		InputConfiguration.problemToSolve = ProblemContinousOpt.class;
		
		InputConfiguration.scheduler = SchedulerDummy.class;
		
		InputConfiguration.availableProblemTools = new Class[]
			{ProblemTolRandomMove.class};
		
	}
	
	public void test02() {
		
		InputConfiguration.automaticStart = true;
		InputConfiguration.individualDistribution = true;
		
		InputConfiguration.inputProblemFileName = "f01.co";
		InputConfiguration.problemToSolve = ProblemContinousOpt.class;
		
		InputConfiguration.scheduler = SchedulerRunEachOnce.class;
		
		InputConfiguration.availableProblemTools = new Class[]
			{ProblemTolRandomMove.class};
	}
	
}
