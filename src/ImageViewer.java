import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageViewer {

	public static void main(String[] args) throws IOException {
		
		File file = new File("C:\\Users\\Babtu\\Pictures\\MNISTTest\\rsz_6.png");
		BufferedImage image = new BufferedImage(28,28,BufferedImage.TYPE_INT_ARGB);
		image = ImageIO.read(file);
		BufferedImage bigImage = createResizedCopy(image,280,280,false);
		int height = 280,width = 280;
		JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(bigImage), JLabel.CENTER);
		frame = new JFrame("Display");
		frame.setPreferredSize(new Dimension(height,width));
		frame.setMaximumSize(new Dimension(height,width));
		frame.setMinimumSize(new Dimension(height,width));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(false);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(label);
		frame.setVisible(true);
		
	}

	static JFrame update(BufferedImage image,JFrame frame,JLabel oldLabel) {
		BufferedImage currentImage = createResizedCopy(image,frame.getWidth(),frame.getHeight(),true);
        JLabel currentLabel = new JLabel(new ImageIcon(currentImage),JLabel.CENTER);
		frame.remove(oldLabel);
		frame.add(currentLabel);
		update(image,frame,oldLabel);
		return frame;
	}
	public static BufferedImage createResizedCopy(Image originalImage,int scaledWidth,int scaledHeight,boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }
}
