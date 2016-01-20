package game;

import ui.ShapeManager;

public class SwarmTank extends Tank {
	
	static final double SPEED = 1.2;
	static final int HIT_POINTS = 90;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/150.0;
	static final int MAX_TIME_TO_FIRE = 135;
	static final int BULLET_OFFSET_X = HALF_WIDTH - SmallMissile.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - SmallMissile.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1500;
	
	public SwarmTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.SWARM_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new SmallMissile(game, team, this, x + BULLET_OFFSET_X, y + BULLET_OFFSET_Y, target, angleTo(target)));
		game.newGameObjects.add(new SmallMissile(game, team, this, x + BULLET_OFFSET_X, y + BULLET_OFFSET_Y, target, sumAngles(angleTo(target), Math.PI/2)));
		game.newGameObjects.add(new SmallMissile(game, team, this, x + BULLET_OFFSET_X, y + BULLET_OFFSET_Y, target, sumAngles(angleTo(target), Math.PI/-2 )));
	}
}
