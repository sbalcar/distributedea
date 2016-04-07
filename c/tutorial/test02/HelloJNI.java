public class HelloJNI {
   static {
         System.loadLibrary("hello"); // Load native library at runtime
	                                    // hello.dll (Windows) or libhello.so (Unixes)
   }
     
   // Declare a native method sayHello() that receives nothing and returns v
   private native int sayHello(int number);

   // Test Driver
   public static void main(String[] args) {
	System.out.println(new HelloJNI().sayHello(3));  // invoke the native method
   }
}
