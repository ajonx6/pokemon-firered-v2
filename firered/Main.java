package firered;

import firered.util.ThreadPool;

import javax.swing.JFrame;

public class Main {
	// Creates the JFrame, as well as a Thread to run the audio, and a Thread to run the game
	public static void main(String[] args) {
		ThreadPool pool = new ThreadPool(2);
		System.out.println("Running on OS: " + System.getProperty("os.name"));

		Game game = Game.getInstance();
		game.frame = new JFrame(Game.TITLE);
		game.frame.setUndecorated(true);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		game.frame.setResizable(false);
		game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setVisible(true);
		
		pool.runTask(game);
		pool.join();
	}
}