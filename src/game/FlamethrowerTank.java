package game;

import ui.ShapeManager;

public class FlamethrowerTank extends Tank implements Drawable {
	static final double SPEED = 1.0;
	static final int HIT_POINTS = 120;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 100.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_FIRE = 25;
	static final int BULLET_OFFSET_X = 7;
	static final int BULLET_OFFSET_Y = 7;
	public static final int CONSTRUCTION_TIME = 1000;
	static final double DEV_ANGLE = Math.PI/90;
	
	int devDir = 1;
	double devAngle = 0.0;

	public FlamethrowerTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.FLAMETHOWER_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		if(devAngle == 0.0) {
			devAngle = DEV_ANGLE*(devDir *= -1);
		} else {
			devAngle = 0.0;
		}
		game.newGameObjects.add(new Flame(this, x + bulletOffsetX, y + bulletOffsetY, sumAngles(angleTo(target), devAngle)));
	}
}
