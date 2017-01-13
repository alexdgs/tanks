package game;

import java.awt.Point;
import java.awt.Rectangle;

public class GameObject {
	
	static final double TWICE_PI = Math.PI*2.0;
	static final double HALF_PI = Math.PI/2.0;
	static final double MINUS_HALF_PI = HALF_PI*-1.0;
	static final double THREE_HALVES_PI = (3.0*Math.PI)/2.0;
	static final int MAX_ANGLE = 360;
	
	Game game;
	protected double x;
	protected double y;
	protected int w;
	protected int h;
	int maxX;
	int maxY;
	double centerX;
	double centerY;
	int halfWidth;
	int halfHeight;
	private int flags;
	int toX;
	int toY;
	double angle;
	double newAngle;

	public GameObject(Game game, double x, double y, int w, int h) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		maxX = Game.WIDTH - w;
		maxY = Game.HEIGHT - h;
		halfWidth = w/2;
		halfHeight = h/2;
		setFlagOn(Flag.ALIVE);
		updateCenter();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public double getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
	
	public void updateCenter() {
		centerX = x + halfWidth;
		centerY = y + halfHeight;
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, w, h);
	}
	
	public Rectangle getRelativeBounds(int xOffset, int yOffset) {
		return new Rectangle((int)x - xOffset, (int)y - yOffset, w, h);
	}
	
	public boolean contains(int x, int y) {
		return this.getBounds().contains(x, y);
	}
	
	public boolean intersects(GameObject go) {
		return this.getBounds().intersects(go.getBounds());
	}
	
	public double cos(double angle) {
		double cos = Math.cos(angle);
		return (Math.abs(cos) < 1e-15 ? 0.0 : cos);
	}
	
	public double sin(double angle) {
		double sin = Math.sin(angle);
		return (Math.abs(sin) < 1e-15 ? 0.0 : sin);
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
	
	public boolean isTinyDif(double d1, double d2) {
		return Math.abs(d1-d2) < 1e-6;
	}
	
	public double distTo(GameObject go) {
		double dx = go.x-x;
		double dy = go.y-y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double distToCenter(GameObject go) {
		double dx = go.centerX-centerX;
		double dy = go.centerY-centerY;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double distTo(double x, double y) {
		double dx = x-this.x;
		double dy = y-this.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double distCenterTo(double x, double y) {
		double dx = x-this.centerX;
		double dy = y-this.centerY;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public static double distBetween(double x1, double y1, double x2, double y2) {
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double angleTo(GameObject go) {
		double dx = go.centerX - centerX;
		double dy = go.centerY - centerY;
		return (dx < 0.0 ? Math.atan(dy/dx)+Math.PI : Math.atan(dy/dx));
	}
	
	public double angleTo(double x, double y) {
		double dx = x - this.x;
		double dy = y - this.y;
		return (dx < 0.0 ? Math.atan(dy/dx)+Math.PI : Math.atan(dy/dx));
	}
	
	public double angleToCenter(double x, double y) {
		double dx = x - this.centerX;
		double dy = y - this.centerY;
		return (dx < 0.0 ? Math.atan(dy/dx)+Math.PI : Math.atan(dy/dx));
	}
	
	public static double angleBetween(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return (dx < 0.0 ? Math.atan(dy/dx)+Math.PI : Math.atan(dy/dx));
	}
	
	public Point getRandomMapPoint() {
		int x = (int)(Math.random()*(Game.WIDTH-w));
		int y = (int)(Math.random()*(Game.HEIGHT-h));
		return new Point(x, y);
	}
	
	public static int getRandomInteger(int lim) {
		return (int)(Math.random()*lim);
	}
	
	public double newAngle(double lastAngle, double targetAngle, double maxTurnAngle) {
		double newAngle = 0.0;
		double difAngle = lastAngle - targetAngle;
		
		if(difAngle >= 0.0) {
			if(difAngle < Math.PI)
				newAngle = lastAngle - Math.min(difAngle, maxTurnAngle);
			else
				newAngle = lastAngle + Math.min(difAngle, maxTurnAngle);
		} else {
			difAngle = Math.abs(difAngle);
			if(difAngle < Math.PI)
				newAngle = lastAngle + Math.min(difAngle, maxTurnAngle);
			else
				newAngle = lastAngle - Math.min(difAngle, maxTurnAngle);
		}
		
		if(newAngle < MINUS_HALF_PI) newAngle += TWICE_PI;
		else if(newAngle > THREE_HALVES_PI) newAngle -= TWICE_PI;
		
		return newAngle;
	}
	
	public double sumAngles(double a, double b) {
		double newAngle = a + b;
		
		if(newAngle < MINUS_HALF_PI) newAngle += TWICE_PI;
		else if(newAngle > THREE_HALVES_PI) newAngle -= TWICE_PI;
		
		return newAngle;
	}
	/*
	public int getDirectionToAngle(double lastAngle, double targetAngle) {
		int direction = Direction.CLOCKWISE;
		double difAngle = lastAngle - targetAngle;
		
		if(difAngle >= 0.0) {
			if(difAngle < Math.PI)
				direction = Direction.COUNTER_CLOCKWISE;
		} else {
			difAngle = Math.abs(difAngle);
			if(difAngle >= Math.PI)
				direction = Direction.COUNTER_CLOCKWISE;
		}
		
		return direction;
	}
	*/
	static class Flag
	{
		public static final int ALIVE = 0;
		public static final int SELECTABLE = 1;
		public static final int MOVEABLE = 2;
		public static final int HITTABLE = 3;
		public static final int CAN_FIRE = 4;
		public static final int ENEMY_AWARE = 8;
		public static final int AI_CONTROLLED = 9;
		public static final int SELECTED = 10;
		public static final int MOVING = 11;
		public static final int FIRING = 14;
		public static final int HEALING = 16;
		public static final int OVERRIDE_ACTION = 17;
		public static final int MANDATORY_TARGET = 18;
		public static final int HEALED = 26;
	}
	
	static class Direction
	{
		public static final int CLOCKWISE = 0;
		public static final int COUNTER_CLOCKWISE = 1;
	}
}
