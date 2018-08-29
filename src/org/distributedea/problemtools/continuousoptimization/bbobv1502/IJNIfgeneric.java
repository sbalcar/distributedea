package org.distributedea.problemtools.continuousoptimization.bbobv1502;

import org.distributedea.problemtools.continuousoptimization.bbobv1502.JNIfgeneric.Params;

public abstract class IJNIfgeneric {

    public native double initBBOB(int funcId, int instanceId, int dim,
            String datapath, Params optParams);

    public native double exitBBOB();

    public native boolean exist(int funcId);

    public native double getFtarget();
    
    public native double getBest();

    public native double getFbest();

    public native double getEvaluations();

    public native void setNoiseSeed(int seed);

    public native void unif(double[] r, int N, int inseed);

    public native double evaluate(double[] X);
    
}
