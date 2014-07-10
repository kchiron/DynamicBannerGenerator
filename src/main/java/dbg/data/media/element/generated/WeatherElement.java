package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties;
import dbg.exception.ImageGenerationException;
import dbg.image.ImagePanel;
import dbg.ui.LocalizedText;
import dbg.ui.util.MultiLineLabel;
import dbg.ui.util.UiUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class WeatherElement extends GeneratedMediaElement {

	private static final long serialVersionUID = 1L;
	
	private WeatherProperties.Type type;
	
	public WeatherElement(WeatherLocation location, int duration) {
		super("", (location==null)?"-":location.toString(), duration);
	}

	public WeatherProperties.Type getType() {
		return type;
	}

	public void setType(WeatherProperties.Type type) {
		this.type = type;
		
		switch (type) {
			case CITY:
				setTitle(LocalizedText.get("city_weather"));
				break;
			case REGIONAL:
				setTitle(LocalizedText.get("regional_weather"));
				break;
			case NATIONAL:
				setTitle(LocalizedText.get("national_weather"));
				break;
		}
	}

	public void setLocation(WeatherLocation location) {
		setSubTitle((location==null)?"-":location.toString());
	}

	@Override
	public File generateImage() throws ImageGenerationException, IOException {
          
	    BufferedImage img = null;
		try {
			File file = new File(getClass().getResource("bckgndWeather.jpg").getPath());
			img = ImageIO.read(file);
		} catch (Exception e) {
			throw new ImageGenerationException(e.getMessage());
		}

	    ImagePanel panel = new ImagePanel(img);
            
	    //Getting the size of the image (=panel size)
	    final Dimension d = panel.getPreferredSize();
            
            InputStream is = this.getClass().getResourceAsStream("cambriai.ttf");	
            Font ttfBase = null;
            Font ttfReal = null;
            try {
                ttfBase = Font.createFont(Font.TRUETYPE_FONT, is);
                ttfReal = ttfBase.deriveFont(Font.BOLD, 48);
            } catch (FontFormatException ex) {
                System.out.println("ERREUR : " + ex);
            }
                
            MultiLineLabel titre = new MultiLineLabel("SNOOK-BOWL\nPALACE", MultiLineLabel.TextAlign.CENTER, ttfReal);
            titre.setBounds(600, 25, 800, 200);
            titre.setFont(ttfReal.deriveFont(Font.BOLD, 95));
            titre.setForeground(Color.WHITE);
            ImagePanel leftLogo = new ImagePanel(ImageIO.read(getClass().getResource("quille_l.png")));
            leftLogo.setBounds(25, 25, 300, 352);
            ImagePanel textArea = new ImagePanel(ImageIO.read(getClass().getResource("textArea.PNG")));
            textArea.setBounds(150, 500, 1625, 600);
            ImagePanel globalWeather = new ImagePanel(ImageIO.read(getClass().getResource("globalWeather.png")));
            globalWeather.setBounds((textArea.getWidth()-(600/2)), 175, 600, 600);
            JLabel town = new JLabel("Clermont-Ferrand", JLabel.LEFT);
            town.setBounds(175, 425, textArea.getWidth(), 150);
            town.setFont(ttfReal.deriveFont(Font.BOLD, 110));
            town.setForeground(Color.WHITE);
            ImagePanel separate = new ImagePanel(ImageIO.read(getClass().getResource("separate.png")));
            separate.setBounds((textArea.getWidth()/2 + 150 - 5), 600, 5, 400);
            ImagePanel morningWeather = new ImagePanel(ImageIO.read(getClass().getResource("set_01/23.png")));
            morningWeather.setBounds(((textArea.getWidth()/2)/2 - 50), 600, 450, 308);
            JLabel morningTemperature = new JLabel("12°C", JLabel.CENTER);
            morningTemperature.setBounds(((textArea.getWidth()/2)/2 - 50), 800, 450, 308);
            morningTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
            morningTemperature.setForeground(Color.WHITE);
	    JLabel morning = new JLabel("Matin", JLabel.CENTER);
            morning.setBounds(((textArea.getWidth()/2)/2 - 50), 875, 450, 308);
            morning.setFont(ttfReal.deriveFont(Font.BOLD, 50));
            morning.setForeground(Color.WHITE);
	    ImagePanel afternoonWeather = new ImagePanel(ImageIO.read(getClass().getResource("set_01/36.png")));
            afternoonWeather.setBounds(((textArea.getWidth()/2) + (textArea.getWidth()/4) - 100), 600, 450, 308);
	    JLabel afternoonTemperature = new JLabel("26°C", JLabel.CENTER);
            afternoonTemperature.setBounds(((textArea.getWidth()/2) + (textArea.getWidth()/4) - 100), 800, 450, 308);
            afternoonTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
            afternoonTemperature.setForeground(Color.WHITE);
	    JLabel afternoon = new JLabel("Après-midi", JLabel.CENTER);
            afternoon.setBounds(((textArea.getWidth()/2) + (textArea.getWidth()/4) - 100), 875, 450, 308);
            afternoon.setFont(ttfReal.deriveFont(Font.BOLD, 50));
            afternoon.setForeground(Color.WHITE);
	    
            panel.setLayout(null);       
            
            panel.add(titre);
            panel.add(leftLogo);
	    panel.add(separate);
	    panel.add(morningWeather);
	    panel.add(morningTemperature);
	    panel.add(morning);
	    panel.add(afternoonWeather);
	    panel.add(afternoonTemperature);
	    panel.add(afternoon);
            panel.add(town);
            panel.add(globalWeather);
            panel.add(textArea);
		
            double scaleX = d.width / 1920.0; //<taille du layout>;
            double scaleY = d.width / 1080.0; //<taille du layout>;
            double scale = Math.min(scaleX, scaleY);
            int shiftX = scale > 1.0 ? (int) ((d.width - 1920.0 * scale) / 2.0) : 0;
            int shiftY = scale > 1.0 ? (int) ((d.height - 1080.0 * scale) / 2.0) : 0;

            //Scale panel and all its components to fit the size of the background
            UiUtils.scaleAndShiftComponents(panel, scale, shiftX, shiftY);

            // Create a temporary file (in tmp folder of the current OS) and export image to this file
            File out = File.createTempFile("DBG-weatherPage-", ".png");
            panel.exportToImage(out, "png");

            return out;
	}
}
