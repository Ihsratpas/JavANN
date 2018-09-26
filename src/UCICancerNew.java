
import ml.NeuralNetwork;

public class UCICancerNew {

	public static void main(String[] args) throws Exception {
		int iteration = 35550000;
		int trainTime = Integer.MAX_VALUE;
		int printError = 5000;
		int printConfiguration = 50000;
		int batchSize = 100;
		float learningRate = (float)1e-4,decay = (float).99;
		String file = "NewCancer/Iteration" + iteration;
		
		NeuralNetwork detector = new NeuralNetwork();
		detector.setNetwork(9,1,1,6,"sigmoid");		
		detector.createNetwork();
		detector.setFileBiases(file + "Biases.txt");
		detector.setFileWeights(file + "Weights.txt");
		detector.getCSVData("uci.csv");
				
		for(int i = 0;i < detector.getTotalData().length;i++) {
			if(detector.getTotalData()[i][9] == 2) {
				detector.setData(i,9,0);
			} else {
				detector.setData(i,9,1);
			}
			for(int j= 0;j < detector.getTotalData()[i].length - 1;j++) {
				detector.setData(i,j,(float)(detector.getTotalData()[i][j] * .1));
			}
		}
		System.out.println(detector.getCost());
		for(int i = iteration;i < trainTime;i++) {
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
