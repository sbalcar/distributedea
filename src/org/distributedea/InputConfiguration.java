package org.distributedea;


/**
 * Contains parameters settings for the current way of solving 
 * @author stepan
 *
 */
public class InputConfiguration {

	/**
	 * Allows automatic skipping machine, offering the option of framework,
	 * in the case of value is True straight starts computing
	 */
	public static boolean automaticStart;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	public static boolean individualDistribution;
	
		
	/**
	 * Declares the type of problem which will be loaded to solve
	 */
	public static Class<?> problemToSolve;

	/**
	 * Defines the filename with the input Problem
	 */
	public static String inputProblemFileName;

	
	/**
	 * Declares the Scheduler Class which will be used to direction of the evolution
	 */
	public static Class<?> scheduler;

	/**
	 * Declares the set of available ProblemTools for Computing Agents
	 */
	public static Class<?> [] availableProblemTools;
	
	
	/**
	 * Test validity of the current setting
	 * @return
	 */
	public static boolean isValid() {
		
		InputContOpt test = new InputContOpt();
		test.test02();
		
		return true;
	}
	
}
