package originalml;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
public class Perceptron_Stochastic {

	public static void main (String[] args) throws Exception{
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the name of the file you would like to use?");
		String fileName = keyboard.next();
		System.out.println("What is the range of the possible outputs?");
		float predictionScalar = keyboard.nextFloat();
		System.out.println("What rate of learning would you like to use?");
		float learningRate = keyboard.nextFloat();
		System.out.println("What fraction of the training set would you like to use for gradient descent?");
		float fraction = keyboard.nextFloat();
		System.out.println("How many iterations would you like to use to compute? 1 000 000 iterations takes about 1 second per input.");
		int trainTime = keyboard.nextInt();
		System.out.println("Computing...");
		
		//copy values from csv file into totalData array
		int columns = 0;
		int row = 0;
		String inLine = "";
		
		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName + ".csv")));
		BufferedReader read = new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName + ".csv"));
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

		//copy attributes from totalData to totalAttributes
		float[] totalAttributes[] = new float[totalData.length][totalData[0].length - 1];
		for(int i = 0;i < totalData.length;i++) {
			
			for(int j = 0;j < totalData[i].length - 1;j++) {
				
				totalAttributes[i][j] = totalData[i][j];
			}
		}

		//initialize variables for training
		float max = (float).1;
		float min = (float)-.1;
		
		float b = min + (float) Math.random() * (max - min);
		float weights[] = new float[totalAttributes[0].length];
		for(int i = 0;i < weights.length;i++) {
			weights[i] = min + (float) Math.random() * (max - min);
		}
		float firstCost = 0;
		float finalCost = 0;

		//training
		for(int j = 0;j < trainTime;j++) {
			
			//initialize total cost and derivatives
			float totalCost = 0;
			float total_dc_dw[] = new float[weights.length];
			float total_dc_db = 0;
			
			//calculate cost and derivatives
			for(int i = 0;i < (int)totalData.length;i++) {
			
				float[] currentExamples[] = new float[(int)(totalData.length * fraction)][totalData[0].length];
				for(int k = 0;k < currentExamples.length;k++) {
					for(int l = 0;l < totalData[k].length;k++) {
						currentExamples[i][j] = totalData[(int)(Math.random() * currentExamples.length)][j];
					}
				}
				
				float[] currentAttributes[] = new float[currentExamples.length][currentExamples[0].length - 1];
				for(int k = 0;k < currentExamples.length;k++) {
					for(int l = 0;l < currentExamples[i].length - 1;l++) {
						currentAttributes[i][j] = currentExamples[i][j];
					}
				}
				float target = currentExamples[i][totalData.length - 1];
				float z = calculate(currentAttributes[i],weights,b);
				float output = predictionScalar * sigmoid(z);
				float cost = (float) Math.pow((output - target),2);
				totalCost += cost;
				
				float dc_do = (float) 2 * (output - target);
				float do_dz = sigmoid(z) * (1 - sigmoid(z));
				float dz_dw[] = new float[weights.length]; 
				for(int k = 0;k < weights.length;k++) {
					
					dz_dw[k] = currentAttributes[i][k];
				}
				float dz_db = 1;
				float dc_dw[] = new float[weights.length];
				for(int k = 0;k < weights.length;k++) {
					
					dc_dw[k] = dc_do * do_dz * dz_dw[k];
				}
				float dc_db = dc_do * do_dz * dz_db;
				for(int k = 0;k < weights.length;k++) {
					
					total_dc_dw[k] += dc_dw[k];
				}
				total_dc_db += dc_db;
			}
			
			//calculate averages
			float averageCost = totalCost / (float)(totalData.length * fraction);
			float avg_dc_dw[] = new float[weights.length];
			for(int k = 0;k < weights.length;k++) {
				
				avg_dc_dw[k] = total_dc_dw[k] / (float)(totalData.length * fraction);
			}
			float avg_dc_db = total_dc_db / (float)(totalData.length * fraction);
			
			//gradient descent
			for(int k = 0;k < weights.length;k++) {
				
				weights[k] -= learningRate * avg_dc_dw[k];
			}
			b -= learningRate * avg_dc_db;
			
			//save error at beginning and end of training
			if(j == 0) {
				firstCost = averageCost; 
			}
			
			if(j == trainTime - 1) {
				finalCost = averageCost;
			}
		}
		//clear console
		for (int i = 0; i < 50; ++i) {
			System.out.println();
		}
		//print first and final errors
		System.out.println("The first mean squared error is " + firstCost + ".");
		System.out.println("The final mean squared error is " + finalCost + ".");
		//print weights, bias, and ask for new attributes
		String allweights = "";
		for(int i = 0;i < weights.length;i++) {
			
			allweights += weights[i];
			if(weights[i] != weights[weights.length - 1]) {
				
				allweights += ", ";
			}
		}
		System.out.println("The weights are: " + allweights + ".");
		System.out.println("The bias is equal to " + b + ".");
		System.out.println("What are the attributes?");
		float inputs[] = new float[totalAttributes[0].length];
		for(int i = 0;i < totalAttributes[0].length;i++) {
			
			inputs[i] = keyboard.nextFloat();
		}

		System.out.println("My prediction is " + step(predictionScalar * sigmoid(calculate(inputs,weights,b))) + ".");
		
	}
	
	//functions
	public static float sigmoid (float x) {
		
		return (float) (1 / (1 + Math.exp(x * -1)));
		
	}
	
	public static float calculate (float[] n,float[] w,float b) {
		
		return w_sum(n,w) + b;
	}

	public static int step (float x) {
		
		return (int) Math.round(x); 
	}
	
	public static float w_sum (float[] x,float[] y) {
		
		float sum = 0;
		for(int i = 0;i < x.length;i++) {
			
			sum += x[i] * y[i];
		}
		return sum;
	}
		
}

