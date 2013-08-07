package ui.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import media.element.MediaElement;
import net.miginfocom.swing.MigLayout;

public class ListViewCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel icon;
	private JLabel title;
	private JLabel subTitle;
	
	private Color titleColor;
	private Color subTitleColor;
	
	private Color textSelected;

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

		panel.add(icon, "cell 0 0 1 2, gap 2px 4px");
		panel.add(title, "cell 1 0, growx, aligny bottom");
		panel.add(subTitle, "cell 1 1, growx, aligny top");
		//panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setBorder(BorderFactory.createEmptyBorder());
		
		titleColor = Color.BLACK;
		subTitleColor = new Color(133, 133, 133);
		
		textSelected = Color.WHITE;
	}

	private void updateData(MediaElement element, boolean isSelected, JTable table) {
		try {
			title.setText(element.getTitle());
			subTitle.setText(element.getSubTitle());

			if (isSelected)
				icon.setIcon(element.getSelectedIcon());
			else 
				icon.setIcon(element.getIcon());
		} catch (NullPointerException e) {}

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			title.setForeground(textSelected);
			subTitle.setForeground(textSelected);
		}
		else {
			panel.setBackground(Color.WHITE);
			title.setForeground(titleColor);
			subTitle.setForeground(subTitleColor);
		}
	}

	public Component getTableCellRendererComponent(JTable parentJTable,
			Object object, boolean isSelected, boolean paramBoolean2,
			int paramInt1, int paramInt2) {

		updateData((MediaElement) object, isSelected, parentJTable);
		return panel;
	}

	public Object getCellEditorValue() {
		return null;
	}

	public Component getTableCellEditorComponent(JTable parentJTable,
			Object object, boolean paramBoolean, int paramInt1,
			int paramInt2) {
		updateData((MediaElement)object, true, parentJTable);
		return panel;
	}

}
