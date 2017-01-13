package game;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Wave extends Projectile implements Drawable {
	
	static final int INIT_RADIUS = 5;
	static final int INC_RADIUS = 1;
	static final int MAX_RADIUS = 210;
	static final float WIDTH = 2.0f;
	static final int DAMAGE = 100;
	
	int radius;
	int maxRadius;
	float alpha;
	Ellipse2D.Double bounds;
	ArrayList<Unit> hitted;

	public Wave(Game game, TeamManager team, double x, double y) {
		super(game, team, null, x, y, INIT_RADIUS, INIT_RADIUS);
		radius = INIT_RADIUS;
		maxRadius = MAX_RADIUS;
		hitted = new ArrayList<Unit>();
	}

	@Override
	public void step() {
		if(radius >= maxRadius) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		bounds = new Ellipse2D.Double(x, y, radius*2, radius*2);
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(hits(u) && !hitted.contains(u)) {
					doHit(u);
				}
			}
		}
		
		radius += INC_RADIUS;
		x -= INC_RADIUS;
		y -= INC_RADIUS;
	}
	
	public int getDamage(Unit u) {
		return DAMAGE;
	}
	
	public boolean hits(Unit u) {
		return bounds.intersects(u.getBounds());
	}
	
	public void doHit(Unit u) {
		hit(u, getDamage(u));
		hitted.add(u);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, radius*2, radius*2);
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		Stroke backup = g2d.getStroke();
		g2d.setStroke(new BasicStroke(WIDTH));
		g2d.setColor(team.color);
		g2d.drawOval(x, y, radius*2, radius*2);
		g2d.setStroke(backup);
	}
}
