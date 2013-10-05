package ui.panel;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class TabContentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TabContentPanel(LayoutManager layout, String name) {
		super(layout);
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setName(name);
	}
	
	@Override
	public Component add(Component comp) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		return super.add(comp);
	}
	
	@Override
	public void add(Component comp, Object constraints) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		super.add(comp, constraints);
	}
	
	@Override
	public void add(Component comp, Object constraints, int index) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		super.add(comp, constraints, index);
	}
}
