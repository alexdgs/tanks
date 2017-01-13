package game;

import ui.ShapeManager;

public class EnergyTank extends Tank {
	
	static final double SPEED = 1.05;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 160.0;
	static final double FIREABLE_DIST = 155.0;
	static final double MAX_TURN_ANGLE = Math.PI/210.0;
	static final int MAX_TIME_TO_FIRE = 125;
	static final int BULLET_OFFSET_X = HALF_WIDTH - ElectricBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - ElectricBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1500;

	public EnergyTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.ENERGY_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new ElectricBullet(this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
}