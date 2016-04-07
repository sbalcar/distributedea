
# kompilace do spustitelne Ceckove formy
build: c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o c/bbob.v15.02/MY_OPTIMIZER.o c/Main.o
	gcc c/bbob.v15.02/benchmarksdeclare.o c/bbob.v15.02/benchmarkshelper.o c/bbob.v15.02/benchmarksnoisy.o c/bbob.v15.02/benchmarks.o c/bbob.v15.02/dirOK-linux.o c/bbob.v15.02/fgeneric.o c/bbob.v15.02/MY_OPTIMIZER.o c/Main.o -lm -o main


c/bbob.v15.02/benchmarksdeclare.o: c/bbob.v15.02/benchmarksdeclare.c
	gcc -c c/bbob.v15.02/benchmarksdeclare.c -o c/bbob.v15.02/benchmarksdeclare.o

c/bbob.v15.02/benchmarkshelper.o: c/bbob.v15.02/benchmarkshelper.c
	gcc -c c/bbob.v15.02/benchmarkshelper.c -o c/bbob.v15.02/benchmarkshelper.o

c/bbob.v15.02/benchmarksnoisy.o: c/bbob.v15.02/benchmarksnoisy.c
	gcc -c c/bbob.v15.02/benchmarksnoisy.c -o c/bbob.v15.02/benchmarksnoisy.o

c/bbob.v15.02/benchmarks.o: c/bbob.v15.02/benchmarks.c
	gcc -c c/bbob.v15.02/benchmarks.c -o c/bbob.v15.02/benchmarks.o

c/bbob.v15.02/dirOK-linux.o: c/bbob.v15.02/dirOK-linux.c
	gcc -c c/bbob.v15.02/dirOK-linux.c -o c/bbob.v15.02/dirOK-linux.o

c/bbob.v15.02/fgeneric.o: c/bbob.v15.02/fgeneric.c
	gcc -c c/bbob.v15.02/fgeneric.c -o c/bbob.v15.02/fgeneric.o

c/bbob.v15.02/MY_OPTIMIZER.o: c/bbob.v15.02/MY_OPTIMIZER.c
	gcc -c c/bbob.v15.02/MY_OPTIMIZER.c -o c/bbob.v15.02/MY_OPTIMIZER.o

c/Main.o: c/Main.c
	gcc -c c/Main.c -o c/Main.o

run:
	./main

clean:
	rm -f main \
	c/*.o \
	c/bbob.v15.02/*.o
