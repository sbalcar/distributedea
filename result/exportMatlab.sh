#!/bin/bash

mkdir subsetOfM;

for dirI in `ls -I subsetOfM -I export*`; do mkdir subsetOfM/$dirI; cp -r $dirI/matlab subsetOfM/$dirI/matlab/; done

