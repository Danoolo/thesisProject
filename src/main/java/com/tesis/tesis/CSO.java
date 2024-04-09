package com.tesis.tesis;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CSO {
    private int numParticles;
    private int numDimensions;
    private int numIterations;
    private double phiAveragePosition;

    public CSO(int numParticles, int numDimensions, int numIterations, double phiAveragePosition) {
        this.numParticles = numParticles % 2 == 0 ? numParticles : numParticles + 1;
        this.numDimensions = numDimensions;
        this.numIterations = numIterations;
        this.phiAveragePosition = phiAveragePosition;
    }

    public void optimize() {
        SecureRandom rand = new SecureRandom();
        List<Particle> swarm = new ArrayList<>();

        // Initialize particles randomly
        for (int i = 0; i < numParticles; i++)
            swarm.add(new Particle(rand.doubles().limit(numDimensions).toArray()));

        // Main optimization loop
        for (int iter = 0; iter < numIterations; iter++) {
            // Empty swarm for the next generation
            List<Particle> nextSwarm = new ArrayList<>();

            // Calculate the fitness of all particles in the swarm
            swarm.forEach(particle -> particle.setFitness(Problem.calculateFitness(particle.getPosition())));

            // Competitive phase
            Particle particle1;
            Particle particle2;
            Particle particleWinner;
            Particle particleLoser;
            while (!swarm.isEmpty()) {
                particle1 = swarm.remove(rand.nextInt(swarm.size()));
                particle2 = swarm.remove(rand.nextInt(swarm.size()));

                if (Problem.compareFitnessValues(particle1.getFitness(), particle2.getFitness()) < 0) {
                    particleLoser = particle1;
                    particleWinner = particle2;
                } else {
                    particleLoser = particle2;
                    particleWinner = particle1;
                }

                Particle pLoserCopy = new Particle(particleLoser);
                pLoserCopy.setVelocity(getNewVelocity(particleWinner, particleLoser, rand, swarm));
                pLoserCopy.setPosition(arrayOperation(particleLoser.getPosition(), pLoserCopy.getVelocity(), "+"));

                nextSwarm.add(new Particle(particleWinner));
                nextSwarm.add(pLoserCopy);

                swarm = nextSwarm;
            }
        }
    }

    private double[] getAveragePosition(List<Particle> swarm) {
        int arrayLength = swarm.size();
        double[] averageArray = new double[arrayLength];

        for (int i = 0; i < arrayLength; i++) {
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
        int numParticles = 20;
        int numDimensions = 10;
        int numIterations = 100;
        double phiAveragePosition = 0.1d;

        CSO cso = new CSO(numParticles, numDimensions, numIterations, phiAveragePosition);
        cso.optimize();

//        System.out.println("Best fitness: " + cso.getBestFitness());
//        System.out.println("Best position: ");
//        for (double val : cso.getBestPosition()) {
//            System.out.print(val + " ");
//        }
    }
}
