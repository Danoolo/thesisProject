package com.tesis.tesis;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParaboloidProblem extends Problem {

    public ParaboloidProblem() {
        dimension = 2;

        domain = new ArrayList<>();
        domain.add(new double[]{-3 , 3});
        domain.add(new double[]{-3 , 3});
    }

    public double calculateFitness(double[] position) {
        return Arrays.stream(position).map(xi -> Math.pow(xi, 2)).sum() + 4;
    }

    /**
     *
     * @return
     *  1: if fitness1 is better than fitness2
     *  0: if fitness1 is equal to fitness2
     * -1: if fitness1 is worst than fitness2
     */
    public int compareFitnessValues(double fitness1, double fitness2) {
        return Double.compare(fitness2, fitness1);
    }

    public double calculateBestFitness(List<double[]> positions) {
        return positions.stream().mapToDouble(this::calculateFitness).min().orElseThrow();
    }

    public List<Particle> generateRandomSwarm(int numParticles) {
        SecureRandom rand = new SecureRandom();
        List<Particle> swarm = new ArrayList<>();
        for (int i = 0; i < numParticles; i++) {
            List<Double> randomPosition = domain.stream()
                    .map(domain -> domain[0] + (domain[1] - domain[0]) * rand.nextDouble())
                    .toList();
            swarm.add(new Particle(randomPosition));
        }
        return swarm;
    }
}
