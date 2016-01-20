package game;

import ui.ShapeManager;

public class MissileTank extends Tank {
	
	static final double SPEED = 1.0;
	static final int HIT_POINTS = 80;
	static final double VISIBLE_DIST = 210.0;
	static final double FIREABLE_DIST = 190.0;
	static final double MAX_TURN_ANGLE = Math.PI/360.0;
	static final int MAX_TIME_TO_FIRE = 225;
	static final int BULLET_OFFSET_X = HALF_WIDTH - Missile.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - Missile.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1800;
	
	public MissileTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.MISSILE_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new Missile(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
}
