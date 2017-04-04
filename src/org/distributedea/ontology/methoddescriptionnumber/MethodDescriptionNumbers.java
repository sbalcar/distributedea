package org.distributedea.ontology.methoddescriptionnumber;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.methodtypenumber.MethodTypeNumber;
import org.distributedea.ontology.methodtypenumber.MethodTypeNumbers;
import org.distributedea.structures.comparators.CpmMethodDescriptionNumbers;

/**
 * Ontology represents List of {@link MethodDescriptionNumber}
 * @author stepan
 *
 */
public class MethodDescriptionNumbers implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<MethodDescriptionNumber> numbers;
	

	/**
	 * Constructor
	 */
	public MethodDescriptionNumbers() {
		this.numbers = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param numbersList
	 */
	public MethodDescriptionNumbers(List<MethodDescriptionNumber> numbersList) {
		if (numbersList == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (MethodDescriptionNumber counterI : numbersList) {
			if (counterI == null || ! counterI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						List.class.getSimpleName() + " is not valid");
			}
		}
		
		this.numbers = numbersList;
	}
	
	/**
	 * Copy constructor
	 * @param numbersToClone
	 */
	public MethodDescriptionNumbers(MethodDescriptionNumbers numbersToClone) {
		if (numbersToClone == null || ! numbersToClone.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionNumbers.class.getSimpleName() + " is not valid");
		}
		
		for (MethodDescriptionNumber counterI : numbersToClone.getMethDescNumbers()) {
			addMetDescNumberAsCouter(counterI.deepClone());
		}
	}
	
	public List<MethodDescriptionNumber> getMethDescNumbers() {
		return numbers;
	}
	@Deprecated
	public void setMethDescNumbers(List<MethodDescriptionNumber> numbers) {
		this.numbers = numbers;
	}
	
	public void addMetDescNumbersAsCounters(List<MethodDescriptionNumber> numbers) {
		if (numbers == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + "is not valid");
		}

		for (MethodDescriptionNumber numberI : numbers) {
			addMetDescNumberAsCouter(numberI);
		}
	}
	
	/**
	 * Adds the given {@link MethodDescriptionNumber} to structure
	 * @param number
	 */
	public void addMetDescNumberAsCouter(MethodDescriptionNumber number) {
		if (number == null || ! number.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionNumber.class.getSimpleName() + "is not valid");
		}
		
		if (this.numbers == null) {
			this.numbers = new ArrayList<>();
		}
		
		MethodDescriptionNumber methodDescriptionNumber =
				exportMethodDescriptionNumberOfGiven(number.getDescription());
		if (methodDescriptionNumber != null) {
			methodDescriptionNumber.increment(number.getNumber());
		} else {
			this.numbers.add(number);
		}
	}

	public int addMethodDescriptionWithUniqueNumber(MethodDescription methodDescription) {
		if (methodDescription == null || ! methodDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + "is not valid");
		}
		
		int number = exportMaxNumberOfMethodDescription();
		if (number == Integer.MIN_VALUE) {
			number = -1;
		}
		
		MethodDescriptionNumber methodDescriptionNumber =
			new MethodDescriptionNumber(methodDescription, number+1);
		this.numbers.add(methodDescriptionNumber);
		
		return number+1;
	}
	
	public boolean containsMethodDescription(MethodDescription methodDescription) {
		
		if (exportMethodDescriptionNumberOfGiven(methodDescription) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean containsMethodDescriptionsWithNumbers(List<Integer> numberIDs) {
		if (numberIDs == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		for (Integer numberI : numberIDs) {
			if (! containsMethodDescriptionWithNumber(numberI)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsMethodDescriptionWithNumber(int number) {
		
		for (MethodDescriptionNumber counterI : this.numbers) {
			int numberI = counterI.getNumber();
			if (numberI == number) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Exports {@link MethodDescriptionNumbers} of given
	 * {@link MethodDescription} types
	 * @param methodDescriptions
	 * @return
	 */
	public MethodDescriptionNumbers exportMethodDescriptionNumbersOfGiven(
			MethodDescriptions methodDescriptions) {

		return new MethodDescriptionNumbers(
				exportMethodDescriptionNumbersListOfGiven(methodDescriptions));
	}
	
	/**
	 * Exports {@link List<MethodDescriptionNumber>} of given
	 * {@link MethodDescription} types
	 * @param methodDescriptions
	 * @return
	 */
	public List<MethodDescriptionNumber> exportMethodDescriptionNumbersListOfGiven(
			MethodDescriptions methodDescriptions) {
		if (methodDescriptions == null || ! methodDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		
		List<MethodDescriptionNumber> descNumbersToExport = new ArrayList<>();
		
		for (MethodDescriptionNumber descrNumberI : numbers) {
			
			MethodDescription descI = descrNumberI.getDescription();
			if (methodDescriptions.containsMethodDescription(descI)) {
				descNumbersToExport.add(descrNumberI.deepClone());
			}
		}
		
		return descNumbersToExport;
	}

	/**
	 * Exports the first {@link MethodDescriptionNumber} of given {@link MethodDescription}
	 * @param methodDescription
	 * @return
	 */
	public MethodDescriptionNumber exportMethodDescriptionNumberOfGiven(
			MethodDescription methodDescription) {
		if (methodDescription == null || ! methodDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		
		for (MethodDescriptionNumber counterI : this.numbers) {
			MethodDescription methodDescriptionI =
					counterI.getDescription();
			if (methodDescriptionI.equals(methodDescription)) {
				return counterI;
			}
		}
		return null;
	}

	/**
	 * Exports {@link MethodDescriptionNumber} of given numberID
	 * @param number
	 * @return
	 */
	public MethodDescriptionNumber exportMethodDescriptionNumberOfGivenNumberID(int number) {
		
		for (MethodDescriptionNumber counterI : this.numbers) {
			if (counterI.getNumber() == number) {
				return counterI;
			}
		}
		return null;
	}
	
	public MethodTypeNumbers exportMethodTypeNumbers() {
		
		MethodTypeNumbers numbersToExport = new MethodTypeNumbers();

		for (MethodDescriptionNumber counterI : this.numbers) {
			
			int numberI = counterI.getNumber();
			MethodDescription descriptionI = counterI.getDescription();
			MethodType methodTypeI = descriptionI.exportMethodType();
			
			MethodTypeNumber numberTypeI = new MethodTypeNumber(methodTypeI, numberI);
			numbersToExport.addAndIncrementNumber(numberTypeI);
		}

		return numbersToExport;
	}
	
	/**
	 * Increment number(counter) of given {@link MethodDescription}
	 * @param methodDescription
	 */
	public void incrementCounterOf(MethodDescription methodDescription) {
		
		if (! containsMethodDescription(methodDescription)) {
			addMetDescNumberAsCouter(new MethodDescriptionNumber(methodDescription, 0));
		}
		
		MethodDescriptionNumber counter =
				exportMethodDescriptionNumberOfGiven(methodDescription);
		counter.increment();
	}
	
	public int exportMaxNumberOfMethodDescription() {
		
		int maxNumber = Integer.MIN_VALUE;
		
		for (MethodDescriptionNumber methodDesciptNumberI : this.numbers) {
			
			int numberI = methodDesciptNumberI.getNumber();
			if (numberI > maxNumber) {
				maxNumber = numberI;
			}
		}

		return maxNumber;
	}
	
	public MethodDescriptionNumber exportMethodDescriptionWithMaxNumber() {
		
		return Collections.max(this.numbers, new CpmMethodDescriptionNumbers());	
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodDescriptionNumbers deepClone() {
		
		return new MethodDescriptionNumbers(this);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (numbers == null) {
			return false;
		}
		for (MethodDescriptionNumber counterI : numbers) {
			if (counterI == null || ! counterI.valid(logger)) {
				return false;
			}
		}
		return true;
	}

}
