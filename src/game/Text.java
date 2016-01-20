package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text extends GameObject implements Steppable, Drawable {
	
	static final int MAX_TIME = 100;
	static final Font font = new Font("Tahoma", Font.BOLD, 18);
	
	String text;
	Color color = Color.GRAY;
	int time;
	float alpha;
	
	public Text(Game game, String text, double x, double y) {
		super(game, x, y, 10, 10);
		this.text = text;
	}
	
	public Text(Game game, int number, double x, double y) {
		super(game, x, y, 10, 10);
		if(number < 0) {
			color = Color.RED;
			text = Integer.toString(number);
		} else {
			color = Color.GREEN;
			text = "+" + number;
		}
		
		
	}
	
	public Text(Game game, String text, double x, double y, Color color) {
		this(game, text, x, y);
		this.color = color;
	}
	
	@Override
	public void step() {
		if(time >= MAX_TIME) {
			setFlagOff(Flag.ALIVE);
			return;
		}
		
		y--;
		time++;
		alpha = 1.0f - ((float)time / MAX_TIME);
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		Font backup = g2d.getFont();
		g2d.setColor(color);
		g2d.setFont(font);
		g2d.drawString(text, x, y);
		g2d.setFont(backup);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}
