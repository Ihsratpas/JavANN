
import ml.NeuralNetwork;

public class UCICancer {

	public static void main(String[] args) throws Exception {
		int trainTime = 100000000;
		int printError = 1000;
		int printConfiguration = 10000;
		int batchSize = 90;
		float learningRate = (float)1e-4;
		
		NeuralNetwork detector = new NeuralNetwork();
		detector.setNetwork(9,1,1,6,"sigmoid");		
		detector.getCSVData("uci.csv");
				
		for(int i = 0;i < detector.getTotalData().length;i++) {
			if(detector.getTotalData()[i][9] == 2) {
				detector.setData(i,9,0);
			} else {
				detector.setData(i,9,1);
			}
		}
		detector.createNetwork();
		System.out.println(detector.getCost());
		float firstCost = 0;
		for(int i = 0;i < trainTime;i++) {
			detector.trainSGD(learningRate,batchSize);
			float cost = detector.getCost();
			if(i == 0) {
				firstCost = cost;
			}
			if(i % printError == 0) {
				System.out.println("Iteration: " + i);
				System.out.println("Cost: " + cost);
				System.out.println("Total Difference: " + (firstCost - cost));
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
