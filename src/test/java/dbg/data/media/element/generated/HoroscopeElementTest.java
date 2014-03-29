/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbg.data.media.element.generated;

import dbg.control.HoroscopeControl;
import dbg.data.property.PropertyManager;
import dbg.exception.ImageGenerationException;
import dbg.ui.LocalizedText;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

/**
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
		PropertyManager.loadFromFile();
		PropertyManager.getHoroscopeProperties().setBackgroundImage(new File(HoroscopeElementTest.class.getResource("soft.png").getPath()));
		//TestUtils.getTestFile("horoscope.jpg")
		instance.setSigns(new HoroscopeControl.Signs[]{HoroscopeControl.Signs.AQUARIUS, HoroscopeControl.Signs.ARIES});
		File result = instance.generateImage();
		assertNotNull(result);
		// TODO review the generated test code and remove the default call to fail.
		//fail("The test case is a prototype.");
	}
}