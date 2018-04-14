package org.distributedea.input;

import java.io.File;

import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.AllDimensionsRestriction;
import org.distributedea.ontology.dataset.continuousoptimization.DomainDefinition;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;

public class DatasetExporter {

	public static void main(String [] args) throws Exception {
		
		DomainDefinition domain = new AllDimensionsRestriction(
				new Interval(-5, 5));
		
		File file = new File("inputs" + File.separator + "weka.co");
		
		DatasetContinuousOpt datasetCOWeka = new DatasetContinuousOpt(domain, file);
		datasetCOWeka.getDomain().exportXML(file);
		
		DatasetContinuousOpt.importXML(file);
	}
}
