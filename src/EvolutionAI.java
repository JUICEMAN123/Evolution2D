import java.io.Serializable;

public class EvolutionAI implements Serializable{

	private Node[] inputs;
	private Node[][] hidden;
	private Node[] outputs;
	private int[] structure = new int[4];
	private int size = 0;
	
	public void setInputs(double[] d) {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].setValue(d[i]);
		}
	}
	
	public Node[] getOutputs() {
		return outputs;
	}
	
	public int[] getStructure() {
		return structure;
	}
	
	private void calcSize() {
		size = inputs.length * hidden.length + hidden.length * hidden[0].length + hidden[0].length * outputs.length;
	}
	
	public EvolutionAI(Node[] ins, Node[][] hid, Node[] outs) {
		inputs = ins;
		hidden = hid;
		outputs = outs;
		
		structure[0] = ins.length;
		structure[1] = hidden.length;
		structure[2] = hidden[0].length;
		structure[3] = outs.length;
		
		calcSize();
	}
	
	public EvolutionAI(int ins, int hiddLay, int hiddSize, int outs) {
		structure[0] = ins;
		structure[1] = hiddLay;
		structure[2] = hiddSize;
		structure[3] = outs;
		
		inputs = new Node[ins];
		hidden = new Node[hiddLay][hiddSize];
		outputs = new Node[outs];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = new Node(hiddSize);
		}
		for (int i = 0; i < hidden.length - 1; i++) {
			for (int j = 0; j < hidden[i].length; j++) {
				hidden[i][j] = new Node(hiddSize);
			}
		}
		for (int i = 0; i < hidden[hidden.length - 1].length; i++) {
			hidden[hidden.length - 1][i] = new Node(outs);
		}
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = new Node(0);
		}
		
		calcSize();
	}
	
	public void calculate() {
		for (int i = 0; i < hidden[0].length; i++) {
			double d = 0d;
			for (int j = 0; j < inputs.length; j++) {
				d += inputs[j].getValue() * inputs[j].getOutMultipliers()[i];
			}
			hidden[0][i].setValue(Math.tanh(d + hidden[0][i].getWeight()));
		}
		for (int l = 1; l < hidden.length; l++) {
			for (int i = 0; i < hidden[l].length; i++) {
				double d = 0d;
				for (int j = 0; j < hidden[l-1].length; j++) {
					d += hidden[l-1][j].getValue() * hidden[l-1][j].getOutMultipliers()[i];
				}
				hidden[l][i].setValue(Math.tanh(d + hidden[0][i].getWeight()));
			}
		}
		for (int i = 0; i < outputs.length; i++) {
			double d = 0d;
			for (int j = 0; j < hidden[hidden.length - 1].length; j++) {
				d += hidden[hidden.length - 1][j].getValue() * hidden[hidden.length - 1][j].getOutMultipliers()[i];
			}
			outputs[i].setValue(Math.tanh(d + outputs[i].getWeight()));
		}
	}
	
	static EvolutionAI generateOffspring(EvolutionAI father, EvolutionAI mother) {
		EvolutionAI offspring = father.duplicate();
		for (int i = 0; i < offspring.hidden.length; i++) {
			for (int j = 0; j < offspring.hidden[0].length; j++) {
				if(Math.random() < 0.5) {
					offspring.hidden[i][j] = mother.hidden[i][j].duplicate();
				}
				if(Math.random() < (1.0 / offspring.size)) {
					offspring.hidden[i][j].setWeight((offspring.hidden[i][j].getWeight() + (Math.random() - 0.5)));
				}
				if(Math.random() < (1.0 / offspring.size)) {
					for (int k = 0; k < offspring.hidden[i][j].getOutMultipliers().length; k++) {
						offspring.hidden[i][j].getOutMultipliers()[k] += (Math.random() - 0.5);
					}
				}
				if(Math.random() < (0.1 / offspring.size)) {
					offspring.hidden[i][j].setWeight(0);
				}
				if(Math.random() < (0.1 / offspring.size)) {
					for (int k = 0; k < offspring.hidden[i][j].getOutMultipliers().length; k++) {
						offspring.hidden[i][j].getOutMultipliers()[k] = 0;
					}
				}
			}
		}		
		return offspring;
	}
	
	public EvolutionAI duplicate() {
		Node[] ins = new Node[inputs.length];
		for (int i = 0; i < ins.length; i++) {
			ins[i] = inputs[i].duplicate();
		}
		Node[][] hid = new Node[hidden.length][hidden[0].length];
		for (int i = 0; i < hid.length; i++) {
			for (int j = 0; j < hid[0].length; j++) {
				hid[i][j] = hidden[i][j].duplicate();
			}
		}
		Node[] outs = new Node[outputs.length];
		for (int i = 0; i < outs.length; i++) {
			outs[i] = outputs[i].duplicate();
		}
		return new EvolutionAI(ins, hid, outs);
	}
	
}