package ml;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class MLP2 implements Serializable{

	private static final long serialVersionUID = 1L;
	//Hyperparameters
	String activation;
	String loss;
	
	//Neurons
	private double[][] neurons;
	private int[] neuronNumbers;
	
	//Training and testing data
	private double[] totalData[];
	private double[] totalAttributes[];
	private double[] testData[];
	private double[] testAttributes[];
	
	//Parameters
	private double[][][] weights;
	private double[][] biases;
	
	//Training cost
	private double averageCost;
	
	//Initializes variables that define the structure of the network
	public MLP2(int[] neuronNumbers,String activation,String loss) {
		this.neuronNumbers = neuronNumbers;
		neurons = new double[neuronNumbers.length][];
		for(int i = 0;i < neurons.length;i++) {
			neurons[i] = new double[neuronNumbers[i]];
		}
		this.activation = activation;
		this.loss = loss;
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

		double[] totalData1[] = new double[columns - 1][reader.nextLine().split(",").length];
		while(reader.hasNextLine()) {
			inLine = reader.nextLine();
			String[] inArray = inLine.split(",");
			for(int x = 0;x < inArray.length;x++) {
				if(inArray[x].equals("?")) {
					totalData1[row][x] = -99999;
				} else {
					totalData1[row][x] = Double.parseDouble(inArray[x]);
				}
			}
			row++;
		}

		//Copy attributes from totalData to totalAttributes
		double[] totalAttributes1[] = new double[totalData1.length][totalData1[0].length - neuronNumbers[neuronNumbers.length - 1]];
		for(int i = 0;i < totalData1.length;i++) {
			for(int j = 0;j < totalData1[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
				totalAttributes1[i][j] = totalData1[i][j];
			}
		}
		totalData = totalData1;
		totalAttributes = totalAttributes1;
		reader.close();
		read.close();
	}
	public void getData(double[][] data) {

		double[] totalAttributes1[] = new double[data.length][data[0].length - neuronNumbers[neuronNumbers.length - 1]];
		for(int i = 0;i < data.length;i++) {
			for(int j = 0;j < data[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
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

		double[] totalData1[] = new double[columns - 1][reader.nextLine().split(",").length];
		while(reader.hasNextLine()) {
			inLine = reader.nextLine();
			String[] inArray = inLine.split(",");
			for(int x = 0;x < inArray.length;x++) {
				if(inArray[x].equals("?")) {
					totalData1[row][x] = -99999;
				} else {
					totalData1[row][x] = Double.parseDouble(inArray[x]);
				}
			}
			row++;
		}
		testData = totalData1;
		reader.close();
		read.close();
	}
	public void getTestData(double[][] data) {
		double[] testAttributes1[] = new double[data.length][data[0].length - neuronNumbers[neuronNumbers.length - 1]];
		for(int i = 0;i < data.length;i++) {
			for(int j = 0;j < data[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
				testAttributes1[i][j] = data[i][j];
			}
		}
		testData = data;
		testAttributes = testAttributes1;
	}
	public double[][] toOneHot(String fileName,int dataLength) throws Exception {
		double[] trainingData[] = new double[dataLength][neuronNumbers[0] + neuronNumbers[neuronNumbers.length - 1]];
		Scanner train = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName)));
		train.nextLine();
		for(int i = 0;i < trainingData.length;i++) {
			String stringValues[] = train.nextLine().split(",");
			double doubleValues[] = new double[stringValues.length];
			for(int j = 0;j < doubleValues.length;j++) {
				doubleValues[j] = Double.parseDouble(stringValues[j]);
			}
			double oneHot[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			int index = (int)doubleValues[0];
			oneHot[index] = 1;
			for(int j = 0;j < neuronNumbers[0];j++) {
				trainingData[i][j] = doubleValues[j + 1];
			}
			for(int j = 0;j < neuronNumbers[neuronNumbers.length - 1];j++) {
				trainingData[i][j + neuronNumbers[0]] = oneHot[j];
			}
		}
		train.close();
		return trainingData;
	}
	
	//Normalizes data
	public double[][] normalize(double[][] x,double range) {
		double[] output[] = new double[x.length][x[0].length];
		for(int i = 0;i < output.length;i++) {
			for(int j = 0;j < output[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
				output[i][j] = x[i][j] / range;
			}
			for(int j = output[i].length - neuronNumbers[neuronNumbers.length - 1];j < output[i].length;j++) {
				output[i][j] = x[i][j];
			}
		}
		return output;
	}
	
	//Getters and setters
	public double[][] getTotalData() {
		return totalData;
	}
	public double[][] getTotalAttributes() {
		return totalAttributes;
	}
	public double[][] getTestingData() {
		return testData;
	}
	public double[][][] getWeights() {
		return weights;
	}
	public double[][] getBiases() {
		return biases;
	}

	public void setData(int i, int j, double newData) {
		this.totalData[i][j] = newData;
	}
	public void setWeight(int i,int j,int k,double newWeight) {
		weights[i][j][k] = newWeight;
	}
	public void setBias(int i,int j,double newBias) {
		biases[i][j] = newBias;
	}

	//Initializes weights and biases randomly for training
	public void createNetwork() {
		
		double max = (double).1;
		double min = (double)-.1;

		double[][] weights1[] = new double[neuronNumbers.length - 1][][];
		double[] biases1[] = new double[neuronNumbers.length - 1][];
		
		for(int i = 0;i < weights1.length;i++) {
			double[] currentWeights[] = new double[neuronNumbers[i + 1]][neuronNumbers[i]];
			for(int j = 0;j < currentWeights.length;j++) {
				for(int k = 0;k < currentWeights[j].length;k++) {
					currentWeights[j][k] = min + (double) Math.random() * (max - min);
				}
			}
			weights1[i] = currentWeights;
		}

		for(int i = 0;i < biases1.length;i++) {
			double tempArray[] = new double[neuronNumbers[i + 1]];
			for(int j = 0;j < tempArray.length;j++) {
				tempArray[j] = min + (double)Math.random() * (max - min);
			}
			biases1[i] = tempArray;
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
				weights[layer][neuron][lastNeuron] = Double.parseDouble(currentLine);
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
				biases[layer][neuron] = Double.parseDouble(currentLine);
				neuron += 1;
			}
		}
		reader.close();
	}
	
	//Completes one iteration of Stochastic gradient descent
	public void trainSGD(double learningRate,int batchSize) {
		for(int i = 0;i < weights.length;i++) {
			for(int j = 0;j < weights[i].length;j++) {
				for(int k = 0;k < weights[i][j].length;k++) {
					if(Double.isNaN(weights[i][j][k])) {
						System.out.println("NaN Weight" + "i: " + i + "j: " + j + "k: " + k);
					}
				}
			}
		}
		//Randomize training examples
		Random rnd = ThreadLocalRandom.current();
	    for(int i = totalData.length - 1; i > 0; i--) {
	      int index = rnd.nextInt(i + 1);
	      double[] a = totalData[index];
	      totalData[index] = totalData[i];
	      totalData[i] = a;
	    }
	    double[] totalAttributes1[] = new double[totalData.length][totalData[0].length - neuronNumbers[neuronNumbers.length - 1]];
		for(int i = 0;i < totalData.length;i++) {
			for(int j = 0;j < totalData[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
				totalAttributes1[i][j] = totalData[i][j];
			}
		}
	    totalAttributes = totalAttributes1;

		//Organize data into batches for Stochastic gradient descent
		double[] totalDataBatch[] = new double[batchSize][];
		for(int i = 0;i < batchSize;i++) {
			totalDataBatch[i] = totalData[i];
		}
		double[] totalAttributesBatch[] = new double[batchSize][];
	    double[] totalAttributesBatch1[] = new double[batchSize][neuronNumbers[0]];
		for(int i = 0;i < totalDataBatch.length;i++) {
			for(int j = 0;j < totalDataBatch[i].length - neuronNumbers[neuronNumbers.length - 1];j++) {
				totalAttributesBatch1[i][j] = totalData[i][j];
			}
		}
	    totalAttributesBatch = totalAttributesBatch1;
			
		//Initialize arrays of total derivatives; e.g. totalDCostDWeights means the total derivative of the cost with respect to the weights
		double totalCost = 0;
		double[][] totalDCostDWeights[] = new double[weights.length][][];
		for(int k = 0;k < weights.length;k++) {
			double[] tempArray[] = new double[weights[k].length][neurons[k].length];
			totalDCostDWeights[k] = tempArray;
		}

		double[] totalDCostDBiases[] = new double[biases.length][];
		for(int k = 0;k < biases.length;k++) {
			double tempArray[] = new double[biases[k].length];
			totalDCostDBiases[k] = tempArray;
		}

		//Iterate through all training examples in the batch
		for(int k = 0;k < totalDataBatch.length;k++) {
			//Feed data forward
			for(int l = 0;l < neurons.length - 1;l++) {
				for(int m = 0;m < neurons[l].length;m++) {
					if(l == 0) {
						neurons[l][m] = totalAttributesBatch[k][m];
					} else if(!activation.equals("softmax")){
						neurons[l][m] = activate(calculate(neurons[l - 1],weights[l - 1][m],biases[l - 1][m]),activation);
					} else {
						neurons[l][m] = calculate(neurons[l - 1],weights[l - 1][m],biases[l - 1][m]);
					}
				}
			}

			//Compute cost
			double targets[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			for(int l = 0;l < targets.length;l++) {
				targets[targets.length - (l + 1)] = totalDataBatch[k][totalDataBatch[k].length - (l + 1)];
			}
			for(int l = 0;l < neurons[neurons.length - 1].length;l++) {
				neurons[neurons.length - 1][l] = calculate(neurons[neurons.length - 2],weights[weights.length - 1][l],biases[biases.length - 1][l]);
			}
			double activatedOutputs[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			if(!activation.equals("softmax")) {
				for(int l = 0;l < activatedOutputs.length;l++) {
					activatedOutputs[l] = activate(neurons[neurons.length - 1][l],activation);
				}
			} else {
				activatedOutputs = softmax(neurons[neurons.length - 1]);
			}
			double cost = 0;
			for(int l = 0;l < neuronNumbers[neuronNumbers.length - 1];l++) {
				cost += loss(activatedOutputs[l],targets[l],loss);
			}
			if(loss.equals("CE")) {
				cost *= -1;
			}
			
			//Initialize and compute arrays of derivatives
			//The derivative of the cost function
			double dCostDActivatedOutputs[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			for(int l = 0;l < dCostDActivatedOutputs.length;l++) {
				if(loss.equals("MSE")) {
					dCostDActivatedOutputs[l] = 2 * (activatedOutputs[l] - targets[l]);
				} else if(loss.equals("CE")) {
					dCostDActivatedOutputs[l] = activatedOutputs[l] - targets[l];
				}
			}

			//The derivative of the activation function
			double[] dActivatedOutputsDOutputs[] = new double[neurons.length - 1][];
			for(int l = 0;l < dActivatedOutputsDOutputs.length;l++) {
				double tempArray[] = new double[neuronNumbers[neuronNumbers.length - (l + 1)]];
				dActivatedOutputsDOutputs[l] = tempArray;
			}
			for(int l = 0;l < dActivatedOutputsDOutputs.length;l++) {
				for(int m = 0;m < dActivatedOutputsDOutputs[l].length;m++) {
					if(activation.equals("sigmoid")) {
						if(l == 0) {
							dActivatedOutputsDOutputs[l][m] = activate(neurons[neurons.length - 1][m],activation) * (1 - activate(neurons[neurons.length - 1][m],activation));
						} else {
							dActivatedOutputsDOutputs[l][m] = activate(neurons[neurons.length - (l + 1)][m],activation) * (1 - activate(neurons[neurons.length - (l + 1)][m],activation));
						}
					} else if(activation.equals("softplus")) {
						if(l == 0) {
							dActivatedOutputsDOutputs[0][0] = activate(neurons[neurons.length - 1][m],"sigmoid");
						} else {
							dActivatedOutputsDOutputs[l][m] = activate(neurons[neurons.length - (l + 1)][m],"sigmoid");
						}
					} else if(activation.equals("softmax")) {
						if(l == 0) {
							dActivatedOutputsDOutputs[l][m] = 1;//activatedOutputs[m] - totalDataBatch[k][m + neuronNumbers[0]];
						} else if(l < dActivatedOutputsDOutputs.length) {
							dActivatedOutputsDOutputs[l][m] = activate(neurons[neurons.length - (l + 1)][m],activation) * (1 - activate(neurons[neurons.length - (l + 1)][m],activation));
						} else {
							dActivatedOutputsDOutputs[l][m] = activate(neurons[neurons.length - 1][m],activation) * (1 - activate(neurons[neurons.length - 1][m],activation));
						}
					} else if(activation.equals("ReLU")) {
						if(neurons[neurons.length - (l + 1)][m] > 0) {
							dActivatedOutputsDOutputs[l][m] = 1;
							System.out.println("0");
						} else {
							dActivatedOutputsDOutputs[l][m] = 0;
						}
					} else {
						dActivatedOutputsDOutputs[l][m] = 1;
					}
				}
			}

			//The derivative of the calculate function
			double[][] dOutputsDWeights[] = new double[weights.length][][];
			for(int l = 0;l < weights.length;l++) {
				double[] tempArray[] = new double[weights[l].length][neurons[l].length];
				dOutputsDWeights[l] = tempArray;
			}
			for(int l = 0;l < dOutputsDWeights.length;l++) {
				for(int m = 0;m < dOutputsDWeights[l].length;m++) {
					for(int n = 0;n < dOutputsDWeights[l][m].length;n++) {
						dOutputsDWeights[l][m][n] = neurons[l][n];
					}
				}
			}

			//The derivative of the cost function with respect to the weights and biases uses the chain rule by multiplying the derivatives of each of the functions being applied to the weights and biases
			//For example, the derivative of cost(sigmoid(calculate(weight))) is dCost * dSigmoid * dCalculate 
			double[][] dCostDWeights[] = new double[weights.length][][];
			for(int l = 0;l < weights.length;l++) {
				double[] tempArray[] = new double[weights[l].length][neurons[l].length];
				dCostDWeights[l] = tempArray;
			}
			for(int l = 0;l < dCostDWeights.length;l++) {
				for(int m = 0;m < dCostDWeights[dCostDWeights.length - (l + 1)].length;m++) {
					for(int n = 0;n < dCostDWeights[dCostDWeights.length - (l + 1)][m].length;n++) {
						if(l == 0) {
							dCostDWeights[dCostDWeights.length - (l + 1)][m][n] = dCostDActivatedOutputs[m] * dActivatedOutputsDOutputs[l][m] * dOutputsDWeights[dOutputsDWeights.length - (l + 1)][m][n];
						} else {
							//Add up the costs for the different possible ways each weight can affect the output
							double derivativeSum = 0;
							for(int o = 0;o < dCostDWeights[dCostDWeights.length - l].length;o++) {
								derivativeSum+= dCostDWeights[dCostDWeights.length - l][o][m];
							}
							//The derivative of a weight in a hidden layer also uses the chain rule; it gets multiplied by the derivative of the weight connected to it in the next layer
							dCostDWeights[dCostDWeights.length - (l + 1)][m][n] = derivativeSum * dActivatedOutputsDOutputs[l][m] * dOutputsDWeights[dOutputsDWeights.length - (l + 1)][m][n];
						}
					}
				}
			}

			double[] dCostDBiases[] = new double[biases.length][];
			for(int l = 0;l < biases.length;l++) {
				double tempArray[] = new double[biases[l].length];
				dCostDBiases[l] = tempArray;
			}
			for(int l = 0;l < dCostDBiases.length;l++) {
				if(l == 0) {
					for(int m = 0;m < dCostDBiases[dCostDBiases.length - (l + 1)].length;m++) {
						dCostDBiases[dCostDBiases.length - (l + 1)][m] = dCostDActivatedOutputs[m] * dActivatedOutputsDOutputs[l][m];
					}
				} else {
					for(int m = 0;m < dCostDBiases[dCostDBiases.length - (l + 1)].length;m++) {
						double derivativeSum = 0;
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
		averageCost = totalCost / totalDataBatch.length;

		//Initialize and compute arrays of average derivatives
		double[][] averageDCostDWeights[] = new double[weights.length][][];
		for(int k = 0;k < weights.length;k++) {
			double[] tempArray[] = new double[weights[k].length][neurons[k].length];
			for(int l = 0;l < tempArray.length;l++) {
				for(int m = 0;m < tempArray[l].length;m++) {
					tempArray[l][m] = totalDCostDWeights[k][l][m] / totalDataBatch.length;
				}
			}
			averageDCostDWeights[k] = tempArray;
		}

		double[] averageDCostDBiases[] = new double[biases.length][];
		for(int k = 0;k < biases.length;k++) {
			double tempArray[] = new double[biases[k].length];
			for(int l = 0;l < tempArray.length;l++) {
				tempArray[l] = totalDCostDBiases[k][l] / totalDataBatch.length;
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

	//Customizable implementation of SGD
	public void customTrain(boolean test,int trainTime,int printError,int saveTime,int batchSize,double learningRate,double decay,String fileName) throws IOException {
		for(int i = 1;i < trainTime + 1;i++) {
			trainSGD(learningRate,batchSize);
			if(i % printError == 0) {
				System.out.println("Iteration: " + i);
				System.out.println("Loss: " + getCost());
				if(test) {
					System.out.println("Accuracy: " + test() + " / " + testData.length);
				}
			}
			if(saveTime != 0 && i % saveTime == 0) {
				save(fileName);
			}
		}
	}
	
	//Returns accuracy for test data
	public double test() {
		for(int i = 0;i < neurons.length;i++) {
			for(int j = 0;j < neurons[i].length;j++) {
				neurons[i][j] = 0;
			}
		}
		double accuracy = 0;
		for(int i = 0;i < testData.length;i++) {
			//Feed data forward
			for(int j = 0;j < neurons.length - 1;j++) {
				for(int k = 0;k < neurons[j].length;k++) {
					if(j == 0) {
						neurons[j][k] = testAttributes[i][k];
					} else {
						neurons[j][k] = activate(calculate(neurons[j - 1],weights[j - 1][k],biases[j - 1][k]),activation);
					}
				}
			}

			double targets[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			for(int j = 0;j < targets.length;j++) {
				targets[targets.length - (j + 1)] = testData[i][testData[i].length - (j + 1)];
			}
			for(int j = 0;j < neurons[neurons.length - 1].length;j++) {
				neurons[neurons.length - 1][j] = calculate(neurons[neurons.length - 2],weights[weights.length - 1][j],biases[biases.length - 1][j]);
			}
			double activatedOutputs[] = new double[neuronNumbers[neuronNumbers.length - 1]];
			if(!activation.equals("softmax")) {
				for(int j = 0;j < activatedOutputs.length;j++) {
					activatedOutputs[j] = activate(neurons[neurons.length - 1][j],activation);
				}
			} else {
				activatedOutputs = softmax(neurons[neurons.length - 1]);
			}
			boolean correct = true;
			for(int j = 0;j < neuronNumbers[neuronNumbers.length - 1];j++) {
				if(step(activatedOutputs[j]) != targets[j]) {
					correct = false;
				}
			}
			if(correct) {
				accuracy += 1;
			}
		}
		return accuracy;
	
	}
	
	public double getCost() {
		return averageCost;
	}
	
	//Returns an array of outputs calculated by the network
	public double[] feedForward(double[] inputs) {
		double newOutputs[] = new double[neuronNumbers[neuronNumbers.length - 1]];
		if(neurons.length == 2) {
			if(!activation.equals("softmax")) {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = activate(calculate(inputs,weights[0][i],biases[0][i]),activation);
				}
			} else {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = calculate(inputs,weights[0][i],biases[0][i]);
				}
				newOutputs = softmax(newOutputs);
			}
		} else {
			for(int i = 0;i < neurons.length - 2;i++) {
				if(i == 0) {
					for(int j = 0;j < neuronNumbers[1];j++) {
						neurons[1][j] = activate(calculate(inputs,weights[i][j],biases[i][j]),activation);
					}
				} else {
					for(int j = 0;j < neuronNumbers[i + 1];j++) {
						neurons[i + 1][j] = activate(calculate(neurons[i - 1],weights[i][j],biases[i][j]),activation);
					}
				}
			}
			if(!activation.equals("softmax")) {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = activate(calculate(neurons[neurons.length - 2],weights[weights.length - 1][i],biases[biases.length - 1][i]),activation);
				}
			} else {
				for(int i = 0;i < newOutputs.length;i++) {
					newOutputs[i] = calculate(neurons[neurons.length - 2],weights[weights.length - 1][i],biases[biases.length - 1][i]);
				}
				newOutputs = softmax(newOutputs);
			}
		}
		return newOutputs;
	}

	//Mathematical functions
	public static double activate(double x,String function) {
		double result = 0;
		if(function.equals("sigmoid") || function.equals("softmax")) {
			result = (double) (1 / (1 + Math.exp(x * -1)));
		} else if(function.equals("abs")) {
			result = Math.abs(x);
		} else if(function.equals("softplus")) {
			result = (double) Math.log(1 + Math.exp(x));
		} else if(function.equals("ReLU")) {
			result = Math.max(0,x);
		} else {
			result = x;
		}
		return result;
	}
	public static double[] softmax(double[] x) {
		double sum = 0;
		double exponentials[] = new double[x.length];
		for(int i = 0;i < x.length;i++) {
			exponentials[i] = Math.min((double)Math.exp(x[i]),Double.MAX_VALUE);
			sum += exponentials[i];
		}
		if(Double.isInfinite((sum))) {
			sum = Double.MAX_VALUE;
		}
		double[] activations = new double[x.length];
		for(int i = 0;i < activations.length;i++) {
			activations[i] = (double)(exponentials[i] / sum);
			if(activations[i] == 0) {
				activations[i] = (double)1e-35;
			}
			if(Double.isNaN(activations[i])) {
				System.out.println("NaN Activation");
			}
		}
		return activations;
	}
	public static double calculate(double[] n,double[] w,double b) {
		return dotProduct(n,w) + b;
	}
	public static double step(double x) {
		return (double)Math.round(x); 
	}
	public static double dotProduct(double[] x,double[] y) {
		double sum = 0;
		for(int i = 0;i < x.length;i++) {
			sum += x[i] * y[i];
		}
		return sum;
	}
	public static double loss(double x,double y,String function) {
		double loss = 23;
		if(function.equals("MSE")) {
			loss = (double)Math.pow(x - y, 2);
		} else if(function.equals("CE")) {
			loss = (double)Math.log(x) * y;
		}
		return loss;
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
	public void printOutputs(double inputs[]) {
		System.out.println("\n" + "Predicted Outputs:");
		for(int i = 0;i < neuronNumbers[neuronNumbers.length - 1];i++) {
			System.out.println(feedForward(inputs)[i]);
		}
	}
	public void save(String fileName) throws IOException {
        OutputStream outStream = new FileOutputStream(fileName);
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(outStream);
        fileObjectOut.writeObject(this);
        fileObjectOut.close();
        outStream.close();
	}
	public static MLP2 load(String fileName) throws IOException, ClassNotFoundException {
        InputStream inStream = new FileInputStream(fileName);
		ObjectInputStream fileObjectIn = new ObjectInputStream(inStream);
        MLP2 loaded = (MLP2)fileObjectIn.readObject();
        fileObjectIn.close();
        return loaded;
	}
}

