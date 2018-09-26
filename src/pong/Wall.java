package pong;
import java.awt.Color;
import java.awt.Graphics;

public class Wall extends GameObject{

	public Wall(int x, int y, ID id) {
		super(x, y, id);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, 32, 640);
	}
}
