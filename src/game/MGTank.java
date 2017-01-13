package game;

import java.awt.Graphics2D;
import java.awt.Point;

import ui.ShapeManager;

public class MGTank extends Tank implements Drawable {
	
	static final double SPEED = 1.25;
	static final int HIT_POINTS = 70;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/90.0;
	static final int MAX_TIME_TO_FIRE = 100;
	static final int BULLET_OFFSET_X = 0;
	static final int BULLET_OFFSET_Y = 0;
	static final int TURRET_DESPL = (WIDTH - Turret.WIDTH)/2;
	public static final int CONSTRUCTION_TIME = 900;
	
	Turret turret;

	public MGTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.MG_TANK);
		setFlagOn(Flag.CAN_FIRE);
		turretDespl = TURRET_DESPL;
		turret = new MGTurret(this);
	}
	
	@Override
	public void move() {
		if(isFlagOn(Flag.MANDATORY_TARGET) && !target.isFlagOn(Flag.ALIVE)) {
			target = null;
			setFlagOff(Flag.MANDATORY_TARGET);
		}
		
		if(isFlagOn(Flag.MANDATORY_TARGET) || (!isFlagOn(Flag.OVERRIDE_ACTION) && isFlagOn(Flag.AI_CONTROLLED) && (target = getTarget()) != null)) {
			double distToTarget = distToCenter(target);
			if(distToTarget > fireableDist) {
				setFlagOff(Flag.FIRING);
				moveToPoint(new Point((int)target.centerX, (int)target.centerY), false);
				setFlagOn(Flag.MOVING);
			} else {
				setFlagOff(Flag.MOVING);
			}
		} else {
			setFlagOff(Flag.FIRING);
		}
		
		if(isFlagOn(Flag.MOVING)) {
			if(distCenterTo(toX, toY) < 5) {
				setFlagOff(Flag.MOVING);
				setFlagOff(Flag.OVERRIDE_ACTION);
				targetPoint = null;
			} else {
				newAngle = angleToCenter(toX, toY);
				if(Math.abs(angle - newAngle) <= MAX_DIF_ANGLE_TO_MOVE) {
					moveStraight();
				} else {
					angle = newAngle(angle, newAngle, maxTurnAngle);
				}
			}
		} else if(!isFlagOn(Flag.FIRING)){
			if(isFlagOn(Flag.AI_CONTROLLED)) {
				if(targetPoint != null) moveToPoint(targetPoint, false);
				else {
					checkUnitStrategy();
					if(strategy == STRATEGY_REPAIR){
						if(distCenterTo(team.xHealer, team.yHealer) >= Healer.VISIBLE_DIST) {
							moveToPoint(new Point(team.xHealer, team.yHealer), false);
						}
					} else {
						if(team.strategy == TeamManager.STRATEGY_BASE){
							if(distCenterTo(team.getBasePoint().x, team.getBasePoint().y) > TeamManager.MAX_DIST_BASE_ON_DEFENSE){
								moveToPoint(team.getBasePoint(), false);
							}
						} else {
							if(group != null) {
								if(group.deployed) {
									Point p = group.getTargetPoint();
									moveToPoint(p != null ? p : getRandomMapPoint(), false);
								}
							} else {
								moveToPoint(getRandomMapPoint(), false);
							}
						}
					}
				}
			}
		}
		
		if(isFlagOn(Flag.FIRING) && timeToFire == 0) {
			fire();
			timeToFire = maxTimeToFire;
		} else if(timeToFire > 0) timeToFire--;
	}
	
	@Override
	public void addSteps() {
		if(timeRemainingFrozen == 0) {
			turret.setTarget(target);
			turret.step();
		}
	};
	
	@Override
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.translate(TURRET_DESPL, TURRET_DESPL);
		turret.draw(g2d, 0, 0);
	}
}
