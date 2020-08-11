package com.github.doobo.soft;

import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
	 * @return
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
	 * @return
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
	 * linux和mac os下面的ipfs环境初始化
	 * @param rs
	 * @return
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
