package org.distributedea;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.input.postprocessing.PostProcessing;


/**
 * Contains parameters settings for the current way of solving.
 * @author stepan
 *
 */
public class InputConfiguration {

	/**
	 * Allows automatic skipping machine, offering the option of framework,
	 * in the case of value is True straight starts computing
	 */
	public static boolean automaticStart = true;
	
	/**
	 * Allows to kill all containers after finishing the last input {@link Batch}
	 */
	public static boolean automaticExit = true;
	
	/**
	 * Allows the {@link Batch} run available {@link PostProcessing}
	 */
	public static boolean runPostProcessing = true;
}
