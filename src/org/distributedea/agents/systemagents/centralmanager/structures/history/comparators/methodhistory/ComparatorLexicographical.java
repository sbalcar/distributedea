package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodhistory;

import java.util.Comparator;

import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistory;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;

public class ComparatorLexicographical implements Comparator<MethodHistory> {

	@Override
	public int compare(MethodHistory o1, MethodHistory o2) {
		
		MethodInstanceDescription metInst1 = o1.getMethodInstanceDescription();
		MethodInstanceDescription metInst2 = o2.getMethodInstanceDescription();
		
		MethodType methodType1 = metInst1.getMethodType();
		MethodType methodType2 = metInst2.getMethodType();
		
		Class<?> agentClass1 = methodType1.exportAgentClass();
		Class<?> agentClass2 = methodType2.exportAgentClass();
		
		int classCmp = agentClass1.getSimpleName().compareTo(
				agentClass2.getSimpleName());
		if (classCmp != 0) {
			return classCmp;
		}
		
		
		ProblemToolDefinition problemToolClass1 = methodType1.getProblemToolDefinition();
		ProblemToolDefinition problemToolClass2 = methodType2.getProblemToolDefinition();
		
		int toolCmp = problemToolClass1.toString().compareTo(
				problemToolClass2.toString());
		if (toolCmp != 0) {
			return toolCmp;
		}
		
		
		int instNumber1 = metInst1.getInstanceNumber();
		int instNumber2 = metInst2.getInstanceNumber();
		
		return instNumber1 - instNumber2;
	}

}
