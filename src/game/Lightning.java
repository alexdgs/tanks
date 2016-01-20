package game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Lightning extends InstantProjectile implements Drawable {
	
	static final int EFFECT_TIME = 50;
	static final Color COLOR = new Color(192, 255, 255);
	static final float THICKNESS = 2.0f;
	static final int DAMAGE = 2;
	static final int RAY_MIN_DELTA = 4;
	static final int RAY_MAX_DELTA = 20;
	static final int RAY_MAX_DEV = 10;
	static final int MAX_POINTS = 50;
	static final int FLASHING_TIME = 4;
	static final float MAX_ALPHA = 0.8f;
	static final double MAX_REFLECTION_DIST = 50;
	
	int numRays;
	int time;
	float alpha;
	
	ArrayList<Unit> targets;
	int[][] pointsRayX;
	int[][] pointsRayY;
	ArrayList<Integer> numPoints;
	ArrayList<Integer[]> rayX;
	ArrayList<Integer[]> rayY;
	
	public Lightning(Unit owner, double offsetX, double offsetY, Unit target) {
		super(owner.game, owner.team, owner, target, (int)(owner.centerX + offsetX), (int)(owner.centerY + offsetY), (int)owner.centerX, (int)owner.centerY);
		time = 0;
		alpha = MAX_ALPHA;
		rayX = new ArrayList<Integer[]>();
		rayY = new ArrayList<Integer[]>();
		targets = new ArrayList<Unit>();
		generateLightning(startX, startY);
	}
	
	public void generateLightning(double fromX, double fromY) {
		numPoints = new ArrayList<Integer>();
		
		while(target != null) {
			targets.add(target);
			generateRay(fromX, fromY, target.centerX, target.centerY);
			fromX = target.centerX;
			fromY = target.centerY;
			
			target = null;
			for(TeamManager t : owner.team.enemies) {
				for(Unit u : t.getUnits()) {
					if(u.isFlagOn(Flag.ALIVE) && !targets.contains(u) && distBetween(fromX, fromY, u.centerX, u.centerY) <= MAX_REFLECTION_DIST) {
						target = u;
						break;
					}
				}
				if(target != null) break;
			}
		}
		numRays = rayX.size();
		pointsRayX = new int[numRays][50];
		pointsRayY = new int[numRays][50];
		for(int i = 0; i < numRays; i++) {
			int numP = numPoints.get(i);
			for(int j = 0; j < numP; j++) {
				pointsRayX[i][j] = rayX.get(i)[j];
				pointsRayY[i][j] = rayY.get(i)[j];
			}
		}
	}
	
	public void generateRay(double x1, double y1, double x2, double y2) {
		double angle = angleBetween(x1, y1, x2, y2);
		double perpAngle = sumAngles(angle, HALF_PI);
		double dist = distBetween(x1, y1, x2, y2);
		double lastDist = 0.0;
		Integer[] rX = new Integer[50];
		Integer[] rY = new Integer[50];
		rX[0] = (int)x1;
		rY[0] = (int)y1;
		int i = 1;
		while(lastDist < dist) {
			lastDist = Math.min(lastDist + RAY_MIN_DELTA + (Math.random()*RAY_MAX_DELTA), dist);
			int dirDev = Math.random() > 0.5 ? 1 : -1;
			double dev = lastDist < dist ? (Math.random()*RAY_MAX_DEV) * dirDev : 0.0;
			double dx = lastDist*cos(angle);
			double dy = lastDist*sin(angle);
			double devX = dev*cos(perpAngle);
			double devY = dev*sin(perpAngle);
			rX[i] = rX[0] + (int)(dx + devX);
			rY[i] = rY[0] + (int)(dy + devY);
			i++;
		}
		/*System.out.println("Num points: " + i);
		for(int j = 0; j < i; j++) {
			System.out.println(rX[j] + "," + rY[j]);
		}*/
		rayX.add(rX);
		rayY.add(rY);
		numPoints.add(i);
	}

	@Override
	public void step() {
		if(time == EFFECT_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		if(time == 0) {
			for(Unit u : targets)
				if(u.isFlagOn(Flag.ALIVE)) {
					hit(u, DAMAGE);
					u.electrified();
				}
		}
			
		time++;
		if(time % FLASHING_TIME == 0) alpha = MAX_ALPHA - alpha;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		AffineTransform atBackup = g2d.getTransform();
		Stroke backup = g2d.getStroke();
		//g2d.translate(x, y);
		g2d.setStroke(new BasicStroke(THICKNESS));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setColor(COLOR);
		for(int i = 0; i < numRays; i++) g2d.drawPolyline(pointsRayX[i], pointsRayY[i], numPoints.get(i));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setStroke(backup);
		g2d.setTransform(atBackup);
	}
}
