package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Flame extends Bullet implements Drawable {
	
	static final int EFFECT_TIME = 335;
	static final int TIME_BETWEEN_DAMAGE = 35;
	static final int DIAMETER = 15;
	static final double SPEED = 0.35;
	static final int DAMAGE = 1;
	static final float ALPHA_DIF = 0.035f;
	static final Color color = new Color(255, 127, 0);
	
	int time;
	double speedX, speedY;
	float alpha;
	boolean fadingOut = false;
	
	public Flame(Unit owner, double x, double y, double angle) {
		super(owner.game, owner.team, owner, x, y, DIAMETER, DIAMETER, SPEED, angle, DIAMETER, DAMAGE);
		alpha = 0.0f;
		fadingOut = false;
		setSpeeds(angle);
	}
	
	public void setSpeeds(double angle) {
		speedX = SPEED*cos(angle);
		speedY = SPEED*sin(angle);
	}
	
	@Override
	public void step() {
		if(time == EFFECT_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		x += speedX;
		y += speedY;
		
		if(time % TIME_BETWEEN_DAMAGE == 0) {
			for(TeamManager tm : team.enemies) {
				for(Unit u : tm.getUnits()) {
					if(intersects(u) && u.isFlagOn(Flag.ALIVE)) hit(u, DAMAGE);
				}
			}
		}
			
		time++;
		if(fadingOut) {
			alpha = Math.max(0.0f,  alpha - ALPHA_DIF);
			if(alpha < 0.3f) fadingOut = false;
		} else {
			alpha = Math.min(1.0f,  alpha + ALPHA_DIF);
			if(alpha >= 1.0f) fadingOut = true;
		}
	}
	

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setColor(color);
		g2d.fillOval(x, y, DIAMETER, DIAMETER);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

}
