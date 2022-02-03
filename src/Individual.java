import java.io.Serializable;

public abstract class Individual implements Serializable{

	private double fitness = Double.NEGATIVE_INFINITY;
	private EvolutionAI ai;
	private boolean alive = true;
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void setAI(EvolutionAI ai) {
		this.ai = ai;
	}
	
	public EvolutionAI getAI() {
		return ai;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public Individual(EvolutionAI ai) {
		this.ai = ai;
	}
	
	public abstract double calcFitness();
	public abstract void reset();
	public abstract boolean dyingCondition();
	
	public Node[] getOutputsOfInputs(double[] inputs) {
		ai.setInputs(inputs);
		ai.calculate();
		return ai.getOutputs();
	}
	
	public void live() {
		if (dyingCondition()) {
			alive = false;
			fitness = Math.max(fitness, calcFitness());
		}
	}
	
}
