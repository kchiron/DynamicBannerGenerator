package dbg.ui.form;

import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {

	private static final long serialVersionUID = 1L;

	public IconButton(URL icon, String fallBack) {
		try {
			setIcon(new ImageIcon(icon));
		} catch (Exception e) {
			setText(fallBack);
		}
	}
	
	public IconButton(URL icon, String fallBack, Font font) {
		this(icon, fallBack);
		setFont(font);
	}
}
