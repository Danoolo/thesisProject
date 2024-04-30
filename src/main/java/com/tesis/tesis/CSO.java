package com.tesis.tesis;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CSO {
    private final int numParticles;
    private final int numDimensions;
    private final int numIterations;
    private final double phiAveragePosition;
    private final Problem problem;

    public CSO(int numParticles, int numDimensions, int numIterations, double phiAveragePosition, Problem problem) {
        this.numParticles = numParticles % 2 == 0 ? numParticles : numParticles + 1;
        this.numDimensions = numDimensions;
        this.numIterations = numIterations;
        this.phiAveragePosition = phiAveragePosition;
        this.problem = problem;
    }

    public double optimize() {
        SecureRandom rand = new SecureRandom();

        // Initialize particles randomly
        List<Particle> swarm = problem.generateRandomSwarm(numParticles);

        // Main optimization loop
        for (int iter = 0; iter < numIterations; iter++) {
            // Empty swarm for the next generation
            List<Particle> nextSwarm = new ArrayList<>();

            // Calculate the fitness of all particles in the swarm
            swarm.forEach(particle -> particle.setFitness(problem.calculateFitness(particle.getPosition())));

            // Competitive phase
            Particle particle1;
            Particle particle2;
            Particle particleWinner;
            Particle particleLoser;
            List<Particle> originalSwarm = swarm.stream().map(Particle::new).toList();
            while (!swarm.isEmpty()) {
                particle1 = swarm.remove(rand.nextInt(swarm.size()));
                particle2 = swarm.remove(rand.nextInt(swarm.size()));

                if (problem.compareFitnessValues(particle1.getFitness(), particle2.getFitness()) < 0) {
                    particleLoser = particle1;
                    particleWinner = particle2;
                } else {
                    particleLoser = particle2;
                    particleWinner = particle1;
                }

                Particle pLoserCopy = new Particle(particleLoser);
                pLoserCopy.setVelocity(getNewVelocity(particleWinner, particleLoser, rand, originalSwarm));
                pLoserCopy.setPosition(arrayOperation(particleLoser.getPosition(), pLoserCopy.getVelocity(), "+"));

                nextSwarm.add(new Particle(particleWinner));
                nextSwarm.add(pLoserCopy);
            }
            swarm = nextSwarm;
        }

        return problem.calculateBestFitness(swarm.stream().map(Particle::getPosition).toList());
    }

    private double[] getAveragePosition(List<Particle> swarm) {
        double[] averageArray = new double[numDimensions];

        for (int i = 0; i < numDimensions; i++) {
            int finalI = i;
            double average = swarm.stream()
                    .mapToDouble(particle -> particle.getPosition()[finalI]) // Get value at current index from each array
                    .average()                      // Calculate average for the current index
                    .orElseThrow();                 // Throw exception if average is not present

            averageArray[i] = average; // Store the average value in the result array
        }

        return averageArray;
    }

    private double[] getNewVelocity(Particle particleWinner, Particle particleLoser, SecureRandom random,
                                    List<Particle> swarm) {
        double[] array1 = arrayOperation(
                random.doubles().limit(numDimensions).toArray(),
                particleLoser.getVelocity(),
                "*");
        double[] array2 = arrayOperation(
                random.doubles().limit(numDimensions).toArray(),
                arrayOperation(
                    particleWinner.getPosition(),
                    particleLoser.getPosition(),
                    "-"),
                "*");
        double[] array3 = arrayOperation(
                random.doubles().limit(numDimensions).map(rand -> rand * phiAveragePosition).toArray(),
                arrayOperation(
                    getAveragePosition(swarm),
                    particleLoser.getPosition(),
                    "-"),
                "*");

        return arrayOperation(arrayOperation(array1, array2, "+"), array3, "+");
    }

    private double[] arrayOperation(double[] array1, double[] array2, String operator) {
        return switch (operator) {
            case "+" -> IntStream.range(0, array1.length)
                    .mapToDouble(i -> array1[i] + array2[i])
                    .toArray();
            case "-" -> IntStream.range(0, array1.length)
                    .mapToDouble(i -> array1[i] - array2[i])
                    .toArray();
            case "*" -> IntStream.range(0, array1.length)
                    .mapToDouble(i -> array1[i] * array2[i])
                    .toArray();
            default -> new double[array1.length];
        };
    }

    // Main method for testing
    public static void main(String[] args) {
        int numParticles = 6;
        int numDimensions = 2;
        int numIterations = 100;
        double phiAveragePosition = 0.1d;

        CSO cso = new CSO(numParticles, numDimensions, numIterations, phiAveragePosition, new ParaboloidProblem());
        double bestFitness = cso.optimize();

        System.out.println("Best fitness: " + bestFitness);
    }
}
