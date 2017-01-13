package game;

import java.awt.Graphics2D;
import java.util.PriorityQueue;
import java.util.Queue;

import ui.ShapeManager;

public class TurretTank extends Tank {
	
	static final double SPEED = 0.8;
	static final int HIT_POINTS = 100;
	static final double VISIBLE_DIST = 170.0;
	static final double FIREABLE_DIST = 150.0;
	static final double MAX_TURN_ANGLE = Math.PI/180.0;
	static final int MAX_TIME_TO_FIRE = 110;
	static final int BULLET_OFFSET_X = HALF_WIDTH - SmallBullet.HALF_DIAMETER;
	static final int BULLET_OFFSET_Y = HALF_HEIGHT - SmallBullet.HALF_DIAMETER;
	public static final int CONSTRUCTION_TIME = 1600;
	
	static final int TURRET_DESPL = (WIDTH - Turret.WIDTH)/2;
	
	Queue<Unit> targets = new PriorityQueue<Unit>(new UnitComparator());
	Turret turret;
	Unit turretTarget;
	
	public TurretTank(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, HIT_POINTS, SPEED, VISIBLE_DIST, FIREABLE_DIST, MAX_TIME_TO_FIRE, MAX_TURN_ANGLE, BULLET_OFFSET_X, BULLET_OFFSET_Y, ShapeManager.TURRET_TANK);
		turretDespl = TURRET_DESPL;
		turret = new Turret(this);
	}
	
	@Override
	public Unit getTarget() {
		targets.clear();
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && distTo(u) <= visibleDist) {
					targets.add(u);
					if(targets.size() > 2) targets.poll();
				}
			}
		}
		if(targets.size() > 1) turretTarget = targets.poll();
		else turretTarget = targets.peek();
		
		return targets.poll();
	};
	
	@Override
	public void addSteps() {
		if(timeRemainingFrozen == 0) {
			turret.setTarget(isFlagOn(Flag.AI_CONTROLLED) ? turretTarget : target);
			turret.step();
		}
	};
	
	@Override
	public void fire() {
		game.newGameObjects.add(new SmallBullet(game, team, this, x + bulletOffsetX, y + bulletOffsetY, angle));
	}
	
	@Override
	public void drawDetail(Graphics2D g2d, int x, int y) {
		g2d.translate(TURRET_DESPL, TURRET_DESPL);
		turret.draw(g2d, 0, 0);
	}
}
