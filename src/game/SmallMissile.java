package game;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import ui.ShapeManager;

public class SmallMissile extends HomingProjectile implements Drawable {
	
	static final int HITTABLE_DIAMETER = 10;
	static final int HALF_DIAMETER = HITTABLE_DIAMETER/2;
	static final double SPEED = 1.8;
	static final int DAMAGE = 3;
	static final double HOMING_RATE = Math.PI / 128.0;
	
	Shape shape;

	public SmallMissile(Game game, TeamManager team, Unit owner, double x, double y, Unit target, double angle) {
		super(game, team, owner, x, y, HITTABLE_DIAMETER, HITTABLE_DIAMETER, target, SPEED, angle, HOMING_RATE);
		shape = game.shapeManager.getShape(ShapeManager.SMALL_MISSILE);
	}
	
	@Override
	public void step() {
		//bounce();
		move();
		updateCenter();
		advanceTime();
		
		if(isInsideMap()) {
			for(TeamManager team : team.enemies) {
				for(Unit u : team.getUnits()) {
					if(hit(u)) {
						hit(u, DAMAGE);
						destroy();
						return;
					}
				}
			}
		} else destroy();
	}
	
	public boolean hit(GameObject go) {
		 if((new Ellipse2D.Double(x, y, HITTABLE_DIAMETER, HITTABLE_DIAMETER)).intersects(go.getBounds())) return true;
		 return false;
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.translate(x, y);
		g2d.rotate(angle, HALF_DIAMETER, HALF_DIAMETER);
		g2d.fill(shape);
		g2d.setTransform(backup);
	}
}
