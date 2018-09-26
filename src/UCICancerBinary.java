import ml.NeuralNetwork;

public class UCICancerBinary {
	public static void main(String[] args) throws Exception {
		int iteration = 7120000;
		int trainTime = Integer.MAX_VALUE;
		int printError = 1000;
		int printConfiguration = 10000;
		int batchSize = 100;
		float learningRate = (float)1e-3,decay = (float).99;
		
		String file = "BinaryCancer/Iteration" + iteration;
		NeuralNetwork detector = new NeuralNetwork();
		detector.setNetwork(36,1,1,6,"sigmoid");
		detector.createNetwork();
		detector.getCSVData("uciBinary.csv");
		detector.setFileBiases(file + "Biases.txt");
		detector.setFileWeights(file + "Weights.txt");
		detector.calculateCost();
		System.out.println("Initial Cost: " + detector.getCost());
		for(int i = iteration + 1;i < trainTime;i++) {
			detector.trainSGD(learningRate,batchSize);
			learningRate *= decay;
			float cost = detector.getCost();
			if(i % printError == 0) {
				System.out.println("Iteration: " + i);
				System.out.println("Cost: " + cost);
				System.out.println("Accuracy: " + (1 - Math.sqrt(cost)));
				System.out.println("");
			}
			if(i % printConfiguration == 0) {
				System.out.println("Biases:");
				detector.printBiases();
				System.out.println("Weights:");
				detector.printWeights();
			}
		}
		
	}

}