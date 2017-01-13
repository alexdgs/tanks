package game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class TinyLaser extends InstantProjectile implements Drawable {
	
	static final int EFFECT_TIME = 25;
	static final Color COLOR = Color.RED;
	static final float THICKNESS = 1.75f;
	static final int DAMAGE = 2;
	
	int time;
	float alpha;
	
	public TinyLaser(Unit owner, Point startPoint, Unit target) {
		super(owner.game, owner.team, owner, target, (int)target.centerX, (int)target.centerY, startPoint.x, startPoint.y);
		time = 0;
		alpha = 1.0f;
	}

	@Override
	public void step() {
		if(time == EFFECT_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		if(time == 0) {
			hit(target, DAMAGE);
		}
			
		time++;
		alpha = 1.0f - (float)time/EFFECT_TIME;
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