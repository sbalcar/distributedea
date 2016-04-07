
.PHONY: clean

# kompilace dynamicke Ceckove knihovny
lib/libhello.so: c/HelloJNI.o src/HelloJNI.class
	gcc -shared -o lib/libhello.so c/HelloJNI.o

# kompilace Cecka
c/HelloJNI.o: c/HelloJNI.h
	gcc -fPIC -c c/HelloJNI.c -o c/HelloJNI.o

# generovani Ceckoveho headru
c/HelloJNI.h:
	javah -cp src -d c HelloJNI

# kompilace javy
src/HelloJNI.class: src/test05/Result.class
	javac -cp src/ src/HelloJNI.java

src/test05/Result.class:
	javac src/test05/Result.java


run:
	java -Djava.library.path=lib -cp src HelloJNI


clean:
	rm c/HelloJNI.h  c/HelloJNI.o \
	lib/libhello.so \
	src/HelloJNI.class src/test05/Result.class
