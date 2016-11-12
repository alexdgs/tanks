package game;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.ShapeManager;

public class ShieldedTank extends Tank {
	
	static final double SPEED = 0.8;
	static final int HIT_POINTS = 180;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/210.0;
	static final int MAX_TIME_TO_FIRE = 80;
	static final int BULLET_OFFSET_X = HALF_WIDTH - CurvyBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - CurvyBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 2000;
	
	public ShieldedTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.SHIELDED_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new SmallBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
		//game.newGameObjects.add(new CurvyBullet(this, x + bulletOffsetX, y + bulletOffsetY, target, angle));
		//game.newGameObjects.add(new MultipleMissile(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle, target));
	}
	
	@Override
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(9, 12, 9, 6);
	}
}
