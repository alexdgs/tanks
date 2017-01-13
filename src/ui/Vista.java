package ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import game.Drawable;
import game.Game;
import game.GameObject;

public class Vista {
	
	static final int DEFAULT_VISIBLE_W = Math.min(760, Game.WIDTH);
	static final int DEFAULT_VISIBLE_H = Math.min(760, Game.HEIGHT);
	
	public static final int LEFT = 0;
	public static final int DOWN = 1;
	public static final int UP = 2;
	public static final int RIGHT = 3;
	
	int x;
	int y;
	int w;
	int h;
	int maxX;
	int maxY;
	int despl = 5;
	
	Game game;
	ArrayList<Drawable> drawables;
	
	public Vista(Game game) {
		this.game = game;
		updateSize(DEFAULT_VISIBLE_W, DEFAULT_VISIBLE_H);
	}
	
	public void updateSize(int w, int h) {
		this.w = w;
		this.h = h;
		maxX = Math.max(0, Game.WIDTH - w);
		maxY = Math.max(0, Game.HEIGHT - h);
		x = Math.min(x, maxX);
		y = Math.min(y, maxY);
	}
	
	public void move(int dir) {
		if(dir == LEFT) x = Math.max(0, x-despl);
		else if(dir == RIGHT) x = Math.min(maxX, x+despl);
		else if(dir == UP) y = Math.max(0, y-despl);
		else y = Math.min(maxY, y+despl);
	}
	
	public void move(int dx, int dy) {
		if(dx < 0) x = Math.max(0, x + dx);
		else if(dx > 0) x = Math.min(maxX, x + dx);
		if(dy < 0) y = Math.max(0, y + dy);
		else if(dy > 0) y = Math.min(maxY, y + dy);
	}
	
	public Point getOrigin() {
		return new Point(x, y);
	}
	
	public Rectangle getViewableArea(double scale) {
		return new Rectangle(x, y, Math.min(Game.WIDTH, (int)(w/scale)), Math.min(Game.HEIGHT, (int)(h/scale)));
	}
	
	public void paint(Graphics2D g2d) {
		try {
			synchronized(drawables = game.getDrawables()) {
				for(Drawable d : drawables) {
					GameObject go = (GameObject)d;
					d.draw(g2d, (int)go.getX() - x, (int)go.getY() - y);
				}
			}
		} catch(ConcurrentModificationException cme) {
			System.out.println("Concurrent issue. Continue");
		}
	}
}
