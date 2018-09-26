package pong;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas{
	
	private static final long serialVersionUID = 9034494958129720942L;
	
	private static JFrame frame;

	public Window(int height,int width,String title,Game game) {
		frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(height,width));
		frame.setMaximumSize(new Dimension(height,width));
		frame.setMinimumSize(new Dimension(height,width));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
	
	public JFrame getFrame() {
		return Window.frame;
	}
}
