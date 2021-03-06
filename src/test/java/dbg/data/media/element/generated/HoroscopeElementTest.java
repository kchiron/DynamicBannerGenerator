/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbg.data.media.element.generated;

import dbg.control.HoroscopeControl;
import dbg.data.property.PropertyManager;
import dbg.exception.ImageGenerationException;
import junit.framework.Assert;
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
		HoroscopeElement instance = new HoroscopeElement(12);

		for (HoroscopeControl.Signs sign : HoroscopeControl.Signs.values()) {
			String result = instance.getContent(sign.toString());
			System.out.println("Result length : " + result.length());
			Assert.assertNotNull(result);
			Assert.assertNotSame(result.length(), 0);
		}
	}

	/**
	 * Test of imageGeneration method, of class HoroscopeElement.
	 */
	@Test
	public void testImageGeneration() throws ImageGenerationException, Exception {
		HoroscopeElement instance = new HoroscopeElement(12);
		PropertyManager.loadFromFile();

		PropertyManager.getHoroscopeProperties().setBackgroundImage(new File(HoroscopeElementTest.class.getResource("soft.png").getPath()));
		//TestUtils.getTestFile("horoscope.jpg")
		instance.setSigns(new HoroscopeControl.Signs[]{HoroscopeControl.Signs.TAURUS, HoroscopeControl.Signs.CANCER});

		File result = instance.generateImage(null);
		result.renameTo(new File(".", result.getName()));

		assertNotNull(result);
	}
}