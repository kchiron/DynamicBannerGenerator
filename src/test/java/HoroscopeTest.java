/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Katia
 */
public class HoroscopeTest {
    
    public HoroscopeTest() {
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
     * Test of getContent method, of class Horoscope.
     */
    @Test
    public void test() throws Exception {
        Horoscope h = new Horoscope();
        System.out.println(h.getContent("Sagittaire"));
    }
}
