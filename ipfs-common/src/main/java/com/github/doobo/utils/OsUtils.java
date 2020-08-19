package com.github.doobo.utils;

import com.github.doobo.params.StringParams;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 系统相关常用工具类
 */
@Slf4j
@PackagePrivate
public class OsUtils {

	/**
	 * 获取系统类型
	 * @return
	 */
	public static StringParams getSystemType() {
		String osName = System.getProperty(StringParams.OS.str());
		if (osName.startsWith(StringParams.MAC_OS.str())) {
			return StringParams.MAC_OS;
		} else if (osName.startsWith(StringParams.Windows.str())) {
			return StringParams.Windows;
		}
		return StringParams.Linux;
	}

	/**
	 * 检测IP端口是否开放
	 * @param ip
	 * @param port
	 */
	public static boolean checkIpPortOpen(String ip, int port){
		Socket connect = new Socket();
		try {
			connect.setReuseAddress(true);
			connect.connect(new InetSocketAddress(ip, port),100);
			return connect.isConnected();
		} catch (IOException e) {
			return false;
		}finally{
			try {
				connect.close();
			} catch (IOException e) {
				log.info("CloseSocketError",e);
			}
		}
	}

	/**
	 * 判断IP是否可连接
	 * @param host
	 * @param timeout
	 */
	public static boolean isHostReachable(String host, Integer timeout) {
		try {
			return InetAddress.getByName(host).isReachable(timeout);
		} catch (Exception e) {
			log.warn("isHostReachableError", e);
		}
		return false;
	}
}
