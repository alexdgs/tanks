package game;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import ui.ShapeManager;

public class Missile extends Bullet implements Drawable {
	
	static final int HITTABLE_DIAMETER = 15;
	static final int HALF_DIAMETER = HITTABLE_DIAMETER/2;
	static final double SPEED = 1.5;
	static final int DAMAGE = 15;
	
	static final int NUM_FRAMES = 4; 
	static final int TICKS_PER_FRAME = 10;
	static final int TIME_ALL_FRAMES = NUM_FRAMES*TICKS_PER_FRAME;
	
	Shape[] shape;
	int time;
	int frame;

	public Missile(Game game, TeamManager team, Unit owner, double x, double y, double angle) {
		super(game, team, owner, x, y, HITTABLE_DIAMETER, HITTABLE_DIAMETER, SPEED, angle, HITTABLE_DIAMETER, DAMAGE);
		shape = new Shape[NUM_FRAMES];
		shape[0] = game.shapeManager.getShape(ShapeManager.MISSILE_1);
		shape[1] = game.shapeManager.getShape(ShapeManager.MISSILE_2);
		shape[2] = game.shapeManager.getShape(ShapeManager.MISSILE_3);
		shape[3] = game.shapeManager.getShape(ShapeManager.MISSILE_2);
	}
	
	@Override
	public void advanceTime() {
		time = (time+1) % TIME_ALL_FRAMES;
		frame = time/TICKS_PER_FRAME;
	}
	
	@Override
	public void bounce() {
		double x2 = x + speedX;
		double y2 = y + speedY;
		if(x2 < 0 || x2 > maxX) {
			speedX *= -1;
			angle = Math.PI - angle;
		}
		if(y2 < 0 || y2 > maxY) {
			speedY *= -1;
			angle = Math.PI*2 - angle;
		}
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.translate(x, y);
		g2d.rotate(angle, HALF_DIAMETER, HALF_DIAMETER);
		g2d.fill(shape[frame]);
		g2d.setTransform(backup);
	}

}
