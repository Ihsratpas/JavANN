import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class GrayscaleConverter {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int width = 28;
		int height = 28;
		BufferedImage image = null;
		File file = null;
		try {
			file = new File("C:\\Users\\Babtu\\Pictures\\Test\\rsz_five.png");
			image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			image = ImageIO.read(file);
			System.out.println("Reading complete.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		
		for(int i = 0;i < width;i++) {
			for(int j = 0;j < height;j++) {
				int argb = image.getRGB(i,j);
				int a = (argb >> 24) & 0xff,r = (argb >> 16) & 0xff,g = (argb >> 8) & 0xff,b = argb & 0xff;
				int average = (int)Math.rint(r * 0.2989 + g * 0.5870 + b * 0.1140);
				argb = 0;
				argb = argb | (a << 24) | (average << 16) | (average << 8) | average;
				image.setRGB(i,j,argb);
			}
		}
		
		try {
			file = new File("C:\\Users\\Babtu\\Pictures\\Test\\TestOutput.jpg");
			ImageIO.write(image,"jpg",file);
			System.out.println("Writing complete.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}

}
