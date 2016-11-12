package game;

import ui.ShapeManager;

public class LightTank extends Tank {
	
	static final double SPEED = 1.2;
	static final int HIT_POINTS = 60;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/150.0;
	static final int MAX_TIME_TO_FIRE = 25;
	static final int BULLET_OFFSET_X = HALF_WIDTH - TinyBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - TinyBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 900;

	public LightTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.LIGHT_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new TinyBullet(game, team, this,  x + bulletOffsetX, y + bulletOffsetY, angle));
	}
}
