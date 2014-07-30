package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties;
import dbg.exception.ImageGenerationException;
import dbg.image.ImagePanel;
import dbg.ui.LocalizedText;
import dbg.ui.util.MultiLineLabel;
import dbg.ui.util.UiUtils;
import dbg.util.TemporaryFileHandler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WeatherElement extends GeneratedMediaElement {

    private static final long serialVersionUID = 1L;
    private WeatherProperties.Type type;
    private String key;
    private String low;
    private String high;
    private String icon;
    private String city;
    private int nbDays;

    public WeatherElement(WeatherLocation location, int duration) {
	super("", (location == null) ? "-" : location.toString(), duration);
	this.key = "e1b5ddb86eff0cfd";
	this.nbDays = PropertyManager.getWeatherProperties().getNbDays();
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
	setSubTitle((location == null) ? "-" : location.toString());
    }

    public void getWeather() throws IOException {
	WeatherLocation loc = PropertyManager.getWeatherProperties().getLocation();
	
	String url = "http://api.wunderground.com/api/" + key + "/forecast/lang:FR/q/" + loc.getLatitude() + "," + loc.getLongitude() + ".xml";
	System.out.println("URL: " + url);
	this.city = loc.getCity();
	
	Document doc = Jsoup.connect(url).get();
	Elements forecast = doc.select("simpleforecast forecastdays forecastday high celsius");
	this.high = forecast.get(0).text();
	forecast = doc.select("simpleforecast forecastdays forecastday low celsius");
	this.low = forecast.get(0).text();	
	forecast = doc.select("icon");
	this.icon = forecast.get(0).text();
	
	System.out.println(this.high + " " + this.low + " " + this.icon);
    }

    @Override
    public File generateImage(TemporaryFileHandler temporaryFileHandler) throws ImageGenerationException, IOException {
	
	BufferedImage img = null;
	try {
		File file = PropertyManager.getWeatherProperties().getBackgroundImage();
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
	titre.setBounds(600, 25, 800, 250);
	titre.setFont(ttfReal.deriveFont(Font.BOLD, 95));
	titre.setForeground(Color.WHITE);
	ImagePanel leftLogo = new ImagePanel(ImageIO.read(getClass().getResource("quille_l.png")));
	leftLogo.setBounds(25, 25, 300, 352);
	ImagePanel textArea = new ImagePanel(ImageIO.read(getClass().getResource("textArea.PNG")));
	textArea.setBounds(150, 500, 1625, 600);
	JLabel town = new JLabel(this.city, JLabel.LEFT);
	town.setBounds(175, 475, textArea.getWidth(), 150);
	town.setFont(ttfReal.deriveFont(Font.BOLD, 110));
	town.setForeground(Color.WHITE);
	
	panel.setLayout(null);
	panel.add(titre);
	panel.add(leftLogo);
	panel.add(town);
	
	if (this.nbDays == 1)    {
	    /* ICON */
	    ImagePanel globalWeather = new ImagePanel(ImageIO.read(getClass().getResource("globalWeather.png")));
	    globalWeather.setBounds((textArea.getX() + 150), 550, 600, 600);
	    /* MORNING */
	    JLabel morningTemperature = new JLabel(this.low + "° C", JLabel.CENTER);
	    morningTemperature.setBounds((textArea.getBounds().x + textArea.getBounds().width / 2 - 50), (globalWeather.getBounds().y + globalWeather.getBounds().height), textArea.getBounds().width / 2, 308);
	    morningTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
	    morningTemperature.setForeground(Color.WHITE);
	    JLabel morning = new JLabel("Matin", JLabel.CENTER);
	    morning.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), 525, textArea.getWidth() / 2, 308);
	    morning.setFont(ttfReal.deriveFont(Font.BOLD, 50));
	    morning.setForeground(Color.WHITE);
	    /* SEPARATE */
	    ImagePanel separate = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separate.setBounds((2*(textArea.getWidth() / 4) + (600/2) + 15), 837, 400, 5);
	    /* AFTERNOON */
	    JLabel afternoonTemperature = new JLabel(this.high + "° C", JLabel.CENTER);
	    afternoonTemperature.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), 775, textArea.getWidth() / 2, 308);
	    afternoonTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
	    afternoonTemperature.setForeground(Color.WHITE);
	    JLabel afternoon = new JLabel("Après-midi", JLabel.CENTER);
	    afternoon.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), 850, textArea.getWidth() / 2, 308);
	    afternoon.setFont(ttfReal.deriveFont(Font.BOLD, 50));
	    afternoon.setForeground(Color.WHITE);
	    
	    /* ADD ELEMENT TO PANEL */
	    panel.add(separate);
	    panel.add(morningTemperature);
	    panel.add(morning);
	    panel.add(afternoonTemperature);
	    panel.add(afternoon);
	    panel.add(globalWeather);
	}
	else if (this.nbDays == 2)   {
	    /* ICONS */
	    ImagePanel globalWeatherDay1 = new ImagePanel(ImageIO.read(getClass().getResource("globalWeather.png")));
	    globalWeatherDay1.setBounds((textArea.getX() + 200 + 25), 575, 400, 400);
	    ImagePanel globalWeatherDay2 = new ImagePanel(ImageIO.read(getClass().getResource("globalWeather.png")));
	    globalWeatherDay2.setBounds((textArea.getX() + textArea.getWidth() - 400 - 200), 600, 400, 400);
	    /* MORNING */
	    JLabel morningTemperatureDay1 = new JLabel(this.low + "° C", JLabel.CENTER);
	    morningTemperatureDay1.setBounds((textArea.getX()), (globalWeatherDay1.getY()), textArea.getWidth() / 2, 100);
	    morningTemperatureDay1.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    morningTemperatureDay1.setForeground(Color.WHITE);
	    JLabel morningTemperatureDay2 = new JLabel(this.low + "° C", JLabel.CENTER);
	    morningTemperatureDay2.setBounds((textArea.getX() + textArea.getWidth() / 2), 775, textArea.getWidth() / 2, 25);
	    morningTemperatureDay2.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    morningTemperatureDay2.setForeground(Color.WHITE);
	    /* SEPARATIONS */
	    ImagePanel separateVertical = new ImagePanel(ImageIO.read(getClass().getResource("separate_v.png")));
	    separateVertical.setBounds((textArea.getX() + (textArea.getWidth() / 2)), 650, 5, 400);
	    ImagePanel separateHorizontalDay1 = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separateHorizontalDay1.setBounds((textArea.getX() + textArea.getWidth()/4 - 100), morningTemperatureDay1.getY() + morningTemperatureDay1.getHeight() + 25, 200, 3);
	    ImagePanel separateHorizontalDay2 = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separateHorizontalDay2.setBounds((2*(textArea.getWidth() / 4) + (600/2) + 15), 837, 400, 5);
	    /* AFTERNOON */
	    JLabel afternoonTemperatureDay1 = new JLabel(this.high + "° C", JLabel.CENTER);
	    afternoonTemperatureDay1.setBounds((textArea.getX()), (separateHorizontalDay1.getY() + separateHorizontalDay1.getHeight()), textArea.getWidth() / 2, 308);
	    afternoonTemperatureDay1.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    afternoonTemperatureDay1.setForeground(Color.WHITE);
	    
	    /* ADD ELEMENT TO PANEL */
	    panel.add(separateVertical);
	    panel.add(morningTemperatureDay1);
	    panel.add(morningTemperatureDay2);
	    panel.add(separateHorizontalDay1);
	    panel.add(afternoonTemperatureDay1);
	    //panel.add(afternoon);
	    panel.add(globalWeatherDay1);
	    panel.add(globalWeatherDay2);
	}
	
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
