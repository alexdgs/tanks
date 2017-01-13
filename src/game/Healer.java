package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import ui.ShapeManager;

public class Healer extends Building implements Drawable {
	
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int HIT_POINTS = 250;
	static final int VISIBLE_DIST = 60;
	
	static final int WAIT_TIME_TO_HEAL = 90;
	static final int POINTS_TO_HEAL = 2;
	
	static final Image DETAIL = (new ImageIcon("src/img/medic_sign.png")).getImage();
	
	Shape shape;
	int timeToHeal;

	public Healer(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, WIDTH, HEIGHT, HIT_POINTS, VISIBLE_DIST);
		timeToHeal = WAIT_TIME_TO_HEAL;
		shape = game.shapeManager.getShape(ShapeManager.HEALER);
	}

	@Override
	public void step() {
		timeToHeal--;
		if(timeToHeal == 0) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && u instanceof Tank && distToCenter(u) <= visibleDist && u.hitPoints < u.maxHitPoints){
					u.heal(POINTS_TO_HEAL);
				}
			}
			timeToHeal = WAIT_TIME_TO_HEAL;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		this.team.healerAvailable = false;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		drawPrevElements(g2d, x, y);
		
		AffineTransform backup = g2d.getTransform();
		g2d.translate(x, y);
		g2d.setColor(team.color);
		g2d.fill(shape);
		g2d.drawImage(DETAIL, 10, 10, null);
		g2d.setTransform(backup);
		
		drawHUDElements(g2d, x, y);
	}

}
