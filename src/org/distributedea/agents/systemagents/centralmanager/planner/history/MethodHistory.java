package org.distributedea.agents.systemagents.centralmanager.planner.history;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;


public class MethodHistory {
	
	private MethodInstanceDescription methodInstanceDescription;
	
	private AgentDescription currentAgent;
	
	private List<MethodStatisticResultWrapper> statistics;

	
	public MethodInstanceDescription getMethodInstanceDescription() {
		return methodInstanceDescription;
	}
	public void setMethodInstanceDescription(
			MethodInstanceDescription methodInstanceDescription) {
		this.methodInstanceDescription = methodInstanceDescription;
	}
	
	public AgentDescription getCurrentAgent() {
		return currentAgent;
	}
	public void setCurrentAgent(AgentDescription currentAgent) {
		this.currentAgent = currentAgent;
	}
	
	public List<MethodStatisticResultWrapper> getStatistics() {
		return statistics;
	}
	public void setStatistics(List<MethodStatisticResultWrapper> statistics) {
		this.statistics = statistics;
	}
	public void addStatistic(MethodStatisticResult statistic,
			Iteration iteration) {
		
		if (this.statistics == null) {
			this.statistics = new ArrayList<>();
		}
		MethodStatisticResultWrapper methodStatisticResultWrp =
				new MethodStatisticResultWrapper(iteration, getCurrentAgent(), statistic);
		this.statistics.add(methodStatisticResultWrp);
	}

	public void remove(MethodStatisticResultWrapper method) {
		this.statistics.remove(method);
	}

	public MethodStatisticResultWrapper exportLastStatistic() {
		
		if (this.statistics == null || this.statistics.isEmpty()) {
			return null;
		}
		
		return this.statistics.get(this.statistics.size() -1);
	}
	public MethodStatisticResultWrapper exportStatistic(Iteration iteration) {
		
		if (this.statistics == null || this.statistics.isEmpty()) {
			return null;
		}
		
		for (MethodStatisticResultWrapper statisticI : statistics) {
			
			Iteration methodIterationI = statisticI.getIteration();
			if (methodIterationI.equals(iteration)) {
				return statisticI;
			}
		}
		return null;
	}
	
	public long exportNumberOfTheBestCreatedIndividuals() {
		
		long numberOfTheBestCreatedIndividuals = 0;
		
		for (MethodStatisticResultWrapper resultWrpI : statistics) {
			MethodStatisticResult resultI = resultWrpI.getMethodStatisticResult();
			
			numberOfTheBestCreatedIndividuals += resultI.getNumberOfTheBestCreatedIndividuals();
		}
		
		return numberOfTheBestCreatedIndividuals;
	}
	
	public long exportNumberOfIteration() {
		if (this.statistics == null) {
			return 0;
		}
		return this.statistics.size();
	}
	
	public ResultOfMethodInstanceIteration exportResultOfMethodInstanceIteration(Iteration iteration) {
	
		MethodStatisticResultWrapper statistI = exportStatistic(iteration);
		
		if (statistI == null) {
			return null;
		}
		
		ResultOfMethodInstanceIteration result =
				new ResultOfMethodInstanceIteration();
		result.setAgentDescription(statistI.getAgentDescription());
		result.setMethodInstanceDescription(getMethodInstanceDescription());
		result.setMethodStatisticResult(statistI.getMethodStatisticResult());
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodHistory)) {
	        return false;
	    }
	    
	    MethodHistory methodHistoryOuther = (MethodHistory)other;
	    
	    boolean areMethodInstanceDescrition = this.getMethodInstanceDescription()
	    		.equals(methodHistoryOuther.getMethodInstanceDescription());
	    
	    if (methodHistoryOuther.statistics == null && this.statistics == null) {
	    	return areMethodInstanceDescrition;
	    }
	    
	    if ((methodHistoryOuther.statistics == null && this.statistics != null) ||
	    		(methodHistoryOuther.statistics != null && this.statistics == null) ||
	    		(methodHistoryOuther.statistics.size() != this.statistics.size())) {
	    	return false;
	    }
	    
	    for (int i = 0; i < this.statistics.size(); i++) {
	    	
	    	MethodStatisticResultWrapper firstI =
	    			this.statistics.get(i);
	    	MethodStatisticResultWrapper sesondI =
	    			methodHistoryOuther.statistics.get(i);
	    	
	    	if (! firstI.equals(sesondI)) {
	    		return false;
	    	}
	    }
	    
	    return areMethodInstanceDescrition;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String string = methodInstanceDescription.toString();
		
		if (statistics != null) {
			for (MethodStatisticResultWrapper methodI : statistics) {
				string += methodI.toString();
			}
		}

		return string;
	}
	
	void exportXML(File monitoringDir) throws FileNotFoundException, JAXBException {
		
		String methodInstanceString = methodInstanceDescription.exportInstanceName();
		
		String methodDirName = monitoringDir.getAbsolutePath() +
				File.separator + methodInstanceString;
				 
		File methodDir = new File(methodDirName);
		
		if (! methodDir.exists()) {
			methodDir.mkdirs();
		}
		
		if (statistics != null) {
			for (MethodStatisticResultWrapper statisticI : statistics) {
				 
				statisticI.exportXML(methodDir);
			}
		}		 
		methodInstanceDescription.exportXML(new File(methodDirName));
		if (currentAgent != null) {
			currentAgent.exportXML(new File(methodDirName));
		}
	}
	
	
	/**
	 * Import the {@link MethodHistory} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static MethodHistory importXML(File methodDir)
			throws FileNotFoundException {
		
		if (methodDir == null || (! methodDir.exists()) ||
				(! methodDir.isDirectory())) {
			return null;
		}
		
		List<MethodStatisticResultWrapper> statistics = new ArrayList<>();
		
		for (File fileI : methodDir.listFiles()) {
		    if (fileI.isDirectory()) {

		        MethodStatisticResultWrapper methodI =
		        		MethodStatisticResultWrapper.importXML(fileI);
		    	
		        statistics.add(methodI);
		    }
		}
		
		String methodDirName = methodDir.getAbsolutePath();	
		
		String currentAgentFileName = methodDirName + File.separator + "instance.xml";
		MethodInstanceDescription methodInstanceImported =
				MethodInstanceDescription.importXML(new File(currentAgentFileName));

		String descriptionFileName = methodDirName + File.separator + "description.xml";
		AgentDescription currentAgent = AgentDescription.importXML(new File(descriptionFileName));
		
		MethodHistory method = new MethodHistory();
		method.setCurrentAgent(currentAgent);
		method.setMethodInstanceDescription(methodInstanceImported);
		method.setStatistics(statistics);
		return method;
		
	}
	
}
