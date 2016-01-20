package game;

public abstract class Projectile extends GameObject implements Steppable {
	
	Unit owner;
	TeamManager team;

	public Projectile(Game game, TeamManager team, Unit owner, double x, double y, int w, int h) {
		super(game, x, y, w, h);
		this.owner = owner;
		this.team = team;
	}
	
	public boolean isInsideMap() {
		if(x < 0 || x > maxX || y < 0 || y > maxY) return false;
		return true;
	}
	
	public void hit(Unit u, int damage) {
		if(u.hitted(damage)) team.score(1);
	}
}
