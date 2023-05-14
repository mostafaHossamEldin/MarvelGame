package views;

import engine.*;
import java.awt.event.*;
import java.io.IOException;

public class Controller implements ActionListener {
	
	private Game game;
	private View view;
	
	public Controller() {
		view = new View(this);
		view.setPlayers();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Controller();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
