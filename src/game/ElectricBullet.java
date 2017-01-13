package game;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;

public class ElectricBullet extends Bullet implements Drawable {
	
	static final int DIAMETER = 10;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double SPEED = 0.70;
	static final int DAMAGE = 0;
	
	static final int NUM_LASERS = 5;
	static final int TIME_TO_FIRE = 30;
	static final double MAX_DIST_TO_FIRE = 45.0;
	static final int TIME_SWAP_DRAWING_FLAG = 4;
	static final float NO_DRAW_ALPHA = 0.35f;
	
	boolean drawBullet = true;
	int timeSwapDrawingFlag = TIME_SWAP_DRAWING_FLAG;
	
	int numFiredLasers = 0;
	int laserFireRemainingTime = TIME_TO_FIRE;

	public ElectricBullet(Unit owner, double x, double y, double angle) {
		super(owner.game, owner.team, owner, x, y, DIAMETER, DIAMETER, SPEED, angle, DIAMETER, DAMAGE);
	}
	
	@Override
	public void step() {
		//bounce();
		move();
		updateCenter();
		advanceTime();
		
		if(isInsideMap()) {
			if(laserFireRemainingTime == 0) {
				Unit target = null;
				for(TeamManager team : team.enemies) {
					for(Unit u : team.getUnits()) {
						double dist = distToCenter(u);
						if(dist <= MAX_DIST_TO_FIRE) {
							if(target == null || dist < distToCenter(target)) {
								target = u;
							}
						}
					}
				}
				if(target != null) {
					fire(target);
					numFiredLasers++;
					laserFireRemainingTime = TIME_TO_FIRE;
				}
			} else laserFireRemainingTime--;
			
			if(numFiredLasers >= NUM_LASERS) {
				destroy();
			}
		} else destroy();
		
		timeSwapDrawingFlag--;
		if(timeSwapDrawingFlag == 0) {
			drawBullet = !drawBullet;
			timeSwapDrawingFlag = TIME_SWAP_DRAWING_FLAG;
		}
	}
	
	public void fire(Unit target) {
		game.newGameObjects.add(new TinyLaser(owner, new Point((int)centerX, (int)(centerY)), target));
	}
	
	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		if(!drawBullet){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, NO_DRAW_ALPHA));
		}
		g2d.setColor(team.color);
		g2d.fillOval(x, y, DIAMETER, DIAMETER);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}
