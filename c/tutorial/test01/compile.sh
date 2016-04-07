#!/bin/bash

# odkaz pro inspiraci
# https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html

# kompilace javy
javac HelloJNI.java

# kompilace Cecka
gcc -fPIC -c HelloJNI.c

# kompilace dinamicke knihovny
gcc -shared -o libhello.so HelloJNI.o

# spisteni javy
java -Djava.library.path=. HelloJNI
