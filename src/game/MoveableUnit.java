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
	
	public void moveToPoint(Point p, boolean override) {
		setFlagOn(Flag.MOVING);
		if(override && (isFlagOn(Flag.MOVING) || isFlagOn(Flag.FIRING))) {
			target = null;
			setFlagOn(Flag.OVERRIDE_ACTION);
			setFlagOff(Flag.MANDATORY_TARGET);
		}
		toX = p.x;
		toY = p.y;
		newAngle = angleTo(p.x, p.y);
	}
}
