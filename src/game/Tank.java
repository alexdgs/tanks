package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public abstract class Tank extends MoveableUnit implements Drawable, Selectable {
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int HALF_WIDTH = WIDTH/2;
	static final int HALF_HEIGHT = HEIGHT/2;
	
	static final double MAX_DIF_ANGLE_TO_MOVE = Math.PI/360.0;
	static final double MAX_DIF_ANGLE_TO_FIRE = Math.PI/360.0;
	
	int type;
	int timeToFire;
	int maxTimeToFire;
	double fireableDist;
	double maxTurnAngle;
	int bulletOffsetX;
	int bulletOffsetY;
	Image imgTank;
	Shape shape;
	
	public Tank(Game game, TeamManager team, double x, double y, int hitPoints, double speed, double visibleDist, double fireableDist, int maxTimeToFire, double maxTurnAngle, int bulletOffsetX, int bulletOffsetY, int type) {
		super(game, team, x, y, WIDTH, HEIGHT, hitPoints, speed, visibleDist);
		this.maxTimeToFire = maxTimeToFire;
		this.fireableDist = fireableDist;
		this.maxTurnAngle = maxTurnAngle;
		this.bulletOffsetX = bulletOffsetX;
		this.bulletOffsetY = bulletOffsetY;
		this.type = type;
		timeToFire = 0;
		shape = game.shapeManager.getShape(type);
		if(team.isFlagOn(TeamManager.Flag.AI_TEAM)) {
			setFlagOn(Flag.AI_CONTROLLED);
			//targetPoint = getRandomBaseEnemyPoint();
		} else setFlagOn(Flag.SELECTABLE);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void step() {
		if(hitPoints < 1) {
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		if(isFlagOn(Flag.MANDATORY_TARGET) && !target.isFlagOn(Flag.ALIVE)) {
			target = null;
			setFlagOff(Flag.MANDATORY_TARGET);
		}
		
		if(isFlagOn(Flag.MANDATORY_TARGET) || (!isFlagOn(Flag.OVERRIDE_ACTION) && isFlagOn(Flag.AI_CONTROLLED) && (target = getTarget()) != null)) {
			double distToTarget = distTo(target);
			if(distToTarget > fireableDist) {
				setFlagOff(Flag.FIRING);
				moveToPoint(new Point((int)target.x, (int)target.y), false);
				setFlagOn(Flag.MOVING);
			} else {
				setFlagOff(Flag.MOVING);
				newAngle = angleTo(target);
				if(angle != newAngle) {
					angle = newAngle(angle, newAngle, maxTurnAngle);
					if(Math.abs(angle - newAngle) <= MAX_DIF_ANGLE_TO_FIRE) setFlagOn(Flag.FIRING);
					else setFlagOff(Flag.FIRING);
				} else setFlagOn(Flag.FIRING);
			}
		} else {
			setFlagOff(Flag.FIRING);
		}
		
		if(isFlagOn(Flag.MOVING)) {
			if(Math.abs(angle - newAngle) <= MAX_DIF_ANGLE_TO_MOVE) {
				moveStraight();
				if(distTo(toX, toY) < 5) {
					setFlagOff(Flag.MOVING);
					setFlagOff(Flag.OVERRIDE_ACTION);
					targetPoint = null;
				}
			}
			
			if(angle != newAngle) {
				angle = newAngle(angle, newAngle, maxTurnAngle);
			}
		} else if(!isFlagOn(Flag.FIRING)){
			if(isFlagOn(Flag.AI_CONTROLLED)) {
				if(targetPoint != null) moveToPoint(targetPoint, false);
				else {
					//targetPoint = getRandomBaseEnemyPoint();
					/*if(targetPoint == null)*/ moveToPoint(getRandomMapPoint(), false);
				}
			}
		}
		
		if(isFlagOn(Flag.FIRING) && timeToFire == 0) {
			fire();
			timeToFire = maxTimeToFire;
		} else if(timeToFire > 0) timeToFire--;
		
		addSteps();
		stepEffects();
	}
	
	public void fire() {
		
	}
	
	public void addSteps() {
		
	}
	
	public int randomTimeToFire() {
		return (int)(Math.random()*maxTimeToFire);
	}
	
	@Override
	public void select() {
		setFlagOn(Flag.SELECTED);
	}
	
	@Override
	public void unselect() {
		setFlagOff(Flag.SELECTED);
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		drawPrevElements(g2d, x, y);
		
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.translate(x, y);
		g2d.rotate(angle, HALF_WIDTH, HALF_HEIGHT);
		g2d.fill(shape);
		
		drawDetail(g2d, x, y);
		
		g2d.setTransform(backup);
		
		drawAddElements(g2d, x, y);
	}
	
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(9, 12, 6, 6);
	}
	
	static class Attribute
	{
		public static final int HIT_POINTS = 0;
		public static final int SPEED = 1;
		public static final double VISIBLE_DIST = 2;
		public static final double FIREABLE_DIST = 3;
	}
}
