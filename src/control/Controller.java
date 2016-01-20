package control;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ui.UI;
import ui.Vista;
import ui.Window;

public class Controller implements KeyListener, MouseListener, MouseMotionListener, WindowListener {
	
	static final int MIN_PIXELS_TO_DRAG = 5;
	
	UI ui;
	Window window;
	
	boolean mouseLeftPressed;
	boolean mouseRightPressed;
	boolean rightDragging;
	Point startPoint;
	Point prevPoint;
	
	public Controller() {
		
	}
	
	public void setComponents(UI ui, Window window) {
		this.ui = ui;
		this.window = window;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		if(e.getKeyCode() == KeyEvent.VK_UP) ui.moveVista(Vista.UP);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) ui.moveVista(Vista.DOWN);
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) ui.moveVista(Vista.LEFT);
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) ui.moveVista(Vista.RIGHT);
		else if(e.getKeyCode() == KeyEvent.VK_I) ui.toggleShowInfo();
		else if(e.getKeyCode() == KeyEvent.VK_F) window.changeFullScreenMode();
		else if(e.getKeyCode() == KeyEvent.VK_A) ui.addPlayerTeamTank();
		else if(e.getKeyCode() == KeyEvent.VK_E) ui.togglePlayerAsTarget();
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) ui.endGame();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getX() + " " + e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouseLeftPressed = true;
			startPoint = new Point(e.getX(), e.getY());
		}
		else if(e.getButton() == MouseEvent.BUTTON3) {
			mouseRightPressed = true;
			prevPoint = new Point(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point releasePoint = new Point(e.getX(), e.getY());
		if(e.getButton() == MouseEvent.BUTTON1 && mouseLeftPressed) {
			if(Math.abs(startPoint.x-releasePoint.x) < MIN_PIXELS_TO_DRAG && Math.abs(startPoint.y-releasePoint.y) < MIN_PIXELS_TO_DRAG) {
				ui.leftClick(releasePoint);
				//System.out.println("Left");
			} else {
				ui.dragged(startPoint, releasePoint);
				//System.out.println("drag");
			}
			mouseLeftPressed = false;
		} else if(e.getButton() == MouseEvent.BUTTON3 && mouseRightPressed) {
			if(rightDragging) {
				rightDragging = false;
			} else {
				ui.rightClick(releasePoint);
			}
			mouseRightPressed = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(mouseLeftPressed) {
			Point dragLimit = new Point(e.getX(), e.getY());
			if(Math.abs(startPoint.x-dragLimit.x) > MIN_PIXELS_TO_DRAG && Math.abs(startPoint.y-dragLimit.y) > MIN_PIXELS_TO_DRAG) {
				ui.dragging(startPoint, dragLimit);
			}
		} else if(mouseRightPressed) {
			int dx = prevPoint.x - e.getX();
			int dy = prevPoint.y - e.getY();
			ui.moveVista(dx, dy);
			prevPoint = new Point(e.getX(), e.getY());
			rightDragging = true;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		//window.endGame();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
