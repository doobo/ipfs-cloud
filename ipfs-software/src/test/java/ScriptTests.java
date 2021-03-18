import com.github.doobo.script.ScriptUtil;
import org.junit.Test;

public class ScriptTests {

	@Test
	public void testPing(){
		String rs = ScriptUtil.execToString("ping", null, 5 * 1000L, "baidu.com");
		System.out.println(rs);
	}
}
