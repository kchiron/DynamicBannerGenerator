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
import java.text.Normalizer;
import java.util.ArrayList;

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

		//TODO: change this HTML parse (the web site changed :( )

		String text;
		sign = Normalizer.normalize(sign, Normalizer.Form.NFD);
		sign = sign.replaceAll("[^\\p{ASCII}]", "");
		String url = "http://www.marieclaire.fr/astro/horoscope-du-jour/" + sign + "/";
		System.out.println(url);
		Document dec = Jsoup.connect(url).get();
		Elements content = dec.select("p.texte_paragraphe");
		text = "";

		for (Element e : content) {
			text = e.text();
		}

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
		//1920X1080
		System.out.println("d : " + d.width + "x" + d.height);

		//title Horoscope
		ImagePanel imgTitle = new ImagePanel(ImageIO.read(getClass().getResource("title.png")));
		imgTitle.setBounds(((d.width - imgTitle.getWidth()) / 2 - 25), 25, 800, 200);
		System.out.println("imgTitle.getWIdth() : " + imgTitle.getWidth());
		System.out.println("d.width - imgTitle.getWidth() : " + (d.width - imgTitle.getWidth()));
		System.out.println("(d.width - imgTitle.getWidth())/2 : " + ((d.width - imgTitle.getWidth()) / 2));

		MultiLineLabel textSign01 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY);
		MultiLineLabel textSign02 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY);
		MultiLineLabel textSign03 = new MultiLineLabel("", MultiLineLabel.TextAlign.JUSTIFY);

		//System.out.println("/"+getContent(signs.get(0).toString()) +"/");
		panel.setLayout(null);

		if (signs.size() == 2) {
			System.out.println("sign : " + signs.get(0).name().toLowerCase());
			textSign01.setText(getContent(signs.get(0).toString()));
			textSign01.setForeground(Color.white);
			textSign01.setBounds(550, 380, 1270, 415);
			textSign01.setFont(new Font("Cambria", Font.BOLD, 50));
			System.out.println("image : " + signs.get(0).name().toLowerCase() + ".png");
			ImagePanel iconeSign01 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(0).name().toLowerCase() + ".png")));
			iconeSign01.setBounds(100, 300, 400, 415);

			textSign02.setText(getContent(signs.get(1).toString()));
			textSign02.setForeground(Color.white);
			textSign02.setBounds(100, 775, 1270, 415);
			textSign02.setFont(new Font("Cambria", Font.BOLD, 50));
			ImagePanel iconeSign02 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(1).name().toLowerCase() + ".png")));
			iconeSign02.setBounds(1420, 715, 400, 415);

			panel.add(iconeSign01);
			panel.add(textSign01);
			panel.add(textSign02);
			panel.add(iconeSign02);
		} else if (signs.size() == 3) {
			textSign01.setText(getContent(signs.get(0).toString()));
			textSign01.setForeground(Color.white);
			textSign01.setFont(new Font("Cambria", Font.BOLD, 42));
			ImagePanel iconeSign01 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(0).name().toLowerCase() + ".png")));
			iconeSign01.setBounds(100, 275, 325, 275);
			textSign01.setBounds(475, 300, 1270, 275);
			System.out.println(textSign01.getText());

			textSign02.setText(getContent(signs.get(1).toString()));
			textSign02.setForeground(Color.white);
			textSign02.setFont(new Font("Cambria", Font.BOLD, 42));
			textSign02.setBounds(100, 600, 1270, 275);
			ImagePanel iconeSign02 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(1).name().toLowerCase() + ".png")));
			iconeSign02.setBounds(1495, 550, 325, 275);

			textSign03.setText(getContent(signs.get(2).toString()));
			textSign03.setForeground(Color.white);
			textSign03.setFont(new Font("Cambria", Font.BOLD, 42));
			ImagePanel iconeSign03 = new ImagePanel(ImageIO.read(getClass().getResource(signs.get(2).name().toLowerCase() + ".png")));
			iconeSign03.setBounds(100, 875, 325, 275);
			textSign03.setBounds(475, 950, 1270, 275);

			panel.add(iconeSign01);
			panel.add(textSign01);
			panel.add(textSign02);
			panel.add(iconeSign02);
			panel.add(iconeSign03);
			panel.add(textSign03);
		}

		panel.add(imgTitle);

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
