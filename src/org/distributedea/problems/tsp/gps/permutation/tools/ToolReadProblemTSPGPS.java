package org.distributedea.problems.tsp.gps.permutation.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.problem.IProblem;

public class ToolReadProblemTSPGPS {

	public static DatasetTSPGPS readDataset(File problemFile, IProblem problem, IAgentLogger logger) {

		List<Position> positions;
		try {
			positions = readProblemTSP(problemFile, logger);
		} catch (Exception e) {
			logger.logThrowable("Can not read " +
					Dataset.class.getSimpleName(), e);
			return null;
		}
		
		List<PositionGPS> positionsGPS = new ArrayList<PositionGPS>();
		for (Position positionI : positions) {
			positionsGPS.add((PositionGPS) positionI);
		}
		
		return new DatasetTSPGPS(positionsGPS, problemFile);
	}
	
	/**
	 * Reads TSP Problem from the file
	 * 
	 * @param fileProblem
	 * @return
	 */
	private static List<Position> readProblemTSP(File fileProblem,
			IAgentLogger logger) {
		
		if (fileProblem == null || ! fileProblem.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is null");
		}
		
		List<Position> positions = new ArrayList<Position>();
		
		BufferedReader br = null;
		
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(fileProblem.getAbsolutePath()));
 
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
			logger.logThrowable("Problem with reading " +
					fileProblem.getName() + " file", exception);
			return null;
			
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				logger.logThrowable("Problem with closing the file: " +
						fileProblem.getName(), ex);
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
	private static Position convertToPosition(int number, double firstValue,
			double secondValue) {
		
		PositionGPS positionI = new PositionGPS();
		positionI.setNumber(number);
		positionI.setLatitude(firstValue);
		positionI.setLongitude(secondValue);
		
		return positionI;
	}

}
