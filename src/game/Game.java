package game;

import game.GameObject.Flag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import ui.ShapeManager;

public class Game {
	
	public static final int WIDTH = 1500;
	public static final int HEIGHT = 1500;
	
	static final int FLAG_SELECTED_UNITS = 2;
	static final int FLAG_PLAYER_TARGETED = 10;
	
	private ArrayList<GameObject> gameObjects;
	ArrayList<GameObject> newGameObjects;
	private ArrayList<Drawable> drawables;
	
	public ArrayList<TeamManager> teams;
	private ArrayList<Unit> selectedUnits;
	private TeamManager playerTeam;
	public boolean changePlayerTeam;
	
	int flags;
	public boolean playerAIControlled;
	TeamManager winner;
	
	ShapeManager shapeManager;
	
	public Game() {
		gameObjects = new ArrayList<GameObject>();
		newGameObjects = new ArrayList<GameObject>();
		drawables = new ArrayList<Drawable>();
		selectedUnits = new ArrayList<Unit>();
		teams = new ArrayList<TeamManager>();
	}
	
	public void setShapeManager(ShapeManager sm) {
		this.shapeManager = sm;
	}
	
	public void setUp() {
		TeamManager blue = new TeamManager(this, Team.CYAN, Color.CYAN, "Cyan", 50, 50, TeamManager.UP, TeamManager.LEFT, true);
		//TeamManager red = new TeamManager(this, Team.RED, Color.RED, "Red", 50, Game.HEIGHT/2 - 50, TeamManager.MIDDLE, TeamManager.LEFT, true);
		TeamManager green = new TeamManager(this, Team.GREEN, Color.GREEN, "Green", Game.WIDTH - 110, 50, TeamManager.UP, TeamManager.RIGHT, true);
		//TeamManager yellow = new TeamManager(this, Team.YELLOW, Color.YELLOW, "Yellow", Game.WIDTH - 110, Game.HEIGHT/2 - 50, TeamManager.MIDDLE, TeamManager.RIGHT, true);
		TeamManager magenta = new TeamManager(this, Team.MAGENTA, Color.MAGENTA, "Magenta", 50, Game.HEIGHT - 110, TeamManager.DOWN, TeamManager.LEFT, true);
		TeamManager orange = new TeamManager(this, Team.ORANGE, Team.COLOR_ORANGE, "Orange", Game.WIDTH - 110, Game.HEIGHT - 110, TeamManager.DOWN, TeamManager.RIGHT, true);
		//TeamManager cyan = new TeamManager(this, Team.CYAN, Color.CYAN, "Cyan", Game.X/2 - 50, 50, TeamManager.UP, TeamManager.MIDDLE);
		TeamManager gray = new TeamManager(this, Team.GRAY, Color.GRAY, "Gray", Game.WIDTH/2 - 50, Game.HEIGHT - 110, TeamManager.DOWN, TeamManager.MIDDLE, false);
		//TeamManager black = new TeamManager(this, Team.BLACK, Color.BLACK, "Gray", Game.WIDTH/2 - 50, Game.HEIGHT/2 - 50, TeamManager.DOWN, TeamManager.MIDDLE, false);
		
		/*
		setEnemies(blue, green);
		setEnemies(blue, red);
		setEnemies(blue, orange);
		setEnemies(yellow, green);
		setEnemies(yellow, red);
		setEnemies(yellow, orange);
		setEnemies(magenta, green);
		setEnemies(magenta, red);
		setEnemies(magenta, orange);
		*/
		
		teams.add(blue);
		//teams.add(red);
		teams.add(green);
		//teams.add(yellow);
		teams.add(magenta);
		teams.add(orange);
		//teams.add(cyan);
		
		for(TeamManager team : teams) {
			for(TeamManager enemy : teams) {
				if(team.id != enemy.id) team.addEnemy(enemy);
			}
			gray.addEnemy(team);
		}
		
		setPlayerTeam(gray);
		teams.add(gray);
		//teams.add(black);
	}
	
	public void setEnemies(TeamManager team1, TeamManager team2) {
		team1.addEnemy(team2);
		team2.addEnemy(team1);
	}
	
	public void move() {
		int teamCounter = 0;
		
		if(changePlayerTeam){
			changePlayerTeam();
			changePlayerTeam = false;
		}
		
		for(TeamManager team : teams){
			team.step();
			if(team.isFlagOn(TeamManager.Flag.ACTIVE)) teamCounter++;
		}
		
		if(teamCounter < 3 && !playerAIControlled) {
			TeamManager target = null;
			for(TeamManager tm : teams) {
				if(tm != getPlayerTeam() && tm.isFlagOn(TeamManager.Flag.ACTIVE)) {
					target = winner = tm;
					break;
				}
			}
			
			for(Unit u : getPlayerTeam().getUnits()) {
				u.targetPoint = target.getBasePoint();
				u.setFlagOn(Unit.Flag.AI_CONTROLLED);
			}
			playerAIControlled = true;
		}
		
		addNewGameObjects();
		
		ArrayList<GameObject> remainingGameObjects = new ArrayList<GameObject>();
		for(GameObject go : gameObjects) {
			if(go instanceof Steppable) ((Steppable)go).step();
			if(go.isFlagOn(Flag.ALIVE)) remainingGameObjects.add(go);
		}
		
		gameObjects = remainingGameObjects;
	}
	
	public void prepareDrawableObjects(Rectangle viewableArea) {
		synchronized(drawables) {
			drawables.clear();
			for(GameObject go : gameObjects)
				if(go instanceof Drawable && !(go instanceof Text) && viewableArea.intersects(go.getBounds())) drawables.add((Drawable)go);
			
			for(TeamManager team : teams) {
				for(Unit u : team.getUnits())
					if(u instanceof Drawable && viewableArea.intersects(u.getBounds())) drawables.add((Drawable)u);
			}
			
			for(GameObject go : gameObjects)
				if(go instanceof Drawable && go instanceof Text && viewableArea.intersects(go.getBounds())) drawables.add((Drawable)go);
			
		}
	}
	
	public void addNewGameObjects() {
		gameObjects.addAll(newGameObjects);
		newGameObjects.clear();
	}
	
	public void scheduleChangePlayerTankTeam() {
		changePlayerTeam = true;
	}
	
	public void changePlayerTeam(){
		synchronized (teams) {
			synchronized (getPlayerTeam()) {
				synchronized (selectedUnits) {
					int index = teams.indexOf(getPlayerTeam());
					int newIndex = (index + 1) % teams.size();
					ArrayList<Unit> unitsToMove = new ArrayList<Unit>();
					for(Unit u : selectedUnits){
						unitsToMove.add(u);
					}
					for(Unit u : unitsToMove){
						teams.get(index).getUnits().remove(u);
						teams.get(newIndex).addUnit(u);
					}
					for(Unit u : teams.get(index).getUnits()) {
						u.setFlagOn(Unit.Flag.AI_CONTROLLED);
					}
					setPlayerTeam(teams.get(newIndex));
				}
				
			}
		}
		changePlayerTeam = false;
	}
	
	
	public boolean processSelectionAt(Point p) {
		if(getPlayerTeam() == null) return false;
		Unit newSelection = null;
		for(Unit u : getPlayerTeam().getUnits()) {
			if(u.getBounds().contains(p) && u instanceof Selectable && u.isFlagOn(GameObject.Flag.SELECTABLE)) {
				((Selectable)u).select();
				newSelection = u;
			}
			if(newSelection != null) break;
		}
		
		if(newSelection != null){
			unselect();
			setFlagOn(FLAG_SELECTED_UNITS);
			selectedUnits.add(newSelection);
		} else if(isFlagOn(FLAG_SELECTED_UNITS)) {
			Unit target = null;
			for(TeamManager tm : teams) {
				if(getPlayerTeam().enemies.contains(tm)) {
					for(Unit u : tm.getUnits()) {
						if(u.getBounds().contains(p)) {
							target = u;
						}
						if(target != null) break;
					}
				}
				if(target != null) break;
			}
			for(Unit u : selectedUnits) {
				if(target != null) {
					u.setTarget(target);
					target.selectedAsTarget();
				} else if(u.isFlagOn(GameObject.Flag.MOVEABLE)) ((MoveableUnit)u).moveToPoint(p, true);
			}
			if(target == null) return true;
			else return false;
		}
		return false;
	}
	
	public void processSelectionAt(Point p, Dimension d) {
		if(getPlayerTeam() == null) return;
		
		unselect();
		ArrayList<Unit> newSelection = new ArrayList<Unit>();
		Rectangle area = new Rectangle(p, d);
		
		for(Unit u : getPlayerTeam().getUnits()) {
			if(u instanceof Selectable && u.isFlagOn(GameObject.Flag.SELECTABLE) && u.getBounds().intersects(area)) {
				((Selectable)u).select();
				newSelection.add(u);
			}
		}
		
		if(newSelection.size() > 0){
			setFlagOn(FLAG_SELECTED_UNITS);
			selectedUnits = newSelection;
		}
	}
	
	public void unselect() {
		for(Unit u : selectedUnits) ((Selectable)u).unselect();
		setFlagOff(FLAG_SELECTED_UNITS);
		selectedUnits.clear();
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}
	
	public void addPlayerTeamTank() {
		Tank t = new IdleTank(this, getPlayerTeam(), getPlayerTeam().getBasePoint().x, getPlayerTeam().getBasePoint().y);
		t.setFlagOn(GameObject.Flag.SELECTABLE);
		t.setFlagOff(Unit.Flag.AI_CONTROLLED);
		t.setFlagOff(Unit.Flag.ENEMY_AWARE);
		getPlayerTeam().addUnit(t);
	}
	
	public void togglePlayerAsTarget() {
		if(isFlagOn(FLAG_PLAYER_TARGETED)) {
			for(TeamManager tm : teams) {
				if(tm != getPlayerTeam() && tm.isFlagOn(TeamManager.Flag.ACTIVE)) tm.removeEnemy(getPlayerTeam());
			}
		} else {
			for(TeamManager tm : teams) {
				if(tm != getPlayerTeam() && tm.isFlagOn(TeamManager.Flag.ACTIVE)) tm.addEnemy(getPlayerTeam());
			}
		}
		toggleFlag(FLAG_PLAYER_TARGETED);
	}
	
	public void toggleEnemyAwareFlag() {
		synchronized (selectedUnits) {
			for(Unit u : selectedUnits){
				u.toggleFlag(Flag.ENEMY_AWARE);
			}
		}
	}
	
	public String getWinnerName() {
		if(winner != null) return winner.getName();
		return null;
	}
	
	public TeamManager getPlayerTeam() {
		return playerTeam;
	}

	public void setPlayerTeam(TeamManager playerTeam) {
		this.playerTeam = playerTeam;
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
	
	public static class Team
	{
		public static final Color COLOR_ORANGE = new Color(255, 128, 0);
		
		public static final int BLUE = 0;
		public static final int RED = 1;
		public static final int GREEN = 2;
		public static final int YELLOW = 3;
		public static final int MAGENTA = 4;
		public static final int ORANGE = 5;
		public static final int CYAN = 6;
		public static final int GRAY = 7;
		public static final int BLACK = 8;
	}
}
