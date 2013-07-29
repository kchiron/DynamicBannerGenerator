package ui.custom.listview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;

public class ListViewCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel icon;
	private JLabel title;
	private JLabel subTitle;

	public ListViewCell() {
		panel = new JPanel(new MigLayout("ins 2, gap 0px 0px", "[30px][grow]", "[15px][15px]"));

		this.icon = new JLabel();
		icon.setPreferredSize(new Dimension(30, 30));
		this.title = new JLabel();
		this.subTitle = new JLabel();

		this.icon.setMinimumSize(new Dimension(30, 30));
		//this.icon.setBorder(BorderFactory.createLineBorder(Color.black));

		this.title.setFont(new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 12));

		this.subTitle.setForeground(Color.gray);
		this.subTitle.setFont(new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 9));

		panel.add(this.icon, "cell 0 0 1 2, gap 2px 4px");
		panel.add(this.title, "cell 1 0, growx, aligny bottom");
		panel.add(this.subTitle, "cell 1 1, growx, aligny top");
		//panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setBorder(BorderFactory.createEmptyBorder());
	}

	private void updateData(Sequence sequence, boolean isSelected, JTable table) {
		try {

			title.setText(sequence.getTitle());
			subTitle.setText(sequence.getSubTitle());
		} catch (Exception e) {}

		try {
			final String pathIcon = "/icon/"+sequence.getType()+(isSelected?"_on":"")+".png";
			icon.setIcon(new ImageIcon(ListViewCell.class.getResource(pathIcon)));
		} catch (NullPointerException e) {
			icon.setIcon(null);
		}

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			title.setForeground(Color.WHITE);
			subTitle.setForeground(Color.WHITE);
		}
		else {
			panel.setBackground(Color.WHITE);
			title.setForeground(Color.BLACK);
			subTitle.setForeground(Color.LIGHT_GRAY);
		}
	}

	public Component getTableCellRendererComponent(JTable paramJTable,
			Object paramObject, boolean paramBoolean1, boolean paramBoolean2,
			int paramInt1, int paramInt2) {

		updateData((Sequence)paramObject, paramBoolean1, paramJTable);
		return panel;
	}

	public Object getCellEditorValue() {
		return null;
	}

	public Component getTableCellEditorComponent(JTable paramJTable,
			Object paramObject, boolean paramBoolean, int paramInt1,
			int paramInt2) {
		updateData((Sequence)paramObject, true, paramJTable);
		return panel;
	}

}
