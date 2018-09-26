import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import ml.MLP;

public class TFMNISTTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {

		MLP mnist = MLP.load("C:/Users/Babtu/Documents/Models/Trained/NewModel.ser");
		System.out.println("Test Accuracy: " + mnist.test() / 9999);
		int width = 28;
		int height = 28;
		BufferedImage image = null;
		File file = null;
		Scanner key = new Scanner(System.in);
		for(int i = 0;i < i + 1;i++) {
			System.out.println("What file would you like to use?");
			try {
				file = new File(key.next());
				image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				image = ImageIO.read(file);
			} catch (IOException e) {
				System.out.println("Error: " + e);
			}
			int[] argb[] = new int[width][height];
			for(int j = 0;j < width;j++) {
				for(int k = 0;k < height;k++) {
					argb[j][k] = image.getRGB(j,k);
					int r = (argb[j][k] >> 16) & 0xff,g = (argb[j][k] >> 8) & 0xff,b = argb[j][k] & 0xff;
					int average = (int)Math.rint(r * 0.2989 + g * 0.5870 + b * 0.1140);
					argb[j][k] = 0;
					argb[j][k] = argb[j][k] | average;
				}
			}
			int count = 0;
			float test[] = new float[784];
			for(int j = 0;j < argb.length;j++) {
				for(int k = 0;k < argb[j].length;k++) {
					test[count] = (float)argb[k][j] / 255;
					count++;
				}
			}
			mnist.printOutputs(test);
			float probabilities[] = mnist.feedForward(test);
			float max = 0;
			int prediction = -1;
			for(int j = 0;j < 10;j++) {
				if(probabilities[j] > max) {
					max = probabilities[j];
					prediction = j;
				}
			}
			System.out.println("\nPrediction: " + prediction);
			System.out.println("Probability: " + max);
		}
		key.close();

	}

}
