import ml.NeuralNetwork;

public class LowCostFinder {

	public static void main(String[] args) throws Exception {
		float lowCost = 0;
		String lowCostString = "";
		for(int i = 0;i < 2;i++) {
			for(int j = 0;j < 4;j++ ) {
				NeuralNetwork net = new NeuralNetwork();
				net.setNetwork(2, 1, i, j, "sigmoid");
				net.getCSVData("test1.csv");
				net.createNetwork();
				for(int k = 0;k < 100000;k++) {
					net.trainGD((float).1);
				}
				if(i == 0 && j == 0) {
					lowCost = net.getCost();
					lowCostString = "Low cost: " + net.getCost() + "\nHidden Layers: " + i + "\nHidden Neurons: " + j;
				} else if(net.getCost() < lowCost) {
					lowCost = net.getCost();
					lowCostString = "Low cost: " + net.getCost() + "\nHidden Layers: " + i + "\nHidden Neurons: " + j;
				}
			}
		}
		System.out.println(lowCostString);
	}

}
