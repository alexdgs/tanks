package game;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class CurvyBullet extends HomingProjectile implements Drawable {
	
	static final int DIAMETER = 5;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double SPEED = 1.0;
	static final int DAMAGE = 3;
	static final double HOMING_RATE = Math.PI/180.0;
	
	static final double MAX_DEV_ANGLE = Math.PI/2.25;
	static final double MIN_DEV_ANGLE = MAX_DEV_ANGLE*-1;
	static final double DELTA_ANGLE = Math.PI/30.0;
	
	static final double MAGNET_DISTANCE = 40.0;
	
	double devAngle;
	boolean incAngle;
	
	public CurvyBullet(Unit owner, double x, double y, Unit target, double angle) {
		super(owner.game, owner.team, owner, x, y, DIAMETER, DIAMETER, target, SPEED, angle, HOMING_RATE);
		devAngle = Math.random() * (Math.random() > 0.5 ? MAX_DEV_ANGLE : MIN_DEV_ANGLE);
		incAngle = false;
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
	
	@Override
	public void move() {
		if(seekingTarget) {
			if(target != null && target.isFlagOn(Flag.ALIVE)) {
				newAngle = angleTo(target);
				if(angle != newAngle) {
					adjustAngle();
				}
			} else {
				seekingTarget = false;
				speed = 1;
			}
		} else {
			getTarget();
			if(target != null) {
				seekingTarget = true;
				speed = SPEED;
			}
		}
		moveStraight();
	}
	
	@Override
	public void setSpeeds() {
		if(incAngle){
			if(devAngle < MAX_DEV_ANGLE) {
				devAngle += DELTA_ANGLE;
			} else {
				incAngle = false;
			}
		} else {
			if(devAngle > MIN_DEV_ANGLE) {
				devAngle -= DELTA_ANGLE;
			} else {
				incAngle = true;
			}
		}
		dX = speed*cos(sumAngles(angle, devAngle));
		dY = speed*sin(sumAngles(angle, devAngle));
	}
	
	public void getTarget() {
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && distToCenter(u) <= MAGNET_DISTANCE) {
					target = u;
					return;
				}
			}
		}
	}
	
	public boolean hit(GameObject go) {
		 if((new Ellipse2D.Double(x, y, DIAMETER, DIAMETER)).intersects(go.getBounds())) return true;
		 return false;
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.setColor(team.color);
		g2d.fillOval(x, y, DIAMETER, DIAMETER);
	}
}
