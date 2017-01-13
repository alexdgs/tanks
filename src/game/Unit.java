package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Unit extends GameObject implements Steppable {
	
	int hitPoints;
	int maxHitPoints;
	TeamManager team;
	int score;
	int turretDespl;
	double visibleDist;
	Bar healthBar;
	Unit target;
	UnitGroup group;
	
	int timeRemainingFrozen = 0;
	
	ArrayList<Circle> effects;
	Point targetPoint;

	public Unit(Game game, TeamManager team, double x, double y, int w, int h, int hitPoints, double visibleDist) {
		super(game, x, y, w, h);
		this.team = team;
		this.hitPoints = hitPoints;
		this.maxHitPoints = hitPoints;
		this.visibleDist = visibleDist;
		healthBar = new Bar(this, 0, -5, w, 5, hitPoints, hitPoints, null, true, Bar.TYPE_HEALTH);
		effects = new ArrayList<Circle>();
		setFlagOn(Flag.MOVEABLE);
	}
	
	@Override
	public void step() {
		if(hitPoints < 1) {
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		if(timeRemainingFrozen == 0) {
			move();
		} else timeRemainingFrozen--;
		
		addSteps();
		stepEffects();
	}
	
	public Unit getTarget() {
		Unit newTarget = null;
		for(TeamManager team : team.enemies) {
			for(Unit u : team.getUnits()) {
				if(u.isFlagOn(Flag.ALIVE) && distToCenter(u) <= visibleDist) {
					if(newTarget == null || (!newTarget.isFlagOn(Flag.CAN_FIRE) && u.isFlagOn(Flag.CAN_FIRE))
							|| (newTarget.isFlagOn(Flag.CAN_FIRE) == u.isFlagOn(Flag.CAN_FIRE) && newTarget.hitPoints > u.hitPoints)) newTarget = u;
				}
			}
		}
		return newTarget;
	}
	
	public Point getRandomBaseEnemyPoint() {
		Point p = null;
		int L = team.enemies.size();
		
		if(L > 0) {
			int T = getRandomInteger(L);
			p = team.enemies.get(T).getBasePoint();
		}
		
		return p;
	}
	
	public void checkUnitStrategy() {
		
	}
	
	public void addSteps() {
		
	}
	
	public void move() {
		
	}
	
	public void heal(int points) {
		if(hitPoints > 0) {
			healthBar.update(hitPoints = Math.min(hitPoints + points, maxHitPoints));
			effects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.HEAL));
			game.newGameObjects.add(new Text(game, points, centerX - 10, centerY));
			checkUnitStrategy();
		}
	}
	
	public boolean hitted(int damage, double x, double y) {
		boolean result = hitted(damage);
		if(!result) underAttack(x, y);
		return result;
	}
	
	public boolean hitted(int damage) {
		if(hitPoints < 1) return false;
		healthBar.update(hitPoints = Math.max(0, hitPoints-damage));
		
		if(hitPoints == 0) {
			team.unitLost();
			destroy();
			return true;
		}
		return false;
	}
	
	public void freeze(int time) {
		timeRemainingFrozen = time;
	}
	
	public void underAttack(double x, double y) {
		
	}
	
	public void destroy() {
		setFlagOff(Flag.ALIVE);
		game.newGameObjects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.EXPL));
	}
	
	public void setTarget(Unit target) {
		this.target = target;
		setFlagOn(Flag.MANDATORY_TARGET);
		setFlagOff(Flag.OVERRIDE_ACTION);
	}
	
	public void setTargetPoint(Point p) {
		this.targetPoint = p;
		toX = p.x;
		toY = p.y;
	}
	
	public void setGroup(UnitGroup ug) {
		group = ug;
	}
	
	public void selectedAsTarget() {
		effects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.TARGET));
	}
	
	public void electrified() {
		effects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.ELECTRIC));
	}
	
	public void score() {
		score++;
	}
	
	public void score(int score) {
		this.score += score;
	}
	
	public void stepEffects() {
		if(effects.size() > 0) {
			ArrayList<Circle> aliveEffects = new ArrayList<Circle>();
			for(Circle c : effects) {
				c.step();
				if(c.isFlagOn(Flag.ALIVE)) aliveEffects.add(c);
			}
			effects = aliveEffects;
		}
	}
	
	public void drawPrevElements(Graphics2D g2d, int x, int y) {
		drawEffects(g2d, x, y);
	}
	
	public void drawAddElements(Graphics2D g2d, int x, int y) {
		if(timeRemainingFrozen > 0) {
			g2d.setColor(FreezerWave.ICE_COLOR);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FreezerWave.ICE_ALPHA));
			g2d.fillRoundRect(x-FreezerWave.ICE_THICKNESS, y-FreezerWave.ICE_THICKNESS, h+FreezerWave.ICE_THICKNESS*2, w+FreezerWave.ICE_THICKNESS*2, FreezerWave.ICE_ARC_WIDTH, FreezerWave.ICE_ARC_WIDTH);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}
	
	public void drawHUDElements(Graphics2D g2d, int x, int y) {
		drawHealthBar(g2d, x, y);
		if(isFlagOn(Flag.SELECTED)) drawSelected(g2d, x, y);
		//drawTeamId(g2d, x, y);
		//drawScore(g2d, x, y);
		//drawHealth(g2d, x, y);
	}
	
	public void drawEffects(Graphics2D g2d, int x, int y) {
		for(Circle c : effects) c.draw(g2d, x + (int)(c.x - this.x), y + (int)(c.y - this.y));
	}
	
	public void drawHealthBar(Graphics2D g2d, int x, int y) {
		healthBar.draw(g2d, x, y);
	}
	
	public void drawSelected(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.GREEN);
		g2d.drawRect(x, y, w, h);
	}
	
	public void drawScore(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.drawString(Integer.toString(score), x, y-5);
	}
	
	public void drawHealth(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.drawString(Integer.toString(hitPoints), x, y-5);
	}
	
	public void drawTeamId(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		g2d.drawString(Integer.toString(team.id), x, y-3);
	}
}
