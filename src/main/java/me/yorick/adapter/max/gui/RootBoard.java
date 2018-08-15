package me.yorick.adapter.max.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
        this.setSize(600, 600);
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
		System.out.println("update"+id);
		boxes.computeIfAbsent(id, k -> {CompositionBox box = new CompositionBox(k); addBox(box);return box;}).update(info);
	}

	public void remvoeChance(int id) {
		CompositionBox box = boxes.remove(id);
		removeBox(box);
	}
	
	
}
