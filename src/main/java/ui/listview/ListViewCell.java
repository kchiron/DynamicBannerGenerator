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
	
	private JLabel lblIcon;
	private JLabel lblTitle;
	private JLabel lblSubTitle;
	
	private Color titleColor;
	private Color subTitleColor;
	private Color textSelected;

	public ListViewCell() {
		panel = new JPanel(new MigLayout("ins 2, gap 0px 0px", "[30px][grow]", "[15px][15px]"));

		String defaultFontFamily = UIManager.getDefaults().getFont("Panel.font").getFamily();
		
		{//Cell icon 30x30
			this.lblIcon = new JLabel();
			this.lblIcon.setPreferredSize(new Dimension(30, 30));
			this.lblIcon.setMinimumSize(new Dimension(30, 30));

			panel.add(lblIcon, "cell 0 0 1 2, gap 2px 4px");
		}
		
		{//Cell title
			this.lblTitle = new JLabel();
			this.lblTitle.setFont(new Font(defaultFontFamily, Font.BOLD, 12));
			
			titleColor = Color.BLACK;
			panel.add(lblTitle, "cell 1 0, growx, aligny bottom");
		}
		
		{//Cell sub-title
			this.lblSubTitle = new JLabel();
			this.lblSubTitle.setFont(new Font(defaultFontFamily, Font.BOLD, 9));

			subTitleColor = new Color(133, 133, 133);
			panel.add(lblSubTitle, "cell 1 1, growx, aligny top");
		}

		panel.setBorder(BorderFactory.createEmptyBorder());
		
		textSelected = Color.WHITE;
	}

	private void updateData(MediaElement element, boolean isSelected, JTable table) {
		try {
			lblTitle.setText(element.getTitle());
			lblSubTitle.setText(element.getSubTitle());

			if (isSelected) lblIcon.setIcon(element.getSelectedIcon());
			else lblIcon.setIcon(element.getIcon());
		} catch (NullPointerException e) {}

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			lblTitle.setForeground(textSelected);
			lblSubTitle.setForeground(textSelected);
		}
		else {
			panel.setBackground(Color.WHITE);
			lblTitle.setForeground(titleColor);
			lblSubTitle.setForeground(subTitleColor);
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
