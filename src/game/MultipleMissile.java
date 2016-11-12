package game;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import ui.ShapeManager;

public class MultipleMissile extends Bullet implements Drawable {
	
	static final int DIAMETER = 15;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double INIT_SPEED = 0.05;
	static final double ACCELERATION = 0.01;
	static final int DAMAGE = 3;
	
	static final int INIT_TIME_FIRE = 75;
	static final int GAP_TIME_FIRE = 30;
	static final int MISSILES = 5;
	
	Shape shape;
	int time;
	double speed;
	int remMissiles;
	boolean firing;
	
	Unit target;

	public MultipleMissile(Game game, TeamManager team, Unit owner, double x, double y, Unit target, double angle) {
		super(game, team, owner, x, y, DIAMETER, DIAMETER, INIT_SPEED, angle, DIAMETER, DAMAGE);
		this.target = target;
		remMissiles = MISSILES;
		shape = game.shapeManager.getShape(ShapeManager.MULTIPLE_MISSILE);
	}
	
	@Override
	public void step() {
		//bounce();
		calcSpeeds();
		move();
		updateCenter();
		advanceTime();
		if(isInsideMap()) {
			if(firing) {
				if(time % GAP_TIME_FIRE == 0) {
					fire();
					remMissiles--;
				}
			} else if(time == INIT_TIME_FIRE) {
				firing = true;
			}
			if(remMissiles < 1) destroy();
		} else destroy();
	}
	
	@Override
	public void advanceTime() {
		time++;
	}
	
	public void destroy() {
		super.destroy();
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(hit(u)) {
					hit(u, damage);
				}
			}
		}
	}
	
	public void calcSpeeds() {
		speed += ACCELERATION;
		speedX = speed*cos(angle);
		speedY = speed*sin(angle);
	}
	
	@Override
	public void bounce() {
		double x2 = x + speedX;
		double y2 = y + speedY;
		if(x2 < 0 || x2 > maxX) {
			speedX *= -1;
			angle = Math.PI - angle;
		}
		if(y2 < 0 || y2 > maxY) {
			speedY *= -1;
			angle = Math.PI*2 - angle;
		}
	}
	
	public void fire() {
		game.newGameObjects.add(new EagerSmallMissile(game, team, target, centerX - EagerSmallMissile.HALF_DIAMETER, centerY - EagerSmallMissile.HALF_DIAMETER, target, angle));
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.translate(x, y);
		g2d.rotate(angle, HALF_DIAMETER, HALF_DIAMETER);
		g2d.fill(shape);
		//System.out.println("MISSILE");
		g2d.setTransform(backup);
	}

}
