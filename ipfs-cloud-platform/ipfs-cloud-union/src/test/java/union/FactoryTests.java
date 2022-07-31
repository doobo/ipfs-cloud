package union;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.vbo.HookTuple;
import com.github.doobo.vbo.ResultTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
}
