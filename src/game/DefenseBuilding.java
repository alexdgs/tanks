package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public abstract class DefenseBuilding extends Building implements Drawable {
	
	Unit target;
	int maxTimeToFire;
	int timeToFire;
	int type;
	
	Image detail;
	Shape shape;
	
	public DefenseBuilding(Game game, TeamManager team, double x, double y, int w, int h, int hitPoints, double visibleDist, int maxTimeToFire, int type) {
		super(game, team, x, y, w, h, hitPoints, visibleDist);
		this.type = type;
		this.maxTimeToFire = maxTimeToFire;
		shape = game.shapeManager.getShape(type);
		setFlagOn(Flag.CAN_FIRE);
	}
	
	@Override
	public void step() {
		if(hitPoints < 1) {
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		target = getTarget();
		if(target != null) {
			setFlagOn(Flag.FIRING);
		} else setFlagOff(Flag.FIRING);
		
		if(isFlagOn(Flag.FIRING) && timeToFire == 0) {
			fire();
			timeToFire = maxTimeToFire;
		} else if(timeToFire > 0) timeToFire--;
		
		stepEffects();
	}
	
	public void fire() {
		
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		drawPrevElements(g2d, x, y);
		
		AffineTransform backup = g2d.getTransform();
		g2d.translate(x, y);
		g2d.setColor(team.color);
		g2d.fill(shape);
		g2d.drawImage(detail, 0, 0, null);
		g2d.setTransform(backup);
		
		drawAddElements(g2d, x, y);
	}
}
