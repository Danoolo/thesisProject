package com.tesis.tesis;

public class Problem {

    public static double calculateFitness(double[] position) {
        double sum = 0;
        for (double x : position) {
            sum += Math.pow(x, 2); // Sphere function
        }
        return sum;
    }

    /**
     *
     * @return
     *  1: if fitnessToCompare is better than fitnessReference
     *  0: if fitnessToCompare is equal to fitnessReference
     * -1: if fitnessToCompare is worst than fitnessReference
     */
    public static int compareFitnessValues(double fitnessToCompare, double fitnessReference) {
        return Double.compare(fitnessToCompare, fitnessReference);
    }
}
