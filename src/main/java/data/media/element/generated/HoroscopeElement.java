package data.media.element.generated;

import java.util.ArrayList;

import control.HoroscopeControl;
import ui.LocalizedText;
import data.media.element.MediaElement;
import data.property.PropertyManager;
import exception.ImageGenerationException;
import image.ImagePanel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HoroscopeElement extends MediaElement {

    private static final long serialVersionUID = 1L;
    private final ArrayList<HoroscopeControl.Signs> signs;

    public HoroscopeElement(int duration) {
	super(LocalizedText.horoscope, "", duration);
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
	Document dec = Jsoup.connect("http://www.marieclaire.fr/astro/horoscope-du-jour/" + sign + "/").get();
	Elements content = dec.select("p.texte_paragraphe");
	text = "";

	for (Element e : content) {
	    text += e.text() + " ";
	}

	return text;
    }
    
    public BufferedImage imageGeneration() throws ImageGenerationException, IOException	{
	BufferedImage img = null;
	try {
	    File file = PropertyManager.getHoroscopeProperties().getBackgroundImage();
	    img = ImageIO.read(file);
	}
	catch (Exception e)   {
	    throw new ImageGenerationException(e.getMessage());
	}
	
	ImagePanel panel = new ImagePanel(img);
	ImagePanel icone = new ImagePanel(ImageIO.read(getClass().getResource("icon.png")));
	JLabel text = new JLabel("Hello, World!");
	
	text.setForeground(Color.white);
        //
	text.setBounds(250, 300, 800, 250);
	
	icone.setBounds(50, 50, 128, 128);
	
	panel.add(icone);
	panel.add(text);
	panel.setLayout(null);
	panel.exportToImage(new File("output.png"), "png");
	
	return null;
    }
}
