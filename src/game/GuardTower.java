package game;

import java.awt.Image;

import javax.swing.ImageIcon;

import ui.ShapeManager;

public class GuardTower extends DefenseBuilding {
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int TIME_TO_FIRE = 20;
	static final int HIT_POINTS = 100;
	static final int VISIBLE_DIST = 200;
	static final Image IMG_DETAIL_TOWER = (new ImageIcon("src/img/guardtower_detail.png")).getImage();
	
	public GuardTower(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, WIDTH, HEIGHT, HIT_POINTS, VISIBLE_DIST, TIME_TO_FIRE, ShapeManager.GUARD_TOWER);
		detail = IMG_DETAIL_TOWER;
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new GuardTowerFire(game, team, this, target));
	}
}
