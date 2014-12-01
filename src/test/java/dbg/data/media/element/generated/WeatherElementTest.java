/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties.Type;
import dbg.exception.ImageGenerationException;
import dbg.util.TemporaryFileHandler;
import org.junit.*;

import java.io.File;
import java.io.IOException;



/**
 *
 * @author Katia
 */
public class WeatherElementTest {
    
    public WeatherElementTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getWeather method, of class WeatherElement.
     */
    /*@Test
    public void testGetWeather() throws Exception {
	System.out.println("getWeather");
	WeatherLocation loc = new WeatherLocation("France", "Auvergne", "Clermont-Ferrand", 3.08, 45.78);
	PropertyManager.loadFromFile();
	PropertyManager.getWeatherProperties().setNbDays(2);
	PropertyManager.getWeatherProperties().setLocation(loc);
	PropertyManager.getWeatherProperties().setBackgroundImage( new File(getClass().getResource("bckgndWeather.jpg").getPath()));
	WeatherElement instance = new WeatherElement(loc, 10);
	instance.setType(Type.CITY);
	instance.getWeather();
	// TODO review the generated test code and remove the default call to fail.
    }*/

    @Test
    public void testGenerateImage() throws IOException, ImageGenerationException, Exception {
	System.out.println("generateImage");
	WeatherLocation loc = new WeatherLocation("France", "Dordogne", "Périgueux", 0.71, 45.18);
	PropertyManager.loadFromFile();
	PropertyManager.getWeatherProperties().setNbDays(2);
	PropertyManager.getWeatherProperties().setBackgroundImage( new File(getClass().getResource("bckgndWeather.jpg").getPath()));
	WeatherElement instance = new WeatherElement(loc, 10);
	instance.setType(Type.CITY);	
	instance.getWeather();
	File result = instance.generateImage(new TemporaryFileHandler());
	result.renameTo(new File(".", result.getName()));
	Assert.assertNotNull(result);
    }
}
