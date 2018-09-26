import java.util.Scanner;

import ml.NeuralNetwork;
import pong.Game;

public class PongCopyPlayer {

	public static void main(String[] args) {

		int trainTime = 1000000;
		int printError = 10000;
		NeuralNetwork testStudent = new NeuralNetwork();
		testStudent.setNetwork(6,1,1,4,"sigmoid");
		testStudent.createNetwork();
		Game game = new Game("key","game",true,60,testStudent);
		float[] trainGame[] = new float[game.getTotalGame().size()][7];
		for(int i = 0;i < game.getTotalGame().size();i++) {
			for(int j = 0;j < game.getTotalGame().get(i).length;j++) {
				trainGame[i][j] = game.getTotalGame().get(i)[j].floatValue();
			}
		}
		testStudent.getData(trainGame);
		for(int i = 0;i < trainTime;i++) {
			testStudent.trainSGD((float).1,150);
			if(i % printError == 0) {
				System.out.println(testStudent.getCost());
			}
		}
		Scanner keyboard = new Scanner(System.in);
		keyboard.next();
		new Game("ai","game",true,60,testStudent);
		keyboard.close();
	}
}
