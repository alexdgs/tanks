package game;

import ui.ShapeManager;

public class DoubleTank extends Tank {
	
	static final double SPEED = 1.1;
	static final int HIT_POINTS = 80;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/180.0;
	static final int MAX_TIME_TO_FIRE = 75;
	static final int BULLET_OFFSET_X = HALF_WIDTH - SmallBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - SmallBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1400;
	
	static final int ABSOLUTE_OFFSET = 6;
	int multiplier = 1;

	public DoubleTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, 0, 0, ShapeManager.DOUBLE_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		int multiplier = getMultiplier();
		bulletOffsetX = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle)*ABSOLUTE_OFFSET*multiplier);
		bulletOffsetY = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle + THREE_HALVES_PI)*ABSOLUTE_OFFSET*multiplier);
		
		game.newGameObjects.add(new SmallBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
	
	public int getMultiplier() {
		return (multiplier * -1);
	}
}
