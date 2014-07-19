package dbg.ui.settings.listview;

import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.ui.LocalizedText;
import dbg.ui.settings.listview.contextmenu.ListViewListener;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ListViewCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private JPanel panel;

	private JLabel lblIcon;
	private JLabel lblTitle;
	private JLabel lblSubTitle;

	private Color titleColor;
	private Color lightGrayColor;
	private Color textSelected;

	private ListViewListener mouseListener;
	private ListView parentTable;

	private MediaElement element;

	private JLabel lblDuration;

	public ListViewCell(ListView parentTable) {
		this.parentTable = parentTable;
		panel = new JPanel(new BorderLayout());

		String defaultFontFamily = UIManager.getDefaults().getFont("Panel.font").getFamily();

		{//Cell icon 30x30
			this.lblIcon = new JLabel();
			this.lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			panel.add(lblIcon, BorderLayout.WEST);
		}

		{//Cell title and sub-title
			JPanel center = new JPanel(new GridLayout(2, 1));
			center.setOpaque(false);
			center.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

			this.lblTitle = new JLabel("T");
			lblTitle.setVerticalAlignment(SwingConstants.BOTTOM);
			this.lblTitle.setFont(new Font(defaultFontFamily, Font.BOLD, 12));
			center.add(lblTitle);

			this.lblSubTitle = new JLabel("St");
			lblSubTitle.setVerticalAlignment(SwingConstants.TOP);
			this.lblSubTitle.setFont(new Font(defaultFontFamily, Font.BOLD, 9));
			center.add(lblSubTitle);

			panel.add(center, BorderLayout.CENTER);
		}

		{//Cell duration time
			this.lblDuration = new JLabel();
			this.lblDuration.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
			this.lblDuration.setFont(new Font(defaultFontFamily, Font.BOLD, 9));
			panel.add(lblDuration, BorderLayout.EAST);
		}

		panel.setBorder(BorderFactory.createEmptyBorder());

		titleColor = Color.BLACK;
		lightGrayColor = new Color(133, 133, 133);
		textSelected = Color.WHITE;

		mouseListener = new ListViewListener(this);
		panel.addMouseListener(mouseListener);
	}

	private void updateData(MediaElement element, boolean isSelected, JTable table) {
		if (element != null) {
			this.element = element;
			mouseListener.setElement(element);
		}
		try {
			lblTitle.setText(element.getTitle());
			lblSubTitle.setText(element.getSubTitle());
			lblDuration.setText(formatDurationTime(element.getDuration()));

			lblIcon.setIcon(getElementIcon(element, isSelected));
		} catch (NullPointerException e) {
		}

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			lblTitle.setForeground(textSelected);
			lblSubTitle.setForeground(textSelected);
			lblDuration.setForeground(textSelected);
		} else {

			panel.setBackground(Color.WHITE);
			panel.setToolTipText("");
			lblTitle.setForeground(titleColor);
			lblSubTitle.setForeground(lightGrayColor);
			lblDuration.setForeground(lightGrayColor);

			if(element instanceof  ImportedMediaElement) {
				ImportedMediaElement importedMediaElement = (ImportedMediaElement) element;
				if(!importedMediaElement.getFile().exists()) {
					panel.setToolTipText(LocalizedText.get("error.message.file_not_found"));
					panel.setBackground(new Color(255, 166, 166));
				}
			}
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
		updateData((MediaElement) object, true, parentJTable);
		return panel;
	}

	public void remove() {
		parentTable.removeSelectedRow();
	}

	public void edit() {
		parentTable.modifyRow((ImportedMediaElement) element);
	}

	private String formatDurationTime(int seconds) {
		String out = "";

		if (seconds > 3600)
			out += String.format("%02d", seconds / 3600) + ":";

		out += String.format("%02d", (seconds % 3600) / 60) + ":";
		out += String.format("%02d", seconds % 3600 % 60);

		return out;
	}

	private ImageIcon getElementIcon(MediaElement element, boolean selected) {
		return new ImageIcon(getClass().getResource(element.getClass().getSimpleName() + (selected ? "_on" : "") + ".png"));
	}
}
