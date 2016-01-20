package game;

import java.awt.Image;

import javax.swing.ImageIcon;

import ui.ShapeManager;

public class Pillbox extends DefenseBuilding {
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int TIME_TO_FIRE = 20;
	static final int HIT_POINTS = 150;
	static final int DIF_FIRE_CANNONS = 8;
	static final int VISIBLE_DIST = 160;
	static final Image IMG_DETAIL_PILLBOX = (new ImageIcon("src/img/pbox_detail.png")).getImage();
	
	public Pillbox(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, WIDTH, HEIGHT, HIT_POINTS, VISIBLE_DIST, TIME_TO_FIRE, ShapeManager.PILLBOX);
		detail = IMG_DETAIL_PILLBOX;
	}
	
	@Override
	public void fire() {
		angle = angleTo(target);
		int dx = (int)(Math.sin(angle)*DIF_FIRE_CANNONS);
		int dy = (int)(Math.sin(angle + THREE_HALVES_PI)*DIF_FIRE_CANNONS);
		int startX1 = (int)(centerX + dx);
		int startY1 = (int)(centerY + dy);
		int startX2 = (int)(centerX + (dx*-1));
		int startY2 = (int)(centerY + (dy*-1));
		game.newGameObjects.add(new PillboxFire(game, team, this, target, startX1, startY1));
		game.newGameObjects.add(new PillboxFire(game, team, this, target, startX2, startY2));
	}
	
}
