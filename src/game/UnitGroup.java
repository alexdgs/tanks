package game;

import java.awt.Point;
import java.util.ArrayList;

public class UnitGroup {

	static final int MIN_UNITS_IN_GROUP = 3;
	static final int MAX_UNITS_IN_GROUP = 5;
	static final int MAX_EXTRA_UNITS_IN_GROUP = MAX_UNITS_IN_GROUP - MIN_UNITS_IN_GROUP;
	
	TeamManager team;
	TeamManager targetTeam;
	int minUnitsToDeploy;
	boolean valid, deployed;
	
	ArrayList<Unit> units;
	
	public UnitGroup(TeamManager team) {
		this.team = team;
		units = new ArrayList<Unit>();
		valid = true;
		this.minUnitsToDeploy = MIN_UNITS_IN_GROUP + GameObject.getRandomInteger(MAX_EXTRA_UNITS_IN_GROUP) + 1;
	}
	
	public UnitGroup(TeamManager team, TeamManager targetTeam) {
		this(team);
		this.targetTeam = targetTeam;
	}
	
	public UnitGroup(TeamManager team, TeamManager targetTeam, int minUnitsToDeploy) {
		this(team, targetTeam);
		this.minUnitsToDeploy = minUnitsToDeploy;
	}
	
	public void addUnit(Unit u) {
		units.add(u);
		if(!deployed) {
			checkDeployment();
		}
	}
	
	public void checkDeployment() {
		if(units.size() >= minUnitsToDeploy) {
			deploy();
		}
	}
	
	public void step() {
		ArrayList<Unit> aliveUnits = new ArrayList<Unit>();
		for(Unit u : units) {
			if(u.isFlagOn(Unit.Flag.ALIVE)) {
				aliveUnits.add(u);
			}
		}
		units = aliveUnits;
		if(units.size() < 1) {
			valid = false;
		}
		
		/*if(team == null || !team.isFlagOn(TeamManager.Flag.ACTIVE)) {
			targetTeam = team.getStrongestEnemyTeam();
		}*/
	}
	
	public void deploy() {
		if(targetTeam == null) {
			targetTeam = team.getStrongestEnemyTeam();
		}
		deployed = true;
	}
	
	public Point getTargetPoint() {
		return (targetTeam != null && targetTeam.isFlagOn(TeamManager.Flag.ACTIVE)) ? targetTeam.getBasePoint() : null;
	}
}
