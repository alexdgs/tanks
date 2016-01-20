package game;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.ShapeManager;

public class IdleTank extends Tank {
		
	static final double SPEED = 1.0;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/180.0;
	static final int MAX_TIME_TO_FIRE = 170;
	static final int BULLET_OFFSET_X = HALF_WIDTH - SimpleBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - SimpleBullet.HALF_DIAMETER;
	
	int hits = 0;

	public IdleTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.CLASSIC_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new SimpleBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
	
	@Override
	public boolean hitted(int damage) {
		hits++;
		return false;
	}
	
	@Override
	public Unit getTarget() {
		return null;
	}
	
	/*@Override
	public Rectangle getBounds() {
		return new Rectangle(0, 0, 1, 1);
	}*/
	
	@Override
	public void drawAddElements(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.drawString(Integer.toString(hits), x, y-5);
	}

}
