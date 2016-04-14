package org.distributedea.problems.continuousoptimization.bbobv1502;

import static java.util.Collections.singletonList;
import static javax.tools.JavaFileObject.Kind.SOURCE;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Scanner;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.distributedea.logging.AgentLogger;
import org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric.Params;

import sun.misc.Unsafe;

@SuppressWarnings({ "restriction", "unused" })

/**
 * Bbob Tool which solve problem, that java is not able to link
 * multiple(for each instance one unique) one C library
 * @author stepan
 *
 */
public class BbobTools {
	
	private Class<?> JAVA_CLASS = JNIfgeneric.class;
	private String JAVA_DIR = "src";
	private String C_DIR = "c";
	
	private String LIB_EXTNAME = "libbbob";
	private String LIB_INTNAME = "bbob";
	private String LIB_DIR = "lib";
	
	private Class<?> JAVA_INTCLASS = JNIfgeneric.Params.class;
	
	private String javaFileName;
	private String javaFileNameWithPath;
	
	private String cFileName;
	private String cFileNameWithPath;
	
	// generates ID of Instance
	private static int counter = 0;
	// represents ID of Instance
	private int numberI;

	private AgentLogger logger;
	
	
	public BbobTools(AgentLogger logger) {
		
		this.logger = logger;
		
		this.javaFileName = JAVA_CLASS.getSimpleName();
		this.javaFileNameWithPath =  JAVA_DIR + File.separator + JAVA_CLASS.getName()
				.replace(".", File.separator) + ".java";
		
		this.cFileName = JAVA_CLASS.getSimpleName();
		this.cFileNameWithPath = C_DIR + File.separator + cFileName + ".c";
		
		assignUniqueNumberToInstance();
	}

	private synchronized void assignUniqueNumberToInstance() {
		
		this.numberI = counter++;
	}
	
	public int getNumber() {
		return this.numberI;
	}
	
	protected void createJavaClassFile() throws IOException {
		
		final String fgenericJavaCodeI = readFile(javaFileNameWithPath);
		
		StringBuffer stringBuffer = new StringBuffer();
		boolean isAppending = true;
		
		Scanner scanner = new Scanner(fgenericJavaCodeI);
		while (scanner.hasNextLine()) {
			
			String line = scanner.nextLine();
			
			// remove internal class Parameters
			if (line.contains("static public class Params {")) {
				isAppending = false;
			} else if (! isAppending && line.contains("}")) {
				isAppending = true;
				continue;
			}
			
			if (isAppending) {
				stringBuffer.append(line).append("\n");
			}
			
		}
		scanner.close();
		
		String fgenericCode = stringBuffer.toString();
		
		// correct class name definition
		String classDefinition = "public class " + javaFileName;
		// correct constructor of the class
		String constructorDefinition = "public " + javaFileName;
		// correct name of the library
		String libraryDefinition = "System.loadLibrary(\"" + LIB_INTNAME;
		
		String paramArgDefinition = JAVA_INTCLASS.getSimpleName() + " ";
		String paramArgDefinitionNew = JAVA_CLASS.getSimpleName() + "." + JAVA_INTCLASS.getSimpleName() + " ";
		
		String codeReplaced = fgenericCode.replace(classDefinition, classDefinition + numberI);
		codeReplaced = codeReplaced.replace(constructorDefinition, constructorDefinition + numberI);
		codeReplaced = codeReplaced.replace(libraryDefinition, libraryDefinition + numberI);
		codeReplaced = codeReplaced.replace(paramArgDefinition, paramArgDefinitionNew);
		
		
		
		
		// write new code to JNIfgenericI.java
		String fgenericFileI = JAVA_DIR + File.separator + JAVA_CLASS.getName()
				.replace(".", File.separator) + numberI + ".java";
		writeToFile(fgenericFileI, codeReplaced);
	}
	
	@SuppressWarnings("rawtypes")
	protected Class<?> getJNIClass() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		String fgenericFileI =  JAVA_DIR + File.separator + JAVA_CLASS.getName()
				.replace(".", File.separator) + numberI + ".java";		
		final String fgenericCodeI = readFile(fgenericFileI);
		
		
		
		// create class JNIfgenericI from runtime
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		final SimpleJavaFileObject simpleJavaFileObject 
	      = new SimpleJavaFileObject(java.net.URI.create(fgenericFileI), SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return fgenericCodeI;
            }

            @Override
            public OutputStream openOutputStream() throws IOException {
                return byteArrayOutputStream;
            }
        };

        @SuppressWarnings({ "unchecked" })
		final JavaFileManager javaFileManager = new ForwardingJavaFileManager(
                ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {

            @Override
            public JavaFileObject getJavaFileForOutput(Location location,
                                                       String className,
                                                       JavaFileObject.Kind kind,
                                                       FileObject sibling) throws IOException {
                return simpleJavaFileObject;
            }
        };
	    
        ToolProvider.getSystemJavaCompiler().getTask(
        		null, javaFileManager, null, null, null, singletonList(simpleJavaFileObject)).call();

        final byte[] bytes = byteArrayOutputStream.toByteArray();

        // use the unsafe class to load in the class bytes
		final Field field = Unsafe.class.getDeclaredField("theUnsafe");
	    field.setAccessible(true);
		final Unsafe unsafe = (Unsafe) field.get(null);

		@SuppressWarnings("deprecation")
		final Class aClass = unsafe.defineClass(JAVA_CLASS.getName() + numberI, bytes, 0, bytes.length);
	    
	    
	    return aClass;
	}

	protected void createCMakeFile() throws IOException {
		
		String cMakefileCode = readFile("Makefile");
		
		String classJNIName = JAVA_CLASS.getSimpleName();
		// correct name of the code files
		String cMakefileCodeNew = cMakefileCode.replace(classJNIName, classJNIName + numberI);
		// correct name of the library
		cMakefileCodeNew = cMakefileCodeNew.replace(LIB_EXTNAME, LIB_EXTNAME + numberI);
		
		// original JNIfgeneric.o have to be compiled with new Class (internal Class Parameters)
		String o_old = "-o " + LIB_DIR + File.separator + LIB_EXTNAME + numberI + ".so";
		String o_new = "-o " + LIB_DIR + File.separator + LIB_EXTNAME + numberI + ".so "
				+ C_DIR + File.separator + cFileName + ".o";
		cMakefileCodeNew = cMakefileCodeNew.replace(o_old, o_new);
		
		// write new code to MakefileI
		String MakefileNew = "Makefile" + numberI;		
		writeToFile(MakefileNew, cMakefileCodeNew);
	}

	protected void createCJNIFile() throws IOException {
		
		String cJNICode = readFile(cFileNameWithPath);
		
		String prefix = JAVA_CLASS.getName().replace(".", "_");
		// correct function names
		String cJNICodeNew = cJNICode.replace(prefix, prefix + numberI);
		// correct include file names
		cJNICodeNew = cJNICodeNew.replace(cFileName + ".h", cFileName + numberI + ".h");
		
		String cFileNameWithPathNew = C_DIR + File.separator + cFileName + numberI + ".c";
		writeToFile(cFileNameWithPathNew, cJNICodeNew);
	}
	
	protected void compileC() throws IOException, InterruptedException{
		
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("make -f Makefile" + numberI );
		pr.waitFor();
		
	}
	
	/**
	 * Reads all file to String
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		  
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		while((line = bufferedReader.readLine()) != null) {
		 	
			stringBuffer.append(line).append("\n");
		}
		bufferedReader.close();
		return stringBuffer.toString();
	}
	
	/**
	 * Writes String to file, if exists then rewrites file 
	 * @param fileName
	 * @param text
	 * @throws IOException
	 */
	private void writeToFile(String fileName, String text) throws IOException {
		
		PrintWriter writer = new PrintWriter(fileName);
		writer.println(text);
		writer.close();
	}

	/**
	 * Cleans all Bbob object files and library
	 * @throws BbobException
	 */
	public void clean() throws BbobException {
		
		Runtime rt = Runtime.getRuntime();
		Process pr;
		try {
			pr = rt.exec("make -f Makefile" + numberI  + " remove");
			pr.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new BbobException("Trash after building Bbob wasn't cleaned");
		}
	}
	
	/**
	 * Alternative Constructor for the JNIfgeneric, each instance contains unique library
	 * @return
	 * @throws BbobException
	 */
	public IJNIfgeneric getInstanceJNIfgeneric() throws BbobException {
		
		Class<?> aClass = null;
		
		try {
			createJavaClassFile();
		} catch (IOException e) {
			logger.logThrowable("JNI class for Bbob wasn't created from runtime", e);
			throw new BbobException("JNI class for Bbob wasn't created from runtime");
		}
		
		try {
			aClass = getJNIClass();
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| IOException e) {
			logger.logThrowable("JNI class for Bbob wasn't loaded from runtime", e);
			throw new BbobException("JNI class for Bbob wasn't loaded from runtime");
		}
		
		try {
			createCMakeFile();
		} catch (IOException e) {
			logger.logThrowable("Makefile for Bbob wasn't created", e);
			throw new BbobException("Makefile for Bbob wasn't created");
		}
		
		try {
			createCJNIFile();
		} catch (IOException e) {
			logger.logThrowable("JNI C-part for Bbob wasn't created", e);
			throw new BbobException("JNI C-part for Bbob wasn't created");
		}
		
		try {
			compileC();
		} catch (IOException | InterruptedException e) {
			logger.logThrowable("Bbob wasn't compiled", e);
			throw new BbobException("Bbob wasn't compiled");
		}
		
	    // instance JNIfgenericI
	    Object object = null;
		try {
			object = aClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("Bbob can't be instanced", e);
			throw new BbobException("Bbob can't be instanced");
		}
	    IJNIfgeneric fgeneric = (IJNIfgeneric) object;
	    
	    return fgeneric;
	}
	
	
	
}
