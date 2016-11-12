package game;

import java.awt.Color;
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
	public static final int CONSTRUCTION_TIME = 900;
	
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
		
		if(target == null || !target.isFlagOn(Flag.ALIVE)|| target.hitPoints == target.maxHitPoints || distToCenter(target) > healableDist) {
			target = getTarget();
		}
		
		if(target != null) {
			double distToTarget = distToCenter(target);
			if(distToTarget > healableDist) {
				setFlagOff(Flag.HEALING);
				moveToPoint(new Point((int)target.x, (int)target.y), false);
			} else {
				targetPoint = null;
				setFlagOff(Flag.MOVING);
				setFlagOn(Flag.HEALING);
			}
		} else {
			setFlagOff(Flag.HEALING);
		}
		
		if(isFlagOn(Flag.MOVING)) {
			newAngle = angleToCenter(toX, toY);
			if(Math.abs(angle - newAngle) >= Math.PI/180.0) angle = newAngle(angle, newAngle, maxTurnAngle);
			else {
				moveStraight();
				if(distCenterTo(toX, toY) < 5) {
					setFlagOff(Flag.MOVING);
					targetPoint = null;
				}
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
				if(u.hitPoints < u.maxHitPoints && distToCenter(u) <= visibleDist) {
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
		g2d.setColor(Color.WHITE);
		g2d.fillRect(24, 15, 3, 6);
		g2d.fillRect(24, 9, 3, 6);
	}
}
