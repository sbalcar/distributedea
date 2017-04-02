package org.distributedea.problems.vertexcover.set.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;

public class ToolReadProblemVC {

	public static DatasetVertexCover readProblem(File fileOfProblem, IAgentLogger logger) {
		
		if (fileOfProblem == null || ! fileOfProblem.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		List<Pair<Integer, Integer>> edges = readProblemVC(fileOfProblem, logger);
		
		Graph graph = new Graph();
		for (Pair<Integer, Integer> edgeI : edges) {
			graph.addEdge(edgeI.first, edgeI.second);
		}
		
//		List<Integer> x = graph.exportVertices();
//		Collections.sort(x);
//		for ( int i : x) {
//			System.out.println(i);
//		}
		return new DatasetVertexCover(graph, fileOfProblem);
	}
	
	
	/**
	 * Reads VC Problem from the file
	 * 
	 * @param fileProblem
	 * @return
	 */
	private static List<Pair<Integer, Integer>> readProblemVC(File fileProblem,
			IAgentLogger logger) {
		
		if (fileProblem == null || ! fileProblem.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is null");
		}
		
		List<Pair<Integer, Integer>> positions = new ArrayList<>();
		
		BufferedReader br = null;
		
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(fileProblem.getAbsolutePath()));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("p")) {
					logger.log(Level.INFO, sCurrentLine);

				} else {
					String delims = "[ ]+";
					String[] tokens = sCurrentLine.split(delims);
					
					int firstValue = Integer.parseInt(tokens[1]);
					int secondValue = Integer.parseInt(tokens[2]);
					
					positions.add(new Pair<Integer, Integer>(firstValue, secondValue));
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
	
}
