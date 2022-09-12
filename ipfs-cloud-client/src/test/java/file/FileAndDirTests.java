package file;

import cn.yours.elfinder.obs.ElfinderConfigurationUtils;
import org.junit.Test;

public class FileAndDirTests {

	@Test
	public void testFile(){
		String s = ElfinderConfigurationUtils.treatPath("/Users/doobo/Downloads/Telegram Desktop/");
		System.out.println(s);
	}
}
