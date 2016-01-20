package main;

import java.io.IOException;

import control.Controller;
import ui.ShapeManager;
import ui.UI;
import ui.Window;
import game.Game;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Game game = new Game();
		Controller controller = new Controller();
		game.setShapeManager(new ShapeManager());
		game.setUp();
		UI ui = new UI(game, controller);
		Window window = new Window(ui, controller);
		controller.setComponents(ui, window);
		window.setVisible(true);
		//int paintFlag = 0;
		
		while(true) {
			game.move();
			game.prepareDrawableObjects(ui.getViewableArea());
			ui.update();
			/*if((paintFlag ^= 1) == 1) */ui.repaint();
			Thread.sleep(11);
		}
	}

}
