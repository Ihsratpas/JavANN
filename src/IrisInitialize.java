import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import ml.NewMLP;

public class IrisInitialize {

	public static void main(String[] args) throws IOException {
		
		int neuronNumbers[] = {4,8,3};
		boolean normalize = false;
		float trainSum[] = {0,0,0,0};
		Scanner train = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/irisTrain.csv")));
		float[] trainingData[] = new float[120][7];
		for(int i = 0;i < trainingData.length;i++) {
			String line = train.nextLine();
			String values[] = line.split(",");
			float realValues[] = new float[7];
			for(int j = 0;j < 4;j++) {
				realValues[j] = Float.parseFloat(values[j]);
				trainSum[j] += realValues[j];
			}
			if(values[4].equals("Iris-setosa")) {
				realValues[4] = 1;
			} else if(values[4].equals("Iris-versicolor")) {
				realValues[5] = 1;
			} else {
				realValues[6] = 1;
			}
			trainingData[i] = realValues;
		}
		if(normalize) {
			for(int i = 0;i < trainingData.length;i++) {
				for(int j = 0;j < 4;j++) {
					trainingData[i][j] = trainingData[i][j] / trainSum[j];
				}
			}
		}
		float testSum[] = {0,0,0,0};
		Scanner test = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/irisTest.csv")));
		float[] testData[] = new float[30][7];
		for(int i = 0;i < testData.length;i++) {
			String line = test.nextLine();
			String values[] = line.split(",");
			float realValues[] = new float[7];
			for(int j = 0;j < 4;j++) {
				realValues[j] = Float.parseFloat(values[j]);
				testSum[j] += realValues[j];
			}
			for(int j = 0;j < 3;j++) {
				realValues[j + 4] = 0;
			}
			if(values[4].equals("Iris-setosa")) {
				realValues[4] = 1;
			} else if(values[4].equals("Iris-versicolor")) {
				realValues[5] = 1;
			} else {
				realValues[6] = 1;
			}
			testData[i] = realValues;
		}
		if(normalize) {
			for(int i = 0;i < testData.length;i++) {
				for(int j = 0;j < 4;j++) {
					testData[i][j] = testData[i][j] / testSum[j];
				}
			}
		}

		NewMLP farmer = new NewMLP(neuronNumbers,"softmax","CE");
		farmer.createNetwork();
		farmer.getData(trainingData);
		farmer.getTestData(testData);
		farmer.save("C:/Users/Babtu/Documents/Models/Iris.ser");
		train.close();
		test.close();
		System.out.println("Model saved!");
		
	}
	
}
