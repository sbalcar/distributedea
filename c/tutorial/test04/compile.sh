#!/bin/bash

# odkaz pro inspiraci
# https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html

# kompilace javy
javac src/test04/Result.java
javac -cp src/ src/HelloJNI.java

# generovani HelloJNI.h
javah -cp src -d c HelloJNI

# kompilace Cecka
gcc -fPIC -c c/HelloJNI.c -o c/HelloJNI.o

# kompilace dinamicke knihovny
gcc -shared -o lib/libhello.so c/HelloJNI.o

# spusteni javy
java -Djava.library.path=lib -cp src HelloJNI
