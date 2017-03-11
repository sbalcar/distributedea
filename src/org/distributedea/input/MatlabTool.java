package org.distributedea.input;

import java.util.ArrayList;
import java.util.List;


/**
 * Tool  to convert java types to Matlab source code.
 * @author stepan
 *
 */
public class MatlabTool {
	
	public static String convertStringsToMatlamArray(List<String> values) {
		
		return "[" + convertToMatlab(values, ",") + "]";
	}

	public static String convertIntegersToMatlamArray(List<Integer> values) {
		
		List<String> valuesStr = new ArrayList<>();
		
		for (Integer valueI : values) {
			
			valuesStr.add("" + valueI);
		}
		
		return convertStringsToMatlamArray(valuesStr);
	}

	public static String convertDoublesToMatlamArray(List<Double> values) {
	
		List<String> valuesStr = new ArrayList<>();
		
		for (Double valueI : values) {
			
			valuesStr.add("" + valueI);
		}
		
		return convertStringsToMatlamArray(valuesStr);
	}

	public static String convertLongsToMatlamArray(List<Long> values) {
		
		List<String> valuesStr = new ArrayList<>();
		
		for (Long valueI : values) {
			
			valuesStr.add("" + valueI);
		}
		
		return convertStringsToMatlamArray(valuesStr);
	}
	
	public static String convertToMatlabMatrix(List<String> values) {

		return "[" + convertToMatlab(values, ";") + "]";
	}

	
	public static String convertToMatlabLegend(List<String> values) {
		
		return "{'" + convertToMatlab(values, "','") + "'}";
	}
	
	private static String convertToMatlab(List<String> values, String delimiter) {
		
		String matlab = "";
		for (int i = 0; i < values.size(); i++) {
			
			matlab += values.get(i); 
			
			if (i < values.size() -1) {
				matlab += delimiter;
			}
		}		
		return matlab;
	}
	
	
	public static String createLabels(List<String> labels) {
		
		String descriptions = "{";
		for (String labelI : labels) {
			
			descriptions += "\'" + labelI + "\'," ;
		}
		
		descriptions += "}";
		descriptions = descriptions.replaceAll(",}", "}");  // remove the last ','
		descriptions = descriptions.replaceAll("\\_", "\\\\_");
		
		return descriptions;
	}
	
}
