package com.github.doobo.soft;

import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

@Slf4j
public class InitUtils {

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
		log.info(home);
		return new File(home + File.separator + ".ipfs").exists();
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
	 * @return
	 */
	public static boolean createIpfsPrivateNetwork(String[] boot){
		creatSwarmKey();
		TerminalUtils.execCmd(IPFS + " bootstrap rm all");
		if(boot != null && boot.length > 0){
			Arrays.stream(boot).forEach(m->{
				TerminalUtils.execCmd(IPFS + " bootstrap add " + m);
			});
		}
		return true;
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
}
