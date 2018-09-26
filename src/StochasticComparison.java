import ml.NeuralNetwork;

public class StochasticComparison {

	public static void main(String[] args) throws Exception {
		int inputs = 2,outputs = 2,hiddenLayers = 1,hiddenNeurons = 3;
		String fileName = "xor.csv",activation = "sigmoid";
		int allTrainTime = 50000,allPrintError = allTrainTime / 5;
		int stochasticTrainTime = allTrainTime / 4,stochasticPrintError = stochasticTrainTime / 5,batchSize = 2;
		float learningRate = (float).1;
		float[] testExamples[] = {{(float)1,(float)1},{(float)0,(float)1}};
		NeuralNetwork stochasticTest = new NeuralNetwork();
		stochasticTest.setNetwork(inputs,outputs,hiddenLayers,hiddenNeurons,activation);
		stochasticTest.getCSVData(fileName);
		stochasticTest.createNetwork();
		double lastTime = (double)System.currentTimeMillis();
		System.out.println("Stochastic Gradient Descent");
		for(int i = 0;i < stochasticTrainTime;i++) {
			stochasticTest.trainSGD(learningRate,batchSize);
			if(i % stochasticPrintError == 0) {
				System.out.println("The mean squared error is " + stochasticTest.getCost());
			}
		}
		System.out.println("Final Cost: " + stochasticTest.getCost());
		System.out.println("Time: " + (System.currentTimeMillis() - lastTime));
		for(int i = 0;i < testExamples.length;i++) {
			System.out.println("Example# " + i + ": " + stochasticTest.test(testExamples[i])[0]);
		}
		System.out.println("\n\n\n");
		NeuralNetwork allExamples = new NeuralNetwork();
		allExamples.setNetwork(inputs,outputs,hiddenLayers,hiddenNeurons,activation);
		allExamples.getCSVData(fileName);
		allExamples.createNetwork();
		lastTime = (double)System.currentTimeMillis();
		System.out.println("Normal Gradient Descent");
		for(int i = 0;i < allTrainTime;i++) {
			allExamples.trainGD(learningRate);
			if(i % allPrintError == 0) {
				System.out.println("The mean squared error is " + allExamples.getCost());
			}
		}
		System.out.println("Final Cost: " + allExamples.getCost());
		System.out.println("Time: " + (System.currentTimeMillis() - lastTime));
		for(int i = 0;i < testExamples.length;i++) {
			System.out.println("Example# " + i + ": "  + allExamples.test(testExamples[i])[0]);
		}
	}

}
