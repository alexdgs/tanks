package game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class TeamManager {
	
	static final int MIDDLE = 0;
	static final int UP = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;
	static final int RIGHT = 4;
	
	static final int BUILDING_DIST = 65;
	static final int TIME_NOTIFY_DEFEND = 400;
	
	static final int STRATEGY_BASE = 1;
	static final int STRATEGY_OUT = 2;
	
	static final int MAX_TANKS_GO_BASE = 2;
	static final int MIN_TANKS_GO_OUT = 5;
	static final double MAX_DIST_BASE_ON_DEFENSE = 90.0;
	
	public Color color;
	public String string;
	int id;
	int xBase;
	int yBase;
	int flags;
	int strategy;
	
	int xHealer;
	int yHealer;
	boolean healerAvailable;
	
	public int score;
	public int looses;
	public int remainingGarages;
	public double efficacy;
	int LR;
	int UD;
	int timeNotifyDefend = 0;
	
	private ArrayList<Unit> units;
	ArrayList<Unit> newUnits;
	ArrayList<TeamManager> enemies;
	ArrayList<UnitGroup> groups;
	Game game;
	
	public TeamManager(Game game, int teamId, Color color, String string, int x, int y, int UD, int LR, boolean ai) {
		this.game = game;
		this.id = teamId;
		this.color = color;
		this.string = string;
		xBase = x;
		yBase = y;
		this.LR = LR;
		this.UD = UD;
		units = new ArrayList<Unit>();
		newUnits = new ArrayList<Unit>();
		enemies = new ArrayList<TeamManager>();
		groups = new ArrayList<UnitGroup>();
		
		if(ai) {
			setFlagOn(Flag.AI_TEAM);
			initAI();
		} else initPlayer();
		
		strategy = STRATEGY_BASE;
		setFlagOn(Flag.ACTIVE);
	}
	
	public void initPlayer() {
		//units.add(new ShieldedTank(game, this, xBase, yBase));
		//units.add(new SwarmTank(game, this, xBase + 30, yBase));
		//units.add(new SwarmTank(game, this, xBase, yBase + 30));
		//units.add(new SwarmTank(game, this, xBase + 30, yBase + 30));
		//units.add(new SwarmTank(game, this, xBase + 60, yBase));
		//units.add(new SwarmTank(game, this, xBase, yBase + 60));
		//units.add(new SwarmTank(game, this, xBase + 60, yBase + 30));
		//units.add(new SwarmTank(game, this, xBase + 30, yBase + 60));
		//Unit u = new IdleTank(game, this, xBase, yBase);
		//u.setFlagOn(Unit.Flag.AI_CONTROLLED);
		//units.add(u);
	}
	
	public void initAI() {
		units.add(new Garage(game, this, xBase, yBase));
		//units.add(new Garage(game, this, xBase, yBase));
		
		int g2x = 0;
		int g2y = 0;
		int g3x = 0;
		int g3y = 0;
		
		if(UD == UP || LR == MIDDLE) {
			if(LR == LEFT) g2x += BUILDING_DIST;
			else g2x -= BUILDING_DIST;
		} else {
			g2y -= BUILDING_DIST;
		}
		
		if(UD == DOWN || LR == MIDDLE) {
			if(LR == RIGHT) g3x -= BUILDING_DIST;
			else g3x += BUILDING_DIST;
		} else {
			g3y += BUILDING_DIST;
		}
		
		units.add(new Garage(game, this, xBase + g2x, yBase + g2y));
		units.add(new Garage(game, this, xBase + g3x, yBase + g3y));
		
		units.add(new GuardTower(game, this, xBase + g2x*2 + BUILDING_DIST/2 - GuardTower.WIDTH/2, yBase + g2y*2 + BUILDING_DIST/2 - GuardTower.HEIGHT/2));
		units.add(new GuardTower(game, this, xBase + g3x*2 + BUILDING_DIST/2 - GuardTower.WIDTH/2, yBase + g3y*2 + BUILDING_DIST/2 - GuardTower.HEIGHT/2));
		
		units.add(new Pillbox(game, this, xBase + g2x*2.5 + BUILDING_DIST/2 - Pillbox.WIDTH/2, yBase + g2y*2.5 + BUILDING_DIST/2 - Pillbox.HEIGHT/2));
		units.add(new Pillbox(game, this, xBase + g3x*2.5 + BUILDING_DIST/2 - Pillbox.WIDTH/2, yBase + g3y*2.5 + BUILDING_DIST/2 - Pillbox.HEIGHT/2));
		
		units.add(new DoubleGun(game, this, xBase + g2x + BUILDING_DIST/2 - DoubleGun.WIDTH/2, yBase + g3y + BUILDING_DIST/2 - DoubleGun.HEIGHT/2));
		units.add(new DoubleGun(game, this, xBase + g3x + BUILDING_DIST/2 - DoubleGun.WIDTH/2, yBase + g2y + BUILDING_DIST/2 - DoubleGun.HEIGHT/2));
		
		xHealer = xBase - g3x/2 + BUILDING_DIST/2 - Healer.WIDTH/2;
		yHealer = yBase - g2y/2 + BUILDING_DIST/2 - Healer.HEIGHT/2;
		units.add(new Healer(game, this, xHealer, yHealer));
		xHealer -= Healer.WIDTH/2;
		yHealer -= Healer.HEIGHT/2;
		
		remainingGarages = 3;
		healerAvailable = true;
		setFlagOn(Flag.HAS_REAMINING_GARAGES);
	}
	
	public void step() {
		if(newUnits.size() > 0) setFlagOn(Flag.ACTIVE);
		
		if(!isFlagOn(Flag.ACTIVE)) return;
		
		if(timeNotifyDefend > 0) timeNotifyDefend--;
		units.addAll(newUnits);
		newUnits.clear();
		
		if(units.size() < 1) {
			deactivate();
			return;
		}
		
		ArrayList<TeamManager> aliveEnemies = new ArrayList<TeamManager>();
		for(TeamManager tm : enemies) {
			if(tm.isFlagOn(Flag.ACTIVE)) aliveEnemies.add(tm);
		}
		enemies = aliveEnemies;
		
		ArrayList<UnitGroup> validGroups = new ArrayList<UnitGroup>();
		for(UnitGroup ug : groups) {
			ug.step();
			if(ug.valid) {
				validGroups.add(ug);
			}
		}
		groups = validGroups;
		
		ArrayList<Unit> remainingUnits = new ArrayList<Unit>();
		int numTanks = 0;
		for(Unit u : units) {
			u.step();
			if(u.isFlagOn(Unit.Flag.ALIVE)) {
				remainingUnits.add(u);
				if(u instanceof Tank && u.isFlagOn(GameObject.Flag.AI_CONTROLLED)) {
					numTanks++;
				}
			}
		}
		units = remainingUnits;
		
		if(!hasRemainingGarages()) setFlagOff(Flag.HAS_REAMINING_GARAGES);
		if(strategy == STRATEGY_BASE && numTanks >= MIN_TANKS_GO_OUT){
			strategy = STRATEGY_OUT;
		} else if(strategy == STRATEGY_OUT && numTanks <= MAX_TANKS_GO_BASE){
			strategy = STRATEGY_BASE;
		}
	}
	
	public boolean hasRemainingGarages() {
		return remainingGarages > 0;
	}

	public void handleNewUnit(Unit u) {
		UnitGroup ug = getAvailableGroup();
		ug.addUnit(u);
		u.setGroup(ug);
	}
	
	public UnitGroup getAvailableGroup() {
		UnitGroup aug = null;
		for(UnitGroup ug : groups) {
			if(!ug.deployed) {
				aug = ug;
				break;
			}
		}
		if(aug == null) {
			aug = new UnitGroup(this);
			groups.add(aug);
		}
		return aug;
	}
	
	public Point getBasePoint() {
		return new Point(xBase, yBase);
	}
	
	public TeamManager getStrongestEnemyTeam() {
		ArrayList<TeamManager> enemyList = new ArrayList<TeamManager>();
		for(TeamManager e : enemies) {
			if(enemyList.size() < 1 || (enemyList.get(0).remainingGarages <= e.remainingGarages)){
				if(enemyList.size() > 0 && enemyList.get(0).remainingGarages < e.remainingGarages) {
					enemyList.clear();
				}
				enemyList.add(e);
			}
		}
		
		int numEnemies = enemyList.size();
		int enemyIdx = GameObject.getRandomInteger(numEnemies);
		return numEnemies > 0 ? enemyList.get(enemyIdx) : null;
	}
	
	public String getName() {
		return string;
	}
	
	public void addEnemy(TeamManager team) {
		enemies.add(team);
	}
	
	public void removeEnemy(TeamManager team) {
		enemies.remove(team);
	}
	
	public void addUnit(Unit u) {
		u.team = this;
		newUnits.add(u);
	}
	
	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}
	
	public int getNumUnits() {
		return units.size();
	}
	
	public Point getTargetPoint(Unit u) {
		if(u.group != null) return u.group.getTargetPoint();
		else return null;
	}

	public void score(int i) {
		score += i;
		updateEfficacy();
	}
	
	public void unitLost() {
		looses++;
		updateEfficacy();
	}
	
	public void defend(double x, double y) {
		if(timeNotifyDefend == 0) {
			for(Unit u : units) {
				if(u instanceof Tank) {
					Tank t = (Tank)u;
					t.defendPoint(x, y);
				}
			}
			timeNotifyDefend = TIME_NOTIFY_DEFEND;
		}
	}
	
	public void updateEfficacy() {
		if(looses == 0) efficacy = 0.0;
		else efficacy = (double)score/looses;
	}
	
	public void deactivate() {
		setFlagOff(Flag.ACTIVE);
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
	
	static class Flag
	{
		static final int ACTIVE = 0;
		static final int AI_TEAM = 1;
		static final int HAS_REAMINING_GARAGES = 5;
	}
}
