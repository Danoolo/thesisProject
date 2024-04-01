package com.tesis.tesis;

import java.security.SecureRandom;

public class MLP {
    private int inputSize;
    private int[] hiddenSizes;
    private int outputSize;
    private double[][][] weights;
    private double[][] biases;
    private double[][] activations;
    private double[][] deltas;
    private double learningRate;
    private double momentum;
    private SecureRandom random;

    public MLP(int inputSize, int[] hiddenSizes, int outputSize, double learningRate, double momentum) {
        this.inputSize = inputSize;
        this.hiddenSizes = hiddenSizes;
        this.outputSize = outputSize;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.random = new SecureRandom();

        initializeWeights();
        initializeBiases();
    }

    private void initializeWeights() {
        int numHiddenLayers = hiddenSizes.length;
        weights = new double[numHiddenLayers + 1][][];
        activations = new double[numHiddenLayers + 2][]; // Add one for input and one for output layer
        deltas = new double[numHiddenLayers + 1][];

        // Initialize weights for input-hidden layers
        weights[0] = new double[inputSize][hiddenSizes[0]];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSizes[0]; j++) {
                weights[0][i][j] = random.nextDouble() - 0.5; // Random weight between -0.5 and 0.5
            }
        }

        // Initialize weights for hidden-output layer
        weights[numHiddenLayers] = new double[hiddenSizes[numHiddenLayers - 1]][outputSize];
        for (int i = 0; i < hiddenSizes[numHiddenLayers - 1]; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[numHiddenLayers][i][j] = random.nextDouble() - 0.5; // Random weight between -0.5 and 0.5
            }
        }

        // Initialize weights for hidden-hidden layers
        for (int i = 1; i < numHiddenLayers; i++) {
            weights[i] = new double[hiddenSizes[i - 1]][hiddenSizes[i]];
            for (int j = 0; j < hiddenSizes[i - 1]; j++) {
                for (int k = 0; k < hiddenSizes[i]; k++) {
                    weights[i][j][k] = random.nextDouble() - 0.5; // Random weight between -0.5 and 0.5
                }
            }
        }
    }

    private void initializeBiases() {
        int numHiddenLayers = hiddenSizes.length;
        biases = new double[numHiddenLayers + 1][];
        for (int i = 0; i < numHiddenLayers; i++) {
            biases[i] = new double[hiddenSizes[i]];
            for (int j = 0; j < hiddenSizes[i]; j++) {
                biases[i][j] = random.nextDouble() - 0.5; // Random bias between -0.5 and 0.5
            }
        }
        biases[numHiddenLayers] = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            biases[numHiddenLayers][i] = random.nextDouble() - 0.5; // Random bias between -0.5 and 0.5
        }
    }

    public double[] feedForward(double[] input) {
        // Forward propagation from input to output
        activations[0] = input;
        for (int i = 0; i < weights.length; i++) {
            activations[i + 1] = calculateLayerOutput(activations[i], weights[i], biases[i]);
        }
        return activations[activations.length - 1];
    }

    private double[] calculateLayerOutput(double[] input, double[][] weights, double[] biases) {
        // weights[0].length represents the qty of neurons in the layer
        double[] output = new double[weights[0].length];
        for (int i = 0; i < weights[0].length; i++) {
            double sum = biases[i];
            for (int j = 0; j < input.length; j++) {
                sum += input[j] * weights[j][i];
            }
            output[i] = sigmoid(sum);
        }
        return output;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private void backpropagation(double[] target) {
        int numLayers = weights.length;
        deltas[numLayers - 1] = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double error = target[i] - activations[numLayers][i];
            deltas[numLayers - 1][i] = error * activations[numLayers][i] * (1 - activations[numLayers][i]);
        }

        for (int i = numLayers - 2; i >= 0; i--) {
            deltas[i] = new double[hiddenSizes[i]];
            for (int j = 0; j < hiddenSizes[i]; j++) {
                double error = 0;
                for (int k = 0; k < deltas[i + 1].length; k++) {
                    error += deltas[i + 1][k] * weights[i + 1][j][k];
                }
                deltas[i][j] = activations[i + 1][j] * (1 - activations[i + 1][j]) * error;
            }
        }

        // Update weights with momentum
        for (int i = 0; i < numLayers; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    double weightUpdate = learningRate * deltas[i][k] * activations[i][j];
                    weights[i][j][k] += weightUpdate;
                    // Apply momentum
                    if (i > 0) {
                        weights[i][j][k] += momentum * weightUpdate;
                    }
                }
            }
        }
    }

    public void train(double[] input, double[] target) {
        // Forward propagation
        feedForward(input);

        // Backpropagation
        backpropagation(target);
    }
}
