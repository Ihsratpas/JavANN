import java.util.ArrayList;
import java.util.Scanner;

import ml.NeuralNetwork;
import ml.Population;
import pong.Game;

public class PongGeneticAlgorithm {

	public static void main(String[] args) {
		int games = 2000;
		int trainTime = 200;
		Float minimumScore = (float)1;
		ArrayList<NeuralNetwork> testStudentsList = new ArrayList<NeuralNetwork>();
		ArrayList<Float> scoresList = new ArrayList<Float>();
		for(int i = 0;i < games;i++) {
			System.out.println("Model# " + i);
			NeuralNetwork testStudent = new NeuralNetwork();
			testStudent.setNetwork(5,1,1,10,"sigmoid");
			testStudent.createNetwork();
			Game game = new Game("ai","game",true,10000,testStudent);
			if(game.getScore() > minimumScore) {
				scoresList.add((float)game.getScore());
				testStudentsList.add(testStudent);
			}
			System.out.println("Score: " + game.getScore());
		}
		NeuralNetwork testStudents[] = new NeuralNetwork[testStudentsList.size()];
		Float scores[] = new Float[scoresList.size()];
		for(int i = 0;i < testStudents.length;i++) {
			testStudents[i] = testStudentsList.get(i);
			scores[i] = scoresList.get(i);
		}
		Population students = new Population(testStudents.length,5,1,1,10,"sigmoid");
		students.setPopulation(testStudents);
		for(int i = 0;i < students.getPopulation().length;i++) {
			students.setFitness(scores[i],i);
		}
		System.out.println("Accepted models: " + scores.length);
		System.out.println("Average accepted score: " + students.getAverageFitness());
		System.out.println("High score: " + students.getFitnesses()[students.getFittest()]);
		Scanner keyboard = new Scanner(System.in);
		keyboard.next();
		new Game("ai","game",true,60,students.getPopulation()[students.getFittest()]);
		System.out.println("Training...");
		for(int i = 0;i < trainTime;i++) {
			students.rouletteSelection();
			students.mutation(.1);
			System.out.println("Generation: " + i);
			for(int j = 0;j < students.getFitnesses().length;j++) {
				Game game = new Game("ai","game",false,1000000,students.getPopulation()[j]);
				students.setFitness((float)game.getScore(),j);
			}
		}
		keyboard.next();
		for(int i = 0;i < students.getPopulation().length;i++) {
			new Game("ai","game",true,60,students.getPopulation()[i]);
			System.out.println("Would you like to save the model?");
			String save = keyboard.next();
			if(save.equals("yes")) {
				System.out.println("Weights:");
				students.getPopulation()[i].printWeights();
				System.out.println("\nBiases:");
				students.getPopulation()[i].printBiases();
			}
		}
		keyboard.close();
	}

}
