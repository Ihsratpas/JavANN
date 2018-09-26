package originalml;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Perceptron_Time {

	public static void main (String[] args) throws Exception{
		

		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the name of the file you would like to use?");
		String fileName = keyboard.next();
		System.out.println("What is the range of the possible outputs?");
		float predictionScalar = keyboard.nextFloat();
		System.out.println("How much time would you like to give to compute? Please answer in milliseconds.");
		long trainTime = (long)keyboard.nextInt();
		//copy values from csv file into total_data array
		int columns = 0;
		int row = 0;
		String inLine = "";
		
		Scanner reader = new Scanner(new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName + ".csv")));
		BufferedReader read = new BufferedReader(new FileReader("C:/Users/Babtu/Documents/Datasets/" + fileName + ".csv"));
		while(read.readLine() != null) {
			
			columns += 1;
		}
		float[] total_data[] = new float[columns - 1][reader.nextLine().split(",").length];

		while(reader.hasNextLine()) {

			inLine = reader.nextLine();
			String[] inArray = inLine.split(",");
			for(int x = 0;x < inArray.length;x++) {
				
				total_data[row][x] = Float.parseFloat(inArray[x]);
			}
			
			row++;
		}

		//copy inputs only, not labels, from total_data to total_values
		float[] total_values[] = new float[total_data.length][total_data[0].length - 1];
		for(int i = 0;i < total_data.length;i++) {
			
			for(int j = 0;j < total_data[i].length - 1;j++) {
				
				total_values[i][j] = total_data[i][j];
			}
		}

		//initialize variables for training
		float max = (float).1;
		float min = (float)-.1;
		float learning_rate = (float).2;
		
		float b = min + (float) Math.random() * (max - min);
		float weights[] = new float[total_values[0].length];
		for(int i = 0;i < weights.length;i++) {
			
			weights[i] = min + (float) Math.random() * (max - min);
		}

		//training
		long startTime = System.currentTimeMillis();
		long timeElapsed = 0;
		long count = 0;
		while(timeElapsed < trainTime) {
			
			//initialize total cost and derivatives
			float total_cost = 0;
			float total_dc_dw[] = new float[weights.length];
			float total_dc_db = 0;
			
			//calculate cost and derivatives
			for(int i = 0;i < (int)total_data.length;i++) {
			
				float target = total_data[i][2];
				float z = calculate(total_values[i],weights,b);
				float output = predictionScalar * sigmoid(z);
				float cost = (float) Math.pow((output - target),2);
				total_cost += cost;
				
				float dc_do = (float) 2 * (output - target);
				float do_dz = sigmoid(z) * (1 - sigmoid(z));
				float dz_dw[] = new float[weights.length]; 
				for(int k = 0;k < weights.length;k++) {
					
					dz_dw[k] = total_values[i][k];
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
			float avg_cost = total_cost / (float)total_data.length;
			float avg_dc_dw[] = new float[weights.length];
			for(int k = 0;k < weights.length;k++) {
				
				avg_dc_dw[k] = total_dc_dw[k] / total_data.length;
			}
			float avg_dc_db = total_dc_db / (float)total_data.length;
			
			//gradient descent
			for(int k = 0;k < weights.length;k++) {
				
				weights[k] -= learning_rate * avg_dc_dw[k];
			}
			b -= learning_rate * avg_dc_db;
			
			//print error at beginning and end of training
			if(count == 0) {
				
				System.out.println("The first mean squared error is " + avg_cost + ".");
			}
			
			if(count == Math.round(trainTime) * 500) {
				
				System.out.println("The final mean squared error is " + avg_cost + ".");
			}
			
			timeElapsed = System.currentTimeMillis() - startTime;
			count += 1;
		}
			
		//print weights, bias, and ask for new inputs
		String allWeights = "";
		for(int i = 0;i < weights.length;i++) {
			
			allWeights += weights[i];
			if(weights[i] != weights[weights.length - 1]) {
				
				allWeights += ", ";
			}
		}
		System.out.println("The weights are: " + allWeights + ".");
		System.out.println("The bias is equal to " + b + ".");
		System.out.println("What are the inputs?");
		float inputs[] = new float[total_values[0].length];
		for(int i = 0;i < total_values[0].length;i++) {
			
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

