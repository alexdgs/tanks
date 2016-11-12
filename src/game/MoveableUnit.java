package game;

import java.awt.Point;

public abstract class MoveableUnit extends Unit {
	
	double speed;

	public MoveableUnit(Game game, TeamManager team, double x, double y, int w, int h, int hitPoints, double speed, double visibleDist) {
		super(game, team, x, y, w, h, hitPoints, visibleDist);
		this.speed = speed;
	}
	
	public void move() {
		
	}
	
	public void moveStraight() {
		x += cos(angle)*speed;
		y += sin(angle)*speed;
		updateCenter();
	}
	
	public void underAttack(double x, double y) {
		if(!isFlagOn(Flag.FIRING) && !isFlagOn(Flag.MOVING) && targetPoint == null) {
			moveToPoint(new Point((int)x, (int)y), false);
		}
	}
	
	public void moveToPoint(Point p, boolean override) {
		if(override) {
			target = null;
			setFlagOn(Flag.OVERRIDE_ACTION);
			setFlagOff(Flag.MANDATORY_TARGET);
		}
		toX = p.x;
		toY = p.y;
		//setTargetPoint(p);
		//newAngle = angleToCenter(p.x, p.y);
		setFlagOn(Flag.MOVING);
	}
}
