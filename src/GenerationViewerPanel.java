import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GenerationViewerPanel extends JPanel {

	private Game game;
	private int gen;
	private GenerationViewerFrame frame;
	private boolean go = false;
	
	public boolean getGo() {
		return go;
	}
	
	public void setGo(boolean go) {
		this.go = go;
	}

	public GenerationViewerPanel(GenerationViewerFrame frame) {
		super();
		this.frame = frame;
		setMinimumSize(new Dimension(1000, 800));
		setMaximumSize(new Dimension(1000, 800));
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		if (game == null)
			return;
		Graphics2D graphics2D = (Graphics2D) graphics;
		game.paintFixed(graphics2D);
		update(graphics2D);
	}

	@Override
	public void update(Graphics graphics) {
		if (game == null)
			return;
		Graphics2D graphics2D = (Graphics2D) graphics;
		game.paintUpdate(graphics2D);
	}

	public void startSimulation(Population population) {
		switch (population.getType()) {
		case "Flappy":
			game = new FlappyBirdGame(population);
			break;
		case "Racetrack":
			game = new RacetrackGame(population);
			break;
		}
		gen = population.getGeneration();
		new Thread(new EvolutionUpdater()).start();
	}

	private class EvolutionUpdater implements Runnable {
		@Override
		public void run() {
			while (go && !game.getPopulation().isGenerationOver()) {
				repaint();
				try {
					game.update();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(frame.speedSlider.getValue() < 1000) {
					try {
						Thread.sleep(1000 / frame.speedSlider.getValue());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			repaint();
		}
	}

}
