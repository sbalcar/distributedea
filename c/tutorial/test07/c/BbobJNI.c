#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "BbobJNI.h"
#include "bbob.v15.02/bbobStructures.h"
#include "bbob.v15.02/benchmarks.h"

// Implementation of native method sayHello() of HelloJNI class
JNIEXPORT void JNICALL Java_BbobJNI_initialize(JNIEnv *env, jobject thisObj, jstring functionName, jint dimension) {

    const char *nativeString = (*env)->GetStringUTFChars(env, functionName, 0);
    printf("Function %s \n", nativeString);

    ParamStruct params = fgeneric_getDefaultPARAMS();
    strcpy(params.dataPath, "tmp");
    params.funcId = 8;
    params.instanceId = 1;
    params.DIM = dimension;

    double ftarget = fgeneric_initialize(params);
}

JNIEXPORT jdouble JNICALL Java_BbobJNI_evaluate(JNIEnv *env, jobject thisObj, jdoubleArray vector) {

    long vectorSize = (*env)->GetArrayLength(env, vector);

    double* xPtr = (*env)->GetDoubleArrayElements(env, vector, NULL);
    //printf("%f \n", *xPtr);

    int dimension = 2;
    double * x = (double *)malloc(sizeof(double) * dimension);

    /* Generate individual */
    int j;
    for (j = 0; j < dimension; j++)
        x[j] = 10. * ((double)rand() / RAND_MAX) - 5.;

    double fitness = fgeneric_evaluate(x);
    printf("Fitness = %lf\n", fitness);

    return fitness;
}

JNIEXPORT void JNICALL Java_BbobJNI_finalizee(JNIEnv *env, jobject thisObj) {

        fgeneric_finalize();
}
