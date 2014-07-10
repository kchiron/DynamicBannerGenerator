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
            titre.setFont(ttfReal.deriveFont(Font.BOLD, 90));
            titre.setForeground(Color.WHITE);
            ImagePanel leftLogo = new ImagePanel(ImageIO.read(getClass().getResource("quille_l.png")));
            leftLogo.setBounds(25, 25, 200, 235);
            ImagePanel textArea = new ImagePanel(ImageIO.read(getClass().getResource("textArea.PNG")));
            textArea.setBounds(150, 500, 1625, 550);
            ImagePanel globalWeather = new ImagePanel(ImageIO.read(getClass().getResource("globalWeather.png")));
            globalWeather.setBounds((textArea.getWidth()-(500/2)), 250, 500, 500);
            JLabel town = new JLabel("Périgueux", JLabel.CENTER);
            town.setBounds((globalWeather.getWidth() - globalWeather.getWidth()/2) - 515, 515, (globalWeather.getWidth()/2), 50);
            town.setFont(ttfReal.deriveFont(Font.BOLD, 60));
            town.setForeground(Color.WHITE);
            
            panel.setLayout(null);       
            
            panel.add(titre);
            panel.add(leftLogo);
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
