package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class BuildingTurret extends Turret {
	
	static final double FIREABLE_DIST = 190;
	static final double MAX_TURN_ANGLE = Math.PI/60.0;
	static final int MAX_TIME_TO_FIRE = 20;

	public BuildingTurret(Unit owner) {
		super(owner);
		maxTimeToFire = MAX_TIME_TO_FIRE;
		maxTurnAngle = MAX_TURN_ANGLE;
		fireableDist = FIREABLE_DIST;
	}
	
	@Override
	public void step() {
		if(target != null && target.isFlagOn(Flag.ALIVE)) {
			double newAngle = angleTo(target);
			double distToTarget = distTo(target);
			
			/*if(distToTarget > fireableDist && owner.target != null) {
				target = owner.target;
				newAngle = angleTo(target);
				distToTarget = distTo(target);
			}*/
			
			if(Math.abs(angle - newAngle) <= MAX_DEV_ANGLE) {
				if(timeToFire == 0 && distToTarget <= fireableDist) {
					fire();
					timeToFire = maxTimeToFire;
				}
			} else {
				angle = newAngle(angle, newAngle, maxTurnAngle);
			}
		} else target = getTarget();
		
		if(timeToFire > 0) timeToFire--;
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.translate(x + owner.turretDespl, y + owner.turretDespl);
		g2d.rotate(angle - owner.angle, HALF_WIDTH - 4, HALF_HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.fill(shape);
		g2d.setTransform(backup);
	}
}
