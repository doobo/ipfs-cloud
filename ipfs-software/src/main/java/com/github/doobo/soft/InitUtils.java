package com.github.doobo.soft;

import com.alibaba.fastjson.JSON;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.conf.Node;
import com.github.doobo.params.StringParams;
import com.github.doobo.utils.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@PackagePrivate
public class InitUtils {

	/**
	 * json-path解析配置
	 */
	private static Configuration CONF
		= Configuration.builder().build().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

	/**
	 * ipfs命令
	 */
	public static String IPFS;

	public static boolean initIpfsEnv(){
		FileUtils.createFile(".ipfs", ".initIpfs");
		switch (OsUtils.getSystemType()){
			case MAC_OS:
				return initMac64Ipfs();
			case Linux:
				return initLinux64Ipfs();
			case Windows:
				return initWin64Ipfs();
			default:
				log.warn("not support system env, can't init ipfs");
		}
		return false;
	}

	/**
	 * 初始化windows环境
	 */
	public static boolean initWin64Ipfs(){
		FileUtils.createFile(".ipfs/go-ipfs", ".goIpfsExe");
		byte[] rs = FileUtils.readResourcesByte("lib/win64/go-ipfs.zip");
		try (OutputStream out = new FileOutputStream(".ipfs/go-ipfs/ipfs.exe")){
			byte[] ipfs = FileUtils.queryFileInZip(rs, "go-ipfs/ipfs.exe");
			out.write(ipfs);
			IPFS = new File(".ipfs/go-ipfs/ipfs.exe").getCanonicalPath();
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
		String home = System.getProperty("user.home");
		return new File(home + File.separator + ".ipfs" + File.separator + "config").exists();
	}

	/**
	 * 拷贝私有网络库
	 */
	public static boolean creatSwarmKey(){
		String home = System.getProperty("user.home");
		String ipfsHome = home + File.separator + ".ipfs";
		FileUtils.createFile(ipfsHome, "swarm.key");
		byte[] rs =FileUtils.readResourcesByte("key/swarm.key");
		try (FileOutputStream out = new FileOutputStream(ipfsHome + File.separator + "swarm.key")){
			out.write(rs);
		} catch (Exception e){
			log.error("InitUtilsCreateSwarmKey",e);
			return false;
		}
		return true;
	}

	/**
	 * 配置IPFS私有网络
	 */
	public static boolean createIpfsPrivateNetwork(String[] boot){
		if(!creatSwarmKey()){
			return false;
		}
		TerminalUtils.execCmd(IPFS + " bootstrap rm all");
		if(boot != null && boot.length > 0){
			Arrays.stream(boot).forEach(m->{
				TerminalUtils.execCmd(IPFS + " bootstrap add " + m);
			});
		}
		return true;
	}

	/**
	 * 启动ipfs daemon程序
	 */
	public static void startDaemon(){
		TerminalUtils.asyncExecute(IPFS + " daemon");
	}

	/**
	 * 获取ipfs节点配置
	 */
	public static Node getIpfsNodeInfo(){
		byte[] bt = TerminalUtils.syncExecute(IPFS, "id");
		if(bt != null && bt.length > 0){
			try {
				Node node = JSON.parseObject(new String(bt, UTF_8.name()), Node.class);
				String swarm = TerminalUtils.syncExecuteStr(IPFS, "config Addresses.Swarm");
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
	 * @param ipfsConfig
	 */
	public static void updateConfig(IpfsConfig ipfsConfig){
		String swarm = TerminalUtils.syncExecuteStr(IPFS, "config Addresses.Swarm");
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
						, JSON.toJSONString(rm).replace("\"","\"\"\""));
				}else{
					TerminalUtils.execCmd(IPFS, "config Addresses.Swarm --json", JSON.toJSONString(rm));
				}
			}
		}
		//修改5001接口
		String api = TerminalUtils.syncExecuteStr(IPFS, "config Addresses.API");
		if(api != null){
			String num = WordUtils.getStrEndNumber(api);
			if(num != null) {
				TerminalUtils.syncExecuteStr(IPFS, "config Addresses.API", api.replace(num
					, ipfsConfig.getAdminPort().toString()));
			}
		}
		//修改8080接口
		String gateway = TerminalUtils.syncExecuteStr(IPFS, "config Addresses.Gateway");
		if(gateway != null){
			String num = WordUtils.getStrEndNumber(gateway);
			if(num != null) {
				TerminalUtils.syncExecuteStr(IPFS, "config Addresses.Gateway", gateway.replace(num
					, ipfsConfig.getHttpPort().toString()));
			}
		}
	}

	/**
	 * linux和mac os下面的ipfs环境初始化
	 * @param rs
	 */
	private static boolean initUnixIpfs(byte[] rs) {
		try (OutputStream out = new FileOutputStream(".ipfs/go-ipfs.tar.gz")){
			out.write(rs);
			out.flush();
			out.close();
			String tar = TerminalUtils.execCmd("which tar");
			if(tar != null && !tar.isEmpty()){
				TerminalUtils.execCmd("tar zxvf go-ipfs.tar.gz", new File(".ipfs"));
			}
			IPFS = new File(".ipfs/go-ipfs/ipfs").getCanonicalPath();
		} catch (Exception e){
			log.error("init windows ipfs env fail", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断当前IPFS节点是否能读取到文件
	 * @param cid
	 */
	public static boolean existIpfsFile(String cid){
		if(cid == null ||  cid.isEmpty()){
			return false;
		}
		String result = TerminalUtils.execCmd(3,IPFS, "block stat", cid);
		if(result == null || result.isEmpty()){
			return false;
		}
		return result.contains(String.format("Key: %s",cid));
	}
}
