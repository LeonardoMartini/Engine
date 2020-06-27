package com.leonardolemosmartini.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.leonardolemosmartini.entities.Entity;
import com.leonardolemosmartini.entities.Player;
import com.leonardolemosmartini.graphics.Spritesheet;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240, HEIGHT = 160, SCALE = 3;
	private BufferedImage image;
	public List<Entity> entities;
	public static Spritesheet spritesheet;
	public static int tamanhoSprite = 16;
	private Player player;

	/*
	private BufferedImage[] player;
	private Spritesheet sheet;
	private int frames = 0, maxFrames = 200, curAnimation = 0, maxAnimation = 4;
	*/
	
	public Game() {
		
		/*Animando personagem
		sheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[4];
		player[0] = sheet.getSprite(tamanhoSprite*0, 0, tamanhoSprite, tamanhoSprite);
		player[1] = sheet.getSprite(tamanhoSprite*1, 0, tamanhoSprite, tamanhoSprite);
		player[2] = sheet.getSprite(tamanhoSprite*2, 0, tamanhoSprite, tamanhoSprite);
		player[3] = sheet.getSprite(tamanhoSprite*3, 0, tamanhoSprite, tamanhoSprite);
		*/
		
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, tamanhoSprite, tamanhoSprite, spritesheet.getSprite((tamanhoSprite*0), 0, tamanhoSprite, tamanhoSprite));
		entities.add(player);
		
	}
	
	public void initFrame() {
		
		frame = new JFrame("Engine");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {

		isRunning = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {

		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		/*Animando personagem tick
		frames++;
		
		if(frames > maxFrames) {
			
			frames = 0;
			curAnimation++;
			
			if(curAnimation >= maxAnimation) {
				
				curAnimation = 0;
				
			}
		}
		*/
		
		for(int i = 0; i < entities.size(); i++) {
			
			Entity e = entities.get(i);
			e.tick();
			
		}
		
	}
	
	
	public void render() {

		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			
			this.createBufferStrategy(3);
			return;
			
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		Graphics2D g2 = (Graphics2D) g;
		
		for(int i = 0; i < entities.size(); i++) {
			
			Entity e = entities.get(i);
			e.render(g);
			
		}
		
		/*"Efeito de luz" - renderiza por ordem.
		//g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		Rotacionar objeto
		//g2.rotate(Math.toRadians(90),90+(tamanhoSprite/2),90+(tamanhoSprite/2));
		g2.drawImage(player,90,90,null);
		
		Escrever
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.BLUE);
		g.drawString("teste", 0, 15);
		
		Renderizar personagem render
		g.drawImage(player[curAnimation], 20, 20, null);
		*/
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
		
	}
	
	@Override
	public void run() {
		
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning) {			
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				
				tick();
				render();
				frames++;
				delta--;
				
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				
				frames = 0;
				timer+=1000;
				
			}
		}
		
		stop();
		
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
			
			player.right = true;
			
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
			
			player.left = true;
			
		}
		if(e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
			
			player.up = true;
			
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
			
			player.down = true;
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
			
			player.right = false;
			
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
			
			player.left = false;
			
		}
		if(e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
			
			player.up = false;
			
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
			
			player.down = false;
			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}