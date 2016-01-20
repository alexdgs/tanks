package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Circle extends GameObject implements Steppable, Drawable {
	
	static final int INIT_FACTOR = 10;
	static final int TIME_EFFECT_HEAL = 65;
	static final int TIME_EFFECT_TARGET = 60;
	static final int TIME_EFFECT_EXPL = 70;
	static final int TIME_EFFECT_ELECTRIC = 50;
	static final Color COLOR_EFFECT_HEAL = Color.YELLOW;
	static final Color COLOR_EFFECT_TARGET = Color.RED;
	static final Color COLOR_EFFECT_EXPL = Color.ORANGE;
	static final Color COLOR_EFFECT_ELECTRIC = Lightning.COLOR;
	
	static final int ELECTRIC_FLASHING_TIME = Lightning.FLASHING_TIME;
	
	int initX;
	int initY;
	int maxTime;
	int time;
	int type;
	int initRadius;
	int radius;
	int addRadius;
	float alpha;
	Color color;
	
	public Circle(int x, int y, int w, int type) {
		super(null, x, y, w, w);
		
		if(type == Type.ELECTRIC) {
			this.x = x - (w/2 + w/(INIT_FACTOR*2));
			this.y = y - (w/2 + w/(INIT_FACTOR*2));
			this.w = this.h = w + (w / INIT_FACTOR);
		} else {
			this.initRadius = w / INIT_FACTOR;
			this.addRadius = w;
			initX = x;
			initY = y;
		}
		
		setType(type);
		time = maxTime;
	}
	
	public void setType(int type) {
		this.type = type;
		switch(type) {
		case Type.EXPL:
			color = COLOR_EFFECT_EXPL;
			maxTime = TIME_EFFECT_EXPL;
			break;
		case Type.HEAL:
			color = COLOR_EFFECT_HEAL;
			maxTime = TIME_EFFECT_HEAL;
			break;
		case Type.TARGET:
			color = COLOR_EFFECT_TARGET;
			maxTime = TIME_EFFECT_TARGET;
			break;
		case Type.ELECTRIC:
			color = COLOR_EFFECT_ELECTRIC;
			maxTime = TIME_EFFECT_ELECTRIC;
			break;
		}
	}
	
	@Override
	public void step() {
		if(time > 0) {
			if(type == Type.ELECTRIC) {
				if(time % ELECTRIC_FLASHING_TIME == 0) alpha = 0.75f - alpha;
			} else {
				float effectFactor = (float)time/maxTime;
				radius = initRadius + (int)(addRadius*(1.0f - effectFactor));
				w = h = radius*2;
				alpha = effectFactor;
				x = initX - radius;
				y = initY - radius;
			}
			time--;
		} else setFlagOff(Flag.ALIVE);
	}
	
	public void restart() {
		alpha = 1.0f;
		time = maxTime;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setColor(color);
		g2d.fillOval(x, y, w, h);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

	static class Type
	{
		static final int EXPL = 0;
		static final int HEAL = 1;
		static final int TARGET = 2;
		static final int ELECTRIC = 3;
	}
}
