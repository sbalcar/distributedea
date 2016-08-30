package org.distributedea.problems;

/**
 * Exception of incorrect use of ProblemTool.
 * Interface of ProblemTool is too general and some services
 * aren't in each implementation allowed 
 * @author stepan
 *
 */
public class ProblemToolException extends Exception {

	public ProblemToolException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
