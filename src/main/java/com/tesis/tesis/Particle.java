package com.tesis.tesis;

import java.security.SecureRandom;

public class Particle {
    private double[] position;
    private double[] velocity;
    private double fitness;

    public Particle(double[] position) {
        SecureRandom random = new SecureRandom();
        this.position = position;
        this.velocity = random.doubles().limit(position.length).toArray();
    }

    public Particle(Particle particle) {
        this.position = particle.getPosition();
        this.velocity = particle.getVelocity();
        this.fitness = 0;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
