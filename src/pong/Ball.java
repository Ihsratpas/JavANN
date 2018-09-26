package pong;
import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject{

	public Ball(int x, int y, ID id) {
		super(x, y, id);
	}
	
	public void tick() {
		boolean touchX = false;
		boolean touchY = false;
		if(x < 5 || x > 606) {
			touchX = true;
		}
		if(y < 5 || y > 418) {
			touchY = true;
		}
		if(touchX) {
			velX *= -1;
		} 
		if(touchY) {
			velY *= -1;
		}
		x += velX;
		y += velY;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(x, y, 32, 32);
	}

}
