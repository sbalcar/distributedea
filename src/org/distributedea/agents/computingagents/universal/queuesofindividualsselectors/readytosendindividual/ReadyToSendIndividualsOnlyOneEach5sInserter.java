package org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

public class ReadyToSendIndividualsOnlyOneEach5sInserter extends AReadyToSendIndividualsInserter {
	
	private int TIME_INTERVAL_SECONDS = 5;

	private Timestamp lastInsertTimestamp; 

	@Override
	public void insertIndiv(IndividualEvaluated individualEval, IProblem problem) {
		if (individualEval == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		insert(individualEval, problem);
	}


	@Override
	public void insertIndivs(List<IndividualEvaluated> individualsEval,
			IProblem problem) {

		if (individualsEval.isEmpty()) {
			return;
		}
		
		IndividualEvaluated individualE = individualsEval.get(0);
		insert(individualE, problem);
	}
	

	private void insert(IndividualEvaluated individualEval, IProblem problem) {
		
		Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
		
		if (lastInsertTimestamp == null) {
			lastInsertTimestamp = timestampNow;
			readyToSendIndividuals.addIndividual(individualEval, problem);
		}
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(lastInsertTimestamp.getTime());
	    cal.add(Calendar.SECOND, TIME_INTERVAL_SECONDS); // subtract given seconds
	    Timestamp nextInsertTimestamp = new Timestamp(cal.getTime().getTime());
		
		if (nextInsertTimestamp.before(timestampNow)) {
			lastInsertTimestamp = timestampNow;
			readyToSendIndividuals.addIndividual(individualEval, problem);
		}
	}
}
