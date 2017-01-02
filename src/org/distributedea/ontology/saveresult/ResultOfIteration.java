package org.distributedea.ontology.saveresult;

import jade.content.Concept;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;

/**
 * Ontology represents results of one {@link Iteration}. Result statistics
 * are not relating to agent but to {@link MethodInstanceDescription}
 * @author stepan
 *
 */
public class ResultOfIteration implements Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * JobRun identification
	 */
	private JobID jobID;
	private Iteration iteration;
	private List<ResultOfMethodInstanceIteration> methodInstanceIterations;
	private Plan plan;
	private RePlan rePlan;
	
	
	/**
	 * COnstructor
	 */
	public ResultOfIteration() {
		methodInstanceIterations = new ArrayList<>();
	}
	
	/**
	 * Returns {@link JobRun} identification
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}

	public void setJobID(JobID jobID) {
		if (jobID == null) {
			throw new IllegalArgumentException();
		}
		this.jobID = jobID;
	}

	/**
	 * Returns current {@link Iteration}
	 * @return
	 */
	public Iteration getIteration() {
		return iteration;
	}

	public void setIteration(Iteration iteration) {
		if (iteration == null) {
			throw new IllegalArgumentException();
		}
		this.iteration = iteration;
	}
	
	public List<ResultOfMethodInstanceIteration> getMethodInstanceIterations() {
		return methodInstanceIterations;
	}

	public void setMethodInstanceIterations(
			List<ResultOfMethodInstanceIteration> methodInstanceIterations) {
		if (methodInstanceIterations == null) {
			throw new IllegalArgumentException();
		}
		this.methodInstanceIterations = methodInstanceIterations;
	}
	
	/**
	 * Returns {@link Plan} of current iteration
	 * @return
	 */
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		if (plan == null || ! plan.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Plan.class.getSimpleName() + " is not valid");
		}
		this.plan = plan;
	}

	/**
	 * Returns {@link RePlan} of current iteration
	 * @return
	 */
	public RePlan getRePlan() {
		return rePlan;
	}

	public void setRePlan(RePlan rePlan) {
		if (rePlan == null || ! rePlan.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					RePlan.class.getSimpleName() + " is not valid");
		}
		this.rePlan = rePlan;
	}

	/**
	 * Tests validity of structure
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		if (methodInstanceIterations == null) {
			return false;
		}
		for (ResultOfMethodInstanceIteration methodI : methodInstanceIterations) {
			if (! methodI.valid(logger)) {
				return false;
			}
		}
		if (plan == null || ! plan.valid(logger)) {
			return false;
		}
		if (rePlan == null || ! rePlan.valid(logger)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Exports to XML
	 * @param dir
	 * @throws Exception
	 */
	public void exportXML(File dir) throws Exception {
		
		if (dir == null || (! dir.isDirectory())) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		if (! valid(new TrashLogger())) {
			return;
		}
		
		String jobIDFileName = dir.getAbsolutePath() + File.pathSeparator + "jobID.txt";
		if (! new File(jobIDFileName).exists()) {
			jobID.exportXML(dir);
		}

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

		// export list of plans
		String planDirName = dir.getAbsolutePath() + File.separator + "plan";
		
		File planDir = new File(planDirName);
		if (! planDir.exists()) {
			planDir.mkdir();
		}
		plan.exportXML(new File(planDirName));
		
		// export list of replans
		String replanDirName = dir.getAbsolutePath() + File.separator + "replan";
		
		File replanDir = new File(replanDirName);
		if (! replanDir.exists()) {
			replanDir.mkdir();
		}
		rePlan.exportXML(new File(replanDirName));
	}
	
}
