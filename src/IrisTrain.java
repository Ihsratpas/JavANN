import java.io.IOException;

import ml.NewMLP;

public class IrisTrain {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		int trainTime = 2000000,printError = 100,saveTime = 100000,batchSize = 80;
		float learningRate = (float)1e-2,decay = (float)1;
		String fileName = "C:/Users/Babtu/Documents/Models/IrisTrained.ser";
		NewMLP mnist = NewMLP.load(fileName);
		mnist.customTrain(true,trainTime,printError,saveTime,batchSize,learningRate,decay,"C:/Users/Babtu/Documents/Models/IrisTrained2.ser");

	}

}
