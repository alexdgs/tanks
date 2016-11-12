package game;

public abstract class HomingProjectile extends DirectedProjectile {
	
	double homingRate;
	double dX;
	double dY;
	double speed;
	boolean seekingTarget;
	
	public HomingProjectile(Game game, TeamManager team, Unit owner, double x, double y, int w, int h, Unit target, double speed, double angle, double homingRate) {
		super(game, team, owner, x, y, w, h, target, angle);
		this.speed = speed;
		dX = speed*cos(angle);
		dY = speed*sin(angle);
		this.homingRate = homingRate;
		seekingTarget = true;
	}
	
	public void move() {
		if(seekingTarget) {
			if(target != null && target.isFlagOn(Flag.ALIVE)) {
				newAngle = angleTo(target);
				if(angle != newAngle) {
					adjustAngle();
				}// else seekingTarget = false;
			} else seekingTarget = false;
		}
		
		moveStraight();
	}
	
	public void advanceTime() {
		
	}
	
	public void bounce() {
		double x2 = x + dX;
		double y2 = y + dY;
		if(x2 < 0 || x2 > maxX) {
			dX *= -1;
			angle = Math.PI - angle;
		}
		if(y2 < 0 || y2 > maxY) {
			dY *= -1;
			angle = Math.PI*2 - angle;
		}
	}
	
	public void destroy() {
		setFlagOff(Flag.ALIVE);
		game.newGameObjects.add(new Circle((int)centerX, (int)centerY, w, Circle.Type.EXPL));
	}
	
	public void moveStraight() {
		x += dX;
		y += dY;
	}

	public void adjustAngle() {
		angle = newAngle(angle, newAngle, homingRate);
		setSpeeds();
		
	}
	
	public void setSpeeds() {
		dX = speed*cos(angle);
		dY = speed*sin(angle);
	}
}
