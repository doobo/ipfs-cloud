package union;

import com.alibaba.fastjson.JSON;
import com.github.doobo.bo.IpfsNodeInfo;
import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.script.ScriptUtil;
import com.github.doobo.vbo.HookTuple;
import com.github.doobo.vbo.ResultTemplate;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Consumer;

public class FactoryTests {

	@BeforeAll
	public static void beforeInit(){
		PlatformInitMacHandler handler = new PlatformInitMacHandler();
		PlatformInitFactory.addHandler(handler);
	}

	@Test
	public void testInterface(){
		HookTuple hook = new HookTuple() {
			@Override
			public <K, V> Consumer<V> beforeTuple(K k) {
				System.out.println("beforeTuple");
				return t -> {
					throw new RuntimeException("异常测试1");
				};
			}

			@Override
			public <K, V> Consumer<V> endTuple(K k) {
				System.out.println("endTuple");
				return null;
			}

			@Override
			public <T> Consumer<T> errorTuple(Throwable e) {
				System.out.println("errorTuple:" + e.getMessage());
				return t -> {
					System.out.println("异常处理："+ t);
				};
			}
		};
		PlatformInitRequest request = new PlatformInitRequest();
		ResultTemplate<PlatformInitResponse> result = PlatformInitFactory
			.executeHandler(request, handler -> handler.initIpfs(request), hook);
		System.out.println(result);
	}

	@Test
	public void testScript(){
		String result = ScriptUtil.execNotHandleQuoting("/Users/diding/other/ipfs-cloud/data/go-ipfs/ipfs"
			, "-c", "/Users/diding/other/ipfs-cloud/data/.ipfs"
			, "config", "Addresses.Swarm", "--json", "[\"/ip6/::/tcp/4001\"]");
		Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(System.out::println);
	}

	@Test
	public void testNode(){
		String result = ScriptUtil.execDefaultTime("/Users/diding/other/ipfs-cloud/data/go-ipfs/ipfs"
			, "-c", "/Users/diding/other/ipfs-cloud/data/.ipfs"
			, "id");
		Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(System.out::println);
		PlatformInitResponse response = new PlatformInitResponse();
		response.setExePath("/Users/diding/other/ipfs-cloud/data/go-ipfs/ipfs");
		response.setConfigDir("/Users/diding/other/ipfs-cloud/data/.ipfs");
		//IpfsNodeInfo info = AbstractPlatformInitHandler.queryNodeInfo(response);
		//System.out.println(JSON.toJSONString(info));
	}
}
