import java.io.Serializable;
import java.util.Arrays;

public class Node implements Serializable{

	private double weight;
	private double[] outMultipliers;
	private double value;
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double[] getOutMultipliers() {
		return outMultipliers;
	}

	public void setOutMultipliers(double[] outMultipliers) {
		this.outMultipliers = outMultipliers;
	}

	public Node(double weight, double[] outMultipliers) {
		this.weight = weight;
		this.outMultipliers = outMultipliers;
	}
	
	public Node(int outs) {		
		this.weight = Math.random() - 0.5;
		this.outMultipliers = new double[outs];
		for (int i = 0; i < outMultipliers.length; i++) {
			outMultipliers[i] = Math.random() - 0.5;
		}
	}
	
	public Node duplicate() {
		return new Node(weight, Arrays.copyOf(outMultipliers, outMultipliers.length));
	}
	
}
