package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.agentinfo.AgentInfoWrapper;
import org.distributedea.ontology.agentinfo.GetAgentInfo;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configuration.RequiredAgent;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.ReadyToBeKilled;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;

public class ManagementOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;

	private ManagementOntology() {
        super("ManagementOntology");

        try {
        	add(DescribeNode.class);
        	add(NodeInfo.class);
        	
        	add(GetAgentInfo.class);
            add(AgentInfo.class);
            add(AgentInfoWrapper.class);
            
            add(CreateAgent.class);
            add(CreatedAgent.class);
            add(PrepareYourselfToKill.class);
            add(ReadyToBeKilled.class);
            add(KillAgent.class);
            add(KillContainer.class);
            add(RequiredAgent.class);
            add(AgentConfiguration.class);
            add(Arguments.class);
            add(Argument.class);


        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ManagementOntology theInstance = new ManagementOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
