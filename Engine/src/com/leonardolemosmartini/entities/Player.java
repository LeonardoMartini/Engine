package com.leonardolemosmartini.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.leonardolemosmartini.main.Game;

public class Player extends Entity{

	public boolean right, up ,left, down;
	public double speed = 2;
	public int rightDir = 0, dir = 0, leftDir = 1;
	private int frames = 0, maxFrames = 6, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite((i*Game.tamanhoSprite), 0, Game.tamanhoSprite, Game.tamanhoSprite);
		}
		
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(64 + (i*Game.tamanhoSprite), 0, Game.tamanhoSprite, Game.tamanhoSprite);
		}
		
	}
	
	public void tick() {
		
		moved = false;
		
		if(right) {
			moved = true;
			dir = rightDir;
			x+=speed;
		}else if(left) {
			moved = true;
			dir = leftDir;
			x-=speed;
		}if(up) {
			moved = true;
			dir = leftDir;
			y-=speed;
		}else if(down) {
			moved = true;
			dir = rightDir;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index>maxIndex) {
					index = 0;
				}
			}
			
		}
		
	}
	
	public void render (Graphics g) {
		
		if(dir == rightDir) {
			
			g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
		
		}else if(dir == leftDir) {
			
			g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);		
		
		}else {
			
			
			
		}
		
	}

}
