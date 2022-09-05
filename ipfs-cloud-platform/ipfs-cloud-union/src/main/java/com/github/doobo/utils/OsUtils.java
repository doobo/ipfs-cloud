package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 系统相关常用工具类
 */
@Slf4j
public abstract class OsUtils {

	/**
	 * 检测IP端口是否开放
	 */
	public static boolean checkIpPortOpen(String ip, int port){
		Socket connect = new Socket();
		try {
			connect.setReuseAddress(true);
			connect.connect(new InetSocketAddress(ip, port),100);
			return connect.isConnected();
		} catch (Exception e) {
			return false;
		}finally{
			try {
				connect.close();
			} catch (Exception e) {
				log.info("CloseSocketError",e);
			}
		}
	}

	/**
	 * 判断IP是否可连接
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
