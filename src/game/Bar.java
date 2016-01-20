package game;

import java.awt.Color;
import java.awt.Graphics2D;


public class Bar {
	
	static final int MAX_RED = 255;
	static final int MAX_GREEN = 255;
	static final int BLUE = 0;
	
	static final int TYPE_HEALTH = 0;
	static final int TYPE_POWER = 1;
	
	Unit owner;
	
	double redStep;
	double greenStep;
	
	int maxPoints;
	double slice;
	int width;
	double length;
	int thickness;
	int xDespl;
	int yDespl;
	int type;
	
	boolean fixedColor;
	int red;
	int green;
	Color color;
	
	boolean drawBorder;
	int borderWidth;
	int borderThickness;
	int borderDesplX;
	int borderDesplY;
	
	public Bar(Unit owner, int xDespl, int yDespl, int width, int thickness, int points, int maxPoints, Color color, boolean drawBorder, int type) {
		this.owner = owner;
		this.xDespl = xDespl;
		this.yDespl = yDespl;
		this.width = width;
		this.thickness = thickness;
		this.maxPoints = maxPoints;
		
		this.drawBorder = drawBorder;
		if(drawBorder) {
			borderWidth = width;
			borderThickness = thickness;
			borderDesplX = xDespl;
			borderDesplY = yDespl;
			this.xDespl += 1;
			this.yDespl += 1;
			this.thickness -= 2;
			this.width -= 2;
		}
		
		slice = (double)width / maxPoints;
		redStep = (double)MAX_RED/maxPoints;
		greenStep = (double)MAX_GREEN/maxPoints;
		
		if(color != null) {
			this.color = color;
			fixedColor = true;
		}
		
		this.type = type;
		update(points);
	}
	
	public void update(int points) {
		/*if(type == TYPE_HEALTH) {
			if((double)points / maxPoints <= 0.35) owner.lowHealth(true);
			else owner.lowHealth(false);
		}*/
		
		length = Math.min(points*slice, width);
		if(!fixedColor) updateColor(points);
	}
	
	public void updateColor(int points) {
		red = (int)(redStep*(maxPoints-points));
		green = (int)(greenStep*points);
		color = new Color(red, green, BLUE);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setMaxHitPoints(int max, int points) {
		maxPoints = max;
		slice = (double)width / maxPoints;
		redStep = MAX_RED/maxPoints;
		greenStep = MAX_GREEN/maxPoints;
		update(points);
	}
	
	public void draw(Graphics2D g, int x, int y) {
		if(drawBorder) {
			g.setColor(Color.BLACK);
			g.fillRect(x + borderDesplX, y + borderDesplY, borderWidth, borderThickness);
		}
		g.setColor(color);
		g.fillRect(x + xDespl, y + yDespl, (int)length, thickness);
	}
}
