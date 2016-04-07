package org.distributedea;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerRunEachOnce;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

/**
 * Defines a set of TSP inputs
 */
public class InputTSP {

	public void initialization01() {
		
		InputConfiguration.automaticStart = true;
		InputConfiguration.individualDistribution = true;
		
		InputConfiguration.inputProblemFileName = "simpleTest.tsp";
		InputConfiguration.problemToSolve = ProblemTSPGPS.class;
		
		InputConfiguration.scheduler = SchedulerRunEachOnce.class;
		
		InputConfiguration.availableProblemTools = new Class[]
			{ProblemToolGPSEuc2D2opt.class};
	}
	
	public void initialization02() {

		InputConfiguration.automaticStart = true;
		InputConfiguration.individualDistribution = true;
		
		InputConfiguration.inputProblemFileName = "wi29.tsp";
		InputConfiguration.problemToSolve = ProblemTSPGPS.class;
		
		InputConfiguration.scheduler = SchedulerRunEachOnce.class;
		
		InputConfiguration.availableProblemTools = new Class[]
			{ProblemToolGPSEuc2D2opt.class};
	}

	public void initialization03() {
		
		InputConfiguration.inputProblemFileName = "djb2036.tsp";
		InputConfiguration.problemToSolve = ProblemTSPPoint.class;
		
		InputConfiguration.scheduler = SchedulerRunEachOnce.class;
		
		InputConfiguration.availableProblemTools = new Class[]
				{ProblemToolPoint2opt.class};
	}
	
}
