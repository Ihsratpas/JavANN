import ml.MLP;

public class LinearRegression {

	public static void main(String[] args) throws Exception {
		
		int neurons[] = {1,1};
		MLP pattern = new MLP(neurons,"none","MSE");
		pattern.getCSVData("line.csv");
		pattern.createNetwork();
		for(int i = 0;i < 50000;i++) {
			pattern.trainSGD((float).001,6);
			if(i % 10000 == 0) {
				System.out.println(pattern.getCost());
			}
		}
		float attributes[] = {(float)0.5};
		pattern.printOutputs(attributes);
		pattern.printParameters();
		
	}

}
