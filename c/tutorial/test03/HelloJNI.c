#include <jni.h>
#include <stdio.h>
#include "HelloJNI.h"
 
// Implementation of native method sayHello() of HelloJNI class
JNIEXPORT jobject JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj, jint num1, jint num2) {
    int i;
    for (i = 0; i < 0; i++) {
       printf("Hello World!\n");
    }

    jclass c = (*env)->FindClass(env, "test03/Result");
    if (c == 0) {
        printf("Find Class Failed.\n");
    } else {
            printf("Found class.\n");
    }
    
    jmethodID cnstrctr;
    cnstrctr = (*env)->GetMethodID(env, c, "<init>", "(II)V");
    if (cnstrctr == 0) {
        printf("Find method Failed.\n");
    } else {
	printf("Found method.\n");
    }
    return (*env)->NewObject(env, c, cnstrctr, num1, num2);
}
