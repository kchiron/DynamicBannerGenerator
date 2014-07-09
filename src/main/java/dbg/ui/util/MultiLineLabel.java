package dbg.ui.util;

import javax.swing.*;
import java.awt.*;

/**
 * JLabel inheriting class with multiple line text and custom text alignment
 *
 * @author gcornut
 */
public class MultiLineLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public enum TextAlign {LEFT, JUSTIFY, CENTER, RIGTH}

	private final Font font;
	private TextAlign align;

	public MultiLineLabel(String text, TextAlign align) {
		this(text, align, null);
	}

	public MultiLineLabel(String text, TextAlign align, Font font) {
		super(wrap(align, text, font));
		this.align = align;
		this.font = font;
		setVerticalAlignment(TOP);
		setVerticalTextPosition(TOP);

		if (font != null) {
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			boolean exists = false;
			for (Font font1 : genv.getAllFonts()) {
				if (font1.equals(font)) {
					exists = true;
					break;
				}
			}
			if(!exists)
				genv.registerFont(font);
		}
	}

	@Override
	public void setText(String text) {
		if (align == null) super.setText(text);
		else super.setText(wrap(align, text, font));
	}

	private static String wrap(TextAlign align, String text, Font font) {
		String style = "";
		if (font != null) {
			style = "<head><style type=\"text/css\">\n" +
					"body { padding: 0; margin: 0; font-family: " + font.getFontName() + "; } </head><body>";
		}

		return "<html>" + style + "<p align=" + align.toString().toLowerCase() + ">" + text;
	}
}