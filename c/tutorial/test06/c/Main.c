#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "bbob.v15.02/bbobStructures.h"
#include "bbob.v15.02/benchmarks.h"

int main( int argc, const char* argv[] )
{
	printf( "Test bbob.v15.02\n" );

	int dimension = 2;
	double * x = (double *)malloc(sizeof(double) * dimension);

	/* Generate individual */
	int j;
	for (j = 0; j < dimension; j++)
		x[j] = 10. * ((double)rand() / RAND_MAX) - 5.;


	ParamStruct params = fgeneric_getDefaultPARAMS();
	strcpy(params.dataPath, "tmp");
	params.funcId = 8;
	params.instanceId = 1;
	params.DIM = dimension;

	double ftarget = fgeneric_initialize(params);

	double fitness = fgeneric_evaluate(x);

	printf("Fitness = %lf\n", fitness);

//	MY_OPTIMIZER(&fgeneric_evaluate, dimension, ftarget, 1e5); /*adjust maxfunevals*/

	fgeneric_finalize();
	free(x);

	return 0;
}
