package org.distributedea.tests.evcharging;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.jobs.InputEVCharging;
import org.distributedea.input.postprocessing.evcharging.PostProcessingEVChargingKillServers;
import org.distributedea.input.preprocessing.evcharging.PreProcessingEVChargingRunServers;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.evcharging.point.ProblemToolHillClimbingEVChargingRandomMove;

public class Test {

	public static void main(String [] args) throws Exception {
		
		start(8080, 16);
		evaluating();
		kill(8080, 16);
	}

	private static void start(int port, int serverCount) throws Exception {
		
		System.out.println("Starting servers");

		Job jobI = InputEVCharging.test01();
		DatasetDescription datasetDesr = (DatasetDescription) jobI.getDatasetDescription();

		PreProcessingEVChargingRunServers pp = new PreProcessingEVChargingRunServers(
				port, serverCount, datasetDesr.exportDatasetFile());
		pp.run(null);
		
		System.out.println("Servers started");
	}

	private static void kill(int port, int serverCount) throws Exception {

		System.out.println("Killing servers");
		
		PostProcessingEVChargingKillServers ps = new PostProcessingEVChargingKillServers(
				port, serverCount);
		ps.run(null);
		
		System.out.println("Servers killed");
	}

	private static void evaluating() throws Exception {

		System.out.println("Evaluating");

		Job job = InputEVCharging.test01();
		IProblem problem = job.getProblem();
		IDatasetDescription datasetDescr = job.getDatasetDescription();
		MethodIDs methodIDs = new MethodIDs(0);
		
		//ProblemToolRandomSearchEVCharging pt = new ProblemToolRandomSearchEVCharging();
		//ProblemToolBruteForceEVCharging pt = new ProblemToolBruteForceEVCharging(0.005);
		ProblemToolHillClimbingEVChargingRandomMove pt = new ProblemToolHillClimbingEVChargingRandomMove(0.1, 0.5);
		
		pt.initialization(problem, null, null, methodIDs, new TrashLogger());
		
		Dataset dataset =
				pt.readDataset(datasetDescr, problem, new TrashLogger());
		
		//Individual indiv0 = pt.generateIndividual(problem, dataset, new TrashLogger());
		for (int i = 0; i < 10; i++) {
			Individual indivI =
					pt.generateIndividual(problem, dataset, new TrashLogger());
					//pt.getNeighbor(indiv0, problem, dataset, i, new TrashLogger());
			System.out.println("Individual: " + indivI.toLogString());
			
			double fitnessVal =
					pt.fitness(indivI, problem, dataset, new TrashLogger());
			
			System.out.println("FitnessVal: " + fitnessVal);
		}
	}

}
