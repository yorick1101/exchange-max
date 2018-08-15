package me.yorick.adapter.max.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.stragtegy.CompositionInfo.TradeInfo;

public class CompositionBox extends JPanel {

	private final int index;
	private JTable table;
	private DefaultTableModel dataModel;
	private JLabel rateLabel;
	private static String[] columnNames = {"Side", "Product", "Price", "Quantity", "Send"};
	
	public CompositionBox(int index) {
		this.index = index;
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3 , true)));
		this.setSize(500, 100);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		dataModel = new DefaultTableModel(columnNames, 3);
		for(int i =0;i<dataModel.getRowCount();i++) {
			dataModel.setValueAt("Send", i, 4);
		}
		
		table = new JTable(dataModel) {
			public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
		};
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		Enumeration<TableColumn> coulmns = table.getColumnModel().getColumns();
		while(coulmns.hasMoreElements()) {
			coulmns.nextElement().setCellRenderer(centerRenderer);
		}
		
		ButtonColumn buttonColumn = new ButtonColumn(table, 4);
		this.add(table.getTableHeader());
		this.add(table);
		
		JPanel comp = new JPanel(new GridLayout(1, 1), false);
		rateLabel = new JLabel("", JLabel.CENTER);
        comp.add(rateLabel);
        this.add(rateLabel);
	}
	
	public void update(final CompositionInfo info) {
		int row = 0;
		for(TradeInfo level : info.getLevels()) {
			dataModel.setValueAt(level.getSide(), row, 0);
			dataModel.setValueAt(level.getProduct(), row, 1);
			dataModel.setValueAt(level.getPrice(), row, 2);
			dataModel.setValueAt(level.getQty(), row, 3);
			row++;
		}
		rateLabel.setText(Double.toString(info.geRate()));
	}
	
	
	
}
