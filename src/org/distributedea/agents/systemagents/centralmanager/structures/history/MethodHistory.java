package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;

/**
 * Structure represents history across all iterations of one method instance.
 * @author stepan
 *
 */
public class MethodHistory {
	
	/**
	 * Description of method instance
	 */
	private MethodInstanceDescription methodInstanceDescription;
	
	/**
	 * Describe current Agent forming method. Field is Null when the method
	 * is in current {@link Iteration} not running.
	 */
	private AgentDescription currentAgent;
	
	/**
	 * Statistics of individual iterations
	 */
	private List<MethodStatisticResultWrapper> statistics;

	
	/**
	 * Constructor
	 * @param methodInstanceDescription
	 * @param currentAgent
	 */
	public MethodHistory(MethodInstanceDescription methodInstanceDescription,
			AgentDescription currentAgent) {
		if (methodInstanceDescription == null ||
				! methodInstanceDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodInstanceDescription.class.getSimpleName() +
					" is not valid");
		}
		
		this.methodInstanceDescription = methodInstanceDescription;
		this.currentAgent = currentAgent;
	}
	
	/**
	 * Returns description of Method instance
	 * @return
	 */
	public MethodInstanceDescription getMethodInstanceDescription() {
		return methodInstanceDescription;
	}
	
	/**
	 * Returns current Agent
	 * @return
	 */
	public AgentDescription getCurrentAgent() {
		return currentAgent;
	}
	public void setCurrentAgent(AgentDescription currentAgent) {
		this.currentAgent = currentAgent;
	}
	
	public List<MethodStatisticResultWrapper> getStatistics() {
		return statistics;
	}
	private void setStatistics(List<MethodStatisticResultWrapper> statistics) {
		this.statistics = statistics;
	}
	
	/**
	 * Add Statistic {@link MethodStatisticResult} with given {@link Iteration}
	 * @param statistic
	 * @param iteration
	 */
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

	public boolean isRunningLastNIteration(Iteration iteration, long iterationCount) {
		
		// is running now
		MethodStatisticResultWrapper resultWrp0 =
				exportStatistic(iteration);
		if (resultWrp0 == null) {
			return false;
		}
		
		// is running in previous iterations
		Iteration iterationI = iteration;
		for (int i = 0; i < iterationCount; i++) {
			
			Iteration newIteration = iterationI.exportPreviousIteration();
			if (newIteration == null) {
				return false;
			}
			iterationI = newIteration;
			
			MethodStatisticResultWrapper resultWrpI =
					exportStatistic(iterationI);
			if (resultWrpI == null) {
				return false;
			}
		}
		return true;
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
	
	/**
	 * Exports number of {@link Iteration}s
	 * @return
	 */
	public long exportNumberOfIteration() {
		if (this.statistics == null) {
			return 0;
		}
		return this.statistics.size();
	}
	
	/**
	 * Exports number of the last {@link Iteration}
	 * @return
	 */
	public long exportNumberOfLastIteration() {
		if (this.statistics == null) {
			return 0;
		}
		MethodStatisticResultWrapper lastStatistic =
				this.statistics.get(this.statistics.size() -1);
		
		return lastStatistic.getIteration().getIterationNumber();
	}
	
	/**
	 * Exports {@link ResultOfMethodInstanceIteration} for one {@link Iteration}.
	 * @param iteration
	 * @return
	 */
	public ResultOfMethodInstanceIteration exportResultOfMethodInstanceIteration(Iteration iteration) {
	
		MethodStatisticResultWrapper statistI = exportStatistic(iteration);
		
		if (statistI == null) {
			return null;
		}
		
		MethodInstanceDescription instanceDescrI =
				getMethodInstanceDescription();
		AgentDescription agentDescrI =
				statistI.getAgentDescription();
		MethodStatisticResult methodStatisticResultI =
				statistI.getMethodStatisticResult();
		
		return new ResultOfMethodInstanceIteration(
				instanceDescrI, agentDescrI, methodStatisticResultI);
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
	
	/**
	 * Exports to XML
	 * @param monitoringDir
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	void exportXML(File monitoringDir) throws IOException {
		
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
	 * @throws IOException
	 */
	public static MethodHistory importXML(File methodDir)
			throws IOException {
		
		if (methodDir == null || ! methodDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
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

		AgentDescription currentAgent = null;
		String descriptionFileName = methodDirName + File.separator + "description.xml";		
		if (new File(descriptionFileName).isFile()) {
			currentAgent = AgentDescription.importXML(new File(descriptionFileName));
		}
		
		MethodHistory method = new MethodHistory(
				methodInstanceImported, currentAgent);
		method.setStatistics(statistics);
		
		return method;
	}
	
}
