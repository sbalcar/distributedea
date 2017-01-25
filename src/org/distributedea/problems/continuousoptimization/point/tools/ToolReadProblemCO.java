package org.distributedea.problems.continuousoptimization.point.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ToolReadProblemCO {

	public static DatasetContinuousOpt readProblem(File fileOfProblem, IAgentLogger logger) {
		
		if (fileOfProblem == null || ! fileOfProblem.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.logThrowable("DocumentBuilder faild by input reading", e);
			return null;
		}
		Document doc;
		try {
			doc = dBuilder.parse(fileOfProblem);
		} catch (SAXException | IOException e) {
			logger.logThrowable("DocumentBuilder faild by input XML reading", e);
			return null;
		}

		doc.getDocumentElement().normalize();				
		NodeList nListCO = doc.getElementsByTagName("continousoptimalization");

		Node nNodeCO = nListCO.item(0);
		if (nNodeCO == null) {
			return null;
		}

		String functionID;
		int dimension;
		List<Interval> intervals = null;
		if (nNodeCO.getNodeType() == Node.ELEMENT_NODE) {

			Element eElementCO = (Element) nNodeCO;

			NodeList nodeLfunctionID = eElementCO.getElementsByTagName("function");
			if (nodeLfunctionID.getLength() != 1) {
				return null;
			}
			Node nodefunctionID = nodeLfunctionID.item(0);
			functionID = nodefunctionID.getTextContent();

			NodeList nodeLdimension = eElementCO.getElementsByTagName("dimensions");
			if (nodeLdimension.getLength() != 1) {
				return null;
			}
			Node nodedimension = nodeLdimension.item(0);
			String dimensionString = nodedimension.getTextContent();
			dimension = Integer.parseInt(dimensionString);
			
			NodeList nodeLDomain = doc.getElementsByTagName("domain");
			if (nodeLDomain.getLength() != 1) {
				return null;
			}
			Node nNodeDomain = nodeLDomain.item(0);
			
			if (nNodeDomain.getNodeType() == Node.ELEMENT_NODE) {
			
				Element eElementDomain = (Element) nNodeDomain;				
				intervals = parseXMLIntervals(eElementDomain);
				if (intervals == null) {
					return null;
				}
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		if(dimension != intervals.size()) {
			return null;
		}
		
		DatasetContinuousOpt problem = new DatasetContinuousOpt();
		problem.importProblemFile(fileOfProblem);
		problem.setFunctionID(functionID);
		problem.setDimension(dimension);
		problem.setIntervals(intervals);

		return problem;
	}
	

	private static List<Interval> parseXMLIntervals(Element eElementDomain) {
		
		if (eElementDomain == null) {
			return null;
		}
		
		NodeList nodeLDim = eElementDomain.getElementsByTagName("dimension");
		
		List<Interval> intervals = new ArrayList<Interval>();
		for (int i = 0; i < nodeLDim.getLength(); i++) {
			intervals.add(null);
		}
		
		for (int i = 0; i < nodeLDim.getLength(); i++) {
		
			Node nodedim = nodeLDim.item(i);
			if (nodedim.getNodeType() == Node.ELEMENT_NODE) {
				
				Element eElementdim = (Element) nodedim;
				String numberString = eElementdim.getAttribute("number");
				int dimNumberI = Integer.parseInt(numberString);
				if (i != dimNumberI) {
					return null;
				}
				
				NodeList nListmin = eElementdim.getElementsByTagName("min");
				if (nListmin.getLength() != 1) {
					return null;
				}
				Node nodemin = nListmin.item(0);
				String minString = nodemin.getTextContent();
				double min = Double.parseDouble(minString);
	
				NodeList nListmax = eElementdim.getElementsByTagName("max");
				if (nListmax.getLength() != 1) {
					return null;
				}
				Node nodemax = nListmax.item(0);
				String maxString = nodemax.getTextContent();
				double max = Double.parseDouble(maxString);
				
				Interval intervalI = new Interval(min, max);
				
				intervals.set(dimNumberI, intervalI);
			}
		}
		
		return intervals;
	}

}
