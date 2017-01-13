package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import ui.ShapeManager;

public class DoubleGun extends DefenseBuilding {
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int TIME_TO_FIRE = 150;
	static final int HIT_POINTS = 175;
	static final int VISIBLE_DIST = 200;
	
	static final double MIN_ANGLE_DIFF_TO_FIRE = Math.PI/360.0;
	static final double MAX_TURN_ANGLE = Math.PI/180.0;
	static final int BULLET_OFFSET_X = WIDTH/2 - SimpleBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HEIGHT/2 - SimpleBullet.HALF_DIAMETER;
	
	static final int ABSOLUTE_OFFSET = 6;
	static final double START_FIRE_MOVE = 0.6;
	static final double FIRE_MOVE_DELTA = 0.025;
	static final Image IMG_DETAIL_DOUBLE_GUN = (new ImageIcon("src/img/double_gun_detail.png")).getImage();
	
	double currentFireMove = 0.0;
	double fireMovePosition = 0.0;
	
	public DoubleGun(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, WIDTH, HEIGHT, HIT_POINTS, VISIBLE_DIST, TIME_TO_FIRE, ShapeManager.DOUBLE_GUN);
		angle = 0;
	}
	
	@Override
	public void move() {
		target = getTarget();
		if(target != null) {
			double angleToTarget = angleTo(target);
			if(Math.abs(angleToTarget - angle) <= MIN_ANGLE_DIFF_TO_FIRE){
				setFlagOn(Flag.FIRING);
			} else {
				angle = newAngle(angle, angleToTarget, MAX_TURN_ANGLE);
				setFlagOff(Flag.FIRING);
			}
		} else setFlagOff(Flag.FIRING);
		
		if(isFlagOn(Flag.FIRING) && timeToFire == 0) {
			fire();
			timeToFire = maxTimeToFire;
		} else if(timeToFire > 0) timeToFire--;
		
		if(fireMovePosition > 0.0) {
			fireMovePosition = Math.max(fireMovePosition + currentFireMove, 0.0);
			currentFireMove -= FIRE_MOVE_DELTA;
		}
	}
	
	@Override
	public void fire() {
		int bulletOffsetX1 = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle)*ABSOLUTE_OFFSET);
		int bulletOffsetY1 = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle + THREE_HALVES_PI)*ABSOLUTE_OFFSET);
		
		int bulletOffsetX2 = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle)*ABSOLUTE_OFFSET*-1);
		int bulletOffsetY2 = ABSOLUTE_OFFSET*2 + (int)(Math.sin(angle + THREE_HALVES_PI)*ABSOLUTE_OFFSET*-1);
		
		game.newGameObjects.add(new SimpleBullet(game, team, this, x + bulletOffsetX1, y + bulletOffsetY1, angle));
		game.newGameObjects.add(new SimpleBullet(game, team, this, x + bulletOffsetX2, y + bulletOffsetY2, angle));
		
		fireMovePosition = currentFireMove = START_FIRE_MOVE;
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		drawPrevElements(g2d, x, y);
		
		AffineTransform backup = g2d.getTransform();
		g2d.translate(x - fireMovePosition*cos(angle), y - fireMovePosition*sin(angle));
		g2d.rotate(angle, WIDTH/2, HEIGHT/2);
		g2d.setColor(team.color);
		g2d.fill(shape);
		g2d.drawImage(IMG_DETAIL_DOUBLE_GUN, 0, 0, null);
		g2d.setTransform(backup);
		
		drawAddElements(g2d, x, y);
		drawHUDElements(g2d, x, y);
	}
}
