package game;

public abstract class DirectedProjectile extends Projectile {
	
	static final double STEER_SPEED = Math.PI/180.0;
	
	Unit target;
	
	public DirectedProjectile(Game game, TeamManager team, Unit owner, double x, double y, int w, int h, Unit target, double angle) {
		super(game, team, owner, x, y, w, h);
		this.target = target;
		this.angle = angle;
	}
}
