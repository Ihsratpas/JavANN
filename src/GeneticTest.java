import ml.NeuralNetwork;
import ml.Population;

public class GeneticTest {
	static Float calculateFitness(NeuralNetwork x) {
		x.calculateCost();
		return 1 / x.getCost();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("--------Start--------");
		int population = 100;
		Population trials = new Population(population,2,1,0,0,"sigmoid");
		for(int i = 0;i < trials.getPopulation().length;i++) {
			trials.getPopulation()[i].getCSVData("test1.csv");
			trials.setFitness(calculateFitness(trials.getPopulation()[i]),i);
		}
		
		int generation = 0;
		Float target = (float)7.5;
		while(trials.getFitnesses()[trials.getFittest()] < target) {
			Float oldFitness = trials.getFitnesses()[trials.getFittest()];
			trials.rouletteSelection();
			trials.superMutation();
			Float newFitness = trials.getFitnesses()[trials.getFittest()];
			if(oldFitness.equals(newFitness)) {
				trials.selectMutation(trials.getPopulation()[trials.getLeastFit()],0,0);
			}
			if(generation % 1000 == 0) {
				System.out.println(trials.getFitnesses()[trials.getFittest()] + ";" + trials.getAverageFitness() + ";" + trials.getFitnesses()[trials.getLeastFit()] );
				for(int j = 0;j < population;j++) {
					trials.setFitness(calculateFitness(trials.getPopulation()[j]),j);
				}
			}
			generation++;
		}
		System.out.println("\n\n\n");
		float attributes[] = {(float)4.5,(float)1};
		System.out.println(trials.getPopulation()[trials.getFittest()].test(attributes)[0]);
	}
}
