import com.github.doobo.script.CollectingByte;
import com.github.doobo.script.CollectingLog;
import com.github.doobo.script.ScriptUtil;
import org.junit.Test;


public class ScriptTests {

	@Test
	public void testPing() throws Exception {
		int rs = ScriptUtil.execCmdByte("ping", null, new CollectingByte(), 10000L
			, "-t","3","baidu.com");
		System.out.println(rs);
	}
}
