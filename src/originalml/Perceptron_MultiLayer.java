package originalml;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
public class Perceptron_MultiLayer {

	public static void main (String[] args) throws Exception{
		
		//Ask for dataset, learning rate, number of hidden layers, number of hidden neurons, number of training iterations, and how often the error should be printed
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the name of the file you would like to use?");
		String fileName = keyboard.next();
		System.out.println("Which activation function would you like to use?");
		String activation = keyboard.next();
		System.out.println("What rate of learning would you like to use?");
		float learningRate = keyboard.nextFloat();
		System.out.println("How many hidden layers would you like to have in your network?");
		int hiddenLayers = keyboard.nextInt();
		System.out.println("How many neurons would you like to have in each hidden layer of your network?");
		int hiddenNeurons = keyboard.nextInt();
		System.out.println("How many iterations would you like to use to train?");
		int trainTime = keyboard.nextInt();
		System.out.println("How often would you like to print the error? Please give your answer in iterations.");
		int printError = keyboard.nextInt();
		System.out.println("Computing...");
		
		//Copy values from CSV file into totalData array
		int columns = 0;
		int row = 0;
		String inLine = "";
		
		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName)));
		BufferedReader read = new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName));
		while(read.readLine() != null) {
			columns += 1;
		}
		
		float[] totalData[] = new float[columns - 1][reader.nextLine().split(",").length];
		while(reader.hasNextLine()) {
			inLine = reader.nextLine();
			String[] inArray = inLine.split(",");
			for(int x = 0;x < inArray.length;x++) {
				totalData[row][x] = Float.parseFloat(inArray[x]);
			}
			row++;
		}

		//Copy attributes from totalData to totalAttributes
		float[] totalAttributes[] = new float[totalData.length][totalData[0].length - 1];
		for(int i = 0;i < totalData.length;i++) {
			for(int j = 0;j < totalData[i].length - 1;j++) {
				totalAttributes[i][j] = totalData[i][j];
			}
		}

		//Initialize maximum and minimum values for weights and bias
		float max = (float).1;
		float min = (float)-.1;
		
		//Initialize array of weights and array of biases randomly
		float[][] weights[] = new float[hiddenLayers + 1][][];
		float[] biases[] = new float[hiddenLayers + 1][];
		int neurons = 0;
		int weightsLength = 0;
		int lastNeurons = 0;
		for(int i = 0;i < weights.length;i++) {
			if(i == hiddenLayers) {
				neurons = 1;
			} else {
				neurons = hiddenNeurons;
			}
			if(i != 0) {
				weightsLength = lastNeurons;
			} else {
				weightsLength = totalAttributes[0].length;
			}
			float[] currentWeights[] = new float[neurons][weightsLength];
			for(int j = 0;j < currentWeights.length;j++) {
				for(int k = 0;k < currentWeights[j].length;k++) {
					currentWeights[j][k] = min + (float) Math.random() * (max - min);
				}
			}
			weights[i] = currentWeights;
			lastNeurons = neurons;
		}
		for(int i = 0;i < biases.length;i++) {
			if(i == hiddenLayers) {
				float tempArray[] = new float[1];
				tempArray[0] = min + (float)Math.random() * (max - min);
				biases[i] = tempArray;
			} else {
				float tempArray[] = new float[hiddenNeurons];
				for(int j = 0;j < tempArray.length;j++) {
					tempArray[j] = min + (float)Math.random() * (max - min);
				}
				biases[i] = tempArray;
			}
		}
		
		//Initialize array of neuron values (excluding output neurons)
		float[] inputs[] = new float[hiddenLayers + 1][];
		for(int i = 0;i < inputs.length;i++) {
			if(i == 0) {
				lastNeurons = totalAttributes[i].length;
			} else {
				lastNeurons = hiddenNeurons;
			}
			float tempInputs[] = new float[lastNeurons];
			inputs[i] = tempInputs;
		}
		
		
		//Training
		for(int i = 0;i < trainTime;i++) {
			
			//Initialize total cost and derivatives
			float totalCost = 0;
			
			float[][] totalDCostDWeights[] = new float[weights.length][][];
			for(int j = 0;j < weights.length;j++) {
				float[] tempArray[] = new float[weights[j].length][inputs[j].length];
				totalDCostDWeights[j] = tempArray;
			}
			
			float[] totalDCostDBiases[] = new float[biases.length][];
			for(int j = 0;j < biases.length;j++) {
				float tempArray[] = new float[biases[j].length];
				totalDCostDBiases[j] = tempArray;
			}
			
			//Calculate cost and derivatives for each training example
			for(int j = 0;j < totalData.length;j++) {
				
				//Feed data forward through network
				for(int k = 0;k < hiddenLayers + 1;k++) {
					for(int l = 0;l < inputs[k].length;l++) {
						if(k == 0) {
							inputs[k][l] = totalAttributes[j][l];
						} else {
							inputs[k][l] = activate(calculate(inputs[k - 1],weights[k - 1][l],biases[k - 1][l]),activation);
						}
					}
				}
				
				//Calculate cost
				float target = totalData[j][totalData[j].length - 1];
				float output = calculate(inputs[inputs.length - 1],weights[weights.length - 1][0],biases[biases.length - 1][0]);
				float activatedOutput = activate(output,activation);
				float cost = (float) Math.pow(activatedOutput - target, 2);
				//Add cost to totalCost
				totalCost += cost;
				
				//Initialize and calculate arrays of derivatives
				//The derivative of the cost function
				float dCostDActivatedOutput = 2 * (activatedOutput - target);
				
				//The derivative of the sigmoid function
				float[] dActivatedOutputDOutput[] = new float[hiddenLayers + 1][];
				for(int k = 0;k < dActivatedOutputDOutput.length;k++) {
					if(k == 0) {
						neurons = 1;
					} else {
						neurons = hiddenNeurons;
					}
					float tempArray[] = new float[neurons];
					dActivatedOutputDOutput[k] = tempArray;
				}
				for(int k = 0;k < dActivatedOutputDOutput.length;k++) {
					for(int m = 0;m < dActivatedOutputDOutput[k].length;m++) {
						if(activation.equals("sigmoid")) {
							if(k == 0) {
								dActivatedOutputDOutput[0][0] = activate(output,activation) * (1 - activate(output,activation));
							} else {
								dActivatedOutputDOutput[k][m] = activate(inputs[inputs.length - k][m],activation) * (1 - activate(inputs[inputs.length - k][m],activation));
							}
						} else if(activation.equals("softplus")) {
							if(k == 0) {
								dActivatedOutputDOutput[0][0] = activate(output,"sigmoid");
							} else {
								dActivatedOutputDOutput[k][m] = activate(inputs[inputs.length - k][m],"sigmoid");
							}
						}
					}
				}
				
				//The derivative of the calculate function
				float[][] dOutputDWeights[] = new float[weights.length][][];
				for(int k = 0;k < weights.length;k++) {
					float[] tempArray[] = new float[weights[k].length][inputs[k].length];
					dOutputDWeights[k] = tempArray;
				}
				for(int k = 0;k < dOutputDWeights.length;k++) {
					for(int l = 0;l < dOutputDWeights[k].length;l++) {
						for(int m = 0;m < dOutputDWeights[k][l].length;m++) {
							dOutputDWeights[k][l][m] = inputs[k][m];
						}
					}
				}
				
				//The final derivative, calculated by using the chain rule and multiplying the separate derivatives together
				float[][] dCostDWeights[] = new float[weights.length][][];
				for(int k = 0;k < weights.length;k++) {
					float[] tempArray[] = new float[weights[k].length][inputs[k].length];
					dCostDWeights[k] = tempArray;
				}
				for(int k = 0;k < dCostDWeights.length;k++) {
					for(int l = 0;l < dCostDWeights[dCostDWeights.length - (k + 1)].length;l++) {
						for(int m = 0;m < dCostDWeights[dCostDWeights.length - (k + 1)][l].length;m++) {
							if(k == 0) {
								dCostDWeights[dCostDWeights.length - (k + 1)][l][m] = dCostDActivatedOutput * dActivatedOutputDOutput[0][0] * dOutputDWeights[dOutputDWeights.length - (k + 1)][l][m];
							} else {
								float derivativeSum = 0;
								for(int n = 0;n < dCostDWeights[dCostDWeights.length - k].length;n++) {
									derivativeSum+= dCostDWeights[dCostDWeights.length - k][n][l];
								}
								dCostDWeights[dCostDWeights.length - (k + 1)][l][m] = derivativeSum * dActivatedOutputDOutput[k][l] * dOutputDWeights[dOutputDWeights.length - (k + 1)][l][m];
							}
						}
					}
				}
				float[] dCostDBiases[] = new float[biases.length][];
				for(int k = 0;k < biases.length;k++) {
					float tempArray[] = new float[biases[k].length];
					dCostDBiases[k] = tempArray;
				}
				for(int k = 0;k < dCostDBiases.length;k++) {
					if(k == 0) {
						dCostDBiases[dCostDBiases.length - (k + 1)][0] = dCostDActivatedOutput * dActivatedOutputDOutput[0][0];
					} else {
						for(int l = 0;l < dCostDBiases[dCostDBiases.length - (k + 1)].length;l++) {
							float derivativeSum = 0;
							for(int m = 0;m < dCostDBiases[dCostDBiases.length - k].length;m++) {
								derivativeSum += dCostDBiases[dCostDBiases.length - k][m];
							}
							dCostDBiases[dCostDBiases.length - (k + 1)][l] = derivativeSum * dActivatedOutputDOutput[k][l];
						}
					}
				}
				
				//Add derivatives to total derivatives
				for(int k = 0;k < totalDCostDWeights.length;k++) {
					for(int l = 0;l < totalDCostDWeights[k].length;l++) {
						for(int m = 0;m < totalDCostDWeights[k][l].length;m++) {
							totalDCostDWeights[k][l][m] += dCostDWeights[k][l][m];
						}
					}
				}
				for(int k = 0;k < totalDCostDBiases.length;k++) {
					for(int l = 0;l < totalDCostDBiases[k].length;l++) {
						totalDCostDBiases[k][l] += dCostDBiases[k][l];
					}
				}
			}
			//Calculate average cost
			float averageCost = totalCost / totalData.length;
			
			//Calculate arrays of average derivatives
			float[][] averageDCostDWeights[] = new float[weights.length][][];
			for(int j = 0;j < weights.length;j++) {
				float[] tempArray[] = new float[weights[j].length][inputs[j].length];
				for(int k = 0;k < tempArray.length;k++) {
					for(int l = 0;l < tempArray[k].length;l++) {
						tempArray[k][l] = totalDCostDWeights[j][k][l] / totalData.length;
					}
				}
				averageDCostDWeights[j] = tempArray;
			}
			
			float[] averageDCostDBiases[] = new float[biases.length][];
			for(int j = 0;j < biases.length;j++) {
				float tempArray[] = new float[biases[j].length];
				for(int k = 0;k < tempArray.length;k++) {
					tempArray[k] = totalDCostDBiases[j][k] / totalData.length;
				}
				averageDCostDBiases[j] = tempArray;
			}
			
			//Subtract gradient from weights and biases
			for(int j = 0;j < weights.length;j++) {
				for(int k = 0;k < weights[j].length;k++) {
					for(int l = 0;l < weights[j][k].length;l++) {
						weights[j][k][l] -= learningRate * averageDCostDWeights[j][k][l];
					}
				}
			}
			
			for(int j = 0;j < biases.length;j++) {
				for(int k = 0;k < biases[j].length;k++) {
						biases[j][k] -= learningRate * averageDCostDBiases[j][k];
				}
			}

			if(i % printError == 0) {
				System.out.println(averageCost);
			}
		}
		
		//Clear console
		for (int i = 0; i < 50; ++i) {
			System.out.println();
		}
		
		//Print weights, biases, and ask for new attributes
		System.out.println("The weights are:");
		printWeights(weights);
		System.out.println("The biases are:");
		printBiases(biases);
		System.out.println("How many times would you like to test the network?");
		int testTime = keyboard.nextInt();
		for(int i = 0;i < testTime;i++) {
			System.out.println("What are the attributes?");
			float attributes[] = new float[totalAttributes[0].length];
			for(int j = 0;j < totalAttributes[0].length;j++) {
				attributes[j] = keyboard.nextFloat();
			}

			float newInputs[] = new float[hiddenNeurons];
			float lastInputs[] = new float[hiddenNeurons];
			float newOutput;
			if(hiddenLayers == 0 || hiddenNeurons == 0) {
				newOutput = activate(calculate(attributes,weights[0][0],biases[0][0]),activation);
			} else {
				for(int j = 0;j < hiddenLayers;j++) {
					if(j == 0) {
						for(int k = 0;k < hiddenNeurons;k++) {
							newInputs[k] = activate(calculate(attributes,weights[j][k],biases[j][k]),activation);
						}
					} else {
						for(int k = 0;k < hiddenNeurons;k++) {
							newInputs[k] = activate(calculate(lastInputs,weights[j][k],biases[j][k]),activation);
						}
					}
					for(int k = 0;k < newInputs.length;k++) {
						lastInputs[k] = newInputs[k];
					}
				}
				newOutput = activate(calculate(newInputs,weights[weights.length - 1][0],biases[biases.length - 1][0]),activation);
			}
			System.out.println("My prediction is " + step(newOutput) + ".");
			System.out.println("");
		}
		keyboard.close();
		reader.close();
		read.close();
	}

	//Mathematical functions
	public static float activate (float x,String function) {
		float result = 0;
		if(function.equals("sigmoid")) {
			result = (float) (1 / (1 + Math.exp(x * -1)));
		} else if(function.equals("abs")) {
			result = Math.abs(x);
		} else if(function.equals("softplus")) {
			result = (float) Math.log(1 + Math.exp(x));
		}
		return result;
	}
	public static float calculate (float[] n,float[] w,float b) {
		return dotProduct(n,w) + b;
	}
	public static int step (float x) {
		return (int) Math.round(x); 
	}
	public static float dotProduct (float[] x,float[] y) {
		float sum = 0;
		for(int i = 0;i < x.length;i++) {
			sum += x[i] * y[i];
		}
		return sum;
	}
	
	//Functions for printing weights and biases
	public static void printWeights (float[][][] x) {
		for(int i = 0;i < x.length;i++) {
			for(int j = 0;j < x[i].length;j++) {
				for(int k = 0;k < x[i][j].length;k++) {
					System.out.println(x[i][j][k]);
				}
				System.out.println("");
			}
			System.out.println("");
		}
	}
	public static void printBiases (float[][] x) {
		for(int i = 0;i < x.length;i++) {
			for(int j = 0;j < x[i].length;j++) {
				System.out.println(x[i][j]);
			}
			System.out.println("");
		}
	}
}

