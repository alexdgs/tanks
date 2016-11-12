package game;

import java.awt.Color;
import java.awt.Graphics2D;

import ui.ShapeManager;

public class MineTank extends Tank {
	static final double SPEED = 1.05;
	static final int HIT_POINTS = 80;
	static final double VISIBLE_DIST = 160.0;
	static final double FIREABLE_DIST = 5.0;
	static final double MAX_TURN_ANGLE = Math.PI/120.0;
	static final int MAX_TIME_TO_FIRE = 10;
	static final int BULLET_OFFSET_X = Tank.HALF_WIDTH - Mine.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = Tank.HALF_HEIGHT - Mine.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1200;
	
	public MineTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.MINE_TANK);
		setFlagOff(Flag.CAN_FIRE);
	}
	
	@Override
	public void step() {
		if(hitPoints < 1) {
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		if(isFlagOn(Flag.MOVING)) {
			if(distCenterTo(toX, toY) < 5) {
				setFlagOff(Flag.OVERRIDE_ACTION);
				putMine();
				setTargetPoint(getRandomMapPoint());
			} else {
				setFlagOn(Flag.MOVING);
				newAngle = angleToCenter(toX, toY);
				if(Math.abs(angle - newAngle) <= MAX_DIF_ANGLE_TO_MOVE) {
					moveStraight();
				} else {
					angle = newAngle(angle, newAngle, maxTurnAngle);
				}
			}
		} else {
			moveToPoint(getRandomMapPoint(), false);
		}
		
		stepEffects();
	}
	
	public void putMine() {
		game.newGameObjects.add(new Mine(game, team, this, centerX, centerY, 0.0));
	}
	
	@Override
	public void defendPoint(double x, double y) {
		
	}
	
	@Override
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(3, 15, 8, 8);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(24, 15, 3, 6);
		g2d.fillRect(24, 9, 3, 6);
	}
}