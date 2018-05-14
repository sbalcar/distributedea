package org.distributedea.agents.systemagents;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.monitor.model.MonitorStatisticModel;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.MonitorOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.monitor.GetStatistic;
import org.distributedea.ontology.monitor.StartMonitoring;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.problem.IProblem;

/**
 * Agent which used to monitor computation of all running instances
 * of {@link Agent_ComputingAgent}. Agent is receiving the results and analyzes
 * them by using statistical tools
 * @author stepan
 *
 */
public class Agent_Monitor extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	private MonitorStatisticModel model;
	
	private MethodDescriptions agentsToMonitor;

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		ontologies.add(MonitorOntology.getInstance());
		return ontologies;
	}

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileLogger(this);
		}
		return logger;
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

					Concept concept = action.getAction();
					
					getLogger().log(Level.INFO, "Request for " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof GetStatistic) {

						return respondToGetStatistic(msgRequest, action);	
					} else if (concept instanceof StartMonitoring) {
						
						return respondToStartMonitoring(msgRequest, action);
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
					Serializable content =  msgInform.getContentObject();
					
					if (content instanceof IndividualWrapper) {
						
						IndividualWrapper individualWrapper =
								(IndividualWrapper) content;
						processIndividualWrapper(msgInform, individualWrapper);
					}
					
				} catch (Exception e) {
					getLogger().logThrowable("Problem extracting content", e);
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

	/**
	 * Process received {@link IndividualWrapper}. Adds the new individual
	 * to {@link MonitorStatisticModel}
	 * @param msgInform
	 * @param action
	 */
	protected void processIndividualWrapper(ACLMessage msgInform, IndividualWrapper individualWrp) {
		
		if (individualWrp == null || ! individualWrp.valid(getLogger())) {
			getLogger().log(Level.INFO, "Received wrong " + IndividualWrapper.class.getSimpleName());
			return;
		}
		
		MethodDescription descr = individualWrp.getMethodDescription();
		AgentConfiguration conf = descr.getAgentConfiguration();
		getLogger().log(Level.INFO, "Individual from : " + conf.exportAgentname());
		
		if (model != null) 	{
			model.addIndividualWrp(individualWrp);
		}
	}

	protected ACLMessage respondToStartMonitoring(ACLMessage request, Action action) {
		
		StartMonitoring startMonitoring = (StartMonitoring) action.getAction();
		JobID jobID = startMonitoring.getJobID();
		IProblem problemToSolve = startMonitoring.getProblemToSolve();
		MethodDescriptions agentsToMonitor = startMonitoring.getAgentsToMonitor();
		
		this.model = new MonitorStatisticModel(jobID, problemToSolve);
		this.agentsToMonitor = agentsToMonitor;
		
		return null;
	}
	
	/**
	 * Process received {@link GetStatistic}. Exports {@link Statistic} from
	 * current {@link MonitorStatisticModel}.
	 * @param request
	 * @param action
	 * @return
	 */
	protected ACLMessage respondToGetStatistic(ACLMessage request, Action action) {
		
		GetStatistic getStatistic = (GetStatistic) action.getAction();
		JobID jobID = getStatistic.getJobID();
		
		Statistic statistic = null;
		if (model != null && model.getJobID().equals(jobID)) {
			
			statistic = model.exportStatistic();
			model = null;
		} else {
			
			statistic = new Statistic(jobID);
		}
		
		if (! statistic.valid(getLogger())) {
			throw new IllegalStateException();
		}
		
		MethodDescriptions monitoredAgents = statistic.exportAgentDescriptions();
		for (MethodDescription descriptionI : monitoredAgents.getAgentDescriptions()) {
			this.getLogger().log(Level.INFO, descriptionI.exportMethodName());
		}
		MethodDescriptions agentsToMonitorClone = this.agentsToMonitor.deepClone();
		
		agentsToMonitorClone.removeAll(monitoredAgents);
		if (! agentsToMonitorClone.isEmpty()) {
			System.out.println("Metody co nic neposlaly:");
			for (MethodDescription agentDescriptionI :
				agentsToMonitorClone.getAgentDescriptions()) {
				System.out.println(agentDescriptionI.exportMethodName());
			}
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(MonitorOntology.getInstance().getName());
		
		// sends individual as object
		try {
			reply.setContentObject(statistic);
		} catch (Exception e) {
			logger.logThrowable("IOException by sending " +
					Statistic.class.getSimpleName(), e);
		}
				
		return reply;
	}
}
