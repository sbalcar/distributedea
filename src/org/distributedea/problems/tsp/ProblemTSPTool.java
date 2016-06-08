package org.distributedea.problems.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.problems.ProblemTool;

/**
 * Abstract Problem Tool for TSP Problem for general Individual representation
 * @author stepan
 *
 */
public abstract class ProblemTSPTool implements ProblemTool {
	
	@Override
	public void initialization(Problem problem, AgentConfiguration agentConf, AgentLogger logger) {
	}

	@Override
	public void exit() {
	}
	
	@Override
	public Problem readProblem(String inputProblemFileName, AgentLogger logger) {

		String inputFileName = Configuration.getInputProblemFile(inputProblemFileName);
		
		List<PositionGPS> positions = new ArrayList<PositionGPS>();
		for (Position positionI : readProblemTSP(inputFileName, logger)) {
			positions.add((PositionGPS) positionI);
		}
		
		ProblemTSPGPS problem = new ProblemTSPGPS();
		problem.setProblemFileName(inputProblemFileName);
		problem.setPositions(positions);
		
		return problem;
	}
	
	/**
	 * Reads TSP Problem from the file
	 * 
	 * @param tspFileName
	 * @return
	 */
	protected List<Position> readProblemTSP(String tspFileName,
			AgentLogger logger) {
		
		List<Position> positions = new ArrayList<Position>();
		
		BufferedReader br = null;
		
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(tspFileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("NAME") ||
					sCurrentLine.startsWith("COMMENT") ||
					sCurrentLine.startsWith("TYPE") ||
					sCurrentLine.startsWith("DIMENSION") ||
					sCurrentLine.startsWith("EDGE_WEIGHT_TYPE") ||
					sCurrentLine.startsWith("NODE_COORD_SECTION") ||
					sCurrentLine.startsWith("EOF") ) {
					
					logger.log(Level.INFO, sCurrentLine);

				} else {
					String delims = "[ ]+";
					String[] tokens = sCurrentLine.split(delims);
					
					int number = Integer.parseInt(tokens[0]);
					double firstValue = Double.parseDouble(tokens[1]);
					double secondValue = Double.parseDouble(tokens[2]);
					
					Position positionI =
							convertToPosition(number, firstValue, secondValue);
					
					positions.add(positionI);
				}
				
			}
 
		} catch (IOException exception) {
			logger.logThrowable("Problem with reading " + tspFileName + " file", exception);
			return null;
			
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				logger.logThrowable("Problem with closing the file: " + tspFileName, ex);
			}
		}
		
		return positions;
	}
	
	/**
	 * Converts to Position
	 * 
	 * @param number
	 * @param firstValue
	 * @param secondValue
	 * @return
	 */
	public Position convertToPosition(int number, double firstValue,
			double secondValue) {
		
		PositionGPS positionI = new PositionGPS();
		positionI.setNumber(number);
		positionI.setLatitude(firstValue);
		positionI.setLongitude(secondValue);
		
		return positionI;
	}
	
}
