import java.io.FileWriter;

import ml.NeuralNetwork;

public class UCIBinaryConversion {

	public static void main(String[] args) throws Exception {
		String fileName = "uciBinary.csv";
		FileWriter writer = new FileWriter("C:/Users/Babtu/Documents/Datasets/" + fileName);

		NeuralNetwork helper = new NeuralNetwork();
		helper.getCSVData("uci.csv");
		for(int i = 0;i < helper.getTotalData().length;i++) {
			for(int j = 0;j < helper.getTotalData()[i].length;j++) {
				if(j < helper.getTotalData()[i].length - 1) {
					String binary = toBinary((int)helper.getTotalData()[i][j]);
					writer.append(binary);
					writer.append(",");
				} else {
					if((int)helper.getTotalData()[i][j] == 4) {
						String one = "1";
						writer.append(one);
					} else {
						String zero = "0";
						writer.append(zero);
					}
					writer.append("\n");
				}
			}
		}
		writer.flush();
		writer.close();

	}
	public static String toBinary(int x) {
		int container[] = new int[4];
	    int i = 0;
	    while (x > 0){
	        container[i] = x % 2;
	        i++;
	        x = x / 2;
	    }
	    String binary = "";
	    for(int j = 0;j < 4;j++) {
	    	binary += container[3 - j];
	    	if(j != 3) {
	    		binary += ",";
	    	}
	    }
	    return binary;
	}

}
