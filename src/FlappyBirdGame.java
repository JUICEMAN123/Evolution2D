import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class FlappyBirdGame extends Game {

	final static int QUANTITY = 1000;

	public FlappyBirdGame() {
		Bird[] birds = new Bird[QUANTITY];
		Bird.pillar = new Pillar(0);
		for (int i = 0; i < birds.length; i++) {
			birds[i] = new Bird(new EvolutionAI(4, 6, 4, 1));
		}
		setPopulation(birds, "Flappy");
	}
	
	public FlappyBirdGame(Population population) {
		super(population);
		Bird.pillar = new Pillar(0);
	}

	@Override
	void paintFixed(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1000, 800);
	}

	@Override
	void paintUpdate(Graphics2D g) {
		// pillar
		g.setColor(Color.DARK_GRAY);
		g.fillRect(Bird.pillar.getX(), 0, Pillar.width, Bird.pillar.getyOpening() - Bird.pillar.getyOpeningSize() / 2);
		g.fillRect(Bird.pillar.getX(), Bird.pillar.getyOpening() + Bird.pillar.getyOpeningSize() / 2, Pillar.width,
				800 - (Bird.pillar.getyOpening() + Bird.pillar.getyOpeningSize() / 2));

		// birds
		g.setColor(Color.BLUE);
		Bird[] birds = (Bird[]) getPopulation().getIndividuals();
		for (int i = 0; i < birds.length; i++) {
			if (birds[i].isAlive()) {
				g.fillRect(Bird.X, birds[i].getY(), Bird.BIRDSIZE, Bird.BIRDSIZE);
			}
		}
		g.setFont(new Font("Arial", Font.BOLD, 60));
		g.drawString("" + Bird.pillar.getElapsed(), 850, 100);
	}

	@Override
	void updateLogic() {
		Bird[] birds = (Bird[]) getPopulation().getIndividuals();
		for (int i = 0; i < birds.length; i++) {
			birds[i].updatePos();
		}
		Bird.pillar.updatePos();
		if (Bird.pillar.getX() + Pillar.width < 0) {
			Bird.pillar = new Pillar(Bird.pillar.getElapsed() + 1);
			for (int i = 0; i < birds.length; i++) {
				if (birds[i].isAlive()) {
					birds[i].incrementScore();
				}
			}
		}
	}

}

class Bird extends Individual {

	static final int X = 150, BIRDSIZE = 20;

	private int y = 400;
	private int score;
	static public Pillar pillar;

	public int getY() {
		return y;
	}

	public void incrementScore() {
		score++;
	}

	public Bird(EvolutionAI ai) {
		super(ai);
	}

	public void updatePos() {
		if (getOutputsOfInputs(new double[] { yDistToPillarOpening(), xDistToPillar(), pillar.getyOpeningSize(), y})[0].getValue() < 0) {
			y -= 45;
		}
		else {
			y += 15;
		}
		live();
	}

	@Override
	public double calcFitness() {
		return (double)score - (Math.tanh(Math.abs(yDistToPillarOpening() / 50.0)));
	}

	@Override
	public void reset() {
		y = 400;
		score = 0;
		pillar = new Pillar(0);
	}

	@Override
	public boolean dyingCondition() {
		return score >= 1000 || pillar == null || y < 0 || y > 800 - BIRDSIZE
				|| (X + BIRDSIZE > pillar.getX() && X < pillar.getX() + Pillar.width)
						&& !(y > pillar.getyOpening() - pillar.getyOpeningSize() / 2
								&& y + BIRDSIZE < pillar.getyOpening() + pillar.getyOpeningSize() / 2);
	}

	public int yDistToPillarOpening() {
		return pillar.getyOpening() - y;
	}

	public int xDistToPillar() {
		return pillar.getX() - X;
	}

}

class Pillar {

	static final int width = 75;
	private int x, yOpening, yOpeningSize, speed;
	private int elapsed;

	public int getElapsed() {
		return elapsed;
	}

	public int getX() {
		return x;
	}

	public int getyOpening() {
		return yOpening;
	}

	public int getyOpeningSize() {
		return yOpeningSize;
	}

	public int getSpeed() {
		return speed;
	}

	public Pillar(int elapsed) {
		x = 1000;
		yOpening = 100 + (int) (Math.random() * 600);
		yOpeningSize = (int)(75 * (Math.log10(1005 - elapsed)));
		speed = (int)(10 * ((Math.log10(elapsed + 10))));
		this.elapsed = elapsed;
	}

	public void updatePos() {
		x -= speed;
	}

}