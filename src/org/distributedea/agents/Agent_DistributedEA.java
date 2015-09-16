package org.distributedea.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.ConsoleLogger;

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

public abstract class Agent_DistributedEA extends Agent {

	private static final long serialVersionUID = 1L;
	
	protected Codec codec = new SLCodec();
	protected AgentLogger logger = new AgentLogger(this);
	
	public Codec getCodec() {
		return codec;
	}
	
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
	public final String getNumberOfContainer() {
		
		String localName = getAID().getLocalName();
		
		int charIndex;
		for (charIndex = localName.length() -1; charIndex >= 0; charIndex--) {
			char charI = localName.charAt(charIndex);
			if (charI == '-') {
				break;
			}
		}
		
		assert(charIndex > 0);
		
		String numberString = localName.substring(charIndex +1);

		return numberString;
	}
	
	/**
	 * Search agents which provides service in local container
	 * 
	 * @param service
	 * @return
	 */
	public AID [] searchLocalContainerDF( String service ) {
	
		int index = getAID().getLocalName().indexOf('-');
		String containerNumber = getAID().getName().substring(index +1);
		
		List<AID> aidList = new ArrayList<>();
		
		AID [] aids = searchDF(service);
		for (int i = 0; i < aids.length; i++) {
			AID aidI = aids[i];
			int indexI = aidI.getLocalName().indexOf('-');
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
        all.setMaxResults(new Long(-1));

        try {
            DFAgentDescription[] result = DFService.search(this, dfd, all);
            AID[] agents = new AID[result.length];
            for (int i = 0; i < result.length; i++) {
                agents[i] = result[i].getName();
            }
            return agents;

        } catch (FIPAException fe) {
        	fe.printStackTrace();
        }
        
        return null;
    }
	
	/**
	 * Log Exception
	 * 
	 * @param string
	 * @param exception
	 */
//	public void logException(String string, Exception exception) {
//		ConsoleLogger.logThrowable(string + exception.getMessage(), exception);
//	}

	/**
	 * Log Serve message
	 * @param message
	 */
//	public void logSevere(String message) {
//		ConsoleLogger.log(Level.SEVERE, message);
//	}
	
	/**
	 * Log Info
	 * 
	 * @param text
	 */
//	public void logInfo(String text) {
//		ConsoleLogger.log(Level.INFO, text);
//	}
	
	
	/**
	 * Agent initialization
	 */
	protected void initAgent() {
		
		String name = getAID().getName();
		logger.log(Level.INFO, "Agent " + name + " is alive...");
		
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
        	logger.logThrowable("Registration faild", fe);
        }
	}
	
	/**
	 * Agent DF deregistration
	 */
	protected void deregistrDF() {
        
        try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			logger.logThrowable("Error by deregistration", e);
		}  
        
	}
}
