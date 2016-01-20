package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class TNT extends Bullet implements Drawable {
	
	static final int DIAMETER = 8;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double SPEED = 1.0;
	static final int DAMAGE = 0;
	static final int MAX_DAMAGE = 10;
	static final double MAX_DISTANCE = 30.0;
	static final double ROTATE_SPEED = Math.PI/60.0;
	static final int CONSTRUCTION_TIME = 2000;
	
	double angle;
	
	public TNT(Game game, TeamManager team, Unit owner, double x, double y, double angle) {
		super(game, team, owner, x, y, DIAMETER, DIAMETER, SPEED, angle, DIAMETER, DAMAGE);
		angle = owner.angle;
	}
	
	@Override
	public void advanceTime() {
		angle = sumAngles(angle, ROTATE_SPEED);
	}
	
	@Override
	public void destroy() {
		double dist = 0.0;
		for(TeamManager tm : team.enemies) {
			for(Unit u : tm.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && (dist = (distToCenter(u) - (u.halfWidth + HALF_DIAMETER))) <= MAX_DISTANCE) {
					hit(u, (int)((1.0 - dist/MAX_DISTANCE) * MAX_DAMAGE));
				}
			}
		}
		
		setFlagOff(Flag.ALIVE);
		game.newGameObjects.add(new Circle((int)centerX, (int)centerY, (int)(w*2.5), Circle.Type.EXPL));
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.rotate(angle, centerX, centerY);
		g2d.fillRoundRect(x, y, DIAMETER, DIAMETER, 3, 3);
		g2d.setTransform(backup);
	}
	
}
