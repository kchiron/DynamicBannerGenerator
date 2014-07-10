/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbg.data.media.element.generated;

import dbg.control.HoroscopeControl;
import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.data.property.WeatherProperties.Type;
import dbg.exception.ImageGenerationException;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * Test of generateImage method, of class WeatherElement.
     */
    @Test
    public void testGenerateImage() throws ImageGenerationException, IOException {
	System.out.println("generateImage");
	WeatherElement element = new WeatherElement(new WeatherLocation("France", "Auvergne", "Chappes", 45.86, 3.21), 10);
	//element.setType(Type.CITY);
        System.out.println("ELEMENT : " + element.getDuration() + " & " + element.getType());
	File result = element.generateImage();
        result.renameTo(new File(".", result.getName()));
        assertNotNull(result);
    }
}

