package ipfs.common;

import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.WordUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import vip.ipav.okhttp.OkHttpClientTools;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTests {

	String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTcxNDAzNzIsIlRva2VuVHlwZSI6MSwiVG9rZW4iOnsidXNlcl9pZCI6MSwidG9rZW5fdHlwZSI6MSwiY3JlYXRlX3RpbWUiOiIyMDIwLTA4LTExVDE2OjA2OjEyLjI4ODE3NzA1OCswODowMCIsImludmFsaWRfdGltZSI6IjIwMjAtMDgtMTFUMTg6MDY6MTIuMjg4MTc3MjA4KzA4OjAwIiwiYWNjb3VudCI6IiJ9LCJhZGRpdGlvbiI6eyJSZW1vdGVBZGRyZXNzIjoiMTAuMjAuNDkuMjUwIiwiYWNjZXNzX3NpZ24iOiI4Mjg0MzQyMy0zYTRkLTQyOWEtODRmYi03MTlmZmViMjk2ZWYiLCJldWlkIjoiMTAwMDAxMDAwMTAwIiwiZml4VXNlckluZm8iOmZhbHNlLCJsb2dpbl90eXBlIjoxfSwidXNlcl9pbmZvIjp7InVzZXJfaWQiOjEsInVzZXJfbGV2ZWxfaWQiOjEsImFjY291bnQiOiJ0ZXN0IiwiaWRjYXJkIjoiMzMwNzgxMTk4NTA5MDcxMjkwIiwidXNlcl9uYW1lIjoidGVzdCIsImdlbmRlciI6MSwicGhvbmUiOiIiLCJ3b3JrZXJfbm8iOiJ0ZXN0IiwidGhpcmRfYWNjb3VudCI6IiIsImlzX3JlcXVpcmVkX25vdGlmaWNhdGlvbiI6dHJ1ZSwiZGVmYXVsdF9sYW5ndWFnZSI6InpoLWhhbnQiLCJub3RpY2VfdHlwZSI6InNtcyJ9LCJzeXN0ZW1fYWNjb3VudCI6InRlc3QiLCJyZWRpcmVjdF91cmkiOiIiLCJzeXN0ZW1faWQiOjEsImFjY2Vzc19zeXN0ZW1zIjpbeyJhY2NvdW50IjoidGVzdCIsImlzX3N5c3RlbV9hZG1pbiI6IjAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZGV2LmVnb3YtY2hpbmEuY29tL3BzZHMvbG9naW4iLCJzb3J0X2NvZGUiOiItMSIsInN5c3RlbV9jb2RlIjoiYWNjZXB0Iiwic3lzdGVtX2ljb24iOiI5YmZjY2M1ZS0xMzk2LTQ2OTctYTMzMC1lMjhjYjBkN2I5YTMucG5nIiwic3lzdGVtX2lkIjoiMSIsInN5c3RlbV9uYW1lIjoi5Y-X55CG57O757WxIn1dLCJjbGllbnRfaXAiOiIxMC4yMC40OS4yNTAiLCJzaWduIjoiODI4NDM0MjMtM2E0ZC00MjlhLTg0ZmItNzE5ZmZlYjI5NmVmIn0.EgXW-kgToeFmnhv6yeEUfynR2AgD4-3EVg293VpivgQqmmCwjaLCG0WasLB-p1DaBB17T_1WhGQtqqHj1M9rbjfcxPsXw_ICnOHcZ2Df0Cg6uMtGS1t-JbREO9_yYuSU66Cr-swenBXwzHCcStW-Ha7DHis652hXJMVGFwYZCck";
	Configuration conf = Configuration.builder().build().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

	@Test
	public void testHttpJson() throws IOException {
		String res = OkHttpClientTools.getInstance()
			.post()
			.url("http://127.0.0.1:9048/api/Affairs/GetCheckAffairInfos")
			.jsonParams("{\"sort_List\":{\"modify_date\":true},\"index\":1,\"size\":600,\"node_standard_code\":0}")
			.addHeader("Authorization",token)
			.execute().body().string();
		System.out.println(res);
		DocumentContext doc = JsonPath.using(conf).parse(res);
		List<String> ls = doc.read("$.data..id");
		if(ls != null && !ls.isEmpty()){
			ls = ls.stream().distinct().filter(Objects::nonNull).sorted().collect(Collectors.toList());
			ls.forEach(m-> System.out.println(m));
		}
	}

	@Test
	public void testIpfs() throws Exception {
		/*TerminalUtils.syncExecute(
			 CommandLine.parse("/Users/doobo/back/ipfs-cloud/.ipfs/go-ipfs/ipfs id")
		);*/

		/*byte[] bt = TerminalUtils.syncExecute(
			"/Users/doobo/back/ipfs-cloud/.ipfs/go-ipfs/ipfs add /Users/doobo/back/ipfs-cloud/data/aaa/bb-\\ c.txt");
		System.out.println(new String(bt, UTF_8.name()));*/

		CommandLine commandLine = new CommandLine("/Users/doobo/back/ipfs-cloud/.ipfs/go-ipfs/ipfs");
		commandLine.addArguments(new String[]{"add","/Users/doobo/back/ipfs-cloud/data/aaa/bb- c.txt"}, false);
		try(ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
			DefaultExecutor executor = new DefaultExecutor();
			executor.setStreamHandler(new PumpStreamHandler(bs, bs));
			executor.execute(commandLine);
			bs.flush();
			ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
			executor.setWatchdog(watchdog);
			System.out.println(new String(bs.toByteArray(), UTF_8.name()));
		}


		/*ProcessBuilder pb = new ProcessBuilder("/Users/doobo/back/ipfs-cloud/.ipfs/go-ipfs/ipfs","add","/Users/doobo/back/ipfs-cloud/data/aaa/bb- c.txt");
		Process p = pb.start();
		int exitCode = p.waitFor();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
			StreamUtils.copy(p.getInputStream(), out);
			System.out.println(new String(out.toByteArray(), UTF_8.name()));
		}
		System.out.println(StreamUtils.copyToString(p.getInputStream(), UTF_8));
		System.out.println(StreamUtils.copyToString(p.getErrorStream(), UTF_8));
		System.out.println(exitCode);*/
	}

	@Test
	public void testNetworkPort(){
		System.out.println(OsUtils.checkIpPortOpen("192.168.4.119", 9201));
		System.out.println(OsUtils.isHostReachable("172.16.30.252", 100));
		System.out.println(WordUtils.getStrEndNumber("abc1234"));
	}

	@Test
	public void testRex(){
		Pattern pattern = Pattern.compile("( )(Qm.*?)( bbc.txt)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher("1.71 KiB / 1.71 KiB  100.00%\u001B[2K\n" +
			"\n" +
			" 1.71 KiB / 1.71 KiB  100.00%added QmSAtiyoVY4GsBptZLKYXe7SCzyYEgwGVBfzTUvXMvNaXK bbc.txt");
		if(matcher.find()){
			System.out.println(matcher.group(2));
		}
	}

	@Test
	public void testPath() throws UnsupportedEncodingException {
		String uri = URLEncoder.encode("bbc/bb- c.txt", UTF_8.name());
		System.out.println(uri);

		String encodeData = URLEncoder.encode("bbc/bb- c.txt", "UTF-8")
			.replaceAll("\\+", "%20")
			.replaceAll("\\!", "%21")
			.replaceAll("\\'", "%27")
			.replaceAll("\\(", "%28")
			.replaceAll("\\)", "%29")
			.replaceAll("\\~", "%7E");
		System.out.println(encodeData);

		String decodeData = URLDecoder.decode("bbc/bb-%20c.txt", "UTF-8");
		System.out.println(decodeData);

		System.out.println(WordUtils.encodeURI("bbc/bb- c.txt"));
	}
}
