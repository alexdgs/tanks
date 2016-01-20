package ui;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.ImageIcon;

public class TrayIconManager {
	
	TrayIcon trayIcon;
	SystemTray tray;
	
	public TrayIconManager() throws UnsupportedOperationException {
		if(!SystemTray.isSupported()) throw new UnsupportedOperationException();
		
		trayIcon = new TrayIcon(new ImageIcon("src/img/tray_icon.png").getImage(), "My Game");
		tray = SystemTray.getSystemTray();
		try {
			tray.add(trayIcon);
			trayIcon.setImageAutoSize(true);
			trayIcon.displayMessage("My Game", "Game is running", TrayIcon.MessageType.INFO);
		} catch(AWTException awte) {
			System.err.println("Error adding tray icon");
		}
	}
	
	public void showMessage(String s) {
		trayIcon.displayMessage("Winner!", s, TrayIcon.MessageType.INFO);
	}
}
