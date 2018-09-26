package originalml;
import java.util.Scanner;

public class Flower {

	public static void main (String[] args) {

		//Initialize dataset of flower length, width, and colour
		float[] total_data[] = {{2,1,0},
								{3,1,0},
								{2,(float).5, 0},
								{1, 1, 0},
								{3,(float)1.5, 1},
								{(float)3.5,(float).5, 1},
								{4,(float)1.5, 1},
								{(float)5.5, 1, 1}};
		
		//Maximum and minimum values for weights and bias
		float max = (float).1;
		float min = (float)-.1;
		//Rate of gradient descent
		float learning_rate = (float).2;
		
		//Initialize weights and bias randomly
		float w0 = min + (float) Math.random() * (max - min);
		float w1 = min +  (float) Math.random() * (max - min);
		float b = min + (float) Math.random() * (max - min);
		
		//Train for 50000 iterations
		for(int j = 0;j < 50000; j++) {
			
			//Initialize total cost and derivatives
			float total_cost = 0;
			float total_dc_dw0 = 0;
			float total_dc_dw1 = 0;
			float total_dc_db = 0;
			
			//Iterate through all training examples
			for(int i = 0;i < (int)total_data.length;i++) {
			
				//Calculate cost
				float length = total_data[i][0];
				float width = total_data[i][1];
				float target = total_data[i][2];
				float z = calculate(length,width,w0,w1,b);
				float output = sigmoid(z);
				float cost = (float) Math.pow((output - target),2);
				//Add cost to total cost
				total_cost += cost;
				
				//Calculate derivatives
				float dc_do = (float) 2 * (output - target);
				float do_dz = sigmoid(z) * (1 - sigmoid(z));
				float dz_dw0 = length;
				float dz_dw1 = width;
				float dz_db = 1;
				float dc_dw0 = dc_do * do_dz * dz_dw0;
				float dc_dw1 = dc_do * do_dz * dz_dw1;
				float dc_db = dc_do * do_dz * dz_db;
				//Add derivatives to total derivatives
				total_dc_dw0 += dc_dw0;
				total_dc_dw1 += dc_dw1;
				total_dc_db += dc_db;
				
			}
			
			//Calculate average cost and derivatives
			float avg_cost = total_cost / (float)total_data.length;
			float avg_dc_dw0 = total_dc_dw0 / (float)total_data.length;
			float avg_dc_dw1 = total_dc_dw1 / (float)total_data.length;
			float avg_dc_db = total_dc_db / (float)total_data.length;
			
			//Subtract a fraction of the gradient from the weights and bias
			w0 -= learning_rate * avg_dc_dw0;
			w1 -= learning_rate * avg_dc_dw1;
			b -= learning_rate * avg_dc_db;
			
			//Print cost every 25000 iterations
			if(j % 25000 == 0) {
				
				System.out.println("The mean squared error is " + avg_cost + ".");
				
			}
			
		}
			
		//Print weights and bias
		Scanner keyboard = new Scanner(System.in);
		System.out.println("w0 is equal to " + w0 + ".");
		System.out.println("w1 is equal to " + w1 + ".");
		System.out.println("b is equal to " + b + ".");
		
		//Gather new length and width values
		System.out.println("What is the length of your petal?");
		float length = keyboard.nextFloat();
		System.out.println("What is the width of your petal?");
		float width = keyboard.nextFloat();
		
		//Predict whether the flower is red or blue
		if(step(sigmoid(calculate(length,width,w0,w1,b))) == 1) {
			
			System.out.println("Your flower is red.");
			
		}
		
		else {
			
			System.out.println("Your flower is blue.");
			
		}
		
		keyboard.close();
		
	}
	
	//Mathematical functions
	public static float sigmoid (float x) {
		
		return (float) (1 / (1 + Math.exp(x * -1)));
		
	}
	
	public static float calculate (float n1,float n2,float w1,float w2,float b) {
		
		return (float) (n1 * w1 + n2 * w2 + b);
		
	}

	public static int step (float x) {
		
		return (int) Math.round(x); 
		
	}
		
}

