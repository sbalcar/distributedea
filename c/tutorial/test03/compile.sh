#!/bin/bash

# odkaz pro inspiraci
# https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html

# kompilace javy
javac test03/Result.java
javac HelloJNI.java

# generovani HelloJNI.h
javah HelloJNI

# kompilace Cecka
gcc -fPIC -c HelloJNI.c

# kompilace dinamicke knihovny
gcc -shared -o libhello.so HelloJNI.o

# spisteni javy
java -Djava.library.path=. HelloJNI
