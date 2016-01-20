package ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ShapeManager {
	
	public static final int CLASSIC_TANK = 0;
	public static final int DOUBLE_TANK = 1;
	public static final int LIGHT_TANK = 2;
	public static final int SHIELDED_TANK = 3;
	public static final int MEDIC_TANK = 4;
	public static final int TNT_TANK = 5;
	public static final int TURRET_TANK = 6;
	public static final int MG_TANK = 7;
	public static final int FLAMETHOWER_TANK = 8;
	public static final int SCATTER_TANK = 9;
	public static final int MISSILE_TANK = 10;
	public static final int SWARM_TANK = 11;
	public static final int TRIPLE_TANK = 12;
	public static final int LIGHTNING_TANK = 13;
	public static final int LASER_TANK = 14;
	public static final int BOMB_TANK = 15;
	
	public static final int GUARD_TOWER = 20;
	public static final int PILLBOX = 21;
	
	public static final int MISSILE_1 = 25;
	public static final int MISSILE_2 = 26;
	public static final int MISSILE_3 = 27;
	public static final int TURRET = 28;
	public static final int SMALL_MISSILE = 29;
	
	static final String[] IMG_FILENAME = {
		"src/img/tank.png",
		"src/img/double_tank.png",
		"src/img/light_tank.png",
		"src/img/shielded_tank.png",
		"src/img/medic_tank.png",
		"src/img/tnt_tank.png",
		"src/img/turret_tank.png",
		"src/img/mg_tank.png",
		"src/img/flamethrower_tank.png",
		"src/img/scatter_tank.png",
		"src/img/missile_tank.png",
		"src/img/swarm_tank.png",
		"src/img/triple_tank.png",
		"src/img/lightning_tank.png",
		"src/img/laser_tank.png",
		"src/img/bomb_tank.png",
		null,
		null,
		null,
		null,
		"src/img/guardtower.png",
		"src/img/pbox.png",
		null,
		null,
		null,
		"src/img/missile1.png",
		"src/img/missile2.png",
		"src/img/missile3.png",
		"src/img/turret.png",
		"src/img/small_missile.png"
	};
	
	Shape[] shapes;
	BufferedImage bi;
	
	public ShapeManager() throws IOException {
		shapes = new Shape[IMG_FILENAME.length];
		for(int i = 0; i < IMG_FILENAME.length; i++)
			if(IMG_FILENAME[i] != null)
				shapes[i] = getOutline(Color.WHITE, ImageIO.read(new File(IMG_FILENAME[i])));
	}
	
	public Shape getShape(int id) {
		return shapes[id];
	}
	
	public Area getOutline(Color target, BufferedImage bi) {
		GeneralPath gp = new GeneralPath();
		
	    boolean cont = false;
	    int targetRGB = target.getRGB();
	    for (int x=0; x<bi.getWidth(); x++) {
	        for (int y=0; y<bi.getHeight(); y++) {
	            if (bi.getRGB(x,y)==targetRGB) {
	                if (cont) {
	                    gp.lineTo(x,y);
	                    gp.lineTo(x,y+1);
	                    gp.lineTo(x+1,y+1);
	                    gp.lineTo(x+1,y);
	                    gp.lineTo(x,y);
	                } else {
	                    gp.moveTo(x,y);
	                }
	                cont = true;
	            } else {
	                cont = false;
	            }
	        }
	        cont = false;
	    }
	    gp.closePath();
	    
	    return new Area(gp);
	}
	
	public static Area buildShape(ArrayList<Point> points) {
		GeneralPath gp = new GeneralPath();
		gp.moveTo(points.get(0).x, points.get(0).y);
		for(int i = 1; i < points.size(); i++) {
			gp.lineTo(points.get(i).x, points.get(i).y);
		}
		gp.closePath();
		
		return new Area(gp);
	}
}
