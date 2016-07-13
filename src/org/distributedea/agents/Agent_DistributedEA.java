package org.distributedea.agents;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.IAgentLogger;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Abstract class of Agent which is inherited by all Agents in system
 * @author stepan
 *
 */
public abstract class Agent_DistributedEA extends Agent {

	private static final long serialVersionUID = 1L;
	
	protected Codec codec = null;
	
	protected IAgentLogger logger = null;

	
	/**
	 * Get Codec for coding ACL messages 
	 * @return
	 */
	public Codec getCodec() {
		
		if (codec == null) {
			this.codec = new SLCodec();
		}
		return codec;
	}

	/**
	 * Get Logger
	 * @return
	 */
	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new AgentLogger(this);
		}
		return logger;
	}

	
	/**
	 * Defines Ontology with which the agent can work
	 * @return
	 */
	public abstract List<Ontology> getOntologies();
	
	/**
	 * Get the type of this agent
	 * 
	 * @return
	 */
	public final String getType() {
		return this.getClass().getName();
	}
	
	/**
	 * Get the number of container where lives this agent
	 * 
	 * @return
	 */
	public static final String getNumberOfContainer() {

		String containerNumber = "";

		String hosname;
		try {
			hosname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			//getLogger().logThrowable("df", e);
			return null;
		}

		for (int charIndex = 0; charIndex < hosname.length(); charIndex++) {
			char charI = hosname.charAt(charIndex);
			if ('0' <= charI && charI <= '9') {
				containerNumber += charI;
			}
		}

		try {
			int numb = Integer.parseInt(containerNumber);
			return "" + numb;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Search agents which provides service in local container
	 * 
	 * @param service
	 * @return
	 */
	public AID [] searchLocalContainerDF(String service) {
	
		int index = getAID().getLocalName().lastIndexOf(Configuration.CONTAINER_NUMBER_PREFIX);
		String containerNumber = getAID().getName().substring(index +1);
		
		List<AID> aidList = new ArrayList<>();
		
		AID [] aids = searchDF(service);
		for (int i = 0; i < aids.length; i++) {
			AID aidI = aids[i];
			int indexI = aidI.getLocalName().lastIndexOf(Configuration.CONTAINER_NUMBER_PREFIX);
			String containerNumberI = aidI.getName().substring(indexI +1);
			
			if (containerNumber.equals(containerNumberI)) {
				aidList.add(aidI);
			}
		}
		
		return aidList.toArray(new AID[aidList.size()]);
	}
	
	/**
	 * Search agents which provides service in all containers
	 * 
	 * @param service
	 * @return
	 */
	public AID [] searchDF( String service ) {
    	
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        
        SearchConstraints all = new SearchConstraints();
        all.setMaxResults(new Long(10000));

        try {
            DFAgentDescription[] result = DFService.search(this, dfd, all);
            AID[] agents = new AID[result.length];
            for (int i = 0; i < result.length; i++) {
                agents[i] = result[i].getName();
            }
            return agents;

        } catch (FIPAException fe) {
        	getLogger().logThrowable("Error by searching in DF", fe);
        }
        
        return null;
    }
	
	/**
	 * Agent initialization
	 */
	protected void initAgent() {
		
		String name = getAID().getName();
		getLogger().log(Level.INFO, "Agent " + name + " is alive...");
		
		getContentManager().registerLanguage(getCodec());

		for (Ontology ontologyI : getOntologies()) {
			getContentManager().registerOntology(ontologyI);
		}
	}
	
	/**
	 * Agent DF registration
	 */
	protected void registrDF() {
		        
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( getType() );
        sd.setName( getLocalName() );
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd );  
        
        } catch (FIPAException fe) {
        	getLogger().logThrowable("Registration faild", fe);
        }
	}
	
	/**
	 * Agent DF deregistration
	 */
	protected void deregistrDF() {
        
        try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			getLogger().logThrowable("Error by deregistration", e);
		}  
        
	}
}
