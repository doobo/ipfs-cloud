package ipfs.common;

import com.github.doobo.utils.CommonUtils;
import com.github.doobo.weight.WeightParent;
import com.github.doobo.weight.WeightRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RandomTests {

	@Test
	public void testRandom() throws Exception {
		List<WeightParent<String>> wss = new ArrayList<>();
		WeightParent<String> ws;
		for(int i = 0; i < 10; i++){
			ws = new WeightParent<String>();
			ws.setT(Character.toString((char) ('A'+i)));
			ws.setCount(10-i);
			wss.add(ws);
		}


		List<WeightParent<String>> w2s = CommonUtils.deepCopy(wss);
		wss.get(0).setCount(100);
		System.out.println(w2s.get(0).getCount());

		/**
		 * 恒定概率随机输出
		 */
		WeightRandom<String> random = new WeightRandom<>(wss);
		random.getElementsByFixed(10).forEach(m->{
			System.out.println(m.getT());
		});
		System.out.println("恒定概率随机输出完成\n");

		random.getNoRepeatElementsByDecrement(10).forEach(m->{
			System.out.println(m.getT());
		});
		System.out.println("递减获取随机值,不重复,输出完成\n");

		random.getNoRepeatElementsByFixed(10).forEach(m->{
			System.out.println(m.getT());
		});
		System.out.println("恒定概率获取不重复的多个元素,输出完成");
	}
}
