import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Game implements Serializable {

	public final static String SAVE_FILEPATH = "D:\\Programs\\eclipse2020-09\\WORKSPACES\\ai-workspace\\EvolutionOn2D\\EvolutionSaves\\";
	private Population population;
	private boolean isSimulation = false;

	public void setPopulation(Individual[] inds, String type) {
		this.population = new Population(inds, type);
	}
	
	public Game() {

	}
	
	public Game(Population population) {
		isSimulation = true;
		this.population = population;
	}

	abstract void paintFixed(Graphics2D g);

	abstract void paintUpdate(Graphics2D g);

	abstract void updateLogic();

	public void update() throws IOException {
		if(isSimulation && population.isGenerationOver()) {
			return;
		}
		if (population.isGenerationOver()) {
			for (int i = 0; i < population.getIndividuals().length; i++) {
				population.getIndividuals()[i].reset();
			}
			save();
			population.generateNewGeneration();
		}
		updateLogic();
	}

	public Population getPopulation() {
		return population;
	}

	private void save() throws IOException {
		//System.out.println("saving...");
		population.revive();
		String gen = "" + population.getGeneration();
		for (int i = gen.length(); i < 4; i++) {
			gen = "0" + gen;
		}
		File directory = new File(SAVE_FILEPATH + population.getType());
		if (!directory.exists()){
	        directory.mkdirs();
	    }
		ObjectOutputStream objectOut = new ObjectOutputStream(
				new FileOutputStream(new File(SAVE_FILEPATH + population.getType() + "\\" + population.getType() + "_" + gen + ".sim")));
		objectOut.writeObject(population);
		objectOut.close();
	}

}
