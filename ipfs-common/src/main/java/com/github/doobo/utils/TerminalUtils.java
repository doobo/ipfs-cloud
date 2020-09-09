package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 基本命令行工具
 */
@Slf4j
public class TerminalUtils {

	private static final long TIMEOUT = 120;

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
	 */
	public static String execCmd(long timeout, String ...cmd) {
		if(cmd != null && cmd.length > 0){
			return execCmd(StringUtils.join(cmd, " "), null, timeout);
		}
		return null;
	}

	public static String execCmd(String ...cmd) {
		return execCmd(TIMEOUT,  cmd);
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

	/**
	 * 异步启动,不阻塞
	 * @param sh
	 */
	public static void asyncExecute(String sh) {
		new Thread(() -> {
			syncExecute(sh, null, Long.MAX_VALUE);
		}).start();
	}

	/**
	 * 有返回执行程序
	 * @param cmd
	 * @param pwd
	 * @param timeout 等待时间,毫秒
	 */
	public static byte[] syncExecute(String cmd, String pwd, long timeout){
		File file = pwd == null?null: new File(pwd);
		try {
			final CommandLine commandLine = CommandLine.parse(cmd);
			try(ByteArrayOutputStream bs = new ByteArrayOutputStream()){
				DefaultExecutor executor = new DefaultExecutor();
				executor.setStreamHandler(new PumpStreamHandler(bs, bs));
				executor.setWorkingDirectory(file);
				executor.execute(commandLine);
				bs.flush();
				log.debug("ExecuteInfo:{}",new String(bs.toByteArray(), UTF_8.name()));
				ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
				executor.setWatchdog(watchdog);
				return bs.toByteArray();
			}
		} catch (Exception e){
			log.error("asyncExecuteError,{}", cmd, e);
		}
		return new byte[0];
	}

	/**
	 * 设置等待时间
	 * @param cmd
	 * @param timeout
	 */
	public static byte[] syncExecute(String cmd, long timeout){
		return syncExecute(cmd, null, timeout);
	}

	/**
	 * 立即返回
	 * @param cmd
	 */
	public static byte[] syncExecute(File pwd, String... cmd){
		String sh = null;
		if(cmd != null && cmd.length > 0){
			sh = StringUtils.join(cmd, " ");
		}
		try {
			String path = pwd==null?null:pwd.getCanonicalPath();
			return syncExecute(sh, path,TIMEOUT * 1000);
		} catch (IOException e) {
			log.error("syncExecuteError,{}" ,sh ,e);
		}
		return new byte[0];
	}

	/**
	 * 立即返回
	 * @param cmd
	 */
	public static byte[] syncExecute(String... cmd){
		return syncExecute(null, cmd);
	}

	/**
	 * 立即返回
	 * @param cmd
	 */
	public static String syncExecuteStr(String... cmd){
		try {
			return new String(syncExecute(null, cmd), UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			log.error("syncExecuteStrError", e);
		}
		return null;
	}

	/**
	 * 主程序带参数执行
	 * @param main
	 * @param params
	 */
	public static String syncMainExecute(String main, String ...params) {
		CommandLine commandLine = new CommandLine(main);
		commandLine.addArguments(params, false);
		try(ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
			DefaultExecutor executor = new DefaultExecutor();
			executor.setStreamHandler(new PumpStreamHandler(bs, bs));
			executor.execute(commandLine);
			bs.flush();
			ExecuteWatchdog watchdog = new ExecuteWatchdog(TIMEOUT * 1000);
			executor.setWatchdog(watchdog);
			return new String(bs.toByteArray(), UTF_8.name());
		} catch (Exception e){
			log.error("syncExecuteError", e);
		}
		return null;
	}

	/**
	 * 多参数执行系统命令
	 * @param cmd
	 */
	public static String syncProcess(String ...cmd){
		StringBuilder sl = new StringBuilder();
		try {
			ProcessBuilder pb = new ProcessBuilder(cmd);
			Process p = pb.start();
			p.waitFor();
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
				StreamUtils.copy(p.getInputStream(), out);
				sl.append(new String(out.toByteArray(), UTF_8.name()));
			}
			sl.append(StreamUtils.copyToString(p.getInputStream(), UTF_8));
			sl.append(StreamUtils.copyToString(p.getErrorStream(), UTF_8));
		} catch (Exception e) {
			log.error("syncProcessError", e);
		}
		return sl.toString();
	}
}
