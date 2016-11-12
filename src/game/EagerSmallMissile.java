package game;

import ui.ShapeManager;

public class EagerSmallMissile extends SmallMissile {
	
	static final double SPEED = 2.2;
	static final double HOMING_RATE = Math.PI / 90.0;
	static final double MAGNET_DISTANCE = 45.0;
	
	public EagerSmallMissile(Game game, TeamManager team, Unit owner, double x, double y, Unit target, double angle) {
		super(game, team, owner, x, y, target, angle);
		homingRate = HOMING_RATE;
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
			} else seekingTarget = false;
		}
		
		if(!seekingTarget) {
			locateNewTarget();
		}
		
		moveStraight();
	}
	
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
