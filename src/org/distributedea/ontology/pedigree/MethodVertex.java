package org.distributedea.ontology.pedigree;

import jade.content.Concept;

import java.beans.MethodDescriptor;
import java.util.List;

public class MethodVertex implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescriptor method;
	
	private List<MethodVertex> ancestors;
	
}
