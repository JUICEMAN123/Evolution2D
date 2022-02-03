import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class RacetrackGame extends Game {

	final static int QUANTITY = 100;

	public RacetrackGame() {
		Car[] cars = new Car[QUANTITY];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new Car(new EvolutionAI(2, 8, 8, 1), 500 - Car.CARSIZE / 2, 137 - Car.CARSIZE / 2);
		}
		setPopulation(cars, "Racetrack");
	}

	public RacetrackGame(Population population) {
		super(population);
	}

	@Override
	void paintFixed(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 800);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(5));

		// outer border
		g.drawArc(100, 100, 400, 400, 90, 180);
		g.drawArc(500, 100, 400, 400, -90, 180);
		g.drawLine(300, 100, 700, 100);
		g.drawLine(300, 500, 700, 500);

		// inner border
		g.drawArc(175, 175, 250, 250, 90, 180);
		g.drawArc(575, 175, 250, 250, -90, 180);
		g.drawLine(300, 175, 700, 175);
		g.drawLine(300, 425, 700, 425);
	}

	@Override
	void paintUpdate(Graphics2D g) {
		Car[] cars = (Car[]) getPopulation().getIndividuals();
		for (int i = 0; i < cars.length; i++) {
			if (cars[i].isAlive()) {
				g.fillRect(cars[i].getX(), cars[i].getY(), Car.CARSIZE, Car.CARSIZE);
			}
		}
	}

	@Override
	void updateLogic() {
		Car[] cars = (Car[]) getPopulation().getIndividuals();
		for (int i = 0; i < cars.length; i++) {
			if (cars[i].isAlive()) {
				cars[i].updatePos();
			}
		}
		Car.updates++;
	}

}

class Car extends Individual {

	public final static int CARSIZE = 10;
	public static int updates = 0;
	
	private int origX, origY;
	private int score;
	private int x, y;
	private ArrayList<Point> previousPoints = new ArrayList<>();
	private ArrayList<Double> directions = new ArrayList<Double>();
	private double direction = 0;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Car(EvolutionAI ai, int x, int y) {
		super(ai);
		origX = this.x = x;
		origY = this.y = y;
	}

	private boolean tracker = false;
	public void updatePos() {
		direction += getOutputsOfInputs(new double[] { distanceToInner(), distanceToOuter() })[0].getValue();
		x += 10 * Math.cos((direction) * Math.PI);
		y += 10 * Math.sin((direction) * Math.PI);
		live();
		previousPoints.add(new Point(x, y));
		directions.add(direction);
		if(!tracker && (Math.abs(x - 300) < 10 && Math.abs(y - 137) < 37)){
			tracker = true;
		}
		else if (tracker && Math.abs(x - 700) < 10) {
			score++;
			previousPoints.clear();
			directions.clear();
		}
	}

	@Override
	public double calcFitness() {
		return score + Math.tanh((10 * score) / (double) updates);
	}

	@Override
	public boolean dyingCondition() {
		return !checkInTrack() || (previousPoints.contains(new Point(x, y)) && directions.contains(direction)) || score == 1000;
	}

	@Override
	public void reset() {
		x = origX;
		y = origY;
		direction = 0d;
		updates = 0;
		score = 0;
		previousPoints.clear();
		directions.clear();
	}

	private boolean checkInTrack() {
		if (x >= 300 && x <= 700) {
			return (y > 100 && y < 175) || (y > 425 && y < 500);
		} else if (x > 700 && x < 900) {
			return inEllipse(700, 300, 200, 200) && !inEllipse(700, 300, 125, 125);
		} else if (x > 100 && x < 300) {
			return inEllipse(300, 300, 200, 200) && !inEllipse(300, 300, 125, 125);
		} else {
			return false;
		}
	}

	private boolean inEllipse(int xShift, int yShift, int xRadius, int yRadius) {
		return (Math.pow(x - xShift, 2) / (xRadius * xRadius)) + (Math.pow(y - yShift, 2) / (yRadius * yRadius)) < 1;
	}

	private double distanceToOuter() {
		if (x >= 300 && x <= 700) {
			return Math.min(Math.abs(y - 100), Math.abs(y - 500));
		} else if (x > 700 && x < 900) {
			return 200 - Math.sqrt((Math.pow(x - 700, 2) + Math.pow(y - 300, 2)));
		} else if (x > 100 && x < 300) {
			return 200 - Math.sqrt((Math.pow(x - 300, 2) + Math.pow(y - 300, 2)));
		}
		return -1;
	}

	private double distanceToInner() {
		if (x >= 300 && x <= 700) {
			return Math.min(Math.abs(y - 175), Math.abs(y - 425));
		} else if (x > 700 && x < 900) {
			return 125 - Math.sqrt((Math.pow(x - 700, 2) + Math.pow(y - 300, 2)));
		} else if (x > 100 && x < 300) {
			return 125 - Math.sqrt((Math.pow(x - 300, 2) + Math.pow(y - 300, 2)));
		}
		return -1;
	}

}