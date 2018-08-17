package me.yorick.adapter.max.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.stragtegy.CompositionInfo.TradeInfo;

public class CompositionBox extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel infoPanel;
	private JLabel rateLabel;
	private static String[] columnNames = {"Side", "Product", "Price", "Quantity"};
	private JLabel[][] rows= new JLabel[3][];
	
	public CompositionBox(CompositionInfo info) {
		this.setMaximumSize(new Dimension(600, 150));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3 , true)));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		createInfo(info);
		this.add(infoPanel);

		JPanel comp = new JPanel(new GridLayout(1, 1), false);
		comp.setSize(new Dimension(600, 30));
		rateLabel = new JLabel(Double.toString(info.geRate()), JLabel.CENTER);
        comp.add(rateLabel);
        this.add(rateLabel);
	}
	
	private void createInfo(CompositionInfo info) {
		infoPanel = new JPanel();
		GridBagLayout infoLayout = new GridBagLayout();
		infoPanel.setLayout(infoLayout);
		
		//header
		for(int i =0;i<columnNames.length;i++) {
			addInfoHeader(i);			
		}
		for(int i =0;i<info.getLevels().length;i++) {
			addInfoRow(i, info.getLevels()[i]);
		}
		
	}
	
	private void addInfoRow(final int index, final TradeInfo level) {
		JLabel[] row = new JLabel[4];
		rows[index]= row;
		addInfoCell(row , index+1, 0, level.getSide().name());
		addInfoCell(row , index+1, 1, level.getProduct());
		addInfoCell(row , index+1, 2, Double.toString(level.getPrice()));
		addInfoCell(row , index+1, 3, Double.toString(level.getQty()));
	}
	
	private void updateInfoRow(final int index, final TradeInfo level) {
		JLabel[] row = rows[index];
		row[2].setText(Double.toString(level.getPrice()));
		row[3].setText(Double.toString(level.getQty()));
	}
	
	private void addInfoCell(JLabel[] row, int rowIndex, int columnIndex, String value) {
		row[columnIndex] = new JLabel(value, JLabel.CENTER);
		GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = columnIndex;
        c1.gridy = rowIndex;
        c1.weightx=1;
        c1.fill = GridBagConstraints.NONE;
        c1.anchor = GridBagConstraints.CENTER;
        infoPanel.add(row[columnIndex], c1);
	}
	
	
	private void addInfoHeader(int index) {
		JLabel h1 = new JLabel(columnNames[index]);
		h1.setFont(h1.getFont().deriveFont(h1.getFont().getStyle() | Font.BOLD));
		GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = index;
        c1.gridy = 0;
        c1.fill = GridBagConstraints.NONE;
        c1.anchor = GridBagConstraints.CENTER;
        infoPanel.add(h1, c1);
	}
	
	public void update(final CompositionInfo info) {
		int row = 0;
		for(TradeInfo level : info.getLevels()) {
			updateInfoRow(row, level);
			row++;
		}
		rateLabel.setText(Double.toString(info.geRate()));
	}
	
	
	
}
