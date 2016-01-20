package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import ui.ShapeManager;

public class MedicTank extends Tank {
	
	static final double SPEED = 1.5;
	static final int HIT_POINTS = 60;
	static final double VISIBLE_DIST = 170.0;
	static final double ACCIONABLE_DIST = 60.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_HEAL = 70;
	static final int HEAL_POINTS = 1;
	public static final int CONSTRUCTION_TIME = 1000;
	
	static final Image IMG_MEDIC_SIGN = (new ImageIcon("src/img/medic_sign.png")).getImage();
	
	int timeToHeal;
	int maxTimeToHeal;
	double healableDist;

	public MedicTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, 0, 0, MAX_TURN_ANGLE, 0, 0, ShapeManager.MEDIC_TANK);
		healableDist = ACCIONABLE_DIST;
		maxTimeToHeal = MAX_TIME_TO_HEAL;
		setFlagOff(Flag.CAN_FIRE);
	}
	
	@Override
	public void step() {
		if(hitPoints < 1) {
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		if(target == null || !target.isFlagOn(Flag.ALIVE)|| target.hitPoints == target.maxHitPoints || distTo(target) > healableDist) {
			target = getTarget();
		}
		
		if(target != null) {
			double distToTarget = distTo(target);
			if(distToTarget > healableDist) {
				setFlagOff(Flag.HEALING);
				moveToPoint(new Point((int)target.x, (int)target.y), false);
			} else {
				setFlagOff(Flag.MOVING);
				setFlagOn(Flag.HEALING);
			}
		} else {
			setFlagOff(Flag.HEALING);
		}
		
		if(isFlagOn(Flag.MOVING)) {
			if(angle != newAngle) angle = newAngle(angle, newAngle, maxTurnAngle);
			else {
				moveStraight();
				if(distTo(toX, toY) < 5) setFlagOff(Flag.MOVING);
			}
		} else if(!isFlagOn(Flag.HEALING)){
			moveToPoint(getRandomMapPoint(), false);
		}
		
		if(isFlagOn(Flag.HEALING) && timeToHeal == 0) {
			heal();
			timeToHeal = maxTimeToHeal;
		} else if(timeToHeal > 0) timeToHeal--;
		
		stepEffects();
	}
	
	@Override
	public Unit getTarget() {
		Unit newTarget = null;
		for(Unit u : team.getUnits()) {
			if(u != this && u instanceof Unit && u.isFlagOn(Flag.ALIVE)) {
				if(u.hitPoints < u.maxHitPoints && distTo(u) <= visibleDist) {
					newTarget = u;
					break;
				}
			}
		}
		
		return newTarget;
	}
	
	public void heal() {
		target.heal(HEAL_POINTS);
	}
	
	@Override
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.drawImage(IMG_MEDIC_SIGN, 6, 9, null);
	}
}
