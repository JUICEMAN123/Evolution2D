import java.io.Serializable;	

public class Population implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	private Individual[] individuals;

	private int generation = 1;
	
	public Individual[] getIndividuals() {
		return individuals;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public Population(Individual[] individuals, String type) {
		this.individuals = individuals;
		this.type = type;
	}
	
	public boolean isGenerationOver() {
		for (Individual individual : individuals) {
			if(individual.isAlive()) {
				return false;
			}
		}
		return true;
	}
	
	public void revive() {
		for (int i = 0; i < individuals.length; i++) {
			individuals[i].setAlive(true);
		}
	}
	
	public void generateNewGeneration() {
		//System.out.println("generating new population... ");
		Individual best, secondBest;
		if(individuals[0].getFitness() > individuals[1].getFitness()) {
			best = individuals[0];
			secondBest = individuals[1];
		}
		else {
			best = individuals[1];
			secondBest = individuals[0];
		}
		
		for (int i = 0; i < individuals.length; i++) {
			if(individuals[i].getFitness() > best.getFitness()) {
				secondBest = best;
				best = individuals[i];
			}
			else if(individuals[i].getFitness() > secondBest.getFitness()) {
				secondBest = individuals[i];
			}
		}

		individuals[0].setAI(best.getAI()); // keep best
		individuals[1].setAI(secondBest.getAI()); // keep second best
		for (int i = 2; i < individuals.length / 10; i++) { 
			int[] stc = best.getAI().getStructure();
			individuals[i].setAI(new EvolutionAI(stc[0], stc[1], stc[2], stc[3])); //gene flow (migration inward)
		}
		for (int i = individuals.length / 10 + 1; i < individuals.length; i++) {
			individuals[i].setAI(EvolutionAI.generateOffspring(best.getAI(), secondBest.getAI()));
		}
		System.out.println(generation + " " + best.getFitness());
		generation++;
	}
	
}