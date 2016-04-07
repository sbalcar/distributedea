import test05.Result;

public class HelloJNI {
   static {
         System.loadLibrary("hello"); // Load native library at runtime
	                                    // hello.dll (Windows) or libhello.so (Unixes)
   }

   // Declare a native method sayHello() that receives nothing and returns v
   private native Result sayHello(String functionName, int dimension, double[] vector);

   // Test Driver
   public static void main(String[] args) {

	String functionName = "f01";
	int dimension = 2;
        double[] x = {1.0,2.0};
        
	HelloJNI helloJNI = new HelloJNI();
	Result result = helloJNI.sayHello(functionName, dimension, x);

	System.out.println("Result:");
	System.out.println("  x: " + result.x);
	System.out.println("  y: " + result.y);
   }
}
