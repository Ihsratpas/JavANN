import java.io.IOException;

import ml.MLP;

public class TFMNISTTrain {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		int trainTime = 20000,printError = 1,saveTime = 10000,batchSize = 100;
		float learningRate = (float).1,decay = (float)1;
		String fileName = "C:/Users/Babtu/Documents/Models/Trained/MNISTSimple.ser";
		MLP mnist = MLP.load(fileName);
		mnist.customTrain(true,trainTime,printError,saveTime,batchSize,learningRate,decay,"C:/Users/Babtu/Documents/Models/NewModel.ser");

	}

}
