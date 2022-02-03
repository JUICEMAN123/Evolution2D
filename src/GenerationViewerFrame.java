import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class GenerationViewerFrame extends JFrame {

	GenerationViewerPanel viewer;
	JComboBox gameSelecter;
	JComboBox genList;
	JButton startButton;
	JSlider speedSlider;

	public GenerationViewerFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 900);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		gameSelecter = new JComboBox<String>(new File(Game.SAVE_FILEPATH).list());
		topPanel.add(gameSelecter, BorderLayout.LINE_START);
		
		genList = new JComboBox<String>(new File(Game.SAVE_FILEPATH + gameSelecter.getSelectedItem()).list());
		topPanel.add(genList, BorderLayout.CENTER);
		gameSelecter.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {	
				genList.removeAllItems();
				for(String s : new File(Game.SAVE_FILEPATH + gameSelecter.getSelectedItem()).list()) {
					genList.addItem(s);
				}
			}
		});
		
		startButton = new JButton("START");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(viewer.getGo()) {
						viewer.setGo(false);
						Thread.sleep(200);
					}	
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(Game.SAVE_FILEPATH + (String) gameSelecter.getSelectedItem() + "\\" + (String) genList.getSelectedItem()));
					viewer.setGo(true);
					viewer.startSimulation((Population) ois.readObject());
				} catch (IOException | ClassNotFoundException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		topPanel.add(startButton, BorderLayout.LINE_END);
		add(topPanel, BorderLayout.PAGE_START);

		viewer = new GenerationViewerPanel(this);
		add(viewer, BorderLayout.CENTER);

		speedSlider = new JSlider(1, 1000, 60);
		speedSlider.setBackground(Color.GREEN);
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
		labels.put(1, new JLabel("1"));
		for (int i = 100; i <= 1000; i += 100) {
			labels.put(i, new JLabel("" + i));
		}
		speedSlider.setLabelTable(labels);
		speedSlider.setPaintLabels(true);
		add(speedSlider, BorderLayout.PAGE_END);

		setVisible(true);
	}

	public static void main(String[] args) {

		new GenerationViewerFrame();

	}

}