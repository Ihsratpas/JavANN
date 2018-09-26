package pong;
import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject{

	public Player(int x, int y, ID id) {
		super(x, y, id);
	}
	
	public void tick() {
		x += velX;
		y += velY;
		if(x > 256) {
			x -= 5;
		} else if(x < 32){
			x += 5;
		}
		if(y > 386) {
			y -= 5;
		} else if(y < 0) {
			y += 5;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, 16, 64);
	}

}
