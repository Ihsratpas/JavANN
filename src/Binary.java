import java.util.Arrays;

import ml.NeuralNetwork;
import ml.Population;

public class Binary{
	
	static Float calculateFitness(NeuralNetwork x) {
		Float sum = (float) 0;
		for(int k = 0;k < x.getWeights()[0].length;k++) {
			sum += x.getWeights()[0][k][0];
		}
		return sum;
	}
	public static void main(String[] args) throws Exception {
		int population = 5;
		Population trials = new Population(population,1,5,0,0,"sigmoid");
		for(int i = 0;i < trials.getPopulation().length;i++) {
			NeuralNetwork currentNetwork = trials.getPopulation()[i]; 
			for(int j = 0;j < 5;j++) {
				double randomWeight = Math.round(Math.random());
				currentNetwork.setWeight(0, j, 0, (float)randomWeight);
			}
			trials.getFitnesses()[i] = calculateFitness(currentNetwork);
		}
		Float target = (float)5;
		while(!Arrays.asList(trials.getFitnesses()).contains(target)) {
			trials.rouletteSelection();
			trials.mutation(.1);
			System.out.println("\n");
			for(int j = 0;j < 5;j++) {
				System.out.print((int)trials.getPopulation()[trials.getFittest()].getWeights()[0][j][0] + ",");
			}
			for(int j = 0;j < population;j++) {
				trials.getFitnesses()[j] = calculateFitness(trials.getPopulation()[j]);
			}
		}
		System.out.println("\n\n\n");
		System.out.println(trials.getFitnesses()[trials.getFittest()]);
		System.out.println("\n\n\n");
		for(int i = 0;i < 5;i++) {
			System.out.print((int)trials.getPopulation()[trials.getFittest()].getWeights()[0][i][0] + ",");
		}
	}
}
