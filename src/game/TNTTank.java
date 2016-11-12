package game;

import ui.ShapeManager;

public class TNTTank extends Tank {
	
	static final double SPEED = 1.0;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 190.0;
	static final double FIREABLE_DIST = 170.0;
	static final double MAX_TURN_ANGLE = Math.PI/360.0;
	static final int MAX_TIME_TO_FIRE = 250;
	static final int BULLET_OFFSET_X = HALF_WIDTH - TNT.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - TNT.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1600;

	public TNTTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.TNT_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new TNT(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
}
