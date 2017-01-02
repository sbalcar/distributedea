package org.distributedea.problems.continuousoptimization.point.tools.bbob;

import org.distributedea.problems.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;

public class ToolExitBbobCO {

	public static void exit(IJNIfgeneric fgeneric, BbobTools bbobTools) throws Exception {
		
		double ret = fgeneric.exitBBOB();
        if (ret == 0.) {
            System.out.println("exitBBOB finished OK.");
        } else {
            System.out.println("exitBBOB returned error: " + ret);
        }
        
        try {
			bbobTools.clean();
		} catch (BbobException e) {
			throw new Exception("Problem Tool can't exit Bbob");
		}
	}
}
