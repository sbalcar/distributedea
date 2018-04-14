package org.distributedea.javaextension;

import java.util.List;

public class ListUtils {

	public static double[] toArrayDblToDbl(List<Double> list) {
		
		double[] array = new double[list.size()];

		for (int indexI = 0; indexI < list.size(); indexI++) {
			double valueI = list.get(indexI);
			array[indexI] = valueI;
		}
		return array;
	}
	
	public static int[] toArrayIntToInt(List<Integer> list) {
		
		int[] array = new int[list.size()];

		for (int indexI = 0; indexI < list.size(); indexI++) {
			int valueI = list.get(indexI);
			array[indexI] = valueI;
		}
		return array;
	}
	
	public static double[] toArrayIntToDbl(List<Integer> list) {
		
		int[] array = toArrayIntToInt(list);
		return convertIntsToDoubles(array);
	}
	
	public static double[] convertIntsToDoubles(int[] array) {
		
		double[] arrayOfDoubles = new double[array.length];
		for (int indexI = 0; indexI < array.length; indexI++) {
			double valueI = array[indexI];
			arrayOfDoubles[indexI] = valueI;
		}
		return arrayOfDoubles;
	}
	
	public static double sumDoubles(List<Double> list) {
		double sum = 0;
		for (double valueI : list) {
			sum += valueI;
		}
		return sum;
	}
	
}
