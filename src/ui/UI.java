package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ConcurrentModificationException;
import java.util.Stack;

import game.Game;
import game.TeamManager;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import control.Controller;

@SuppressWarnings("serial")
public class UI extends JPanel {
	
	static final int WIDTH = Vista.DEFAULT_VISIBLE_W;
	static final int HEIGHT = Vista.DEFAULT_VISIBLE_H;
	static final Color bgColor = new Color(0, 85, 30);
	
	static final int vistaX = 20;
	static final int vistaY = 20;
	
	static final int DEFAULT_INFO_X = WIDTH - 290;
	static final int INFO_Y = 15;
	static final int INFO_W = 280;
	static final int INFO_H_INIT = 25;
	static final int INFO_H_TEAM = 15;
	static final int DIF_STRING_LINE = 15;
	static final float INFO_APLHA = 0.75f;
	
	ShapeManager sm;
	
	Game game;
	Vista vista;
	Controller controller;
	int numTeams;
	int flags;
	int infoX = DEFAULT_INFO_X;
	
	Rectangle viewableArea;
	TrayIconManager tim;
	
	int xViewOffset;
	int yViewOffset;
	
	boolean showInfo;
	boolean selectingArea;
	int startX;
	int startY;
	int selectionWidth;
	int selectionHeight;
	
	int scaleWidth;
	int scaleHeight;
	static final double[] SCALES = {0.25, 0.5, 0.75, 1, 1.25, 1.5};
	int scaleIndex = 3;
	double minScale;
	double currentScale = SCALES[scaleIndex];
	
	boolean movingIndicator;
	float alphaMovingInd;
	int timeShowMovingInd;
	int movingIndicatorX;
	int movingIndicatorY;
	static final int TIME_DRAW_MOVING_IND = 40;
	static final int MOVING_IND_HALF_WIDTH = 10;
	static final int MOVING_INF_HALF_HEIGHT = 10;
	static final Image IMG_MOVING_IND = (new ImageIcon("src/img/moving.png")).getImage().getScaledInstance(MOVING_IND_HALF_WIDTH*2, MOVING_INF_HALF_HEIGHT*2, Image.SCALE_FAST);
	
	static final DecimalFormat df = new DecimalFormat("0.00");
	
	public UI(Game game, Controller controller) throws IOException {
		this.game = game;
		this.numTeams = game.teams.size();
		vista = new Vista(game);
		minScale = Math.min(WIDTH/(double)Game.WIDTH, HEIGHT/(double)Game.HEIGHT);
		//vista.setLocation(vistaX, vistaY);
		//add(vista);
		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setBackground(Color.BLACK);
		//addKeyListener(controller);
		addMouseListener(controller);
		addMouseMotionListener(controller);
		addMouseWheelListener(controller);
		
		sm = new ShapeManager();
		
		try {
			tim = new TrayIconManager();
		} catch (Exception e) {
			System.err.println("Tray icon not supported");
		}
		
		setVisible(true);
	}
	
	public void update() {
		if(timeShowMovingInd > 0) {
			alphaMovingInd = (float)timeShowMovingInd/TIME_DRAW_MOVING_IND;
			timeShowMovingInd--;
		} else {
			movingIndicator = false;
		}
		
		Stack<TeamManager> lost = game.getGarageLostTeams();
		synchronized (lost) {
			if(!lost.isEmpty()) {
				int countLost = 0;
				String lostTeams = lost.size() > 1 ? " teams had lost a Garage." : " team has lost a Garage.";
				while(!lost.isEmpty()) {
					lostTeams = lost.pop().getName() + lostTeams;
					countLost++;
					if(!lost.isEmpty()) {
						if(countLost >= 2) lostTeams = ", " + lostTeams;
						else if(countLost == 1) lostTeams = " and " + lostTeams;
					}
				}
				tim.showMessage("Update", lostTeams);
			}
		}
		
		if(!isFlagOn(Flag.WINNER)) {
			if(game.playerAIControlled && tim != null) {
				tim.showMessage("Winner!", "Winner team is " + game.getWinnerName());
				setFlagOn(Flag.WINNER);
			}
		}
	}
	
	public void updateSize(int w, int h) {
		w = Math.min(w, Game.WIDTH);
		h = Math.min(h, Game.HEIGHT);
		setSize(w, h);
		infoX = w - 290;
		vista.updateSize(w, h);
		//vista.updateScale(SCALES[scaleIndex]);
	}
	
	public void moveVista(int dir) {
		vista.move(dir);
		setRelativeOrigin(vista.getOrigin());
	}
	
	public void moveVista(int dx, int dy) {
		vista.move(dx, dy);
		setRelativeOrigin(vista.getOrigin());
	}
	
	public void setRelativeOrigin(Point p) {
		xViewOffset = p.x;
		yViewOffset = p.y;
	}
	
	public void toggleShowInfo() {
		showInfo = !showInfo;
	}
	
	public void leftClick(Point p) {
		try {
			boolean unitsMoved = game.processSelectionAt(new Point(p.x + xViewOffset, p.y + yViewOffset));
			if(unitsMoved) {
				timeShowMovingInd = TIME_DRAW_MOVING_IND;
				alphaMovingInd = 1.0f;
				movingIndicatorX = p.x - MOVING_IND_HALF_WIDTH;
				movingIndicatorY = p.y - MOVING_INF_HALF_HEIGHT;
				movingIndicator = true;
			}
		} catch(ConcurrentModificationException cme) {
			
		}
		
	}
	
	public void rightClick(Point p) {
		game.unselect();
	}
	
	public void dragging(Point p1, Point p2) {
		startX = Math.min(p1.x, p2.x);
		startY = Math.min(p1.y, p2.y);
		selectionWidth = Math.abs(p1.x-p2.x);
		selectionHeight = Math.abs(p1.y-p2.y);
		selectingArea = true;
	}
	
	public void dragged(Point p1, Point p2) {
		Point start = new Point(xViewOffset + startX, yViewOffset + startY);
		Dimension area = new Dimension(selectionWidth, selectionHeight);
		try {
			game.processSelectionAt(start, area);
		} catch(ConcurrentModificationException cme) {
			
		}
		selectingArea = false;
	}
	
	public void scheduleChangePlayerTeam() {
		game.changePlayerTeam = true;
	}
	
	public void toggleEnemyAwareFlag() {
		game.toggleEnemyAwareFlag();
	}
	
	public void toggleSlowMotion() {
		game.toggleSlowMotion();
	}
	
	public void toggleFastForward() {
		game.toggleFastForward();
	}
	
	public Rectangle getViewableArea() {
		return vista.getViewableArea(SCALES[scaleIndex]);
	}
	
	public void zoomIn() {
		scaleIndex = Math.min(scaleIndex + 1, SCALES.length - 1);
		updateCurrentScale();
		//vista.updateScale(SCALES[scaleIndex]);
	}
	
	public void zoomOut() {
		if(SCALES[scaleIndex] > minScale) {
			scaleIndex = Math.max(scaleIndex - 1, 0);
			updateCurrentScale();
		}
		//vista.updateScale(SCALES[scaleIndex]);
	}
	
	public void updateCurrentScale() {
		currentScale = Math.max(SCALES[scaleIndex], minScale);
	}
	
	public void endGame() {
		System.exit(0);
	}
	
	public void addPlayerTeamTank() {
		game.addPlayerTeamTank();
	}
	
	public void togglePlayerAsTarget() {
		game.togglePlayerAsTarget();
	}
	
	public boolean isFlagOn(int flag) {
		return (flags & (1 << flag)) != 0;
	}
	
	public void setFlagOn(int flag) {
		flags  |= (1 << flag);
	}
	
	public void setFlagOff(int flag) {
		flags  &= ~(1 << flag);
	}
	
	public void toggleFlag(int flag) {
		flags ^= (1 << flag);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(bgColor);
		g2d.fillRect(0, 0, (int)Math.min(Game.WIDTH*currentScale, WIDTH), (int)Math.min(Game.HEIGHT*currentScale, HEIGHT));
		
		if(selectingArea) {
			g2d.setColor(Color.GREEN);
			g2d.drawRect(startX, startY, selectionWidth, selectionHeight);
		}
		
		if(movingIndicator) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaMovingInd));
			g2d.drawImage(IMG_MOVING_IND, movingIndicatorX, movingIndicatorY, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		AffineTransform noScaleTransform = g2d.getTransform();
		g2d.scale(currentScale, currentScale);
		vista.paint(g2d);
		g2d.setTransform(noScaleTransform);
		
		if(showInfo) drawInfo(g2d, infoX, INFO_Y, INFO_W, INFO_H_INIT + numTeams*INFO_H_TEAM);
		drawCurrTeam(g2d, 5, HEIGHT-15, 200, 30);
		//drawSpike(g2d);
	}
	
	public void drawInfo(Graphics2D g2d, int x, int y, int w, int h) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, INFO_APLHA));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, w, h);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score", x + 60, y + DIF_STRING_LINE);
		g2d.drawString("Looses", x + 100, y + DIF_STRING_LINE);
		g2d.drawString("Efficacy", x + 145, y + DIF_STRING_LINE);
		g2d.drawString("Rem. Garages", x +  190, y + DIF_STRING_LINE);
		
		
		for(int i = 0; i < numTeams; i++) {
			TeamManager team = game.teams.get(i);
			int lineHeight = y + DIF_STRING_LINE*(i+1) + 15;
			g2d.setColor(team.getNumUnits() > 0 ? team.color : Color.GRAY);
			g2d.drawString(team.string, x + 10, lineHeight);
			g2d.drawString(Integer.toString(team.score), x + 65, lineHeight);
			g2d.drawString(Integer.toString(team.looses), x + 110, lineHeight);
			g2d.drawString(df.format(team.efficacy), x + 155, lineHeight);
			g2d.drawString(Integer.toString(team.remainingGarages), x + 225, lineHeight);
		}
	}
	
	public void drawCurrTeam(Graphics2D g2d, int x, int y, int w, int h) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, INFO_APLHA));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, w, h);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setColor(game.getPlayerTeam().color);
		
		g2d.drawString("Curr.Team: " + game.getPlayerTeam().string + "    " +
				(game.isFlagOn(Game.FLAG_SLOW_MOTION) ? "Slow Motion: " + game.getSlowMotionRate() + "x" :
					game.isFlagOn(Game.FLAG_FAST_FORWARD) ? "Fast Fwd.: " + game.getFastForwardRate()  + "x" : "Normal Speed"), x+10, y+10);
	}
	
	public void drawSpike(Graphics2D g2d) {
		
	}
	
	static class Flag
	{
		static final int FULL_SCREEN = 1;
		static final int WINNER = 10;
	}
}
