import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SimulationPanel extends JPanel {

	static final int gensPerEvolution = 10;
	
	Game game;
	
	public SimulationPanel() {
		super();
		game = new RacetrackGame();
		new Thread(new EvolutionUpdater()).start();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		game.paintFixed(graphics2D);
		update(graphics2D);
	}

	@Override
	public void update(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		game.paintUpdate(graphics2D);
	}

	private class EvolutionUpdater implements Runnable {
		@Override
		public void run() {
			while (true) {
				while (game.getPopulation().getGeneration() % gensPerEvolution != 0) {
					try {
						game.update();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				while (game.getPopulation().getGeneration() % gensPerEvolution == 0) {
					repaint();
					try {
						game.update();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
		}
	}

}
