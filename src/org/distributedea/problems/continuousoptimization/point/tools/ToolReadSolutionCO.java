package org.distributedea.problems.continuousoptimization.point.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.distributedea.Configuration;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;

public class ToolReadSolutionCO {

	public static IndividualPoint readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		String solutionString;
		try {
			solutionString = BbobTools.readFile(fileOfSolution.getAbsolutePath());
		} catch (IOException e) {
			logger.logThrowable("Can't read solution from file " + fileOfSolution.getName(), e);
			return null;
		}
		
		String individualString = "";
		
		Scanner scanner = new Scanner(solutionString);
		while (scanner.hasNextLine()) {
			
			String line = scanner.nextLine();
			
			if (! line.startsWith(Configuration.COMMENT_CHAR)) {
				individualString += line + "\n";
			}
		}
		scanner.close();
		
		
		individualString = individualString.trim();
		
		boolean startsAndEndsWithBrackets = individualString.startsWith("[")
				&& individualString.endsWith("]");
		if (! startsAndEndsWithBrackets) {
			return null;
		}
		
		String individualStringWithoutBrackets =
				individualString.substring(1, individualString.length() -1);
		final String[] tokens =
				individualStringWithoutBrackets.split(Pattern.quote(","));
		
		List<Double> coordinates = new ArrayList<Double>();
		for (String valueI : tokens) {
			try {
				double valueDoubI = Double.parseDouble(valueI);
				coordinates.add(valueDoubI);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		
		return new IndividualPoint(coordinates);
	}
}
