package org.distributedea.problems.continuousoptimization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.distributedea.Configuration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.continousoptimalization.Interval;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric;
import org.distributedea.problems.exceptions.ProblemToolException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Abstract Problem Tool for Continuous Optimization Problem for general Individual representation
 * @author stepan
 *
 */
public abstract class ProblemContinuousOptTool implements ProblemTool {

	protected IJNIfgeneric fgeneric;
	protected BbobTools bbobTools;
	
	
	@Override
	public Class<?> problemWhichSolves() {
		return ProblemContinousOpt.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualPoint.class;
	}

	@Override
	public void initialization(Problem problem, AgentConfiguration agentConf,
			AgentLogger logger) throws ProblemToolException {
		
    	System.setProperty("java.library.path", "." + File.separator + "lib");
    	
    	ProblemContinousOpt problemContinousOpt = (ProblemContinousOpt)problem;
    	
    	int dim = problemContinousOpt.getDimension();
    	
    	String functionIDString = problemContinousOpt.getFunctionID();
    	int functionID = Integer.parseInt(functionIDString.substring(1));
    	
    	String containerSuffix = agentConf.exportContainerSuffix();
    	
    	bbobTools = new BbobTools(containerSuffix, logger);
    	
    	try {
			fgeneric = bbobTools.getInstanceJNIfgeneric();
		} catch (BbobException e) {
			throw new ProblemToolException("Problem Tool can't initialize Bbob");
		}

    	
        JNIfgeneric.Params params = new JNIfgeneric.Params();
        
        String path = "log" + File.separator + "bbob" + File.separator +
        		"data" + bbobTools.getNumber();
        double ret = fgeneric.initBBOB(functionID, 1, dim, path, params);
        if (ret == 0.) {
            System.out.println("initBBOB finished OK.");
        } else {
            System.out.println("initBBOB returned error: " + ret);
        }
	}

	@Override
	public void exit() throws ProblemToolException {
		
		double ret = fgeneric.exitBBOB();
        if (ret == 0.) {
            System.out.println("exitBBOB finished OK.");
        } else {
            System.out.println("exitBBOB returned error: " + ret);
        }
        
        try {
			bbobTools.clean();
		} catch (BbobException e) {
			throw new ProblemToolException("Problem Tool can't exit Bbob");
		}
	}
	
	@Override
	public double fitness(Individual individual, Problem problem,
			AgentLogger logger) {
		
		if (individual == null || (! problem.testIsValid(individual, logger)) ) {
			if (problem.isMaximizationProblem()) {
				return Double.MIN_VALUE;
			} else {
				return Double.MAX_VALUE;
			}
		}
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		List<Double> coordinatesList = individualPoint.getCoordinates();

		
		Double[] coordinatesArray = new Double[coordinatesList.size()];
		coordinatesList.toArray(coordinatesArray);
		
		double[] coordinates = ArrayUtils.toPrimitive(coordinatesArray);
		
		return fgeneric.evaluate(coordinates);
	}
	
	@Override
	public Problem readProblem(String inputFileName, AgentLogger logger) {
		
		File fXmlFile = new File(inputFileName);
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
			doc = dBuilder.parse(fXmlFile);
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
		
		ProblemContinousOpt problem = new ProblemContinousOpt();
		problem.setProblemFileName(inputFileName);
		problem.setFunctionID(functionID);
		problem.setDimension(dimension);
		problem.setIntervals(intervals);

		return problem;
	}

	private List<Interval> parseXMLIntervals(Element eElementDomain) {
		
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
				int min = Integer.parseInt(minString);
	
				NodeList nListmax = eElementdim.getElementsByTagName("max");
				if (nListmax.getLength() != 1) {
					return null;
				}
				Node nodemax = nListmax.item(0);
				String maxString = nodemax.getTextContent();
				int max = Integer.parseInt(maxString);
				
				Interval intervalI = new Interval();
				intervalI.setMin(min);
				intervalI.setMax(max);
				
				intervals.set(dimNumberI, intervalI);
			}
		}
		
		return intervals;
	}
	
	@Override
	public Individual generateIndividual(Problem problem, AgentLogger logger) {

		ProblemContinousOpt problemContinousOpt = (ProblemContinousOpt) problem;
		
		IndividualPoint individual = new IndividualPoint();
		for (int i = 0; i < problemContinousOpt.getIntervals().size(); i++) {
			Interval intervalI = problemContinousOpt.getIntervals().get(i);
			
			double min = intervalI.getMin();
			double max = intervalI.getMax();
			double dis = max -min;
			
			double val = min + Math.random() * dis;
			individual.importCoordinate(i, val);
		}
		
		return individual;
	}
	
	@Override
	public Individual readSolution(String fileName, Problem problem,
			AgentLogger logger) {

		String solutionString;
		try {
			solutionString = BbobTools.readFile(fileName);
		} catch (IOException e) {
			logger.logThrowable("Can't read solution from file " + fileName, e);
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
				individualString.substring(1, individualString.length() -3);
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

		
		IndividualPoint indvidualPoint = new IndividualPoint();
		indvidualPoint.setCoordinates(coordinates);
		
		return indvidualPoint;
	}

	public ProblemContinuousOptTool deepClone() {
		return this;
	}
}
