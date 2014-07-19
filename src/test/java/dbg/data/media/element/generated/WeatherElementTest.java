/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.WeatherProperties.Type;
import java.io.File;
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
     * Test of getWeather method, of class WeatherElement.
     */
    @Test
    public void testGetWeather() {
	System.out.println("getWeather");
	WeatherLocation loc = new WeatherLocation("France", "Auvergne", "Clermont-Ferrand", 3.08, 45.78);
	WeatherElement instance = new WeatherElement(loc, 10);
	instance.getWeather();
	// TODO review the generated test code and remove the default call to fail.
    }

    //COPY PASTE TEST GETGENERATEDIMAGE
}
