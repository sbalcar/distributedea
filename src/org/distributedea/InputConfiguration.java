package org.distributedea;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerDummy;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerRunEachOnce;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerSimple;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

public class InputConfiguration {

	public static boolean automaticStart = true;
	public static boolean individualDistribution = true;
	
	//public static String inputProblemFileName = "wi29.tsp";
	public static String inputProblemFileName = "simpleTest.tsp";
	
	public static Class<?> problemToSolve = ProblemTSPGPS.class;
	
	//public static Class<?> scheduler = SchedulerSimple.class;
	//public static Class<?> scheduler = SchedulerDummy.class;
	public static Class<?> scheduler = SchedulerRunEachOnce.class;

	public static Class<?> [] availableProblemTools =
			{ProblemToolGPSEuc2D2opt.class};
}
