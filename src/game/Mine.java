package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import ui.ShapeManager;

public class Mine extends Bullet implements Drawable {
	
	static final int DIAMETER = 12;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double SPEED = 0.0;
	static final int DAMAGE = 90;
	static final int TIME = 8000;
	static final float TIME_STROKE_WIDTH = 1.5f;
	
	Shape shape;
	Arc2D.Double timeArc;
	int remTime;

	public Mine(Game game, TeamManager team, Unit owner, double x, double y, double angle) {
		super(game, team, owner, x, y, DIAMETER, DIAMETER, SPEED, angle, DIAMETER, DAMAGE);
		shape = game.shapeManager.getShape(ShapeManager.MINE);
		remTime = TIME;
		timeArc = new Arc2D.Double(-1, -1, DIAMETER+2, DIAMETER+2, 90.0, 450.0, Arc2D.OPEN);
	}
	
	@Override
	public void advanceTime() {
		if(remTime == 0){
			destroy();
		} else {
			remTime--;
		}
		timeArc.setAngleExtent((remTime/(float)TIME)*360.0);
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform backup = g2d.getTransform();
		g2d.setColor(team.color);
		g2d.translate(x, y);
		g2d.fill(shape);
		
		g2d.setStroke(new BasicStroke(TIME_STROKE_WIDTH));
		g2d.draw(timeArc);
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(4, 4, 3, 3);
		
		g2d.setTransform(backup);
		//g2d.drawString(Integer.toString(remTime), x, y);
	}
}
