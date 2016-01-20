package game;

public class GradualWave extends Wave {
	
	static final int DAMAGE = 60;
	static final int MAX_RADIUS = 100;
	
	float alpha;
	
	public GradualWave(Unit owner, double x, double y) {
		super(owner.game, owner.team, x, y);
		maxRadius = MAX_RADIUS;
	}
	
	@Override
	public int getDamage(Unit u) {
		return (int)(DAMAGE * Math.max(0, (maxRadius - distToCenter(u)) / maxRadius));
	}
}
