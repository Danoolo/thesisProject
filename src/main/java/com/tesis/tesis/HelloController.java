package com.tesis.tesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class HelloController {

    @FXML
    private TextField inputSizeField;
    @FXML
    private TextField hiddenLayerSizesField;
    @FXML
    private TextField outputSizeField;
    @FXML
    private TextField learningRateField;
    @FXML
    private TextField momentumField;
    @FXML
    private TextArea trainingDataArea;
    @FXML
    private TextArea targetsArea;
    @FXML
    private TextArea logArea;

    @FXML
    private void trainAndTestMLP() {
        // Parse user input
        int inputSize = Integer.parseInt(inputSizeField.getText());
        int[] hiddenLayerSizes = parseArray(hiddenLayerSizesField.getText());
        int outputSize = Integer.parseInt(outputSizeField.getText());
        double learningRate = Double.parseDouble(learningRateField.getText());
        double momentum = Double.parseDouble(momentumField.getText());
        double[][] trainingData = parse2DArray(trainingDataArea.getText());
        double[][] targets = parse2DArray(targetsArea.getText());

        // Example usage
        MLP mlp = new MLP(inputSize, hiddenLayerSizes, outputSize, learningRate, momentum);

        // Train the network
        for (int epoch = 0; epoch < 100000; epoch++) {
            for (int i = 0; i < trainingData.length; i++) {
                mlp.train(trainingData[i], targets[i]);
            }
        }

        // Test the network
        logArea.setText("");
        for (double[] input : trainingData) {
            double[] output = mlp.feedForward(input);
            log("Input: " + Arrays.toString(input) + " Output: " + Arrays.toString(output));
        }
    }

    private int[] parseArray(String input) {
        String[] tokens = input.split(",");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            result[i] = Integer.parseInt(tokens[i].trim());
        }
        return result;
    }

    private double[][] parse2DArray(String input) {
        String[] lines = input.split("\n");
        double[][] result = new double[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String[] tokens = lines[i].split(",");
            result[i] = new double[tokens.length];
            for (int j = 0; j < tokens.length; j++) {
                result[i][j] = Double.parseDouble(tokens[j].trim());
            }
        }
        return result;
    }

    private void log(String message) {
        logArea.appendText(message + "\n");
    }
}