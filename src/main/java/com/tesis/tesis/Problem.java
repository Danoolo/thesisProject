package com.tesis.tesis;

import java.util.ArrayList;
import java.util.List;

public abstract class Problem {
    protected int dimension;
    protected List<double[]> domain;

    abstract double calculateFitness(double[] position);
    abstract int compareFitnessValues(double fitness1, double fitness2);
    abstract double calculateBestFitness(List<double[]> positions);
    abstract List<Particle> generateRandomSwarm(int numParticles);
}
