package game;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class ConstructorBuilding extends Building {
	
	int timeActualProcess;
	int timeFinishProcess;
	Bar progressBar;
	
	public ConstructorBuilding(Game game, TeamManager team, double x, double y, int w, int h, int hitPoints, double visibleDist) {
		super(game, team, x, y, w, h, hitPoints, visibleDist);
		progressBar = new Bar(this, 0, 0, w, 5, 1, 1, Color.CYAN, true, Bar.TYPE_POWER);
	}
	
	public void build() {
		
	}
	
	@Override
	public void drawAddElements(Graphics2D g2d, int x, int y) {
		drawHealthBar(g2d, x, y);
		drawProgressBar(g2d, x, y);
		//drawHealth(g2d, x, y);
	}
	
	public void drawProgressBar(Graphics2D g2d, int x, int y) {
		progressBar.draw(g2d, x, y);
	}
}
