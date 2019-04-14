#!/bin/sh

mkdir subsetOfR;

for dirBatchI in `ls -I description.txt -I subsetOfR -I subsetOfM -I export*`;
do
   dirNewBatchI=subsetOfR/$dirBatchI;
   mkdir $dirNewBatchI;

   cp -r $dirBatchI/input $dirNewBatchI/input/;
   cp -r $dirBatchI/matlab $dirNewBatchI/matlab/;
   mkdir $dirNewBatchI/jobs;

   for dirJobI in `ls $dirBatchI/jobs`;
   do
      dirNewJobI=$dirNewBatchI/jobs/$dirJobI;
      mkdir $dirNewJobI;

      for dirRunI in `ls $dirBatchI/jobs/$dirJobI`;
      do
         dirNewRunI=$dirNewJobI/$dirRunI/;
         mkdir $dirNewRunI;

         cp $dirBatchI/jobs/$dirJobI/$dirRunI/result\-$dirJobI\-$dirRunI.txt $dirNewRunI;
      done

   done

done

