package game;

import java.awt.geom.Ellipse2D;

public class Bullet extends Projectile {
	
	double speedX;
	double speedY;
	int diameter;
	int damage;

	public Bullet(Game game, TeamManager team, Unit owner, double x, double y, int w, int h, double speed, double angle, int diameter, int damage) {
		super(game, team, owner, x, y, w, h);
		this.diameter = diameter;
		this.damage = damage;
		speedX = speed*cos(angle);
		speedY = speed*sin(angle);
		this.angle = angle;
	}
	
	@Override
	public void step() {
		//bounce();
		move();
		updateCenter();
		advanceTime();
		
		if(isInsideMap()) {
			for(TeamManager team : team.enemies) {
				for(Unit u : team.getUnits()) {
					if(hit(u)) {
						hit(u, damage);
						destroy();
						return;
					}
				}
			}
		} else destroy();
	}
	
	public void move() {
		moveStraight();
	}
	
	public void moveStraight() {
		x += speedX;
		y += speedY;
	}
	
	public void advanceTime() {
		
	}
	
	public void destroy() {
		setFlagOff(Flag.ALIVE);
		game.newGameObjects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.EXPL));
	}
	
	public void bounce() {
		double x2 = x + speedX;
		double y2 = y + speedY;
		if(x2 < 0 || x2 > maxX) speedX *= -1;
		if(y2 < 0 || y2 > maxY) speedY *= -1;
	}
	
	public boolean hit(GameObject go) {
		 if((new Ellipse2D.Double(x, y, diameter, diameter)).intersects(go.getBounds())) return true;
		 return false;
	}
}
