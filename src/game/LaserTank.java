package game;

import ui.ShapeManager;

public class LaserTank extends Tank implements Drawable {
	static final double SPEED = 1.1;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 130.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_FIRE = 40;
	static final int BULLET_OFFSET_X = 0;
	static final int BULLET_OFFSET_Y = 0;
	public static final int CONSTRUCTION_TIME = 1800;

	public LaserTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.LASER_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new Laser(this, target));
	}
}
