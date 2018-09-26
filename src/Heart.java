import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import ml.NeuralNetwork;

public class Heart {

	public static void main(String[] args) throws FileNotFoundException {
		
		int trainTime = Integer.MAX_VALUE,printError = 5000,printConfiguration = 100000,batchSize = 50;
		float learningRate = (float)1e-4,decay = (float).99;
		Scanner read = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/Heart.csv")));
		float[] trainingData[] = new float[303][18];
		for(int i = 0;i < 303;i++) {
			String line = read.nextLine();
			float realValues[] = new float[18];
			for(int j = 0;j < 13;j++) {
				trainingData[i][j] = 0;
				try {
					realValues[j] = Float.parseFloat(line.split(",")[j]);
				} catch (Exception e) {
					realValues[j] = -99999;
				}
			}
			for(int j = 0;j < 5;j++) {
				trainingData[i][j + 13] = 0;
				try {
					realValues[(int)(Float.parseFloat(line.split(",")[13]) + 13)] = 1;
				} catch (Exception e) {
					realValues[j + 13] = -99999;
				}
			}
			trainingData[i] = realValues;
		}
		NeuralNetwork doctor = new NeuralNetwork();
		doctor.setNetwork(13,5,1,11,"sigmoid");
		doctor.getData(trainingData);
		doctor.createNetwork();
		for(int i = 0;i < trainTime;i++) {
			doctor.trainSGD(learningRate, batchSize);
			learningRate *= decay;
			if(i % printError == 0) {
				System.out.println("Accuracy: " + (1 - Math.sqrt(doctor.getCost())));
			}
			if(i % printConfiguration == 0) {
				System.out.println("Weights: ");
				doctor.printWeights();
				System.out.println("Biases: ");
				doctor.printBiases();
			}
		}
		read.close();
	}

}
