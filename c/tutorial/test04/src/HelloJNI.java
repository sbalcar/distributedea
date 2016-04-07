import test04.Result;

public class HelloJNI {
   static {
         System.loadLibrary("hello"); // Load native library at runtime
	                                    // hello.dll (Windows) or libhello.so (Unixes)
   }

   // Declare a native method sayHello() that receives nothing and returns v
   private native Result sayHello(int num1, int num2);

   // Test Driver
   public static void main(String[] args) {

	HelloJNI helloJNI = new HelloJNI();
	Result result = helloJNI.sayHello(2, 3);

	System.out.println("Result:");
	System.out.println("  x: " + result.x);
	System.out.println("  y: " + result.y);
   }
}
