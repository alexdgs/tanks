package game;

import ui.ShapeManager;

public class LightningTank extends Tank implements Drawable {
	static final double SPEED = 1.0;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 160.0;
	static final double FIREABLE_DIST = 140.0;
	static final double MAX_TURN_ANGLE = Math.PI/150.0;
	static final int MAX_TIME_TO_FIRE = 50;
	static final int BULLET_OFFSET_X = 0;
	static final int BULLET_OFFSET_Y = 0;
	static final int RAY_OFFSET = 12;
	public static final int CONSTRUCTION_TIME = 1800;

	public LightningTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.LIGHTNING_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		double rayOffsetX = RAY_OFFSET * cos(angle);
		double rayOffsetY = RAY_OFFSET * sin(angle);
		game.newGameObjects.add(new Lightning(this, rayOffsetX, rayOffsetY, target));
	}
}
