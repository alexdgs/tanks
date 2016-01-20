package game;

public class MGTurret extends Turret {
	
	static final int MAX_TIME_TO_FIRE = 20;
	static final double MAX_TURN_ANGLE = Math.PI/60.0;
	
	public MGTurret(Unit owner) {
		super(owner);
		maxTimeToFire = MAX_TIME_TO_FIRE;
		maxTurnAngle = MAX_TURN_ANGLE;
	}
	
	@Override
	public void fire() {
		game.newGameObjects.add(new MGTankFire(game, owner.team, owner, target));
	}
}
