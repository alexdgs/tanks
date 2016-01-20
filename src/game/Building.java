package game;

public abstract class Building extends Unit {

	public Building(Game game, TeamManager team, double x, double y, int w, int h, int hitPoints, double visibleDist) {
		super(game, team, x, y, w, h, hitPoints, visibleDist);
		setFlagOff(Flag.MOVEABLE);
	}
	
}
