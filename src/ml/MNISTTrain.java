package ml;

import java.io.IOException;

public class MNISTTrain {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		int trainTime = 50000,printError = 1000,saveTime = 5000,batchSize = 50;
		double learningRate = 1e-3,decay = .99;
		String fileName = "C:/Users/Babtu/Documents/Models/MNISTV2.ser";
		MLP2 mnist = MLP2.load("C:/Users/Babtu/Documents/Models/MNISTV1.ser");
		mnist.customTrain(true,trainTime,printError,saveTime,batchSize,learningRate,decay,fileName);

	}

}
