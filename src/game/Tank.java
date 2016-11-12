package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public abstract class Tank extends MoveableUnit implements Drawable, Selectable {
	
	static final int STRATEGY_NORMAL = 1;
	static final int STRATEGY_REPAIR = 2;
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int HALF_WIDTH = WIDTH/2;
	static final int HALF_HEIGHT = HEIGHT/2;
	
	static final double MAX_DIF_ANGLE_TO_MOVE = Math.PI/360.0;
	static final double MAX_DIF_ANGLE_TO_FIRE = Math.PI/90.0;
	
	int type;
	int timeToFire;
	int maxTimeToFire;
	double fireableDist;
	double maxTurnAngle;
	int bulletOffsetX;
	int bulletOffsetY;
	Image imgTank;
	Shape shape;
	
	int strategy;
	
	public Tank(Game game, TeamManager team, double x, double y, int hitPoints, double speed, double visibleDist, double fireableDist, int maxTimeToFire, double maxTurnAngle, int bulletOffsetX, int bulletOffsetY, int type) {
		super(game, team, x, y, WIDTH, HEIGHT, hitPoints, speed, visibleDist);
		this.maxTimeToFire = maxTimeToFire;
		this.fireableDist = fireableDist;
		this.maxTurnAngle = maxTurnAngle;
		this.bulletOffsetX = bulletOffsetX;
		this.bulletOffsetY = bulletOffsetY;
		this.type = type;
		timeToFire = 0;
		strategy = STRATEGY_NORMAL;
		shape = game.shapeManager.getShape(type);
		if(team.isFlagOn(TeamManager.Flag.AI_TEAM)) {
			setFlagOn(Flag.AI_CONTROLLED);
		} else setFlagOn(Flag.SELECTABLE);
		setFlagOn(Flag.ENEMY_AWARE);
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
			setFlagOff(Flag.MOVING);
		}
		
		if(isFlagOn(Flag.MANDATORY_TARGET) || (!isFlagOn(Flag.OVERRIDE_ACTION) && isFlagOn(Flag.ENEMY_AWARE) && (target = getTarget()) != null)) {
			targetPoint = null;
			double distToTarget = distToCenter(target);
			if(distToTarget > fireableDist) {
				setFlagOff(Flag.FIRING);
				setTargetPoint(new Point((int)target.x, (int)target.y));
				setFlagOn(Flag.MOVING);
			} else {
				setFlagOff(Flag.MOVING);
				newAngle = angleTo(target);
				if(Math.abs(angle - newAngle) > MAX_DIF_ANGLE_TO_MOVE) {
					angle = newAngle(angle, newAngle, maxTurnAngle);
				}
				if(Math.abs(angle - newAngle) <= MAX_DIF_ANGLE_TO_FIRE) {
					setFlagOn(Flag.FIRING);
				} else {
					setFlagOff(Flag.FIRING);
				}
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
							moveToPoint(getRandomMapPoint(), false);
						} 
					}
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
	
	@Override
	public boolean hitted(int damage) {
		boolean out = super.hitted(damage);
		if(!out) {
			int oldStrategy = strategy;
			checkUnitStrategy();
			if(strategy == STRATEGY_REPAIR && oldStrategy == STRATEGY_NORMAL && !isFlagOn(Flag.FIRING)) {
				setFlagOff(Flag.MOVING);
				targetPoint = null;
			}
		}
		return out;
	}
	
	@Override
	public void checkUnitStrategy() {
		double healthRate = hitPoints/(double)maxHitPoints;
		if(strategy == STRATEGY_NORMAL && healthRate <= 0.3 && team.healerAvailable) {
			strategy = STRATEGY_REPAIR;
		} else if((strategy == STRATEGY_REPAIR && healthRate > 0.9) || !team.healerAvailable) {
			strategy = STRATEGY_NORMAL;
		}
	}
	
	public void defendPoint(double x, double y) {
		targetPoint = new Point((int)x, (int)y);
		//moveToPoint(new Point((int)x, (int)y), false);
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
		if(!isFlagOn(Flag.AI_CONTROLLED)) {
			if(isFlagOn(Flag.ENEMY_AWARE)) {
				g2d.setColor(Color.RED);
				g2d.fillRect(9, 12, 6, 6);
			}
		}
	}
	
	static class Attribute
	{
		public static final int HIT_POINTS = 0;
		public static final int SPEED = 1;
		public static final double VISIBLE_DIST = 2;
		public static final double FIREABLE_DIST = 3;
	}
}
