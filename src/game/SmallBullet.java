package game;

import java.awt.Graphics2D;

public class SmallBullet extends Bullet implements Drawable {
	
	static final int DIAMETER = 6;
	static final int HALF_DIAMETER = DIAMETER/2;
	static final double SPEED = 2.2;
	static final int DAMAGE = 5;
	
	public SmallBullet(Game game, TeamManager team, Unit owner, double x, double y, double angle) {
		super(game, team, owner, x, y, DIAMETER, DIAMETER, SPEED, angle, DIAMETER, DAMAGE);
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.setColor(team.color);
		g2d.fillOval(x, y, DIAMETER, DIAMETER);
	}
}
