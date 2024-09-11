package tetrominos;

import tetrominos.controller.GameController;

class Tetrominos {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameController game = new GameController();
				game.start();
			}
		});
	}

}