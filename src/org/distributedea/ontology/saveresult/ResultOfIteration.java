package org.distributedea.ontology.saveresult;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodInstanceDescription;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.job.JobID;


public class ResultOfIteration implements Concept {

	private static final long serialVersionUID = 1L;
	
	private JobID jobID;
	private Iteration iteration;
	private List<ResultOfMethodInstanceIteration> methodInstanceIterations;
	
	@Deprecated
	public ResultOfIteration() {}  //only for JADE
	
	public ResultOfIteration(JobID jobID, Iteration iteration,
			List<ResultOfMethodInstanceIteration> methodInstanceIterations) {
		
		this.jobID = jobID;
		this.iteration = iteration;
		this.methodInstanceIterations = methodInstanceIterations;
	}

	
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}

	public Iteration getIteration() {
		return iteration;
	}
	@Deprecated
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}
	
	public List<ResultOfMethodInstanceIteration> getMethodInstanceIterations() {
		return methodInstanceIterations;
	}
	@Deprecated
	public void setMethodInstanceIterations(
			List<ResultOfMethodInstanceIteration> methodInstanceIterations) {
		this.methodInstanceIterations = methodInstanceIterations;
	}
	
	public void exportXML(File dir) throws FileNotFoundException, JAXBException {
		
		if (dir == null || (! dir.exists()) || (! dir.isDirectory())) {
			return;
		}
		
		if (methodInstanceIterations != null) {
			for (ResultOfMethodInstanceIteration resultI : methodInstanceIterations) {
				
				MethodInstanceDescription methodI =
						resultI.getMethodInstanceDescription();
				
				File methodDirI = new File(dir.getAbsolutePath() +
						File.separator + methodI.exportInstanceName());
				if (! methodDirI.exists()) {
					methodDirI.mkdir();
				}
	
				File instanceDirI = new File(methodDirI.getAbsolutePath() +
						File.separator + iteration.exportIterationToString());
				if (! instanceDirI.exists()) {
					instanceDirI.mkdir();
				}
	
				resultI.exportXML(instanceDirI);
			}
		}
	}
	
}
