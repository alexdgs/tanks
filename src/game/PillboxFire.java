package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class PillboxFire extends InstantProjectile implements Drawable {
	
	static final int EFFECT_TIME = 20;
	static final Color COLOR = Color.PINK;
	static final float THICKNESS = 1.0f;
	static final int DAMAGE = 2;
	static final float[] LINE_SHAPE = {5.0f, 10.0f};
	static final float PHASE_DIF = 1.2f;
	
	int time;
	float phase;
	
	public PillboxFire(Game game, TeamManager team, Unit owner, Unit target, int x, int y) {
		super(game, team, owner, target, (int)target.centerX, (int)target.centerY, x, y);
		time = 0;
	}

	@Override
	public void step() {
		if(time == EFFECT_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		if(time == 0) hit(target, DAMAGE);
			
		time++;
		phase += PHASE_DIF;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		Stroke backup = g2d.getStroke();
		g2d.setStroke(new BasicStroke(THICKNESS, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, LINE_SHAPE, phase));
		g2d.setColor(COLOR);
		g2d.drawLine(x + startXd, y + startYd, x + endXd, y + endYd);
		g2d.setStroke(backup);
	}
}
