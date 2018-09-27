import java.io.IOException;

import ml.NeuralNetwork;
import pong.Game;

public class PongNetworkTest {

	public static void main(String[] args) throws IOException {
		String file = args[0];//"C:/Users/Babtu/Documents/PongNetworks1/Batch37";
		NeuralNetwork fileTest = new NeuralNetwork();
		fileTest.setNetwork(6,1,1,10,"sigmoid");
		fileTest.createNetwork();
		fileTest.setFileBiases(file + "Biases.txt");
		fileTest.setFileWeights(file + "Weights.txt");
		double averageScore = 0;
		int games = 20;
		for(int i = 0;i < games;i++) {
			Game game = new Game("ai","game",true,200,fileTest);
			averageScore += game.getScore() / games;
		}
		System.out.println("Average Score: " + averageScore);
		
	}

}
