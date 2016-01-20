package game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.ArrayList;

import ui.ShapeManager;

public class Laser extends InstantProjectile implements Drawable {
	
	static final int EFFECT_TIME = 40;
	static final Color COLOR = Color.CYAN;
	static final float THICKNESS = 4.0f;
	static final int DAMAGE = 3;
	
	int time;
	float alpha;
	Shape shape;
	
	public Laser(Unit owner, Unit target) {
		super(owner.game, owner.team, owner, target, (int)target.centerX, (int)target.centerY, (int)owner.centerX, (int)owner.centerY);
		time = 0;
		alpha = 1.0f;
		shape = buildShape();
	}
	
	public Shape buildShape() {
		ArrayList<Point> points = new ArrayList<Point>(4);
		double angle = owner.angleTo(target);
		angle = sumAngles(angle, HALF_PI);
		double dx = cos(angle)*THICKNESS/2.0;
		double dy = sin(angle)*THICKNESS/2.0;
		points.add(new Point((int)(startX - dx), (int)(startY - dy)));
		points.add(new Point((int)(startX + dx), (int)(startY + dy)));
		points.add(new Point((int)(endX + dx), (int)(endY + dy)));
		points.add(new Point((int)(endX - dx), (int)(endY - dy)));
		return ShapeManager.buildShape(points);
	}

	@Override
	public void step() {
		if(time == EFFECT_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		if(time == 0) {
			for(TeamManager tm : team.enemies) {
				for(Unit u : tm.getUnits()) {
					if(intersects(u) && u.isFlagOn(Flag.ALIVE)) hit(u, DAMAGE);
				}
			}
		}
			
		time++;
		alpha = 1.0f - (float)time/EFFECT_TIME;
	}
	
	public boolean intersects(Unit u) {
		return shape.intersects(u.getBounds());
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		Stroke backup = g2d.getStroke();
		g2d.setStroke(new BasicStroke(THICKNESS));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setColor(COLOR);
		g2d.drawLine(x + startXd, y + startYd, x + endXd, y + endYd);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setStroke(backup);
	}
}
