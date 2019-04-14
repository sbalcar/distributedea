package org.distributedea.problems.evcharging;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetEVCharging;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemEVCharging;
import org.distributedea.problems.AProblemTool;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadProblemCO;
import org.distributedea.problems.continuousoptimization.point.tools.ToolReadSolutionCO;
import org.distributedea.problems.evcharging.point.tools.ToolGenerateIndividualEVCharging;

public abstract class AAProblemToolEVCharging extends AProblemTool {

	private boolean DEBUG = !false;
	
	private XmlRpcClient server;
	private int domainSize;
	
	private Random random;
	private double normalDistMultiplicator;
		
	private String EVALUATE = "evaluate";
	private String GET_INDIVIDUAL_SIZE = "get_individual_size";

	@Deprecated
	public AAProblemToolEVCharging() {
		this.random = new Random();
	}
	
	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 */
	public AAProblemToolEVCharging(double normalDistMultiplicator) {
		this();
		this.normalDistMultiplicator = normalDistMultiplicator;
	}
	
	
	@Override
	public Class<?> datasetReprezentation() {
		return DatasetEVCharging.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemEVCharging.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualPoint.class;
	}
	
	@Override
	public Arguments exportArguments() {
		Arguments arguments = new Arguments();
		arguments.addArgument(new Argument("normalDistMultiplicator", normalDistMultiplicator));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		this.normalDistMultiplicator = arguments.exportArgument("normalDistMultiplicator").exportValueAsDouble();
	}


	@Override
	public void initialization(IProblem problem, Dataset dataset, AgentConfiguration agentConf,
			MethodIDs methodIDs, IAgentLogger logger) throws Exception {
		
		ProblemEVCharging problemEVCh = (ProblemEVCharging)problem;
		
		int port = problemEVCh.getPort() + methodIDs.getMethodGlobalID();
		String serverIP = problemEVCh.getServerIP();
				
		String url = "http://" + serverIP + ":" + port;
		
		logger.log(Level.CONFIG, "Connection to " + url);
		
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url));
        
        this.server = new XmlRpcClient();
        this.server.setConfig(config);
        
        Vector<String> params = new Vector<String>();
        Object[] result = (Object[])this.server.execute(GET_INDIVIDUAL_SIZE, params);
        this.domainSize = (Integer)result[0];        
	}

	@Override
	public void exit() throws Exception {
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		if (DEBUG) {
//			System.out.println("IndividualPoint: " + individualPoint.toLogString());
		}
		double[] indArr = individualPoint.exortAsArray();
		
        Vector<String> params = new Vector<>();
        params.addElement(Arrays.toString(indArr));

        Object[] result;
		try {
			result = (Object[])this.server.execute(EVALUATE, params);
		} catch (XmlRpcException e) {
			logger.logThrowable("Can't contact EVCharging server", e);
			
			return Double.MAX_VALUE;
		}
		
		double fitnessVal = (Double)result[0];
		if (DEBUG) {
			System.out.println("FitnessVal: " + fitnessVal);
		}
		
        return fitnessVal;
	}
	
	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {
		
		DatasetDescription datasetDescr = (DatasetDescription) datasetDescription;
		return ToolReadProblemCO.readProblem(datasetDescr, problem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		return ToolGenerateIndividualEVCharging.generateIndividual(domainSize,
				random, normalDistMultiplicator, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionCO.readSolution(fileOfSolution, logger);
	}

	/**
	 * Returns clone
	 * @return
	 */
	public AAProblemToolEVCharging deepClone() {
		return this;
	}

}
