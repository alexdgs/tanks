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
		int paintCounter = 0;
		
		while(true) {
			game.move();
			game.prepareDrawableObjects(ui.getViewableArea());
			ui.update();
			if(!(game.isFlagOn(Game.FLAG_FAST_FORWARD)) || (paintCounter = ++paintCounter % game.getFastForwardRate()) == 0) ui.repaint();
			Thread.sleep(game.loopMilis);
		}
	}

}
