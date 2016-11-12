package game;

import ui.ShapeManager;

public class BombTank extends Tank {
	static final double SPEED = 0.8;
	static final int HIT_POINTS = 70;
	static final double VISIBLE_DIST = 160.0;
	static final double FIREABLE_DIST = 5.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_FIRE = 10;
	static final int BULLET_OFFSET_X = 0;
	static final int BULLET_OFFSET_Y = 0;
	public static final int CONSTRUCTION_TIME = 1100;
	
	public BombTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.BOMB_TANK);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public Unit getTarget() {
		Unit newTarget = null;
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && distToCenter(u) <= visibleDist) {
					if(newTarget == null || (!newTarget.isFlagOn(Flag.CAN_FIRE) && u.isFlagOn(Flag.CAN_FIRE))) {
						newTarget = u;
						fireableDist = halfWidth + u.halfWidth;
					} else if(newTarget.hitPoints < u.hitPoints) {
						newTarget = u;
						fireableDist = halfWidth + u.halfWidth;
					}
				}
			}
		}
		return newTarget;
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new GradualWave(this, centerX, centerY));
		hitted(hitPoints);
	}
}
