package dbg.ui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

import dbg.ui.LocalizedText;

/**
 * Class regrouping swing UI utilities 
 * @author gcornut
 */
public class UiUtils {
	/**
	 * Initializes the UiManager with some basic settings
	 */
	public static void initUIManager() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		UIManager.put("FileChooser.saveButtonText", LocalizedText.save);
		UIManager.put("FileChooser.openButtonText", LocalizedText.open);
		UIManager.put("FileChooser.cancelButtonText", LocalizedText.cancel);
		UIManager.put("FileChooser.updateButtonText", LocalizedText.reload);
		UIManager.put("FileChooser.helpButtonText", LocalizedText.help);
		UIManager.put("FileChooser.saveButtonToolTipText", LocalizedText.save_the_file);
		UIManager.put("FileChooser.filesOfTypeLabelText", LocalizedText.file_format);

		UIManager.put("OptionPane.cancelButtonText", LocalizedText.cancel);
		UIManager.put("OptionPane.noButtonText", LocalizedText.no);
		UIManager.put("OptionPane.okButtonText", LocalizedText.ok);
		UIManager.put("OptionPane.yesButtonText", LocalizedText.yes);
	}

	/**
	 * Forces relayout of a component and its child
	 * @param c a Component
	 */
	public static void layoutComponent(Component c) {
		synchronized (c.getTreeLock()) {
			c.doLayout();
			if(c instanceof Container) {
				for(Component child: ((Container)c).getComponents())
					layoutComponent(child);
			}
		}
	}

	/*
	 *	Try to make the component transparent.
	 *  For components that use renderers, like JTable, you will also need to
	 *  change the renderer to be transparent. An easy way to do this it to
	 *  set the background of the table to a Color using an alpha value of 0.
	 */
	public static void makeComponentTransparent(JComponent component) {
		component.setOpaque(false);

		if(component instanceof JScrollPane) {
			JScrollPane scrollPane = (JScrollPane) component;
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque(false);
			Component c = viewport.getView();

			if(c instanceof JComponent)
				((JComponent)c).setOpaque(false);
		}
	}

	/**
	 * This method scales and shift components that are contained in a parent element having an absolute layout
	 * @param parent the parent container component having child component to be scaled and shifted
	 * @param scale the scale factor applied on the parent's child(s)
	 * @param shiftX the shift value on X axis
	 * @param shiftY the shift value on Y axis
	 */
	public static void scaleAndShiftComponents(Container parent, double scale, int shiftX, int shiftY) {
		//Apply scale and shift on all component in the container
		for(Component child: parent.getComponents()) {
			//Scaling and shifting the component position and size
			Rectangle r = child.getBounds();
			r.setBounds(
					(int)(r.getX()*scale+shiftX),	//X scaled and shifted
					(int)(r.getY()*scale+shiftY),	//Y scaled and shifted
					(int)(r.getWidth()*scale),		//Width scaled
					(int)(r.getHeight()*scale)		//Height scaled
					);
			child.setBounds(r);

			// Scaling text size
			Font f = child.getFont();
			child.setFont(new Font(f.getFamily(), f.getStyle(), (int)(f.getSize()*scale)));
		}
	}
}
