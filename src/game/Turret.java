package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import ui.ShapeManager;

public class Turret extends Unit implements Drawable {
	
	static final int WIDTH = 18;
	static final int HEIGHT = 18;
	static final int HALF_WIDTH = WIDTH/2;
	static final int HALF_HEIGHT = HEIGHT/2;
	static final double FIREABLE_DIST = 170;
	static final double MAX_TURN_ANGLE = Math.PI/90.0;
	static final double MAX_DEV_ANGLE = Math.PI/360.0;
	static final int MAX_TIME_TO_FIRE = 45;
	static final int BULLET_OFFSET_X = HALF_WIDTH - TinyBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - TinyBullet.HALF_DIAMETER;
	static final int CONSTRUCTION_TIME = 1800;
	
	Shape shape;
	double angle;
	int timeToFire;
	int maxTimeToFire;
	double fireableDist;
	double maxTurnAngle;
	Unit owner;
	Unit target;
	
	public Turret(Unit owner) {
		super(owner.game, owner.team, owner.x + owner.turretDespl, owner.y + owner.turretDespl, WIDTH, HEIGHT, 0, FIREABLE_DIST);
		this.owner = owner;
		maxTimeToFire = MAX_TIME_TO_FIRE;
		maxTurnAngle = MAX_TURN_ANGLE;
		fireableDist = FIREABLE_DIST;
		shape = game.shapeManager.getShape(ShapeManager.TURRET);
	}
	
	public void setTarget(Unit target) {
		this.target = target;
	}
	
	@Override
	public void step() {
		x = owner.x + owner.turretDespl;
		y = owner.y + owner.turretDespl;
		updateCenter();
		
		if(target != null) {
			double newAngle = angleTo(target);
			double distToTarget = distTo(target);
			
			if(distToTarget > fireableDist && owner.target != null) {
				target = owner.target;
				newAngle = angleTo(target);
				distToTarget = distTo(target);
			}
			
			if(Math.abs(angle - newAngle) <= MAX_DEV_ANGLE) {
				if(timeToFire == 0 && distToTarget <= fireableDist) {
					fire();
					timeToFire = maxTimeToFire;
				}
			} else {
				angle = newAngle(angle, newAngle, maxTurnAngle);
			}
		}
		
		if(timeToFire > 0) timeToFire--;
	}
	
	public void fire() {
		game.newGameObjects.add(new TinyBullet(game, owner.team, owner, x + BULLET_OFFSET_X, y + BULLET_OFFSET_Y, angle));
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.rotate(angle - owner.angle, HALF_WIDTH - 4, HALF_HEIGHT);
		g2d.setColor(Color.WHITE);
		g2d.fill(shape);
	}
}
