package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties;
import dbg.exception.ImageGenerationException;
import dbg.image.ImagePanel;
import dbg.ui.LocalizedText;
import dbg.ui.util.UiUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
	    System.out.println("TYPE : " + type.toString());
	    BufferedImage img = null;
	    try {
		    File bckgndImg = new File(getClass().getResource("bckgndWeather.jpg").getPath());
		    File file = PropertyManager.getWeatherProperties().setBackgroundImage(bckgndImg);
		    img = ImageIO.read(file);
	    } catch (Exception e) {
		    throw new ImageGenerationException(e.getMessage());
	    }

	    ImagePanel panel = new ImagePanel(img);
	    //Getting the size of the image (=panel size)
	    final Dimension d = panel.getPreferredSize();
		
		    
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
