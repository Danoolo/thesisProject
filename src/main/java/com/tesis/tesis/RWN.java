package com.tesis.tesis;

import java.util.Random;

public class RWN {
    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private double[][] inputToHiddenWeights;
    private double[][] hiddenToOutputWeights;

    public RWN(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;

        // Initialize weights randomly
        Random rand = new Random();
        inputToHiddenWeights = new double[inputSize][hiddenSize];
        hiddenToOutputWeights = new double[hiddenSize][outputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                inputToHiddenWeights[i][j] = rand.nextGaussian();
            }
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                hiddenToOutputWeights[i][j] = rand.nextGaussian();
            }
        }
    }

    public double[] forward(double[] inputs) {
        // Forward pass through the network
        double[] hiddenLayer = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += inputs[j] * inputToHiddenWeights[j][i];
            }
            hiddenLayer[i] = sigmoid(sum);
        }

        double[] outputLayer = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenLayer[j] * hiddenToOutputWeights[j][i];
            }
            outputLayer[i] = sigmoid(sum);
        }

        return outputLayer;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static void main(String[] args) {
        int inputSize = 2;
        int hiddenSize = 3;
        int outputSize = 1;

        RWN rwn = new RWN(inputSize, hiddenSize, outputSize);

        double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] outputs = {{0}, {1}, {1}, {0}};

        // Test the trained network
        for (double[] input : inputs) {
            double[] output = rwn.forward(input);
            System.out.print("Input: [");
            for (double value : input) {
                System.out.print(value + " ");
            }
            System.out.print("], Output: [");
            for (double value : output) {
                System.out.print(value + " ");
            }
            System.out.println("]");
        }
    }
}