package org.distributedea;

import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

public class InputConfiguration2 {

	public static String inputProblemFileName = "djb2036.tsp";
	
	public static Class<?> problemToSolve = ProblemTSPPoint.class;

	public static Class<?> [] availableProblemTools =
			{ProblemToolPoint2opt.class};

}
