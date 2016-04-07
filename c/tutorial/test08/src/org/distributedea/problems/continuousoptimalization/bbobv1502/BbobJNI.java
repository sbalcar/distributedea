package org.distributedea.problems.continuousoptimalization.bbobv1502;

import java.util.Random;

public class BbobJNI {
   static {
         System.loadLibrary("bbob"); // Load native library at runtime
                                     // library.dll (Windows) or liblibrary.so (Unixes)
   }

   // Declare a native methods
   private native void initialize(String functionName, int dimension);
   private native double evaluate(double[] vector);
   private native void finalizee();

   // Test Driver
   public static void main(String[] args) {

	String functionName = "f01";
	int dimension = 2;

        double[] x = new double[dimension];

	Random rand = new Random();
        for (int i = 0; i < x.length; i++) {
            x[i] = rand.nextDouble();
        }

	BbobJNI bbobJNI = new BbobJNI();
	bbobJNI.initialize(functionName, dimension);
	bbobJNI.evaluate(x);
	bbobJNI.finalizee();
   }

}
