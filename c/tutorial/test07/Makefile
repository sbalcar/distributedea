
.PHONY: clean

# kompilace dynamicke Ceckove knihovny
lib/libbbob.so: c/BbobJNI.o src/BbobJNI.class c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o
	gcc -fPIC -shared -o lib/libbbob.so c/BbobJNI.o c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o


# kompilace Cecka
c/BbobJNI.o: c/BbobJNI.h
	gcc -fPIC -c c/BbobJNI.c -o c/BbobJNI.o

# generovani Ceckoveho headru
c/BbobJNI.h:
	javah -cp src -d c BbobJNI

# kompilace javy
src/BbobJNI.class:
	javac -cp src/ src/BbobJNI.java


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
	java -Djava.library.path=lib -cp src BbobJNI



clean:
	rm -f main \
	c/*.o \
	c/BbobJNI.h \
	c/bbob.v15.02/*.o \
	lib/libhello.so \
	src/BbobJNI.class
