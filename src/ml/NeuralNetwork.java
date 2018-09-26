package ml;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class NeuralNetwork{

	//Hyperparameters
	private int hiddenLayers;
	private int hiddenNeurons;
	private int inputNumber;
	private int outputNumber;
	String activation;
	
	//Training and testing data
	private float[] totalData[];
	private float[] totalAttributes[];
	private float[] testData[];
	private float[] testAttributes[];
	
	//Parameters
	private float[][][] weights;
	private float[][] biases;
	
	//Training cost
	private float averageCost;
	
	//Initializes variables that define the structure of the network
	public void setNetwork(int inputNumber,int outputNumber,int hiddenLayers,int hiddenNeurons,String activation) {
		this.inputNumber = inputNumber;
		this.outputNumber = outputNumber;
		this.hiddenLayers = hiddenLayers;
		this.hiddenNeurons = hiddenNeurons;
		this.activation = activation;
	}
	
	//Retrieves data from either a CSV file or an array
	public void getCSVData(String fileName) throws Exception {
		//Copy values from CSV file into totalData array
		int columns = 0;
		int row = 0;
		String inLine = "";

		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName)));
		BufferedReader read = new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName));
		while(read.readLine() != null) {
			columns += 1;
		}

		float[] totalData1[] = new float[columns - 1][reader.nextLine().split(",").length];
		while(reader.hasNextLine()) {
			inLine = reader.nextLine();
			String[] inArray = inLine.split(",");
			for(int x = 0;x < inArray.length;x++) {
				if(inArray[x].equals("?")) {
					totalData1[row][x] = -99999;
				} else {
					totalData1[row][x] = Float.parseFloat(inArray[x]);
				}
			}
			row++;
		}

		//Copy attributes from totalData to totalAttributes
		float[] totalAttributes1[] = new float[totalData1.length][totalData1[0].length - getOutputNumber()];
		for(int i = 0;i < totalData1.length;i++) {
			for(int j = 0;j < totalData1[i].length - getOutputNumber();j++) {
				totalAttributes1[i][j] = totalData1[i][j];
			}
		}
		totalData = totalData1;
		totalAttributes = totalAttributes1;
		reader.close();
		read.close();
	}
	public void getData(float[][] data) {

		float[] totalAttributes1[] = new float[data.length][data[0].length - getOutputNumber()];
		for(int i = 0;i < data.length;i++) {
			for(int j = 0;j < data[i].length - getOutputNumber();j++) {
				totalAttributes1[i][j] = data[i][j];
			}
		}
		totalData = data;
		totalAttributes = totalAttributes1;
	}
	public void getCSVTestData(String fileName) throws Exception {
		//Copy values from CSV file into totalData array
				int columns = 0;
				int row = 0;
				String inLine = "";

				Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName)));
				BufferedReader read = new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName));
				while(read.readLine() != null) {
					columns += 1;
				}

				float[] totalData1[] = new float[columns - 1][reader.nextLine().split(",").length];
				while(reader.hasNextLine()) {
					inLine = reader.nextLine();
					String[] inArray = inLine.split(",");
					for(int x = 0;x < inArray.length;x++) {
						if(inArray[x].equals("?")) {
							totalData1[row][x] = -99999;
						} else {
							totalData1[row][x] = Float.parseFloat(inArray[x]);
						}
					}
					row++;
				}
				testData = totalData1;
				reader.close();
				read.close();
	}
	public void getTestData(float[][] data) {
		float[] testAttributes1[] = new float[data.length][data[0].length - getOutputNumber()];
		for(int i = 0;i < data.length;i++) {
			for(int j = 0;j < data[i].length - getOutputNumber();j++) {
				testAttributes1[i][j] = data[i][j];
			}
		}
		testData = data;
		testAttributes = testAttributes1;
	}
	
	//Getters and setters
	public float[][] getTotalData() {
		return totalData;
	}
	public float[][] getTotalAttributes() {
		return totalAttributes;
	}
	public float[][] getTestingData() {
		return testData;
	}	
	public int getOutputNumber() {
		return outputNumber;
	}	
	public int getInputNumber() {
		return inputNumber;
	}
	public float[][][] getWeights() {
		return weights;
	}
	public float[][] getBiases() {
		return biases;
	}

	public void setData(int i, int j, float newData) {
		this.totalData[i][j] = newData;
	}
	public void setWeight(int i,int j,int k,float newWeight) {
		weights[i][j][k] = newWeight;
	}
	public void setBias(int i,int j,float newBias) {
		biases[i][j] = newBias;
	}

	//Initializes weights and biases randomly for training
	public void createNetwork() {
		
		float max = (float)10;
		float min = (float)-10;

		float[][] weights1[] = new float[hiddenLayers + 1][][];
		float[] biases1[] = new float[hiddenLayers + 1][];
		int neurons = 0;
		int weightsLength = 0;
		int lastNeurons = 0;
		for(int i = 0;i < weights1.length;i++) {
			if(i == hiddenLayers) {
				neurons = getOutputNumber();
			} else {
				neurons = hiddenNeurons;
			}
			if(i != 0) {
				weightsLength = lastNeurons;
			} else {
				weightsLength = inputNumber;
			}
			float[] currentWeights[] = new float[neurons][weightsLength];
			for(int j = 0;j < currentWeights.length;j++) {
				for(int k = 0;k < currentWeights[j].length;k++) {
					currentWeights[j][k] = min + (float) Math.random() * (max - min);
				}
			}
			weights1[i] = currentWeights;
			lastNeurons = neurons;
		}

		for(int i = 0;i < biases1.length;i++) {
			if(i == hiddenLayers) {
				float tempArray[] = new float[getOutputNumber()];
				for(int j = 0;j < tempArray.length;j++) {
					tempArray[j] = min + (float)Math.random() * (max - min);
				}
				biases1[i] = tempArray;
			} else {
				float tempArray[] = new float[hiddenNeurons];
				for(int j = 0;j < tempArray.length;j++) {
					tempArray[j] = min + (float)Math.random() * (max - min);
				}
				biases1[i] = tempArray;
			}
		}
		weights = weights1;
		biases = biases1;
	}
		
	//Retrieves weights and biases from text files
	public void setFileWeights(String fileName) throws IOException {
		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/" + fileName)));
		int layer = 0;
		int neuron = 0;
		int lastNeuron = 0;
		int line = 1;
		String currentLine = "";
		String lastLine = null;
		while(reader.hasNextLine()) {
			currentLine = reader.nextLine();
			if(line != 1 && currentLine.equals("")) {
				lastNeuron = 0;
				neuron += 1;
				if(lastLine.equals("")) {
					layer += 1;
					neuron = 0;
				}
			} else {
				weights[layer][neuron][lastNeuron] = Float.parseFloat(currentLine);
				lastNeuron++;
			}
			line++;
			lastLine = currentLine;
		}
		reader.close();
	}
	public void setFileBiases(String fileName) throws IOException {
		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/" + fileName)));
		int layer = 0;
		int neuron = 0;
		String currentLine = "";
		while(reader.hasNextLine()) {
			currentLine = reader.nextLine();
			if(currentLine.equals("")) {
				layer += 1;
				neuron = 0;
			} else {
				biases[layer][neuron] = Float.parseFloat(currentLine);
				neuron += 1;
			}
		}
		reader.close();
	}
	

	//Completes one epoch of gradient descent
	public void trainGD(float learningRate) {
		//Initialize array of input and hidden neurons called "inputs"
		int neurons;
		int lastNeurons;
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
		
		//Initialize arrays of total derivatives; e.g. totalDCostDWeights means the total derivative of the cost with respect to the weights
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

		//Iterate through all training examples
		for(int j = 0;j < totalData.length;j++) {
			
			//Feed data forward through the network to get values of input and hidden neurons
			for(int k = 0;k < hiddenLayers + 1;k++) {
				for(int l = 0;l < inputs[k].length;l++) {
					if(k == 0) {
						inputs[k][l] = totalAttributes[j][l];
					} else {
						inputs[k][l] = activate(calculate(inputs[k - 1],weights[k - 1][l],biases[k - 1][l]),activation);
					}
				}
			}

			//Compute cost
			float targets[] = new float[getOutputNumber()];
			for(int k = 0;k < targets.length;k++) {
				targets[targets.length - (k + 1)] = totalData[j][totalData[j].length - (k + 1)];
			}
			float outputs[] = new float[getOutputNumber()];
			for(int k = 0;k < outputs.length;k++) {
				outputs[k] = calculate(inputs[inputs.length - 1],weights[weights.length - 1][k],biases[biases.length - 1][k]);
			}
			float activatedOutputs[] = new float[getOutputNumber()];
			for(int k = 0;k < activatedOutputs.length;k++) {
				activatedOutputs[k] = activate(outputs[k],activation);
			}
			float cost = 0;
			for(int k = 0;k < getOutputNumber();k++) {
				cost += (float) Math.pow(activatedOutputs[k] - targets[k], 2);
			}

			//Initialize and compute arrays of derivatives
			//The derivative of the cost function
			float dCostDActivatedOutputs[] = new float[getOutputNumber()];
			for(int k = 0;k < dCostDActivatedOutputs.length;k++) {
				dCostDActivatedOutputs[k] = 2 * (activatedOutputs[k] - targets[k]);
			}

			//The derivative of the sigmoid function
			float[] dActivatedOutputsDOutputs[] = new float[hiddenLayers + 1][];
			for(int k = 0;k < dActivatedOutputsDOutputs.length;k++) {
				if(k == 0) {
					neurons = getOutputNumber();
				} else {
					neurons = hiddenNeurons;
				}
				float tempArray[] = new float[neurons];
				dActivatedOutputsDOutputs[k] = tempArray;
			}
			for(int k = 0;k < dActivatedOutputsDOutputs.length;k++) {
				for(int m = 0;m < dActivatedOutputsDOutputs[k].length;m++) {
					if(activation.equals("sigmoid")) {
						if(k == 0) {
							dActivatedOutputsDOutputs[k][m] = activate(outputs[m],activation) * (1 - activate(outputs[m],activation));
						} else {
							dActivatedOutputsDOutputs[k][m] = activate(inputs[inputs.length - k][m],activation) * (1 - activate(inputs[inputs.length - k][m],activation));
						}
					} else if(activation.equals("softplus")) {
						if(k == 0) {
							dActivatedOutputsDOutputs[0][0] = activate(outputs[m],"sigmoid");
						} else {
							dActivatedOutputsDOutputs[k][m] = activate(inputs[inputs.length - k][m],"sigmoid");
						}
					} else if(activation.equals("softmax")) {
						if(k == 0) {
							dActivatedOutputsDOutputs[k][m] = activatedOutputs[m] - totalData[j][m + getOutputNumber()];
						} else if(k < dActivatedOutputsDOutputs.length) {
							dActivatedOutputsDOutputs[k][m] = activate(inputs[inputs.length - k][m],activation) * (1 - activate(inputs[inputs.length - k][m],activation));
						} else {
							dActivatedOutputsDOutputs[k][m] = activate(outputs[m],activation) * (1 - activate(outputs[m],activation));
						}
					}
				}
			}

			//The derivative of the calculate function
			float[][] dOutputsDWeights[] = new float[weights.length][][];
			for(int k = 0;k < weights.length;k++) {
				float[] tempArray[] = new float[weights[k].length][inputs[k].length];
				dOutputsDWeights[k] = tempArray;
			}
			for(int k = 0;k < dOutputsDWeights.length;k++) {
				for(int l = 0;l < dOutputsDWeights[k].length;l++) {
					for(int m = 0;m < dOutputsDWeights[k][l].length;m++) {
						dOutputsDWeights[k][l][m] = inputs[k][m];
					}
				}
			}

			float[][] dCostDWeights[] = new float[weights.length][][];
			for(int k = 0;k < weights.length;k++) {
				float[] tempArray[] = new float[weights[k].length][inputs[k].length];
				dCostDWeights[k] = tempArray;
			}
			//Backpropagation for weights and biases
			for(int k = 0;k < dCostDWeights.length;k++) {
				for(int l = 0;l < dCostDWeights[dCostDWeights.length - (k + 1)].length;l++) {
					for(int m = 0;m < dCostDWeights[dCostDWeights.length - (k + 1)][l].length;m++) {
						if(k == 0) {
							dCostDWeights[dCostDWeights.length - (k + 1)][l][m] = dCostDActivatedOutputs[l] * dActivatedOutputsDOutputs[k][l] * dOutputsDWeights[dOutputsDWeights.length - (k + 1)][l][m];
						} else {
							//Add up the costs for the different possible ways each weight can affect the output
							float derivativeSum = 0;
							for(int n = 0;n < dCostDWeights[dCostDWeights.length - k].length;n++) {
								derivativeSum+= dCostDWeights[dCostDWeights.length - k][n][l];
							}
							dCostDWeights[dCostDWeights.length - (k + 1)][l][m] = derivativeSum * dActivatedOutputsDOutputs[k][l] * dOutputsDWeights[dOutputsDWeights.length - (k + 1)][l][m];
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
					for(int l = 0;l < dCostDBiases[dCostDBiases.length - (k + 1)].length;l++) {
						dCostDBiases[dCostDBiases.length - (k + 1)][l] = dCostDActivatedOutputs[l] * dActivatedOutputsDOutputs[k][l];
					}
				} else {
					for(int l = 0;l < dCostDBiases[dCostDBiases.length - (k + 1)].length;l++) {
						//Add up the costs for the different possible ways each bias can affect the output
						float derivativeSum = 0;
						for(int m = 0;m < dCostDBiases[dCostDBiases.length - k].length;m++) {
							derivativeSum += dCostDBiases[dCostDBiases.length - k][m];
						}
						dCostDBiases[dCostDBiases.length - (k + 1)][l] = derivativeSum * dActivatedOutputsDOutputs[k][l];
					}
				}
			}
			
			//Add cost and derivatives to totals
			totalCost += cost;
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
		averageCost = totalCost / totalData.length;

		//Initialize and calculate arrays of average derivatives
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

		//Subtract a fraction of the gradient from the weights and biases
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
	}
	//Completes one epoch of Stochastic gradient descent
	public void trainSGD(float learningRate,int batchSize) {
		//Randomize training examples
		Random rnd = ThreadLocalRandom.current();
	    for (int i = totalData.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      float[] a = totalData[index];
	      totalData[index] = totalData[i];
	      totalData[i] = a;
	    }
	    float[] totalAttributes1[] = new float[totalData.length][totalData[0].length - getOutputNumber()];
		for(int i = 0;i < totalData.length;i++) {
			for(int j = 0;j < totalData[i].length - getOutputNumber();j++) {
				totalAttributes1[i][j] = totalData[i][j];
			}
		}
	    totalAttributes = totalAttributes1;

		//Organize data into batches for Stochastic gradient descent
		float[][] totalDataBatches[];
		if(totalData.length % batchSize == 0) {
			totalDataBatches = new float[(int)(totalData.length / batchSize)][][];
		} else {
			totalDataBatches = new float[(int)(totalData.length / batchSize) + 1][][];
		}
		int totalDataExamples = 0;
		for(int i = 0;i < totalDataBatches.length;i++) {
			int examples = 0;
			if(i != totalDataBatches.length - 1) {
				examples = batchSize;
			} else if(totalData.length % batchSize == 0) {
				examples = batchSize;
			} else {
				examples = totalData.length % batchSize;
			}
			float[][] currentExamples = new float[examples][];
			for(int j = 0;j < examples;j++) {
				currentExamples[j] = totalData[totalDataExamples];
				totalDataExamples++;
			}
			totalDataBatches[i] = currentExamples;
		}
		float[][] totalAttributesBatches[] = new float[(int)(totalData.length / batchSize) + 1][][];
		int totalAttributesExamples = 0;
		for(int i = 0;i < totalAttributesBatches.length;i++) {
			int examples = 0;
			if(i != totalAttributesBatches.length - 1) {
				examples = batchSize;
			} else {
				examples = totalAttributes.length % batchSize;
			}
			float[][] currentExamples = new float[examples][];
			for(int j = 0;j < examples;j++) {
				currentExamples[j] = totalAttributes[totalAttributesExamples];
				totalAttributesExamples++;
			}
			totalAttributesBatches[i] = currentExamples;
		}

		//Initialize array of input and hidden neurons called "inputs"
		int neurons;
		int lastNeurons;
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

		//Iterate through all of the batches
		for(int j = 0;j < 1;j++) {
			
				//Initialize arrays of total derivatives; e.g. totalDCostDWeights means the total derivative of the cost with respect to the weights
				float totalCost = 0;
				float[][] totalDCostDWeights[] = new float[weights.length][][];
				for(int k = 0;k < weights.length;k++) {
					float[] tempArray[] = new float[weights[k].length][inputs[k].length];
					totalDCostDWeights[k] = tempArray;
				}

				float[] totalDCostDBiases[] = new float[biases.length][];
				for(int k = 0;k < biases.length;k++) {
					float tempArray[] = new float[biases[k].length];
					totalDCostDBiases[k] = tempArray;
				}

				//Iterate through all training examples in the current batch
				for(int k = 0;k < totalDataBatches[j].length;k++) {
					//Feed data forward
					for(int l = 0;l < hiddenLayers + 1;l++) {
						for(int m = 0;m < inputs[l].length;m++) {
							if(l == 0) {
								inputs[l][m] = totalAttributesBatches[j][k][m];
							} else if(!activation.equals("softmax")){
								inputs[l][m] = activate(calculate(inputs[l - 1],weights[l - 1][m],biases[l - 1][m]),activation);
							} else {
								inputs[l][m] = calculate(inputs[l - 1],weights[l - 1][m],biases[l - 1][m]);
							}
						}
					}
					if(activation.equals("softmax")) {
						inputs[inputs.length - 1] = softmax(inputs[inputs.length - 1]);
					}

					//Compute cost
					float targets[] = new float[getOutputNumber()];
					for(int l = 0;l < targets.length;l++) {
						targets[targets.length - (l + 1)] = totalDataBatches[j][k][totalDataBatches[j][k].length - (l + 1)];
					}
					float outputs[] = new float[getOutputNumber()];
					for(int l = 0;l < outputs.length;l++) {
						outputs[l] = calculate(inputs[inputs.length - 1],weights[weights.length - 1][l],biases[biases.length - 1][l]);
					}
					float activatedOutputs[] = new float[getOutputNumber()];
					for(int l = 0;l < activatedOutputs.length;l++) {
						activatedOutputs[l] = activate(outputs[l],activation);
					}
					float cost = 0;
					for(int l = 0;l < getOutputNumber();l++) {
						cost += (float)Math.pow(activatedOutputs[l] - targets[l], 2) / getOutputNumber();
					}

					//Initialize and compute arrays of derivatives
					//The derivative of the cost function
					float dCostDActivatedOutputs[] = new float[getOutputNumber()];
					for(int l = 0;l < dCostDActivatedOutputs.length;l++) {
						dCostDActivatedOutputs[l] = 2 * (activatedOutputs[l] - targets[l]);
					}

					//The derivative of the activation function
					float[] dActivatedOutputsDOutputs[] = new float[hiddenLayers + 1][];
					for(int l = 0;l < dActivatedOutputsDOutputs.length;l++) {
						if(l == 0) {
							neurons = getOutputNumber();
						} else {
							neurons = hiddenNeurons;
						}
						float tempArray[] = new float[neurons];
						dActivatedOutputsDOutputs[l] = tempArray;
					}
					for(int l = 0;l < dActivatedOutputsDOutputs.length;l++) {
						for(int m = 0;m < dActivatedOutputsDOutputs[l].length;m++) {
							if(activation.equals("sigmoid")) {
								if(l == 0) {
									dActivatedOutputsDOutputs[l][m] = activate(outputs[m],activation) * (1 - activate(outputs[m],activation));
								} else {
									dActivatedOutputsDOutputs[l][m] = activate(inputs[inputs.length - l][m],activation) * (1 - activate(inputs[inputs.length - l][m],activation));
								}
							} else if(activation.equals("softplus")) {
								if(l == 0) {
									dActivatedOutputsDOutputs[0][0] = activate(outputs[m],"sigmoid");
								} else {
									dActivatedOutputsDOutputs[l][m] = activate(inputs[inputs.length - l][m],"sigmoid");
								}
							} else if(activation.equals("softmax")) {
								if(l == 0) {
									dActivatedOutputsDOutputs[l][m] = activatedOutputs[m] - totalDataBatches[j][k][m + getInputNumber()];
								} else if(l < dActivatedOutputsDOutputs.length) {
									dActivatedOutputsDOutputs[l][m] = activate(inputs[inputs.length - l][m],activation) * (1 - activate(inputs[inputs.length - l][m],activation));
								} else {
									dActivatedOutputsDOutputs[l][m] = activate(outputs[m],activation) * (1 - activate(outputs[m],activation));
								}
							}
						}
					}

					//The derivative of the calculate function
					float[][] dOutputsDWeights[] = new float[weights.length][][];
					for(int l = 0;l < weights.length;l++) {
						float[] tempArray[] = new float[weights[l].length][inputs[l].length];
						dOutputsDWeights[l] = tempArray;
					}
					for(int l = 0;l < dOutputsDWeights.length;l++) {
						for(int m = 0;m < dOutputsDWeights[l].length;m++) {
							for(int n = 0;n < dOutputsDWeights[l][m].length;n++) {
								dOutputsDWeights[l][m][n] = inputs[l][n];
							}
						}
					}

					//The derivative of the cost function with respect to the weights and biases uses the chain rule by multiplying the derivatives of each of the functions being applied to the weights and biases
					//For example, the derivative of cost(sigmoid(calculate(weight))) is dCost * dSigmoid * dCalculate 
					float[][] dCostDWeights[] = new float[weights.length][][];
					for(int l = 0;l < weights.length;l++) {
						float[] tempArray[] = new float[weights[l].length][inputs[l].length];
						dCostDWeights[l] = tempArray;
					}
					for(int l = 0;l < dCostDWeights.length;l++) {
						for(int m = 0;m < dCostDWeights[dCostDWeights.length - (l + 1)].length;m++) {
							for(int n = 0;n < dCostDWeights[dCostDWeights.length - (l + 1)][m].length;n++) {
								if(l == 0) {
									dCostDWeights[dCostDWeights.length - (l + 1)][m][n] = dCostDActivatedOutputs[m] * dActivatedOutputsDOutputs[l][m] * dOutputsDWeights[dOutputsDWeights.length - (l + 1)][m][n];
								} else {
									//Add up the costs for the different possible ways each weight can affect the output
									float derivativeSum = 0;
									for(int o = 0;o < dCostDWeights[dCostDWeights.length - l].length;o++) {
										derivativeSum+= dCostDWeights[dCostDWeights.length - l][o][m];
									}
									//The derivative of a weight in a hidden layer also uses the chain rule; it gets multiplied by the derivative of the weight connected to it in the next layer
									dCostDWeights[dCostDWeights.length - (l + 1)][m][n] = derivativeSum * dActivatedOutputsDOutputs[l][m] * dOutputsDWeights[dOutputsDWeights.length - (l + 1)][m][n];
								}
							}
						}
					}
					
					float[] dCostDBiases[] = new float[biases.length][];
					for(int l = 0;l < biases.length;l++) {
						float tempArray[] = new float[biases[l].length];
						dCostDBiases[l] = tempArray;
					}
					for(int l = 0;l < dCostDBiases.length;l++) {
						if(l == 0) {
							for(int m = 0;m < dCostDBiases[dCostDBiases.length - (l + 1)].length;m++) {
								dCostDBiases[dCostDBiases.length - (l + 1)][m] = dCostDActivatedOutputs[m] * dActivatedOutputsDOutputs[l][m];
							}
						} else {
							for(int m = 0;m < dCostDBiases[dCostDBiases.length - (l + 1)].length;m++) {
								float derivativeSum = 0;
								//Add up the costs for the different possible ways each bias can affect the output
								for(int n = 0;n < dCostDBiases[dCostDBiases.length - l].length;n++) {
									derivativeSum += dCostDBiases[dCostDBiases.length - l][n];
								}
								dCostDBiases[dCostDBiases.length - (l + 1)][m] = derivativeSum * dActivatedOutputsDOutputs[l][m];
							}
						}
					}

					//Add cost and derivatives to totals
					totalCost += cost;
					for(int l = 0;l < totalDCostDWeights.length;l++) {
						for(int m = 0;m < totalDCostDWeights[l].length;m++) {
							for(int n = 0;n < totalDCostDWeights[l][m].length;n++) {
								totalDCostDWeights[l][m][n] += dCostDWeights[l][m][n];
							}
						}
					}
					for(int l = 0;l < totalDCostDBiases.length;l++) {
						for(int m = 0;m < totalDCostDBiases[l].length;m++) {
							totalDCostDBiases[l][m] += dCostDBiases[l][m];
						}
					}
				}
				averageCost = totalCost / totalDataBatches[j].length;
				
				//Initialize and compute arrays of average derivatives
				float[][] averageDCostDWeights[] = new float[weights.length][][];
				for(int k = 0;k < weights.length;k++) {
					float[] tempArray[] = new float[weights[k].length][inputs[k].length];
					for(int l = 0;l < tempArray.length;l++) {
						for(int m = 0;m < tempArray[l].length;m++) {
							tempArray[l][m] = totalDCostDWeights[k][l][m] / totalDataBatches[j].length;
						}
					}
					averageDCostDWeights[k] = tempArray;
				}

				float[] averageDCostDBiases[] = new float[biases.length][];
				for(int k = 0;k < biases.length;k++) {
					float tempArray[] = new float[biases[k].length];
					for(int l = 0;l < tempArray.length;l++) {
						tempArray[l] = totalDCostDBiases[k][l] / totalDataBatches[j].length;
					}
					averageDCostDBiases[k] = tempArray;
				}
				
				//Subtract a fraction of the gradient from the weights and biases
				for(int k = 0;k < weights.length;k++) {
					for(int l = 0;l < weights[k].length;l++) {
						for(int m = 0;m < weights[k][l].length;m++) {
							weights[k][l][m] -= learningRate * averageDCostDWeights[k][l][m];
						}
					}
				}
				
				for(int k = 0;k < biases.length;k++) {
					for(int l = 0;l < biases[k].length;l++) {
						biases[k][l] -= learningRate * averageDCostDBiases[k][l];
					}
				}
		}
	}

	//Returns total cost for test data
	public float calculateCost() {
		int lastNeurons;
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
		float totalCost = 0;
		for(int i = 0;i < testData.length;i++) {
			//Feed data forward
			for(int j = 0;j < inputs.length;j++) {
				for(int k = 0;k < inputs[j].length;k++) {
					if(j == 0) {
						inputs[j][k] = testAttributes[i][k];
					} else {
						inputs[j][k] = activate(calculate(inputs[j - 1],weights[j - 1][k],biases[j - 1][k]),activation);
					}
				}
			}

			float targets[] = new float[getOutputNumber()];
			for(int j = 0;j < targets.length;j++) {
				targets[targets.length - (j + 1)] = testData[i][testData[i].length - (j + 1)];
			}
			float outputs[] = new float[getOutputNumber()];
			for(int j = 0;j < outputs.length;j++) {
				outputs[j] = calculate(inputs[inputs.length - 1],weights[weights.length - 1][j],biases[biases.length - 1][j]);
			}
			float activatedOutputs[] = new float[getOutputNumber()];
			for(int j = 0;j < activatedOutputs.length;j++) {
				activatedOutputs[j] = activate(outputs[j],activation);
			}
			boolean correct = true;
			for(int j = 0;j < getOutputNumber();j++) {
				float finalActivatedOutput = (float)Math.rint(activatedOutputs[j]);
				if(finalActivatedOutput != targets[j]) {
					correct = false;
				}
			}
			if(correct) {
				totalCost += 1;
			}
		}
		return totalCost;
	
	}
	
	public float getCost() {
		return averageCost;
	}
	
	//Returns an array of outputs calculated by the network
	public float[] test(float[] attributes) {
		float newInputs[] = new float[hiddenNeurons];
		float lastInputs[] = new float[hiddenNeurons];
		float newOutputs[] = new float[getOutputNumber()];
		if(hiddenLayers == 0 || hiddenNeurons == 0) {
			if(!activation.equals("softmax")) {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = activate(calculate(attributes,weights[0][i],biases[0][i]),activation);
				}
			} else {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = calculate(attributes,weights[0][i],biases[0][i]);
				}
				newOutputs = softmax(newOutputs);
			}
		} else {
			for(int i = 0;i < hiddenLayers;i++) {
				if(i == 0) {
					for(int j = 0;j < hiddenNeurons;j++) {
						newInputs[j] = activate(calculate(attributes,weights[i][j],biases[i][j]),activation);
					}
				} else {
					for(int j = 0;j < hiddenNeurons;j++) {
						newInputs[j] = activate(calculate(lastInputs,weights[i][j],biases[i][j]),activation);
					}
				}
				for(int j = 0;j < newInputs.length;j++) {
					lastInputs[j] = newInputs[j];
				}
			}
			if(!activation.equals("softmax")) {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = activate(calculate(newInputs,weights[weights.length - 1][i],biases[biases.length - 1][i]),activation);
				}
			} else {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = calculate(newInputs,weights[weights.length - 1][i],biases[biases.length - 1][i]);
				}
				newOutputs = softmax(newOutputs);
			}
		}
		return newOutputs;
	}

	//Mathematical functions
	public static float activate (float x,String function) {
		float result = 0;
		if(function.equals("sigmoid") || function.equals("softmax")) {
			result = (float) (1 / (1 + Math.exp(x * -1)));
		} else if(function.equals("abs")) {
			result = Math.abs(x);
		} else if(function.equals("softplus")) {
			result = (float) Math.log(1 + Math.exp(x));
		}
		return result;
	}
	public static float[] softmax (float[] x) {
		float sum = 0;
		for(int i = 0;i < x.length;i++) {
			sum += Math.exp(x[i]);
		}
		float[] activations = new float[x.length];
		for(int i = 0;i < activations.length;i++) {
			activations[i] = (float)(Math.exp(x[i]) / sum);
		}
		return activations;
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

	//Prints weights and biases
	public void printWeights() {
		for(int i = 0;i < weights.length;i++) {
			for(int j = 0;j < weights[i].length;j++) {
				for(int k = 0;k < weights[i][j].length;k++) {
					System.out.println(weights[i][j][k]);
				}
				System.out.println("");
			}
			System.out.println("");
		}
	}
	public void printBiases() {
		for(int i = 0;i < biases.length;i++) {
			for(int j = 0;j < biases[i].length;j++) {
				System.out.println(biases[i][j]);
			}
			System.out.println("");
		}
	}
	public void printParameters() {
		System.out.println("Weights:");
		printWeights();
		System.out.println("Biases:");
		printBiases();
	}
}

