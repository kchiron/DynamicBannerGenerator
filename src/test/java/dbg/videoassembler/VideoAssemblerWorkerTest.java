package dbg.videoassembler;

import java.util.concurrent.ExecutionException;

import org.junit.BeforeClass;
import org.junit.Test;

import dbg.data.property.PropertyManager;
import dbg.util.TestUtils;

public class VideoAssemblerWorkerTest {

	@BeforeClass	
	public static void setUpClass() throws Exception {
		PropertyManager.setConfigFile(TestUtils.getTestFile("/videoassembler/testConf.bin"));
		PropertyManager.loadFromFile();
	}
	
	@Test
	public void test() throws InterruptedException, ExecutionException {
		VideoAssemblerWorker va = new VideoAssemblerWorker(null);
		va.execute();
		System.out.println(va.get());
	}

}
