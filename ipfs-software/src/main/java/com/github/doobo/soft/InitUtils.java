package com.github.doobo.soft;

import com.alibaba.fastjson.JSON;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.conf.Node;
import com.github.doobo.params.StringParams;
import com.github.doobo.script.CollectingConsole;
import com.github.doobo.script.CollectingLog;
import com.github.doobo.script.ScriptUtil;
import com.github.doobo.utils.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static java.lang.Thread.sleep;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@PackagePrivate
public class InitUtils {

	/**
	 * json-path解析配置
	 */
	private final static Configuration CONF
		= Configuration.builder().build().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

	/**
	 * ipfs启动线程池
	 */
	private final static ForkJoinPool POOL = new ForkJoinPool(2);

	/**
	 * ipfs命令
	 */
	private static String IPFS_DIR = ".ipfs";

	public static String IPFS_CONF = "";

	public static String[] IPFS_CONF_ARRAY= new String[]{"-c", "", "daemon", "--enable-pubsub-experiment"};

	public static String IPFS;

	public static String IPFS_EXTEND;

	private static String NODE_ID;

	public static boolean initIpfsEnv(String ipfsDir){
		IPFS_DIR = ipfsDir == null? IPFS_DIR: ipfsDir;
		File file = FileUtils.createFile(IPFS_DIR+"/.ipfs", ".initIpfs");
		switch (OsUtils.getSystemType()){
			case MAC_OS:
				return initMac64Ipfs();
			case Linux:
				return initLinux64Ipfs();
			case Windows:
				return initWin64Ipfs();
			default:
				if(file.isDirectory() && file.delete()){
					log.warn("delete .initIpfs file");
				}
				log.warn("not support system env, can't init ipfs");
		}
		return false;
	}

	/**
	 * 兼容原版本
	 */
	public static boolean initIpfsEnv(){
		return initIpfsEnv(null);
	}

	/**
	 * 初始化windows环境
	 */
	public static boolean initWin64Ipfs(){
		FileUtils.createFile(IPFS_DIR + "/go-ipfs", ".goIpfsExe");
		byte[] rs = FileUtils.readResourcesByte("lib/win64/go-ipfs.zip");
		try (OutputStream out = new FileOutputStream(IPFS_DIR+"/go-ipfs/ipfs.exe")){
			byte[] ipfs = FileUtils.queryFileInZip(rs, "go-ipfs/ipfs.exe");
			out.write(ipfs);
			IPFS = new File(IPFS_DIR+"/go-ipfs/ipfs.exe").getCanonicalPath();
			IPFS_CONF =new File(IPFS_DIR+"/.ipfs/").getCanonicalPath();
			IPFS_CONF_ARRAY[1] = new File(IPFS_DIR+"/.ipfs/").getCanonicalPath();
			IPFS_EXTEND = String.format("%s -c '%s'", IPFS, new File(IPFS_DIR+"/.ipfs/").getCanonicalPath());
		} catch (Exception e){
			log.error("init windows ipfs env fail", e);
			return false;
		}
		return true;
	}

	/**
	 * 初始化macOs环境
	 */
	public static boolean initMac64Ipfs(){
		byte[] rs =FileUtils.readResourcesByte("lib/mac64/go-ipfs.tar.gz");
		return initUnixIpfs(rs);
	}

	/**
	 * 初始化Linux环境
	 */
	public static boolean initLinux64Ipfs(){
		byte[] rs =FileUtils.readResourcesByte("lib/linux64/go-ipfs.tar.gz");
		return initUnixIpfs(rs);
	}

	/**
	 * 检测Ipfs是否初始化
	 */
	public static boolean isIpfsInit(){
		//String home = System.getProperty("user.home");
		return new File(IPFS_DIR + File.separator + ".ipfs" + File.separator + "config").exists();
	}

	/**
	 * 拷贝私有网络库
	 */
	public static boolean creatSwarmKey(String swarmKey){
		byte[] rs = new byte[0];
		if(StringUtils.isBlank(swarmKey)){
			rs =FileUtils.readResourcesByte("key/swarm.key");
		}else {
			try {
				swarmKey = swarmKey.replace("\\n", System.lineSeparator());
				rs = swarmKey.getBytes(UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				log.warn("creatSwarmKeyError", e);
			}
		}
		String ipfsHome = IPFS_DIR + File.separator + ".ipfs";
		FileUtils.createFile(ipfsHome, "swarm.key");
		try (FileOutputStream out = new FileOutputStream(ipfsHome + File.separator + "swarm.key")){
			out.write(rs);
		} catch (Exception e){
			log.error("creatSwarmKeyError",e);
			return false;
		}
		return true;
	}

	/**
	 * 删除私有密钥
	 */
	public static void delSwarmKey(){
		String ipfsHome = IPFS_DIR + File.separator + ".ipfs";
		File file = FileUtils.createFile(ipfsHome, "swarm.key");
		file.deleteOnExit();
	}

	/**
	 * 配置IPFS私有网络
	 */
	public static boolean createIpfsPrivateNetwork(String[] boot, String swarmKey){
		if(!creatSwarmKey(swarmKey)){
			return false;
		}
		TerminalUtils.syncExecute(IPFS, IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1], " bootstrap rm all");
		if(boot != null && boot.length > 0){
			Arrays.stream(boot).forEach(m->{
				TerminalUtils.syncExecuteStr(IPFS_EXTEND, " bootstrap add " + m);
			});
		}
		return true;
	}

	/**
	 * 旧方法兼容
	 */
	public static boolean createIpfsPrivateNetwork(String[] boot){
		return createIpfsPrivateNetwork(boot, null);
	}

	/**
	 * 启动ipfs daemon程序
	 */
	public static void startDaemon(){
		POOL.execute(() -> {
			try {
				File lock = new File(IPFS_CONF + File.separator + "repo.lock");
				lock.deleteOnExit();
				ScriptUtil.execCmdLine(IPFS,  null, new CollectingConsole(), Long.MAX_VALUE, IPFS_CONF_ARRAY);
			} catch (Exception e) {
				log.info("start ipfs daemon Error", e);
			}
		});
	}

	/**
	 * 获取ipfs节点配置
	 */
	public static Node getIpfsNodeInfo(){
		byte[] bt = TerminalUtils.syncExecute(IPFS_EXTEND, "id");
		if(bt.length > 0){
			try {
				Node node = JSON.parseObject(new String(bt, UTF_8.name()), Node.class);
				String swarm = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.Swarm");
				//获取ipfs端口号
				if(swarm != null) {
					List<String> sm = JsonPath.using(CONF).parse(swarm).read("$");
					if(CommonUtils.hasValue(sm)){
						for(String m : sm){
							String num = WordUtils.getStrEndNumber(m);
							if(num != null){
								node.setPort(num);
								break;
							}
						}
					}
				}
				return node;
			} catch (Exception e) {
				log.error("getNodeInfoError", e);
			}
		}
		return null;
	}

	/**
	 * 更新ipfs控制
	 */
	public static void updateConfig(IpfsConfig ipfsConfig){
		String swarm = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.Swarm");
		//修改4001接口
		if(swarm != null){
			List<String> sm = JsonPath.using(CONF).parse(swarm).read("$");
			List<String> rm = new ArrayList<>();
			if(CommonUtils.hasValue(sm)){
				sm.forEach(m->{
					String num = WordUtils.getStrEndNumber(m);
					if(num != null){
						rm.add(m.replace(num, ipfsConfig.getPort().toString()));
					}
					if(m.endsWith("quic")){
						num = WordUtils.getStrEndNumber(m.substring(0,m.lastIndexOf("/")));
						if(num != null) {
							rm.add(m.replace(num, ipfsConfig.getPort().toString()));
						}
					}
				});
			}
			if(!rm.isEmpty()){
				if(OsUtils.getSystemType() == StringParams.Windows){
					TerminalUtils.execCmd(IPFS, "config Addresses.Swarm --json"
						, JSON.toJSONString(rm).replace("\"","\"\"\""), IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1]);
				}else{
					TerminalUtils.execCmd(IPFS, "config Addresses.Swarm --json", JSON.toJSONString(rm), IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1]);
				}
			}
		}
		//修改5001接口
		String api = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.API");
		if(api != null){
			String num = WordUtils.getStrEndNumber(api);
			if(num != null) {
				TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.API", api.replace(num
					, ipfsConfig.getAdminPort().toString()));
			}
		}
		//修改8080接口
		String gateway = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.Gateway");
		if(gateway != null){
			String num = WordUtils.getStrEndNumber(gateway);
			if(num != null) {
				TerminalUtils.syncExecuteStr(IPFS_EXTEND, "config Addresses.Gateway", gateway.replace(num
					, ipfsConfig.getHttpPort().toString()));
			}
		}
	}

	/**
	 * linux和mac os下面的ipfs环境初始化
	 */
	private static boolean initUnixIpfs(byte[] rs) {
		String filepath = IPFS_DIR + "/go-ipfs.tar.gz";
		try (OutputStream out = new FileOutputStream(filepath)){
			out.write(rs);
			out.flush();
			out.close();
			//添加执行权限
			FileUtils.deGzipArchive(IPFS_DIR, filepath);
			IPFS = new File(IPFS_DIR + "/go-ipfs/ipfs").getCanonicalPath();
			ScriptUtil.execToString("chmod", null, 3 * 1000L, "+x" , IPFS);
			IPFS_CONF = new File(IPFS_DIR+"/.ipfs/").getCanonicalPath();
			IPFS_CONF_ARRAY[1] = new File(IPFS_DIR+"/.ipfs/").getCanonicalPath();
			IPFS_EXTEND = String.format("%s -c %s", IPFS, new File(IPFS_DIR+"/.ipfs/").getCanonicalPath());
		} catch (Exception e){
			log.error("init windows ipfs env fail", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断当前IPFS节点是否能读取到文件
	 */
	public static boolean existIpfsFile(String cid){
		if(cid == null ||  cid.isEmpty()){
			return false;
		}
		String result = ScriptUtil.execToString(IPFS, null, 3000L,
			"block", "stat", cid, IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1]);
		if(result == null || result.isEmpty()){
			return false;
		}
		return result.contains(String.format("Key: %s",cid));
	}

	/**
	 * 发送广播数据
	 */
	public static void pubMsg(String topic, String data) {
		ScriptUtil.execToString(IPFS, null, 5*1000L
			, "pubsub", "pub", topic, data, IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1]);
	}

	/**
	 * 获取节点ID
	 */
	public static String getNodeId(){
		if(NODE_ID != null){
			return NODE_ID;
		}
		Node ipfsNodeInfo = getIpfsNodeInfo();
		if(ipfsNodeInfo != null && StringUtils.isNotBlank(ipfsNodeInfo.getCid())){
			NODE_ID = ipfsNodeInfo.getCid();
		}
		return NODE_ID;
	}

	public static void updateBootstrap(List<IpfsConfig> nodeConfigList) {
		if (nodeConfigList == null || nodeConfigList.isEmpty()) {
			return;
		}
		NODE_ID = getNodeId();
		nodeConfigList.forEach(m -> {
			if (CommonUtils.hasValue(m.getNodes())) {
				Node node = m.getNodes().get(0);
				node.setPort(node.getPort() == null ? m.getPort().toString() : node.getPort());
				if (OsUtils.checkIpPortOpen(node.getIp(), Integer.parseInt(node.getPort()))) {
					String ipfs;
					if(node.getIp() != null &&  "localhost".equals(node.getIp())){
						node.setIp("127.0.0.1");
					}
					if (WordUtils.isIpV4Address(node.getIp())) {
						ipfs = String.format("/ip4/%s/tcp/%s/ipfs/%s", node.getIp(), node.getPort(), node.getCid());
					} else {
						ipfs = String.format("/dnsaddr/%s/tcp/%s/ipfs/%s", node.getIp(), node.getPort(), node.getCid());
					}
					if(NODE_ID != null && !NODE_ID.equals(node.getCid())){
						TerminalUtils.syncExecuteStr(IPFS_EXTEND, "bootstrap add", ipfs);
					}
				}
			}
		});
	}

	/**
	 * 订阅广播
	 */
	public  static boolean initSub(IpfsConfig ipfsConfig) throws InterruptedException {
		String rs = "Error";
		int i = 0;
		while (i < 10 && rs != null && rs.contains("Error")){
			i += 1;
			sleep(1000L);
			rs = ScriptUtil.execToString(IPFS, null, 2 * 1000L,
				IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1],
				"pubsub", "ls");
			log.info("pubsub ls: {}", rs);
		}
		rs = ScriptUtil.execToString(IPFS, null, 2 * 1000L,
			IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1],
			"pubsub", "ls");
		log.info("pubsub ls: {}", rs);
		if(rs != null && rs.contains(ipfsConfig.getTopic())){
			return Boolean.TRUE;
		}
		POOL.execute(()->{
			try {
				ScriptUtil.execCmdLine(InitUtils.IPFS, null, new CollectingLog(ipfsConfig.getTopic()), Long.MAX_VALUE
					, "pubsub", "sub", ipfsConfig.getTopic(), "--encoding", "json"
					, InitUtils.IPFS_CONF_ARRAY[0], InitUtils.IPFS_CONF_ARRAY[1]);
			} catch (Exception e) {
				log.error("Ipfs Sub Error", e);
			}
		});
		i = 0;
		while (i < 10 && rs != null &&  !rs.contains(ipfsConfig.getTopic())){
			i += 1;
			sleep(1000L);
			rs = ScriptUtil.execToString(IPFS, null, 2 * 1000L,
				IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1],
				"pubsub", "ls");
			rs = rs == null? "" : rs;
			log.info("pubsub ls: {}", rs);
		}
		if(rs != null && rs.contains(ipfsConfig.getTopic())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 关闭线程池
	 */
	public static void closePool(){
		POOL.shutdownNow();
	}
}
