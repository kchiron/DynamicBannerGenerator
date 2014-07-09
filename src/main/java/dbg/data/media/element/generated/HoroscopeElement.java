package dbg.data.media.element.generated;

import dbg.control.HoroscopeControl;
import dbg.data.property.PropertyManager;
import dbg.exception.ImageGenerationException;
import dbg.image.ImagePanel;
import dbg.ui.LocalizedText;
import dbg.ui.util.MultiLineLabel;
import dbg.ui.util.UiUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
=======
>>>>>>> 5f1e3297dd092c39568d8820fb799f72fe02a582

public class HoroscopeElement extends GeneratedMediaElement {

	private static final long serialVersionUID = 1L;
	private final ArrayList<HoroscopeControl.Signs> signs;

	public HoroscopeElement(int duration) {
		super(LocalizedText.get("horoscope"), "", duration);
		this.signs = new ArrayList<HoroscopeControl.Signs>();
	}

	public void setSigns(HoroscopeControl.Signs[] signs) {
		this.signs.clear();
		String subTitle = "";
		for (HoroscopeControl.Signs sign : signs) {
			if (sign != null) {
				this.signs.add(sign);
				subTitle += sign.toString().toLowerCase() + ", ";
			}
		}
		subTitle = subTitle.substring(0, subTitle.length() - 2);
		this.setSubTitle(subTitle);
	}


	/*
	 * Retourne tout le contenu d'un seul signe
	 */
	public String getContent(String sign) throws IOException {
		String text;
		sign = Normalizer.normalize(sign, Normalizer.Form.NFD);
		sign = sign.replaceAll("[^\\p{ASCII}]", "");
		String url = "http://www.mon-horoscope-du-jour.com/horoscopes/quotidien/" + sign.toLowerCase() + ".htm";
		System.out.println(url);
		Document doc = Jsoup.connect(url).get();
		Elements content = doc.select("p[class=sp_left sp_right]");
		text = content.get(0).text();		
		return text;
	}

	@Override
	public File generateImage() throws ImageGenerationException, IOException {

		BufferedImage img = null;
		try {
			File file = PropertyManager.getHoroscopeProperties().getBackgroundImage();
			img = ImageIO.read(file);
		} catch (Exception e) {
			throw new ImageGenerationException(e.getMessage());
		}

		ImagePanel panel = new ImagePanel(img);

		//Getting the size of the image (=panel size)
		final Dimension d = panel.getPreferredSize();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM");
		Date date = new Date();

		//title Horoscope
		ImagePanel imgTitle = new ImagePanel(ImageIO.read(getClass().getResource("title.png")));
		imgTitle.setBounds((((d.width - imgTitle.getWidth())/2)-100), 25, 800, 200);
		
		InputStream is = this.getClass().getResourceAsStream("cambriai.ttf");
			
		Font ttfBase = null;
		Font ttfReal = null;
			
		try {
		    ttfBase = Font.createFont(Font.TRUETYPE_FONT, is);
		    ttfReal = ttfBase.deriveFont(Font.BOLD, 48);
		} catch (FontFormatException ex) {
		    System.out.println("ERREUR : " + ex);
		}
		   
		MultiLineLabel titre = new MultiLineLabel("", MultiLineLabel.TextAlign.CENTER, ttfReal);
		MultiLineLabel textSign01 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY, ttfReal);
		MultiLineLabel textSign02 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY, ttfReal);
		MultiLineLabel textSign03 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY, ttfReal);
		MultiLineLabel signe01 = new MultiLineLabel("", MultiLineLabel.TextAlign.CENTER);
		MultiLineLabel signe02 = new MultiLineLabel("", MultiLineLabel.TextAlign.CENTER);
		MultiLineLabel signe03 = new MultiLineLabel("", MultiLineLabel.TextAlign.CENTER);
				
		signe01.setFont(ttfReal.deriveFont(Font.BOLD, 38));
		signe02.setFont(ttfReal.deriveFont(Font.BOLD, 38));
		signe03.setFont(ttfReal.deriveFont(Font.BOLD, 38));
		
		ImagePanel logo = new ImagePanel(ImageIO.read(getClass().getResource("logo.png")));
		logo.setBounds(1675, 1075, 225, 104);
		titre.setText("Horoscope du " + dateFormat.format(date));
		titre.setBounds(600, 25, 800, 200);
		titre.setFont(ttfReal.deriveFont(Font.BOLD, 90));
		titre.setForeground(Color.WHITE);
		
		ImagePanel leftLogo = new ImagePanel(ImageIO.read(getClass().getResource("quille_l.png")));
		leftLogo.setBounds(25, 25, 200, 235);
		ImagePanel rightLogo = new ImagePanel(ImageIO.read(getClass().getResource("quille_r.png")));
		rightLogo.setBounds(1695, 25, 200, 235);

		panel.setLayout(null);

		if (signs.size() == 2) {
		    textSign01.setText(getContent(signs.get(0).toString()));
		    textSign01.setForeground(Color.white);
		    textSign01.setBounds(550, 380, 1270, 415);
		    textSign01.setFont(ttfReal);
		    textSign01.setBackground(Color.white);
		    ImagePanel iconeSign01 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(0).name().toLowerCase() + ".png")));
		    iconeSign01.setBounds(125, 375, 300, 270);
		    signe01.setText("(" + signs.get(0) + ")");
		    signe01.setBounds(190, 625, 300, 50);
		    signe01.setForeground(Color.white);

		    textSign02.setText(getContent(signs.get(1).toString()));
		    textSign02.setForeground(Color.white);
		    textSign02.setBounds(100, 775, 1270, 415);
		    textSign02.setFont(ttfReal);
		    ImagePanel iconeSign02 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(1).name().toLowerCase() + ".png")));
		    iconeSign02.setBounds(1420, 715, 300, 270);
		    signe02.setText("(" + signs.get(1) + ")");
		    signe02.setBounds(1495,1015,300, 50);
		    signe02.setForeground(Color.white);

		    panel.add(iconeSign01);
		    panel.add(textSign01);
		    panel.add(signe01);
		    panel.add(textSign02);
		    panel.add(iconeSign02);
		    panel.add(signe02);
		} else if (signs.size() == 3) {
		    textSign01.setText(getContent(signs.get(0).toString()));
		    textSign01.setForeground(Color.white);
		    textSign01.setFont(new Font("Cambria", Font.BOLD, 42));
		    ImagePanel iconeSign01 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(0).name().toLowerCase() + ".png")));
		    iconeSign01.setBounds(125, 275, 300, 270);
		    textSign01.setBounds(500, 275, 1300, 250);
		    signe01.setText("(" + signs.get(0) + ")");
		    signe01.setBounds(180, 525, 300, 50);
		    signe01.setForeground(Color.white);

		    textSign02.setText(getContent(signs.get(1).toString()));
		    textSign02.setForeground(Color.white);
		    textSign02.setFont(new Font("Cambria", Font.BOLD, 42));
		    textSign02.setBounds(100, 625, 1300, 275);
		    ImagePanel iconeSign02 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(1).name().toLowerCase() + ".png")));
		    iconeSign02.setBounds(1495, 575, 325, 275);
		    signe02.setText("(" + signs.get(1) + ")");
		    signe02.setBounds(1540,825,300, 50);
		    signe02.setForeground(Color.white);

		    textSign03.setText(getContent(signs.get(2).toString()));
		    textSign03.setForeground(Color.white);
		    textSign03.setFont(new Font("Cambria", Font.BOLD, 42));
		    ImagePanel iconeSign03 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(2).name().toLowerCase() + ".png")));
		    iconeSign03.setBounds(100, 875, 325, 275);
		    textSign03.setBounds(500, 950, 1300, 250);
		    signe03.setText("(" + signs.get(2) + ")");
		    signe03.setBounds(180, 1140, 300, 50);
		    signe03.setForeground(Color.white);

		    panel.add(iconeSign01);
		    panel.add(textSign01);
		    panel.add(signe01);
		    panel.add(textSign02);
		    panel.add(iconeSign02);
		    panel.add(signe02);
		    panel.add(iconeSign03);
		    panel.add(textSign03);
		    panel.add(signe03);
		}
		
		panel.add(titre);
		panel.add(logo);
		panel.add(leftLogo);
		panel.add(rightLogo);
		
		double scaleX = d.width / 1920.0; //<taille du layout>;
		double scaleY = d.width / 1080.0; //<taille du layout>;
		double scale = Math.min(scaleX, scaleY);
		int shiftX = scale > 1.0 ? (int) ((d.width - 1920.0 * scale) / 2.0) : 0;
		int shiftY = scale > 1.0 ? (int) ((d.height - 1080.0 * scale) / 2.0) : 0;

		//Scale panel and all its components to fit the size of the background
		UiUtils.scaleAndShiftComponents(panel, scale, shiftX, shiftY);

		// Create a temporary file (in tmp folder of the current OS) and export image to this file
		File out = File.createTempFile("DBG-horoscopePage-", ".png");
		panel.exportToImage(out, "png");

		return out;
	}
}
