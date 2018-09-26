package ml;
public class Population {
	NeuralNetwork[] population;
	Float[] fitnesses;
	
	//Initializes networks
	public Population(int populationSize,int inputNumber,int outputNumber,int hiddenLayers,int hiddenNeurons,String activation) {
		population = new NeuralNetwork[populationSize];
		fitnesses = new Float[populationSize];
		for(int i = 0;i < populationSize;i++) {
			population[i] = new NeuralNetwork();
			population[i].setNetwork(inputNumber, outputNumber, hiddenLayers, hiddenNeurons, activation);
			population[i].createNetwork();
		}
	}
	public NeuralNetwork[] getPopulation() {
		return population;
	}
	public void setPopulation(NeuralNetwork[] population) {
		this.population = population;
	}
	
	public Float[] getFitnesses() {
		return fitnesses;
	}
	public void setFitnesses(Float[] fitnesses) {
		this.fitnesses = fitnesses;
	}
	
	public void setFitness(Float newFitness,int index) {
		fitnesses[index] = newFitness;
	}

	public int getFittest() {
		int x = 0;
		for(int i = 0;i < fitnesses.length;i++) {
			if(fitnesses[x] < fitnesses[i]) {
				x = i;
			}
		}
		return x;
	}
	public int getLeastFit() {
		int x = 0;
		for(int i = 0;i < fitnesses.length;i++) {
			if(fitnesses[x] > fitnesses[i]) {
				x = i;
			}
		}
		return x;
	}
	public float getAverageFitness() {
		float totalFitness = 0;
		for(int i = 0;i < fitnesses.length;i++) {
			totalFitness += fitnesses[i];
		}
		return totalFitness/fitnesses.length;
	}
	
	//Different methods of selecting individuals from one generation to the next
	public void rouletteSelection() {
		float totalScore = 0;
		for(float i: fitnesses) {
			totalScore += i;
		}
		
		float probabilities[] = new float[population.length];
		for(int i = 0;i < probabilities.length;i++) {
			probabilities[i] = fitnesses[i] / totalScore;
		}

		int count = 0;
		NeuralNetwork[] probabilityArray = new NeuralNetwork[100];
		for(int i = 0;i < probabilities.length;i++) {
			try {
				for(int j = 0;j < Math.round(probabilities[i] * 100);j++) {
					probabilityArray[count] = population[i];
					count++;
				}
			} catch(Exception e) {
				probabilityArray[99] = population[i];
			}
		}
		for(int i = 0;i < probabilityArray.length;i++) {
			if(probabilityArray[i] == null) {
				probabilityArray[i] = population[0];
			}
		}
		
		for(int l = 0;l < population.length;l++) {
			if(!(population[l].equals(population[this.getFittest()]))) {
			for(int i = 0;i < population[l].getWeights().length;i++) {
				for(int j = 0;j < population[l].getWeights()[i].length;j++) {
					int decider = (int)(Math.random() * probabilityArray.length);
					int crossoverPoint = (int)Math.rint((Math.random() * probabilityArray[decider].getWeights()[i][j].length));
					for(int k = 0;k < crossoverPoint;k++) {
						population[l].setWeight(i,j,k,probabilityArray[decider].getWeights()[i][j][k]);
					}
				}
			}
			for(int i = 0;i < population[l].getBiases().length;i++) {
				int decider = (int)(Math.random() * population.length);
				int crossoverPoint = (int)Math.rint((Math.random() * population[decider].getBiases()[i].length));
				for(int j = 0;j < crossoverPoint;j++) {
					population[l].setBias(i,j,population[decider].getBiases()[i][j]);
				}
			}
			}
		}
	}
	public void truncatedSelection() {
	
		int bestIndex1 = 0;
		int bestIndex2 = 0;
		
		int fittestIndex = this.getFittest();
		Float lastFitness = fitnesses[fitnesses.length - 1];
		fitnesses[fitnesses.length - 1] = fitnesses[fittestIndex];
		fitnesses[fittestIndex] = lastFitness;
		for(int i = 0;i < fitnesses.length - 1;i++) {
			if(fitnesses[bestIndex2] < fitnesses[i]) {
				bestIndex2 = i;
			}
		}
		NeuralNetwork lastNetwork = population[population.length - 1];
		population[fitnesses.length - 1] = population[fittestIndex];
		population[fittestIndex] = lastNetwork;
		
		for(int i = 0;i < population[this.getLeastFit()].getWeights().length;i++) {
			for(int j = 0;j < population[this.getLeastFit()].getWeights()[i].length;j++) {
				for(int k = 0;k < population[this.getLeastFit()].getWeights()[i][j].length;k++) {
					if(Math.rint(Math.random()) == 0) {
						population[this.getLeastFit()].setWeight(i,j,k,population[bestIndex1].getWeights()[i][j][k]);
					} else {
						population[this.getLeastFit()].setWeight(i,j,k,population[bestIndex2].getWeights()[i][j][k]);
					}
				}
			}
		}
		for(int i = 0;i < population[this.getLeastFit()].getBiases().length;i++) {
			for(int j = 0;j < population[this.getLeastFit()].getBiases()[i].length;j++) {
				if(Math.rint(Math.random()) == 0) {
					population[this.getLeastFit()].setBias(i,j,population[bestIndex1].getBiases()[i][j]);
				} else {
					population[this.getLeastFit()].setBias(i,j,population[bestIndex2].getBiases()[i][j]);
				}
			}
		}
	}
	
	//Different methods of mutating
	public void mutation(double mutationRate) {
		for(int i = 0;i < population.length;i++) {
			if(i != getFittest()) {
				for(int j = 0;j < population[i].getWeights().length;j++) {
					for(int k = 0;k < population[i].getWeights()[j].length;k++) {
						if(Math.random() <= mutationRate) {
							int randomIndex = (int)(Math.random() * population[i].getWeights()[j][k].length);
							population[i].setWeight(j,k,randomIndex,(float)Math.random());
						}
					}
				}
				for(int j = 0;j < population[i].getBiases().length;j++) {
					if(Math.random() <= mutationRate) {
						int randomIndex = (int)(Math.random() * population[i].getWeights()[j].length);
						population[i].setBias(j,randomIndex,(float)Math.random());
					}
				}
			}
		}
	}
	public void superMutation() {
		for(int i = 0;i < population.length;i++) {
			if(i != this.getFittest()) {
				for(int j = 0;j < population[i].getWeights().length;j++) {
					for(int k = 0;k < population[i].getWeights()[j].length;k++) {
						for(int l = 0;l < population[i].getWeights()[j][k].length;l++) {
							float previousWeight = population[i].getWeights()[j][k][l];
							Float previousFitness = fitnesses[i];
							population[i].setWeight(j,k,l,(float)Math.random());
							if(!(previousFitness <= fitnesses[i])) {
								population[i].setWeight(j,k,l,previousWeight);
							}
						}
					}
				}
				for(int j = 0;j < population[i].getBiases().length;j++) {
					for(int k = 0;k < population[i].getBiases()[j].length;k++) {
						float previousBias = population[i].getBiases()[j][k];
						Float previousFitness = fitnesses[i];
						population[i].setBias(j,k,(float)Math.random());
						if(!(previousFitness <= fitnesses[i])) {
							population[i].setBias(j,k,previousBias);
						}
					}
				}
			}
		}
	}
	public void selectMutation(NeuralNetwork selected,int min,int max) {
		for(int j = 0;j < selected.getWeights().length;j++) {
			for(int k = 0;k < selected.getWeights()[j].length;k++) {
				int randomIndex = (int)(Math.random() * selected.getWeights()[j][k].length);
				selected.setWeight(j,k,randomIndex,min + (float)Math.random() * (max - min));
			}
		}
		for(int j = 0;j < selected.getBiases().length;j++) {
			int randomIndex = (int)(Math.random() * selected.getWeights()[j].length);
			selected.setBias(j,randomIndex,min + (float)Math.random() * (max - min));
		}
	}
}
	
	
	
