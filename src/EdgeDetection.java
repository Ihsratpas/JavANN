import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EdgeDetection {

	public static void main(String[] args) {
		
		int width = 2048;
		int height = 1536;
		BufferedImage image = null;
		File file = null;
		try {
			file = new File("C:\\Users\\Babtu\\Pictures\\TestImages\\Test.jpg");
			image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			image = ImageIO.read(file);
			System.out.println("Reading complete.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		float[] filter[] = {
			{(float)-1,(float)1,(float)-1},
			{(float)1,(float)0,(float)1},
			{(float)-1,(float)1,(float)-1}};
		int newWidth = width - filter.length + 1,newHeight = height - filter.length + 1;
		BufferedImage newImage = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_ARGB);

		for(int i = 1;i < width - 1;i++) {
			for(int j = 1;j < height - 1;j++) {
				System.out.println("Row: " + i + " Column: " + j);
				int[] argb[] = {{image.getRGB(i - 1,j - 1),image.getRGB(i - 1,j),image.getRGB(i - 1,j + 1)},
								{image.getRGB(i - 0,j - 1),image.getRGB(i - 0,j),image.getRGB(i - 0,j + 1)},
								{image.getRGB(i + 1,j - 1),image.getRGB(i + 1,j),image.getRGB(i + 1,j + 1)},
				};
				
				int newA = argb[1][1] >> 24,newAverage = 0;
				int[] averages[] = new int[argb.length][argb[0].length];
				for(int k = 0;k < averages.length;k++) {
					for(int l = 0;l < averages.length;l++) {
						int r = (argb[k][l] >> 16) & 0xff,g = (argb[k][l] >> 8) & 0xff,b = argb[k][l] & 0xff;
						averages[k][l] = (int)Math.rint(r * 0.2989 + g * 0.5870 + b * 0.1140);
						newAverage += filter[k][l] * averages[k][l];
					}
				}
				
				argb[1][1] = 0;
				argb[1][1] = argb[1][1] | (newA << 24) | (newAverage << 16) | (newAverage << 8) | newAverage;
				newImage.setRGB(i - 1,j - 1,argb[1][1]);
				
			}
		}
		
		try {
			file = new File("C:\\Users\\Babtu\\Pictures\\TestImages\\NewOutput.jpg");
			ImageIO.write(newImage,"jpg",file);
			System.out.println("Writing complete.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
