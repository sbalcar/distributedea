
# kompilace dynamicke Ceckove knihovny
lib/libbbob.so: c/JNIfgeneric.o src/org/distributedea/problems/continuousoptimization/bbobv1502/JNIfgeneric.class  c/JNIfgeneric.o c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o
	gcc -fPIC -shared -o lib/libbbob.so c/JNIfgeneric.o c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o


# kompilace Cecka
c/JNIfgeneric.o: c/JNIfgeneric.c c/JNIfgeneric.h
	gcc -fPIC -c c/JNIfgeneric.c -o c/JNIfgeneric.o


# generovani Ceckoveho headru
c/JNIfgeneric.h:
	javah -cp src/ -o c/JNIfgeneric.h org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric

# kompilace javy
src/org/distributedea/problems/continuousoptimization/bbobv1502/JNIfgeneric.class:
	javac -cp src/ src/org/distributedea/problems/continuousoptimization/bbobv1502/JNIfgeneric.java


# kompilace benchmarku od Bbob
c/bbob.v15.02/benchmarksdeclare.o: c/bbob.v15.02/benchmarksdeclare.c
	gcc -fPIC -c c/bbob.v15.02/benchmarksdeclare.c -o c/bbob.v15.02/benchmarksdeclare.o

c/bbob.v15.02/benchmarkshelper.o: c/bbob.v15.02/benchmarkshelper.c
	gcc -fPIC -c c/bbob.v15.02/benchmarkshelper.c -o c/bbob.v15.02/benchmarkshelper.o

c/bbob.v15.02/benchmarksnoisy.o: c/bbob.v15.02/benchmarksnoisy.c
	gcc -fPIC -c c/bbob.v15.02/benchmarksnoisy.c -o c/bbob.v15.02/benchmarksnoisy.o

c/bbob.v15.02/benchmarks.o: c/bbob.v15.02/benchmarks.c
	gcc -fPIC -c c/bbob.v15.02/benchmarks.c -o c/bbob.v15.02/benchmarks.o

c/bbob.v15.02/dirOK-linux.o: c/bbob.v15.02/dirOK-linux.c
	gcc -fPIC -c c/bbob.v15.02/dirOK-linux.c -o c/bbob.v15.02/dirOK-linux.o

c/bbob.v15.02/fgeneric.o: c/bbob.v15.02/fgeneric.c
	gcc -fPIC -c c/bbob.v15.02/fgeneric.c -o c/bbob.v15.02/fgeneric.o

c/bbob.v15.02/MY_OPTIMIZER.o: c/bbob.v15.02/MY_OPTIMIZER.c
	gcc -fPIC -c c/bbob.v15.02/MY_OPTIMIZER.c -o c/bbob.v15.02/MY_OPTIMIZER.o


run:
	java -Djava.library.path=lib -cp src org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric


# command for removing the  whole java JNI wrapper
remove:
	rm -f \
	c/JNIfgeneric.h \
	c/JNIfgeneric.c \
	c/JNIfgeneric.o \
	lib/libbbob.so \
	src/org/distributedea/problems/continuousoptimization/bbobv1502/JNIfgeneric.class \
	src/org/distributedea/problems/continuousoptimization/bbobv1502/JNIfgeneric.java
