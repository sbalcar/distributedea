
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
        double[] x = {1.0,2.0};

	BbobJNI bbobJNI = new BbobJNI();
	bbobJNI.initialize(functionName, dimension);
	bbobJNI.evaluate(x);
	bbobJNI.finalizee();
   }

}
