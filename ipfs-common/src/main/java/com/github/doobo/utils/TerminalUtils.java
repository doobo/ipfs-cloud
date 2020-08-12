package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 基本命令行工具
 */
@Slf4j
public class TerminalUtils {

	private static final long TIMEOUT = 60;

	/**
	 * 执行系统命令, 返回执行结果
	 * @param cmd 需要执行的命令
	 * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
	 * @param timeout 最长等待多长时间,单位秒
	 */
	public static String execCmd(String cmd, File dir, long timeout) {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		StringBuilder result = new StringBuilder();
		// 使用Callable接口作为构造参数
		FutureTask<String> future = new FutureTask<>(() ->{
			Process process = null;
			BufferedReader bufIn = null;
			BufferedReader bufError = null;
			try {
				// 执行命令, 返回一个子进程对象（命令在子进程中执行）
				process = Runtime.getRuntime().exec(cmd, null, dir);
				// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
				bufIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
				bufError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
				// 读取输出
				String line;
				while ((line = bufIn.readLine()) != null) {
					result.append(line).append('\n');
				}
				while ((line = bufError.readLine()) != null) {
					result.append(line).append('\n');
				}
				// 方法阻塞, 等待命令执行完成（成功会返回0）
				process.waitFor();
			} catch (Exception e){
				return e.getMessage();
			} finally {
				closeStream(bufIn);
				closeStream(bufError);
				// 销毁子进程
				if (process != null) {
					process.destroy();
				}
			}
			return result.toString();
		});
		executor.execute(future);
		try {
			//取得结果，同时设置超时执行时间,同样可以用future.get(),不设置执行超时时间取得结果
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(true);
		} finally {
			executor.shutdown();
		}
		return result.toString();
	}

	/**
	 * 执行系统命令, 返回执行结果
	 * @param cmd 需要执行的命令
	 */
	public static String execCmd(String cmd) {
		return execCmd(cmd, null, TIMEOUT);
	}

	/**
	 * 执行系统命令, 返回执行结果
	 * @param cmd 需要执行的命令
	 * @param pwd 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
	 */
	public static String execCmd(String cmd, File pwd) {
		return execCmd(cmd, pwd, TIMEOUT);
	}

	/**
	 * 执行系统命令, 返回执行结果
	 * @param cmd 需要执行的命令
	 * @param timeout 最长等待多长时间,单位秒
	 */
	public static String execCmd(String cmd, long timeout) {
		return execCmd(cmd, null, timeout);
	}

	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				log.error("TerminalUtils关闭流失败");
			}
		}
	}
}
