//package pong;
import pong.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;

import ml.NeuralNetwork;

public class Game extends Canvas{

	private static final long serialVersionUID = -1442798787354930462L;
	
	public static final int WIDTH = 640,HEIGHT = WIDTH / 12 * 9;
	
	private static boolean running = false;

	private Handler handler;
	
	private float score = 0;
	
	ArrayList<Boolean> recordedScore = new ArrayList<Boolean>();
	ArrayList<Boolean> totalRecordedScore = new ArrayList<Boolean>();

	ArrayList<Float[]> totalGame = new ArrayList<Float[]>();
	
	private NeuralNetwork student;
	
	public NeuralNetwork getStudent() {
		return student;
	}
	public void setStudent(NeuralNetwork student) {
		this.student = student;
	}
	
	public ArrayList<Float[]> getTotalGame() {
		return totalGame;
	}
	
	public Game(String inputType,String gameType,boolean render,int fps,NeuralNetwork x) {	

		setStudent(x);
		handler = new Handler();
		if(inputType.equals("key")) {
			this.addKeyListener(new KeyInput(handler));
		}
		
		Window window = new Window(WIDTH,HEIGHT,"Game In-Training",this);
		window.getFrame().setVisible(render);
		
		int randY = (int)Math.rint(Math.random() * (HEIGHT - 128)) + 64;
		handler.addObject(new Player(WIDTH / 2 - 192,HEIGHT / 2 - 32,ID.Player));
		handler.addObject(new Barrier(WIDTH / 2 - 32,0,ID.Barrier));
		handler.addObject(new Ball(WIDTH / 2 - 32,randY,ID.Ball));
		handler.addObject(new Wall(0,0,ID.Wall));
		GameObject ball = handler.object.get(2);
		ball.setVelX(5);
		int velY;
		float randomY = (float)Math.random();
		if(Math.round(randomY) == 0) {
			velY = -5;
		} else {
			velY = 5;
		}
		ball.setVelY(velY);
		run(inputType,gameType,render,fps);
		while(running);
		window.getFrame().dispose();
	}

	public void start() {
		score = 0;
		running = true;
	}
	public void stop() {
		running = false;
	}
	
	public void run(String inputType,String gameType,boolean render,int fps) {
		long lastTime = System.nanoTime();
		double amountOfTicks = fps;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				if(inputType.equals("key")) {
					playTick(frames,fps,gameType);
				} else {
					GameObject player = handler.object.get(0);
					GameObject ball = handler.object.get(2);
					float[] attributes = {(float)player.getY(),(float)ball.getX(),(float)ball.getY(),(float)ball.getVelX(),(float)ball.getVelY()};
					trainTick(frames,student.test(attributes),fps,gameType);
				}
				delta--;
			}
			if(running) {
				render(render);
			}
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
				recordedScore.clear();
			}
		}
		stop();
	}
	
	private void playTick(int frames,int fps,String gameType) {
		GameObject player = handler.object.get(0);
		GameObject ball = handler.object.get(2);
		boolean touchX = false;
		boolean touchY = false;
		if(player.getX() - 1 < ball.getX() && ball.getX() < player.getX() + 17) {
			touchX = true;
		}
		if(player.getY() - 33 < ball.getY() && ball.getY() < player.getY() + 65) {
			touchY= true;
		}
		int currentScore = 0;
		if(touchX && touchY && !recordedScore.contains(true)) {
			ball.setVelX(ball.getVelX() * -1);
			boolean scored = true;
			for(int i = 0;i < totalGame.size();i++) {
				if(totalRecordedScore.get(i) == true && totalGame.get(i)[1] == (float)player.getY()) {
					scored = false;
				}
			}
			if(scored) {
				currentScore = 1;
			}
			recordedScore.add(true);
			totalRecordedScore.add(true);
			if(gameType.equals("episode")) {
				stop();
			}
		} else {
			recordedScore.add(false);
			totalRecordedScore.add(false);
		}
		score += currentScore;//= (float)((System.currentTimeMillis() - beginningTime) * (fps / 60.0));
		float newVelY;
		if(player.getVelY() == -5) {
			newVelY = 0;
		} else if(player.getVelY() == 0) {
			newVelY = (float).5;
		} else {
			newVelY = 1;
		}
		Float currentFrame[] = {(float)player.getX(),(float)player.getY(),(float)ball.getX(),(float)ball.getY(),(float)ball.getVelX(),(float)ball.getVelY(),newVelY};
		totalGame.add(currentFrame);
		handler.tick();
		if(ball.getX() < 33) {
			stop();
		}
	}
	private void trainTick(int frames,float[] inputs,int fps,String gameType) {
		
		if(Math.rint(inputs[0]) == 1) {
			if(Math.random() > inputs[0]) {
				inputs[0] = flip(inputs[0]);
			}
		} else {
			if(Math.random() < inputs[0]) {
				inputs[0] = flip(inputs[0]);
			}
		}
		float[] realInputs = new float[inputs.length];
		for(int i = 0;i < realInputs.length;i++) {
			realInputs[i] = (float)Math.rint(inputs[i]);
		}
		int velY = 0;
		if(realInputs[0] == 1) {
			velY = 5;
		} else {
			velY = -5;
		}
		GameObject player = handler.object.get(0);
		player.setVelX(0);
		player.setVelY(velY);
		GameObject ball = handler.object.get(2);
		boolean touchX = false;
		boolean touchY = false;
		if(player.getX() - 1 < ball.getX() && ball.getX() < player.getX() + 17) {
			touchX = true;
		}
		if(player.getY() - 33 < ball.getY() && ball.getY() < player.getY() + 65) {
			touchY= true;
		}
		int currentScore = 0;
		if(touchX && touchY && !recordedScore.contains(true)) {
			ball.setVelX(ball.getVelX() * -1);
			boolean scored = true;
			for(int i = 0;i < totalGame.size();i++) {
				if(totalRecordedScore.get(i) == true && totalGame.get(i)[1] == (float)player.getY()) {
					scored = false;
				}
			}
			if(scored) {
				currentScore = 1;
			}
			recordedScore.add(true);
			totalRecordedScore.add(true);
			if(gameType.equals("episode")) {
				stop();
			}
		} else {
			recordedScore.add(false);
			totalRecordedScore.add(false);
		}
		score += currentScore;//= (float)((System.currentTimeMillis() - beginningTime) * (fps / 60.0));
		float newVelY;
		if(player.getVelY() == -5) {
			newVelY = 0;
		} else if(player.getVelY() == 0) {
			newVelY = (float).5;
		} else {
			newVelY = 1;
		}
		Float currentFrame[] = {(float)player.getX(),(float)player.getY(),(float)ball.getX(),(float)ball.getY(),(float)ball.getVelX(),(float)ball.getVelY(),newVelY};
		totalGame.add(currentFrame);
		handler.tick();
		if(ball.getX() < 33) {
			stop();
		}
	}
	
	private void render(boolean render) {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		if(render) {
			Graphics g = bs.getDrawGraphics();

			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			handler.render(g);

			g.dispose();
			bs.show();
		}
	}
	
	public float getScore() {
		return score;
	}
	
	public static boolean checkBoolean(boolean[] x,boolean y) {
		boolean z = false;
		for(int i = 0;i < x.length;i++) {
			if(x[i] == y) {
				z = true;
			}
		}
		return z;
	}
	
	public static float flip(float x) {
		return Math.abs(x - 1);
	}
	
	public static void main(String[] args) throws IOException {
		
		NeuralNetwork testStudent = new NeuralNetwork();
		testStudent.setNetwork(6,1,1,10,"sigmoid");
		testStudent.createNetwork();
		new Game("key","game",true,150,testStudent);
//		System.out.println("Score: " + game.getScore());

	}

}
