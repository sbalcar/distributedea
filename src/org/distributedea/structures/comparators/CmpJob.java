package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;

public class CmpJob implements Comparator<Job> {

	@Override
	public int compare(Job job0, Job job1) {

		String jobID0 = job0.getJobID();
		String jobID1 = job1.getJobID();
		
		return jobID0.compareTo(jobID1);
	}

}
