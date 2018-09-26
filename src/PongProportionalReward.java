import java.io.IOException;

import ml.NeuralNetwork;
import pong.Game;

public class PongProportionalReward {

	public static void main(String[] args) throws IOException {
		String file = "PongNetworks1/Batch52";
		int trainGames = 250,trainTime = 2500,batchSize = 100,batches = 800;
		int fps = 1000;
		float learningRate = (float)1e-3;
		NeuralNetwork gamer = new NeuralNetwork();
		gamer.setNetwork(6,1,1,10,"sigmoid");
		gamer.createNetwork();
		gamer.setFileBiases(file + "Biases.txt");
		gamer.setFileWeights(file + "Weights.txt");
		Game games[] = new Game[trainGames];
		float[][] totalGames[] = new float[trainGames][][];
		int batchNumber = 53;
		for(int i = 37;i < batches;i++) {
			int totalFrames = 0;
			int gamesWon = 0;
			int gamesLost = 0;
			for(int j = 0;j < trainGames;j++) {
				NeuralNetwork testGame = new NeuralNetwork();
				testGame.setNetwork(6,1,1,10,"sigmoid");
				testGame.createNetwork();
				Game episode = new Game("ai","episode",false,fps,gamer);
				games[j] = episode;
				if(episode.getScore() == 1) {
					gamesWon++;
				} else {
					gamesLost++;
				}
				float[] totalGame[] = new float[episode.getTotalGame().size()][episode.getTotalGame().get(0).length];
				for(int k = 0;k < totalGame.length;k++) {
					for(int l = 0;l < episode.getTotalGame().get(k).length;l++) {
						totalGame[k][l] = (float)episode.getTotalGame().get(k)[l];
					}
				}
				totalGames[j] = totalGame;
				totalFrames += totalGame.length;
			}
			System.out.println("Games Won: " + gamesWon);
			System.out.println("Games Lost: " + gamesLost);
			System.out.println("Total frames: " + totalFrames);
			System.out.println("Average frames: " + totalFrames / trainGames);
			for(int j = 0;j < trainGames;j++) {
				Game game = games[j];
				float[] totalGame[] = totalGames[j];
				if(game.getScore() == 0) {
					for(int k = 0;k < totalGame.length;k++) {
						totalGame[k][6] = Game.flip(totalGame[k][6]);
					}
				}
			}
			float[] totalTrainFrames[] = new float[totalFrames][];
			int game = 0;
			int frame = 0;
			for(int j = 0;j < totalTrainFrames.length;j++) {
				totalTrainFrames[j] = totalGames[game][frame];
				if(frame == totalGames[game].length - 1) {
					game++;
					frame = 0;
				} else {
					frame++;
				}
			}
			gamer.getData(totalTrainFrames);
			for(int j = 0;j < trainTime;j++) {
				gamer.trainSGD(learningRate,batchSize);
				System.out.println("Training iteration " + j + " of batch " + batchNumber + " with error " + Math.sqrt(gamer.getCost()));
			}
			new Game("ai","full",true,60,gamer);
			System.out.println("Batch " + batchNumber);
			System.out.println("Weights:");
			gamer.printWeights();
			System.out.println("Biases:");
			gamer.printBiases();
			batchNumber++;
		}
	}

}
