#include <jni.h>
#include <stdio.h>
#include "HelloJNI.h"
 
// Implementation of native method sayHello() of HelloJNI class
JNIEXPORT jobject JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj, jstring functionName, jint dimension, jdoubleArray vector) {

    const char *nativeString = (*env)->GetStringUTFChars(env, functionName, 0);
    printf("Function %s \n", nativeString);

    long vectorSize = (*env)->GetArrayLength(env, vector);

    double* xPtr = (*env)->GetDoubleArrayElements(env, vector, NULL);
    //printf("%f \n", *xPtr);

    jclass c = (*env)->FindClass(env, "test05/Result");
    if (c == 0) {
        printf("Find Class Failed.\n");
    } else {
    }

    jmethodID cnstrctr = (*env)->GetMethodID(env, c, "<init>", "(DD)V");
    if (cnstrctr == 0) {
        printf("Find method Failed.\n");
    } else {
    }

    return (*env)->NewObject(env, c, cnstrctr, *xPtr, *(xPtr+1));
}
