/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.media.element.generated;

import control.HoroscopeControl;
import data.property.PropertyManager;
import exception.ImageGenerationException;
import java.awt.image.BufferedImage;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import ui.*;
import util.TestUtils;

/**
 *
 * @author Katia
 */
public class HoroscopeElementTest {
    
    public HoroscopeElementTest() {
    }

    /**
     * Test of setSigns method, of class HoroscopeElement.
     */
//    @Test
//    public void testSetSigns() {
//	System.out.println("setSigns");
//	HoroscopeControl.Signs[] signs = null;
//	HoroscopeElement instance = null;
//	instance.setSigns(HoroscopeControl.Signs.values());
//	// TODO review the generated test code and remove the default call to fail.
//	//fail("The test case is a prototype.");
//    }

    /**
     * Test of getContent method, of class HoroscopeElement.
     */
    @Test
    public void testGetContent() throws Exception {
	System.out.println("getContent");
	HoroscopeElement instance = new HoroscopeElement(12);
	String expResult = "";
	String result = instance.getContent(LocalizedText.aries);
	//assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	System.out.println("Result : " + result);
	//fail("The test case is a prototype.");
    }

    /**
     * Test of imageGeneration method, of class HoroscopeElement.
     */
    @Test
    public void testImageGeneration() throws ImageGenerationException, Exception {
	System.out.println("imageGeneration");
	HoroscopeElement instance = new HoroscopeElement(12);
	BufferedImage expResult = null;
	PropertyManager.loadFromFile();
	PropertyManager.getHoroscopeProperties().setBackgroundImage(new File (HoroscopeElementTest.class.getResource("horoscope.jpg").getPath()));
	//TestUtils.getTestFile("horoscope.jpg")
	BufferedImage result = instance.imageGeneration();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	//fail("The test case is a prototype.");
    }
}