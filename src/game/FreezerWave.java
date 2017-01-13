package game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class FreezerWave extends Wave {
	
	public static final Color ICE_COLOR = Color.CYAN;
	public static final float ICE_ALPHA = 0.5f;
	public static final int ICE_ARC_WIDTH = 6;
	public static final int ICE_THICKNESS = 3;
	
	static final int DAMAGE = 0;
	static final int MAX_RADIUS = 100;
	static final int FREEZE_TIME = 1000;

	
	public FreezerWave(Unit owner, double x, double y) {
		super(owner.game, owner.team, x, y);
		maxRadius = MAX_RADIUS;
	}
	
	@Override
	public void doHit(Unit u) {
		u.freeze(FREEZE_TIME);
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		Stroke backup = g2d.getStroke();
		g2d.setStroke(new BasicStroke(WIDTH));
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ICE_ALPHA/2.0f));
		g2d.setColor(ICE_COLOR);
		g2d.fillOval(x, y, radius*2, radius*2);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		g2d.setColor(team.color);
		g2d.drawOval(x, y, radius*2, radius*2);
		g2d.setStroke(backup);
	}
}
