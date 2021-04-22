import com.github.doobo.script.CollectingLog;
import com.github.doobo.script.ScriptUtil;
import org.junit.Test;

import java.io.IOException;


public class ScriptTests {

	@Test
	public void testPing() throws Exception {
		int rs = ScriptUtil.execCmdLine("ping", null, new CollectingLog(), 10000L
			, "-t","3","baidu.com");
		System.out.println(rs);
	}

	@Test
	public void testSub() throws IOException {
		int i = ScriptUtil.execCmdLine("/Users/doobo/back/ipfs-cloud/.ipfs/go-ipfs/ipfs", null, new CollectingLog(), 60*1000L
			,"pubsub", "sub", "145ee96d-fea0-4555-8573-db2ce1dd7e55"
			, "-c", "/Users/doobo/back/ipfs-cloud/.ipfs/.ipfs", "--encoding", "json");
		System.out.println(i);
	}
}
