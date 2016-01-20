package game;

public abstract class InstantProjectile extends DirectedProjectile {
	
	int startX;
	int startY;
	int endX;
	int endY;
	
	int startXd;
	int startYd;
	int endXd;
	int endYd;

	public InstantProjectile(Game game, TeamManager team, Unit owner, Unit target, int startX, int startY, int endX,int endY) {
		super(game, team, owner, Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX-endX), Math.abs(startY-endY), target, 0);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
		int dx = Math.abs(startX-endX);
		int dy = Math.abs(startY-endY);
		
		if(startX > endX) startXd = dx;
		else endXd = dx;
		if(startY > endY) startYd = dy;
		else endYd = dy;
	}
}
