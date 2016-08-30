package org.distributedea.tests.individual;

import java.util.ArrayList;
import java.util.Arrays;

import org.distributedea.ontology.individuals.IndividualPermutation;

public class TestCompare {

	
	public static boolean testBothNull() {

		IndividualPermutation p1 =
				new IndividualPermutation(new ArrayList<Integer>());
		IndividualPermutation p2 =
				new IndividualPermutation(new ArrayList<Integer>());

		if (p1 == p2) {
			return false;
		}

		if (! p1.equals(p2)) {
			return false;
		}

		return true;
	}
	
	public static boolean testOneNull() {

		IndividualPermutation p1 = new IndividualPermutation(
				new ArrayList<Integer>());

		IndividualPermutation p2 = new IndividualPermutation(
        		new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
        
		if (p1 == p2) {
			return false;
		}

		if (p1.equals(p2) && p2.equals(p1)) {
			return false;
		}

		return true;
	}
	
	public static boolean testBouthValues() {

		IndividualPermutation p1 = new IndividualPermutation(
        		new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		IndividualPermutation p2 = new IndividualPermutation(
        		new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
        
		if (p1 == p2) {
			return false;
		}

		if ( p1.equals(p2) && p2.equals(p1) ) {
		} else  {
			return false;
		}

		return true;
	}
	
	public static void main(String [ ] args) {
		
		boolean tes1 = testBothNull();
		boolean tes2 = testOneNull();
		boolean tes3 = testBouthValues();
		
		if (tes1 && tes2 && tes3) {
			System.out.println("Test OK");
		} else  {
			System.out.println("Some Test is wrong");
		} 
	}
}
