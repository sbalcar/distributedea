package org.distributedea.agents.systemagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.agents.systemagents.monitor.model.MonitorStatisticModel;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.MonitorOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.monitor.GetStatistic;
import org.distributedea.ontology.monitor.Statistic;

public class Agent_Monitor extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	private MonitorStatisticModel model = new MonitorStatisticModel(new ResultsOfComputing());

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		ontologies.add(MonitorOntology.getInstance());
		return ontologies;
	}

	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		
		final MessageTemplate mesTemplateRequest =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(MonitorOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateRequest) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgRequest) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgRequest);
					
					if (action.getAction() instanceof GetStatistic) {
						getLogger().log(Level.INFO, "Request for GetStatistics");
						return respondToGetStatistic(msgRequest, action);
						
					}

				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				ACLMessage failure = msgRequest.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
		
		final MessageTemplate mesTemplateResultInform =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(ResultOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateResultInform) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgInform) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(msgInform);
					
					if (action.getAction() instanceof IndividualWrapper) {
						//getLogger().log(Level.INFO, "Received Individual" );
						processIndividual(msgInform, action);
						
					}

				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				return null;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
	}

	protected void processIndividual(ACLMessage msgInform, Action action) {
		
		IndividualWrapper individualWrp = (IndividualWrapper) action.getAction();
		JobID jobID = individualWrp.getJobID();
		
		if (model.getJobID() == null) {
			model.setJobID(jobID);
		}
		
		// if received Indindividual from another Job
		if (! model.getJobID().equals(jobID)) {
			ResultsOfComputing resultOfComputingAgents =
					ComputingAgentService.sendAccessesResult_(this, getLogger());
		    model = new MonitorStatisticModel(resultOfComputingAgents);
		}
		
		model.addIndividualWrp(individualWrp);
	}
	
	protected ACLMessage respondToGetStatistic(ACLMessage request, Action action) {
		
		@SuppressWarnings("unused")
		GetStatistic getStatistic = (GetStatistic) action.getAction();
		

		ResultsOfComputing resultOfComputingAgents =
				ComputingAgentService.sendAccessesResult_(this, getLogger());
		MonitorStatisticModel model2 = new MonitorStatisticModel(resultOfComputingAgents);
		Statistic statistic2 = model2.exportStatistic();
		IndividualEvaluated best2 = statistic2.exportBestIndividual();
		
		Statistic statistic = model.exportStatistic();
		IndividualEvaluated best1 = statistic.exportBestIndividual();
		model = model2;

		if (best2.getFitness() > best1.getFitness()) {
			System.out.println(".................Chyba");
			statistic2.exportBestIndividual();
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(MonitorOntology.getInstance().getName());

		Result result = new Result(action.getAction(), statistic);

		try {
		    getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
		    getLogger().logThrowable("CodecException by sending Statistic", e);
		} catch (OntologyException e) {
		    getLogger().logThrowable(e.getMessage(), e);
		}

		return reply;
	}
}
