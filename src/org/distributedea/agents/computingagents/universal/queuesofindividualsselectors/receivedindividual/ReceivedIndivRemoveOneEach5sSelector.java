package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual;

import java.sql.Timestamp;
import java.util.Calendar;

import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

public class ReceivedIndivRemoveOneEach5sSelector extends AReceivedIndividualSelector{
	
	private int TIME_INTERVAL_SECONDS = 5;

	private Timestamp lastInsertTimestamp; 
	
	@Override
	public IndividualWrapper getIndividual(IProblem problem) {

		Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
		
		if (lastInsertTimestamp == null) {
			lastInsertTimestamp = timestampNow;
			return receivedIndividuals.removeIndividual(problem);
		}
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(lastInsertTimestamp.getTime());
	    cal.add(Calendar.SECOND, -TIME_INTERVAL_SECONDS); // subtract given seconds
	    Timestamp nextInsertTimestamp = new Timestamp(cal.getTime().getTime());
		
		if (nextInsertTimestamp.before(timestampNow)) {
			return receivedIndividuals.removeIndividual(problem);
		}
		return null;
	}

}
