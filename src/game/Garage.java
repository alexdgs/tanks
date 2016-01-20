package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;

public class Garage extends ConstructorBuilding implements Drawable {
	
	static final int WIDTH = 60;
	static final int HEIGHT = 60;
	static final int HIT_POINTS = 250;
	static final double VISIBLE_DIST = 100.0;
	static final int TURRET_DESPL = (WIDTH - Turret.WIDTH)/2;
	static final Image IMG_GARAGE = (new ImageIcon("src/img/garage.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_FAST);
	
	static final int TYPE_TO_ORDER = Type.RANDOM;
	@SuppressWarnings("rawtypes")
	static final Class[] classes = {
		ClassicTank.class,
		DoubleTank.class,
		LightTank.class,
		ShieldedTank.class,
		MedicTank.class,
		TNTTank.class,
		TurretTank.class,
		MGTank.class,
		FlamethrowerTank.class,
		ScatterTank.class,
		MissileTank.class,
		SwarmTank.class,
		TripleTank.class,
		LightningTank.class,
		LaserTank.class,
		BombTank.class
	};
	
	static Constructor<Tank>[] constructors;
	int constructionTime[];
	
	int unitInProcess;
	int medicCounter = 0;
	
	//Turret turret;
	
	public Garage(Game game, TeamManager team, double x, double y) {
		super(game, team, x, y, WIDTH, HEIGHT, HIT_POINTS, VISIBLE_DIST);
		turretDespl = TURRET_DESPL;
		//turret = new BuildingTurret(this);
		prepareConstructors();
		//setFlagOn(Flag.CAN_FIRE);
		
		order(TYPE_TO_ORDER);
	}
	
	@SuppressWarnings("unchecked")
	public void prepareConstructors() {
		constructors = new Constructor[classes.length];
		constructionTime = new int[classes.length];
		for(int i = 0; i < classes.length; i++) {
			constructors[i] = (classes[i].getConstructors())[0];
			try {
				constructionTime[i] = classes[i].getField("CONSTRUCTION_TIME").getInt(classes[i]);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void step() {
		if(hitPoints < 1) {
			game.newGameObjects.add(new Wave(game, team, centerX, centerY));
			setFlagOff(Flag.ALIVE);
			destroy();
			return;
		}
		
		if(timeFinishProcess > 0) {
			if(timeActualProcess == timeFinishProcess) {
				build();
				order(TYPE_TO_ORDER);
			} else progressBar.update(++timeActualProcess);
		}
		
		stepEffects();
		//turret.step();
	}
	
	public void order(int op) {
		int type = op;
		if(op == Type.RANDOM) {
			do {
				if((type = getRandomInteger(classes.length)) == Type.MEDIC) medicCounter++;
			} while(medicCounter < 3 && type == Type.MEDIC);
			
			if(medicCounter == 3) {
				type = Type.MEDIC;
				medicCounter = 0;
			}
		}
		
		unitInProcess = type;
		timeFinishProcess = constructionTime[type];
		progressBar.setMaxHitPoints(timeFinishProcess, timeActualProcess = 0);
	}
	
	public void underAttack() {
		team.defend(x, y);
	}
	
	@Override
	public void build() {
		Tank t;
		try {
			t = (Tank) constructors[unitInProcess].newInstance(game, team, x + (halfWidth - Tank.HALF_WIDTH), y + (halfHeight - Tank.HALF_HEIGHT));
			team.newUnits.add(t);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		drawPrevElements(g2d, x, y);
		
		g2d.setColor(team.color);
		g2d.fillRect(x, y, WIDTH, HEIGHT);
		g2d.drawImage(IMG_GARAGE, x, y, null);
		//turret.draw(g2d, x, y);
		
		drawAddElements(g2d, x, y);
	}
	
	static class Type
	{	
		static final int RANDOM = -1;
		static final int CLASSIC = 0;
		static final int DOUBLE = 1;
		static final int LIGHT = 2;
		static final int SHIELDED = 3;
		static final int MEDIC = 4;
		static final int TNT = 5;
		static final int TURRET = 6;
		static final int MG = 7;
		static final int FLAMETHOWER = 8;
		static final int SCATTER = 9;
		static final int MISSILE = 10;
		static final int SWARM = 11;
		static final int TRIPLE = 12;
		static final int LIGHTNING = 13;
		static final int LASER = 14;
		static final int BOMB = 15;
	}
}
