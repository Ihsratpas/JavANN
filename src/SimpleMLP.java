import java.util.Scanner;

import ml.MLP;

public class SimpleMLP {

	public static void main(String[] args) throws Exception {

		int neurons[] = {2,1};
		MLP mlp = new MLP(neurons,"sigmoid","MSE");
		mlp.getCSVData("flower.csv");
		mlp.createNetwork();
		for(int i = 0;i < 50000;i++) {
			mlp.trainSGD((float).1,8);
			if(i % 10000 == 0) {
				System.out.println("The mean squared error is " + mlp.getCost());
			}
		}
		System.out.println();
		mlp.printParameters();
		System.out.println("\nWhat are the inputs?");
		Scanner key = new Scanner(System.in);
		float inputs[] = new float[neurons[0]];
		for(int i = 0;i < neurons[0];i++) {
			inputs[i] = key.nextFloat();
		}
		mlp.printOutputs(inputs);
		key.close();
		
	}

}
