package pong;
import java.awt.Color;
import java.awt.Graphics;

public class Barrier extends GameObject{

	public Barrier(int x, int y, ID id) {
		super(x, y, id);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(x, y, 32, 640);
	}
}
