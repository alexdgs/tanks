package ui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import control.Controller;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	static final int DEFAULT_WIDTH = UI.WIDTH;
	static final int DEFAULT_HEIGHT = UI.HEIGHT;
	
	UI ui;
	Controller controller;
	GraphicsDevice gdevice;
	
	public Window(UI ui, Controller controller) {
		this.ui = ui;
		this.controller = controller;
		
		add(ui);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//setBackground(Color.BLACK);
		setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(controller);
		addKeyListener(controller);
		checkFullScreenAvailability();
	}
	
	public void checkFullScreenAvailability() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gdevice = ge.getDefaultScreenDevice();
		//if(gdevice.isFullScreenSupported()) setFlagOn(FULL_SCREEN_AVAILABLE);
	}
	
	public void changeFullScreenMode() {
		//if(isFlagOn(FULL_SCREEN_AVAILABLE)) {
			try {
				if(ui.isFlagOn(UI.Flag.FULL_SCREEN)) {
					ui.updateSize(Vista.DEFAULT_VISIBLE_W, Vista.DEFAULT_VISIBLE_H);
					ui.setFlagOff(UI.Flag.FULL_SCREEN);
				}
				else {
					gdevice.setFullScreenWindow(this);
					DisplayMode dp = gdevice.getDisplayMode();
					ui.updateSize(dp.getWidth(), dp.getHeight());
					ui.setFlagOn(UI.Flag.FULL_SCREEN);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(!ui.isFlagOn(UI.Flag.FULL_SCREEN)) gdevice.setFullScreenWindow(null);
			}
		//} else System.out.println("Full screen not available");
	}
}
