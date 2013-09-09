package ui.custom;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/*
 * 
 */
public class TabContentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TabContentPanel(LayoutManager layout, String name) {
		super(layout);
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setName(name);
	}
}
