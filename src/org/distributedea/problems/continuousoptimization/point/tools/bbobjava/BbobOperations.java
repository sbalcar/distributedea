package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import java.util.Random;

public class BbobOperations {

	public static double[][] getRandomRotationMatrix(int d) {
        
    	double[][] matrix = new double[d][d];
        
        Random rnd = new Random(333);
        
        //generate matrix
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++)
                matrix[i][j] = rnd.nextGaussian();
        }

        double prod;

        // Gramm-Schmidt orthonormalization
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < i; j++) {
                prod = 0;
                for (int k = 0; k < d; k++) {
                    prod += matrix[k][i] * matrix[k][j];
                }
                for (int k = 0; k < d; k++) {
                    matrix[k][i] -= prod * matrix[k][j];
                }
            }
            prod = 0;
            for (int k = 0; k < d; k++) {
                prod += matrix[k][i] * matrix[k][i];
            }
            for (int k = 0; k < d; k++) {
                matrix[k][i] /= Math.sqrt(prod);
            }
        }

        return matrix;
    }
	
	public static double[][] getDiagonalMatrix(int alfa, int d) {
		
		double[][] matrix = new double[d][d];
		
		for (int i = 0; i < d; i++) {
			
			matrix[i][i] = Math.pow(alfa, 0.5 * i / (d -1));
		}
		
		return matrix;
	}
	
    public static double[] multipl(double[][] matrix, double[] vector) {
    	
    	int d = vector.length;
    	
        double[] newVector = new double[d];

        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                newVector[i] += matrix[i][j] * vector[j];
            }
        }

        return newVector;
    }

    public static double[][] multipl(double[][] matrixA, double[][] matrixB) {
    
        int aRows = matrixA.length;
        int aColumns = matrixA[0].length;
        int bRows = matrixB.length;
        int bColumns = matrixB[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException();
        }

        double[][] matrixC = new double[aRows][bColumns];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
            	matrixC[i][j] = 0;
                for (int k = 0; k < aColumns; k++) {
                	matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    	
    	return matrixC;
    }
    
	public static double[] countsTasy(double beta, double[] vector) {
		
		int d = vector.length;
		
		double[] result = new double[d];
		
		for (int i = 0; i < d; i++) {
			
			double xi = vector[i];
			if (xi > 0) {
				result[i] = Math.pow(xi, 1 + beta * i/(d-1) * Math.sqrt(xi));
			} else {
				result[i] = xi;
			}
		}
		
		return result;
	}
			
	public static double countsi(int i, double xi, int d) {
		
		double result = 1;
		if (i % 2 == 0 && xi > 0) {
			result *= 10;
		}
		
		return result * Math.pow(Math.sqrt(10), i/(d-1));
	}

	public static double[] countTosz(double[] x) {
		int d = x.length;
		
		double[] result = new double[d];
		
		for (int i = 0; i < d; i++) {
			result[i] = countTosz(i, x[i], d);
		}
		
		return result;
	}
	public static double countTosz(int i, double xi, int d) {
		
		double xistr = 0;
		if (xi != 0) {
			xistr = Math.log(Math.abs(xi));
		}
		
		double c1;
		if (xi > 0) {
			c1 = 10.0;
		} else {
			c1 = 5.5;
		}

		double c2;
		if (xi > 0) {
			c2 = 7.9;
		} else {
			c2 = 3.1;
		}
		
		return Math.signum(xi) * Math.exp(xistr +0.049*(Math.sin(c1*xistr) + Math.sin(c2*xistr)));
	}

	
	public static double fpen(double[] x) {
		
		double sum = 0;
		for (int i = 0; i < x.length; i++) {
			
			double max = Math.max(0, Math.abs(x[i]) -5);
			
			sum += Math.pow(max, 2);
		}
		
		return sum;
	}

}
