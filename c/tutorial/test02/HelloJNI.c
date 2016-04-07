#include <jni.h>
#include <stdio.h>
#include "HelloJNI.h"
 
// Implementation of native method sayHello() of HelloJNI class
JNIEXPORT int JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj, jint number) {
    int i;
    for (i = 0; i < number; i++)
       printf("Hello World!\n");
    return number;
}
