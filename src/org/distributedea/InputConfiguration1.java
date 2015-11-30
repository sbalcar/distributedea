package org.distributedea;

import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

public class InputConfiguration1 {

	public static String inputProblemFileName = "wi29.tsp";
	
	public static Class<?> problemToSolve = ProblemTSPGPS.class;

	public static Class<?> [] availableProblemTools =
			{ProblemToolGPSEuc2D2opt.class};
}
