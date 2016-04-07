
.PHONY: clean


build: c/Main.o c/Function.o
	gcc c/Main.o c/Function.o -o hello

c/Main.o: c/Main.cpp
	gcc -c c/Main.cpp -o c/Main.o

c/Function.o: c/Function.cpp
	gcc -c c/Function.cpp -o c/Function.o

run: build
	./hello

clean:
	rm hello \
	c/Function.o \
	c/Main.o
