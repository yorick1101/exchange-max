package me.yorick.adapter.max.gui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import me.yorick.adapter.max.stragtegy.CompositionInfo;

public class RootBoard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<Integer, CompositionBox> boxes = new HashMap<>();
	private JPanel panel;
	public RootBoard() {
		super("Chances");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(605, 650);
        this.setLayout(new BorderLayout());
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JScrollPane listScroller = new JScrollPane(panel);
		this.add(listScroller, BorderLayout.CENTER);
	}
	
	private void addBox(CompositionBox box) {
		panel.add(box);
	}
	
	private void removeBox(CompositionBox box) {
		panel.remove(box);
	}

	public void updateChance(int id, CompositionInfo info) {
		CompositionBox box = boxes.get(id);
		if(box==null) {
			box = new CompositionBox(info); 
			addBox(box);
			boxes.put(id, box);
		}else
			box.update(info);
	
	}

	public void remvoeChance(int id) {
		CompositionBox box = boxes.remove(id);
		removeBox(box);
	}
	
	
}
