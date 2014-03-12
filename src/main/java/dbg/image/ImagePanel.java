package dbg.image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import dbg.ui.util.UiUtils;

/*
 *  Support custom painting on a panel in the form of
 *
 *  a) images - that can be scaled, tiled or painted at original size
 *  b) non solid painting - that can be done by using a Paint object
 *
 *    Any component added directly to this panel will be made
 *  non-opaque so that the custom painting can show through.
 *  
 *  Source:
 *  	-http://tips4java.wordpress.com/2008/10/13/screen-image/
 *  	-http://tips4java.wordpress.com/2008/10/12/background-panel/
 */
public class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public enum Scaling {SCALED, TILED, ACTUAL};

	private Paint painter;
	private Image image;
	private Scaling style = Scaling.SCALED;
	private float alignmentX;
	private float alignmentY;
	private boolean isTransparentAdd = true;

	/*
	 *  Set image as the background with the SCALED style
	 */
	public ImagePanel(Image image) {
		this(image, Scaling.SCALED, 0.5f, 0.5f);
	}

	/*
	 *  Set image as the background with the specified style
	 */
	public ImagePanel(Image image, Scaling style) {
		this(image, style, 0.5f, 0.5f);
	}

	/*
	 *  Set image as the background with the specified style and alignment
	 */
	public ImagePanel(Image image, Scaling style, float alignmentX, float alignmentY) {
		setImage(image);
		setStyle(style);
		setImageAlignmentX(alignmentX);
		setImageAlignmentY(alignmentY);
		setLayout(new BorderLayout());
	}

	/*
	 *  Use the Paint interface to paint a background
	 */
	public ImagePanel(Paint painter) {
		this(null, null, 0.5f, 0.5f);
		setPaint(painter);
		setLayout(new BorderLayout());
	}

	/*
	 *	Set the image used as the background
	 */
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

	/*
	 *	Set the style used to paint the background image
	 */
	public void setStyle(Scaling style) {
		this.style = style;
		repaint();
	}

	/*
	 *	Set the Paint object used to paint the background
	 */
	public void setPaint(Paint painter) {
		this.painter = painter;
		repaint();
	}

	/*
	 *  Specify the horizontal alignment of the image when using ACTUAL style
	 */
	public void setImageAlignmentX(float alignmentX) {
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
		repaint();
	}

	/*
	 *  Specify the horizontal alignment of the image when using ACTUAL style
	 */
	public void setImageAlignmentY(float alignmentY) {
		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;
		repaint();
	}

	/*
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component) {
		add(component, null);
	}

	/*
	 *  Override to provide a preferred size equal to the image size
	 */
	@Override
	public Dimension getPreferredSize() {
		if (image == null)
			return super.getPreferredSize();
		else
			return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	/*
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component, Object constraints) {
		if (isTransparentAdd)
			UiUtils.makeComponentTransparent(component);

		super.add(component, constraints);
	}

	/*
	 *  Controls whether components added to this panel should automatically
	 *  be made transparent. That is, setOpaque(false) will be invoked.
	 *  The default is set to true.
	 */
	public void setTransparentAdd(boolean isTransparentAdd) {
		this.isTransparentAdd = isTransparentAdd;
	}

	/*
	 *  Add custom painting
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//  Invoke the painter for the background
		if (painter != null) {
			Dimension d = getSize();
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(painter);
			g2.fill( new Rectangle(0, 0, d.width, d.height) );
		}

		//  Draw the image
		if (image == null ) return;

		switch (style) {
			case SCALED :	drawScaled(g);
				break;
			case TILED  :	drawTiled(g);
				break;
			case ACTUAL :	drawActual(g);
				break;
			default:	drawScaled(g);
		}
	}

	/*
	 *  Custom painting code for drawing a SCALED image as the background
	 */
	private void drawScaled(Graphics g) {
		Dimension d = getSize();
		g.drawImage(image, 0, 0, d.width, d.height, null);
	}

	/*
	 *  Custom painting code for drawing TILED images as the background
	 */
	private void drawTiled(Graphics g) {
		Dimension d = getSize();
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		for (int x = 0; x < d.width; x += width) {
			for (int y = 0; y < d.height; y += height) {
				g.drawImage(image, x, y, null, null);
			}
		}
	}

	/*
	 *  Custom painting code for drawing the ACTUAL image as the background.
	 *  The image is positioned in the panel based on the horizontal and
	 *  vertical alignments specified.
	 */
	private void drawActual(Graphics g) {
		Dimension d = getSize();
		Insets insets = getInsets();
		int width = d.width - insets.left - insets.right;
		int height = d.height - insets.top - insets.left;
		float x = (width - image.getWidth(null)) * alignmentX;
		float y = (height - image.getHeight(null)) * alignmentY;
		g.drawImage(image, (int)x + insets.left, (int)y + insets.top, this);
	}


	/**
	 * Export the panel graphics as a BufferedImage that can be then written in a image file.
	 */
	public BufferedImage asImage() {
		Dimension d = getSize();

		if(d.width == 0 || d.height == 0) {
			d = this.getPreferredSize();
			setSize(d);
		}
		UiUtils.layoutComponent(this);

		Rectangle region = new Rectangle(0, 0, d.width, d.height);

		BufferedImage export = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = export.createGraphics();

		if(!isOpaque()) {
			g2.setColor(getBackground());
			g2.fillRect(region.x, region.y, region.width, region.height);
		}

		g2.translate(-region.x, -region.y);
		paint(g2);
		g2.dispose();
		return export;
	}
	
	/**
	 * Export and save the panel as an image file
	 * @param exportImage
	 * @param format
	 * @throws IOException if an error occurs during writing.
	 */
	public void exportToImage(File exportImage, String format) throws IOException {
		ImageIO.write(asImage(), format, exportImage);
	}
}
