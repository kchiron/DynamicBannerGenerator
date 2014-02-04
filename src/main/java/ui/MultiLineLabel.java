package ui;

import javax.swing.JLabel;

/**
 * JLabel inheriting class with multiple line text and custom text alignment
 * @author gcornut
 */
public class MultiLineLabel extends JLabel {
	
	private static final long serialVersionUID = 1L; 
	public enum TextAlign {LEFT, JUSTIFY, CENTER, RIGTH};
	private TextAlign align;
	
	public MultiLineLabel(String text, TextAlign align) {
		super(wrap(align, text));
		this.align = align;
                setVerticalAlignment(TOP);
                setVerticalTextPosition(TOP);
	}
	
	@Override
	public void setText(String text) {
		if(align == null) super.setText(text);
		else super.setText(wrap(align, text));
	}
	
	/**
	 * Gets the text of the label without the html
	 * @return the text only without the html
	 */
	public String getTextOnly() {
		return unWrap(super.getText());
	}

	private static String wrap(TextAlign align, String text) {
		return "<html><p align=" + align.toString().toLowerCase() + ">" + text;
	}

	private static String unWrap(String text) {
		return text.replaceAll("<html><p align=.*>", "");
	}
}