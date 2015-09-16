package org.distributedea.problems.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.problems.ProblemTool;

public abstract class ProblemTSPTool implements ProblemTool {

	@Override
	public Problem readProblem(String inputFileName, AgentLogger logger) {
		
		return readProblemTSP(inputFileName, logger);		
	}
	
	/**
	 * Reads TSP Problem from the file
	 * 
	 * @param tspFileName
	 * @return
	 */
	private ProblemTSP readProblemTSP(String tspFileName, AgentLogger logger) {
	
		List<PositionGPS> positions = new ArrayList<PositionGPS>();
		positions.add(new PositionGPS());
		
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
					double latitude = Double.parseDouble(tokens[1]);
					double longitude = Double.parseDouble(tokens[2]);
							
					PositionGPS positionI = new PositionGPS();
					positionI.setNumber(number);
					positionI.setLatitude(latitude /1000);
					positionI.setLongitude(longitude /1000);
					
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
		
		ProblemTSP problem = new ProblemTSP();
		problem.setPositions(positions);
		
		return problem;
	}
	
}
