package game;

import ui.ShapeManager;

public class ScatterTank extends Tank implements Drawable {
	
	static final double SPEED = 1.15;
	static final int HIT_POINTS = 90;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/180.0;
	static final int MAX_TIME_TO_FIRE = 80;
	static final int BULLET_OFFSET_X = HALF_WIDTH - TinyBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - TinyBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1500;
	
	static final double LEFT_DEV_ANGLE = Math.PI/20.0;
	static final double RIGHT_DEV_ANGLE = LEFT_DEV_ANGLE * -1.0;
	
	public ScatterTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.SCATTER_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new TinyBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
		game.newGameObjects.add(new TinyBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, sumAngles(angle, LEFT_DEV_ANGLE)));
		game.newGameObjects.add(new TinyBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, sumAngles(angle, RIGHT_DEV_ANGLE)));
	}
}
