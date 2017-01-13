package game;

import ui.ShapeManager;

public class FreezeBombTank extends BombTank {
	static final double SPEED = 0.8;
	static final int HIT_POINTS = 70;
	static final double VISIBLE_DIST = 160.0;
	static final double FIREABLE_DIST = 5.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_FIRE = 10;
	static final int BULLET_OFFSET_X = 0;
	static final int BULLET_OFFSET_Y = 0;
	public static final int CONSTRUCTION_TIME = 1100;
	
	public FreezeBombTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, ShapeManager.FREEZEBOMB_TANK);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new FreezerWave(this, centerX, centerY));
		hitted(hitPoints);
	}
}