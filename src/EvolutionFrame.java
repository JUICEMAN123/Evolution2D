import javax.swing.JFrame;
import javax.swing.JPanel;

public class EvolutionFrame extends JFrame {

	JPanel simulation;
	
	public EvolutionFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setResizable(false);
		
		simulation = new SimulationPanel();
		add(simulation);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		new EvolutionFrame();
		
	}
	
}