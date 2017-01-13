package game;

import ui.ShapeManager;

public class EagerSmallMissile extends SmallMissile {
	
	static final double SPEED = 2.25;
	static final double MAX_HOMING_RATE = Math.PI / 69.0;
	static final double MAGNET_DISTANCE = 50.0;
	static final double MAX_EAGER_DISTANCE = 90.0;
	static final double HOMING_DELTA = Math.PI / 1380.0;
	
	double currentTurnAngle = 0.0;
	int currentDirection = Direction.CLOCKWISE;
	
	public EagerSmallMissile(Game game, TeamManager team, Unit owner, double x, double y, Unit target, double angle) {
		super(game, team, owner, x, y, target, angle);
		homingRate = MAX_HOMING_RATE;
		shape = game.shapeManager.getShape(ShapeManager.EAGER_SMALL_MISSILE);
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
				currentTurnAngle = 0.0;
			}
		}
		
		if(!seekingTarget) {
			locateNewTarget();
		}
		
		moveStraight();
	}
	
	@Override
	public double newAngle(double lastAngle, double targetAngle, double maxTurnAngle) {
		int newDirection = Direction.CLOCKWISE;
		double newAngle = 0.0;
		double difAngle = lastAngle - targetAngle;
		
		if(difAngle >= 0.0) {
			if(difAngle < Math.PI)
				newDirection = Direction.COUNTER_CLOCKWISE;
		} else {
			difAngle = Math.abs(difAngle);
			if(difAngle >= Math.PI)
				newDirection = Direction.COUNTER_CLOCKWISE;
		}
		
		if(newDirection == currentDirection) {
			currentTurnAngle = Math.min(currentTurnAngle + HOMING_DELTA, maxTurnAngle);
		} else {
			currentTurnAngle = Math.max(currentTurnAngle - HOMING_DELTA, 0);
			if(currentTurnAngle == 0) currentDirection ^= 1;
			else currentTurnAngle *= (1.0 - 0.05*(Math.min(difAngle, maxTurnAngle)/maxTurnAngle));
		}
		
		if(currentDirection == Direction.CLOCKWISE) {
			newAngle = lastAngle + currentTurnAngle;
		} else {
			newAngle = lastAngle - currentTurnAngle;
		}
		
		if(newAngle < MINUS_HALF_PI) newAngle += TWICE_PI;
		else if(newAngle > THREE_HALVES_PI) newAngle -= TWICE_PI;
		
		return newAngle;
	}
	/*
	@Override
	public void adjustAngle() {
		//newAngle;
		//homingRate = (0.6 + 0.4*(1.0 - Math.min(1.0, distToCenter(target)/MAX_EAGER_DISTANCE)))* HOMING_RATE;
		int newDirection = getDirectionToAngle(angle, newAngle);
		if(newDirection == currentDirection) {
			homingRate = Math.min(homingRate + HOMING_DELTA, MAX_HOMING_RATE);
		} else {
			homingRate = Math.max(homingRate - HOMING_DELTA, 0);
			if(homingRate == 0) {
				currentDirection ^= 1;
			}
		}
		super.adjustAngle();
	}
	*/
	public void locateNewTarget() {
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && distToCenter(u) <= MAGNET_DISTANCE) {
					target = u;
					seekingTarget = true;
					return;
				}
			}
		}
	}
}
