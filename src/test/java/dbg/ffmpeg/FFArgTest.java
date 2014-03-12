package dbg.ffmpeg;

import org.junit.Test;
import org.junit.Assert;

public class FFArgTest {

	@Test
	public void test1() {
		FFArg f = new FFArg("-toto");
		
		String expected = "-toto";
		String actual = f.toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test2() {
		FFArg f = new FFArg("-toto", "3");
		
		String expected = "-toto 3";
		String actual = f.toString();
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test4() {
		FFArg f = new FFArg("-toto");
		f.setValue("3");
		
		String expected = "-toto 3";
		String actual = f.toString();
		
		Assert.assertEquals(expected, actual);
	}
}
