package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties;
import dbg.exception.ImageGenerationException;
import dbg.image.ImagePanel;
import dbg.ui.util.MultiLineLabel;
import dbg.ui.util.UiUtils;
import dbg.util.TemporaryFileHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeatherElement extends GeneratedMediaElement {

    private static final long serialVersionUID = 1L;
    private WeatherProperties.Type type;
    private WeatherLocation location;
    private String key;
    private String lowDay1;
    private String highDay1;
    private String iconDay1;
    private String lowDay2;
    private String highDay2;
    private String iconDay2;
    private String city;
    private int nbDays;

    public WeatherElement(WeatherLocation location, int duration) {
	super("", (location == null) ? "-" : location.toString(), duration);
	this.key = "e1b5ddb86eff0cfd";
	this.location = location;
	this.nbDays = PropertyManager.getWeatherProperties().getNbDays();
    }

    public WeatherProperties.Type getType() {
	return type;
    }

    public void setType(WeatherProperties.Type type) {
	this.type = type;
	setTitle(type.toString());
    }

    public void setLocation(WeatherLocation location) {
	setSubTitle((location == null) ? "-" : location.toString());
	this.location = location;
    }

    public void getWeather() throws IOException {
	WeatherLocation loc = location;
	/* DATE */
	Calendar cal = Calendar.getInstance();
	String currentDay  = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	String nextDay  = String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1);
	
	String url = "http://api.wunderground.com/api/" + key + "/forecast/lang:FR/q/" + loc.getLatitude() + "," + loc.getLongitude() + ".xml";
	System.out.println("URL: " + url);
	this.city = loc.getCity();
	
	if (nbDays == 1) {
	    Document doc = Jsoup.connect(url).get();
	    Elements forecastdays = doc.select("simpleforecast forecastdays forecastday");
	    for (Element forecastday : forecastdays) {
		this.highDay1 = forecastday.select("high celsius").text();
		this.lowDay1 = forecastday.select("low celsius").text();
		this.iconDay1 = forecastday.select("icon").text();
	    }
	    System.out.println(this.highDay1 + " " + this.lowDay1 + " " + this.iconDay1);
	}
	else if (nbDays ==2)	{
	    Document doc = Jsoup.connect(url).get();
	    Elements forecastdays = doc.select("simpleforecast forecastdays forecastday");
	    String day;
	    for (Element forecastday : forecastdays) {
		day = forecastday.select("date day").text();
		if (day.equals(currentDay)) {
		    this.highDay1 = forecastday.select("high celsius").text();
		    this.lowDay1 = forecastday.select("low celsius").text();
		    this.iconDay1 = forecastday.select("icon").text();
		}
		else if (day.equals(nextDay)) {
		    this.highDay2 = forecastday.select("high celsius").text();
		    this.lowDay2 = forecastday.select("low celsius").text();
		    this.iconDay2 = forecastday.select("icon").text();
		}
	    }
	    // check if icons does not exsits to avoid errors
	    if (this.iconDay1.isEmpty()) {
		this.iconDay1 = "na";
	    }
	    if (this.iconDay2.isEmpty()) {
		this.iconDay2 = "na";
	    }
	    System.out.println(this.highDay1 + " " + this.lowDay1 + " " + this.iconDay1);
	    System.out.println(this.highDay2 + " " + this.lowDay2 + " " + this.iconDay2);
	}
	/*
	WeatherLocation loc = location;//PropertyManager.getWeatherProperties().getLocation();

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
	*/
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
	    ImagePanel globalWeather = new ImagePanel(ImageIO.read(getClass().getResource(this.iconDay1 + ".png")));
	    globalWeather.setBounds((textArea.getX() + 150), 615, 600, 411);
	    /* MORNING */
	    JLabel morning = new JLabel("Matin", JLabel.CENTER);
	    morning.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), textArea.getY() + 125, textArea.getWidth() / 2, 50);
	    morning.setFont(ttfReal.deriveFont(Font.BOLD, 50));
	    morning.setForeground(Color.WHITE);
	    JLabel morningTemperature = new JLabel(this.lowDay1 + "° C", JLabel.CENTER);
	    morningTemperature.setBounds((textArea.getBounds().x + textArea.getBounds().width / 2 - 50), morning.getY() + morning.getHeight() + 25, textArea.getWidth() / 2, 75);
	    morningTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
	    morningTemperature.setForeground(Color.WHITE);
	    /* SEPARATE */
	    System.out.println("height : " + (morningTemperature.getY() + morningTemperature.getHeight()));
	    ImagePanel separate = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separate.setBounds((2*(textArea.getWidth() / 4) + (600/2) + 15), morningTemperature.getY() + morningTemperature.getHeight() + 35, 400, 5);
	    /* AFTERNOON */
	    JLabel afternoonTemperature = new JLabel(this.highDay1 + "° C", JLabel.CENTER);
	    afternoonTemperature.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), separate.getY() + separate.getHeight() + 25, textArea.getWidth() / 2, 75);
	    afternoonTemperature.setFont(ttfReal.deriveFont(Font.BOLD, 80));
	    afternoonTemperature.setForeground(Color.WHITE);
	    JLabel afternoon = new JLabel("Après-midi", JLabel.CENTER);
	    afternoon.setBounds((textArea.getX() + textArea.getWidth() / 2 - 50), afternoonTemperature.getY() + afternoonTemperature.getHeight() + 20, textArea.getWidth() / 2, 50);
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
	    ImagePanel globalWeatherDay1 = new ImagePanel(ImageIO.read(getClass().getResource(iconDay1 + ".png")));
	    globalWeatherDay1.setBounds((textArea.getX() + 200), 615, 400, 274);
	    ImagePanel globalWeatherDay2 = new ImagePanel(ImageIO.read(getClass().getResource(iconDay2 + ".png")));
	    globalWeatherDay2.setBounds((textArea.getX() + textArea.getWidth() - 400 - 200), 615, 400, 274);
	    /* MORNING */
	    JLabel morningTemperatureDay1 = new JLabel(this.lowDay1 + "° C", JLabel.CENTER);
	    morningTemperatureDay1.setBounds((textArea.getX()), (globalWeatherDay1.getY() + globalWeatherDay1.getHeight()), textArea.getWidth() / 2, 100);
	    morningTemperatureDay1.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    morningTemperatureDay1.setForeground(Color.WHITE);
	    JLabel morningTemperatureDay2 = new JLabel(this.lowDay2 + "° C", JLabel.CENTER);
	    morningTemperatureDay2.setBounds((textArea.getX() + textArea.getWidth() / 2), (globalWeatherDay1.getY() + globalWeatherDay1.getHeight()), textArea.getWidth() / 2, 100);
	    morningTemperatureDay2.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    morningTemperatureDay2.setForeground(Color.WHITE);
	    /* SEPARATIONS */
	    ImagePanel separateVertical = new ImagePanel(ImageIO.read(getClass().getResource("separate_v.png")));
	    separateVertical.setBounds((textArea.getX() + (textArea.getWidth() / 2)), 650, 5, 400);
	    ImagePanel separateHorizontalDay1 = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separateHorizontalDay1.setBounds((textArea.getX() + textArea.getWidth()/4 - 100), morningTemperatureDay1.getY() + morningTemperatureDay1.getHeight(), 200, 3);
	    ImagePanel separateHorizontalDay2 = new ImagePanel(ImageIO.read(getClass().getResource("separate_h.png")));
	    separateHorizontalDay2.setBounds((textArea.getX() + 3*(textArea.getWidth()/4) - 100), morningTemperatureDay1.getY() + morningTemperatureDay1.getHeight(), 200, 3);
	    /* AFTERNOON */
	    JLabel afternoonTemperatureDay1 = new JLabel(this.highDay1 + "° C", JLabel.CENTER);
	    afternoonTemperatureDay1.setBounds((textArea.getX()), (separateHorizontalDay1.getY()), textArea.getWidth() / 2, 100);
	    afternoonTemperatureDay1.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    afternoonTemperatureDay1.setForeground(Color.WHITE);
	    JLabel afternoonTemperatureDay2 = new JLabel(this.highDay2 + "° C", JLabel.CENTER);
	    afternoonTemperatureDay2.setBounds((textArea.getX() + 2*(textArea.getWidth()/4)), separateHorizontalDay2.getY(), textArea.getWidth() / 2, 100);
	    afternoonTemperatureDay2.setFont(ttfReal.deriveFont(Font.BOLD, 60));
	    afternoonTemperatureDay2.setForeground(Color.WHITE);
	    
	    /* ADD ELEMENT TO PANEL */
	    panel.add(separateVertical);
	    panel.add(morningTemperatureDay1);
	    panel.add(morningTemperatureDay2);
	    panel.add(separateHorizontalDay1);
	    panel.add(separateHorizontalDay2);
	    panel.add(afternoonTemperatureDay1);
	    panel.add(afternoonTemperatureDay2);
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
