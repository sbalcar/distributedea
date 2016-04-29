package org.distributedea.agents.systemagents.centralmanager;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.InputContOpt;
import org.distributedea.ontology.job.Job;

public class InputJobQueue {

	public static List<Job> getInputJobs() {
		
		List<Job> jobs = new ArrayList<Job>();
		
		InputContOpt test = new InputContOpt();
		Job job1 = test.test02();
		
		jobs.add(job1);
		
		return jobs;
	}
}
