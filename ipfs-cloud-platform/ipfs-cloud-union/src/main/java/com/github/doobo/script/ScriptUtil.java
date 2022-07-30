package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 1、内嵌编译器如"PythonInterpreter"无法引用扩展包，因此推荐使用java调用控制台进程方式"Runtime.getRuntime().execCmd()"来运行脚本(shell或python)；
 * 2、因为通过java调用控制台进程方式实现，需要保证目标机器PATH路径正确配置对应编译器；
 * 3、暂时脚本执行日志只能在脚本执行结束后一次性获取，无法保证实时性；因此为确保日志实时性，可改为将脚本打印的日志存储在指定的日志文件上；
 * 4、python 异常输出优先级高于标准输出，体现在Log文件中，因此推荐通过logging方式打日志保持和异常信息一致；否则用prinf日志顺序会错乱
 */
@Slf4j
public abstract class ScriptUtil {

	/**
	 * 超时时间
	 */
	private final static Long TIMEOUT_TIME = 60 * 1000L;

	/**
	 * make script file
	 */
	public static void markScriptFile(String scriptFileName, String content) throws Exception {
		try(FileOutputStream fileOutputStream = new FileOutputStream(scriptFileName)){
			fileOutputStream.write(content.getBytes(UTF_8.name()));
		}
	}

	/**
	 * 日志文件输出方式
	 * 优点：支持将目标数据实时输出到指定日志文件中去
	 * 缺点：
	 * 标准输出和错误输出优先级固定，可能和脚本中顺序不一致
	 * Java无法实时获取
	 */
	public static int execToFile(String command, String scriptFile, String logFile, String... params){
		// 标准输出：print （null if watchdog timeout）
		// 错误输出：logging + 异常 （still exists if watchdog timeout）
		// 标准输入
		try (FileOutputStream fileOutputStream = new FileOutputStream(logFile, true)){
			PumpStreamHandler streamHandler = new PumpStreamHandler(fileOutputStream, fileOutputStream, null);
			return execCmd(command, scriptFile, params, streamHandler);
		} catch (Exception e) {
			log.error("execToFileError", e);
			return -1;
		}
	}

	public static int execCmd(String command, String scriptFile, CollectingLog clg, String... params) throws IOException {
		PumpStreamHandler streamHandler = new PumpStreamHandler(clg);
		return execCmd(command, scriptFile, params, streamHandler);
	}

	public static int execCmdLine(String command, String scriptFile, CollectingLog clg, Long timeout, String... params) throws IOException {
		PumpStreamHandler streamHandler = new PumpStreamHandler(clg);
		return execCmd(command, scriptFile, params, streamHandler, timeout);
	}

	public static int execCmdByte(String command, String scriptFile, CollectingByte clb, Long timeout, String... params) throws IOException {
		PumpStreamHandler streamHandler = new PumpStreamHandler(clb);
		return execCmd(command, scriptFile, params, streamHandler, timeout);
	}

	public static int execCmdPwd(String command, File pwd, CollectingLog clg, Long timeout, String... params) throws IOException {
		PumpStreamHandler streamHandler = new PumpStreamHandler(clg);
		return execCmd(command, pwd, params, streamHandler, timeout);
	}

	public static int execCmd(String command, String scriptFile, String[] params, PumpStreamHandler streamHandler) throws IOException {
		CommandLine commandline = new CommandLine(command);
		if(!StringUtils.isEmpty(scriptFile)){
			commandline.addArgument(scriptFile);
		}
		if (params != null && params.length > 0) {
			commandline.addArguments(params);
		}
		// execCmd
		DefaultExecutor exec = new DefaultExecutor();
		exec.setExitValues(null);
		exec.setStreamHandler(streamHandler);
		return exec.execute(commandline);
	}

	/**
	 * 指定超时时间
	 */
	public static int execCmd(String command, String scriptFile, String[] params, PumpStreamHandler streamHandler, Long timeout) throws IOException {
		CommandLine commandline = new CommandLine(command);
		if(!StringUtils.isEmpty(scriptFile)){
			commandline.addArgument(scriptFile);
		}
		if(timeout == null){
			timeout = TIMEOUT_TIME;
		}
		if (params != null && params.length > 0) {
			commandline.addArguments(params);
		}
		// execCmd
		DefaultExecutor exec = new DefaultExecutor();
		exec.setExitValues(null);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		exec.setWatchdog(watchdog);
		exec.setStreamHandler(streamHandler);
		return exec.execute(commandline);
	}

	/**
	 * 指定超时时间和脚本执行路径
	 */
	public static int execCmd(String command, File pwd, String[] params, PumpStreamHandler streamHandler, Long timeout) throws IOException {
		CommandLine commandline = new CommandLine(command);
		if (params != null && params.length > 0) {
			commandline.addArguments(params);
		}
		if(timeout == null){
			timeout = TIMEOUT_TIME;
		}
		// execCmd
		DefaultExecutor exec = new DefaultExecutor();
		exec.setExitValues(null);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		exec.setWatchdog(watchdog);
		if(pwd != null){
			exec.setWorkingDirectory(pwd);
		}
		exec.setStreamHandler(streamHandler);
		return exec.execute(commandline);
	}

	/**
	 *直接返回字符串
	 */
	public static String execToString(String command, String scriptFile, Long timeout, String... params) {
		// 标准输出：print （null if watchdog timeout）
		// 错误输出：logging + 异常 （still exists if watchdog timeout）
		// 标准输入
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
			execCmd(command, scriptFile, params, streamHandler, timeout);
			return outputStream.toString(UTF_8.name());
		} catch (Exception e) {
			log.warn("execToStringError", e);
		}
		return null;
	}

	/**
	 *直接返回二进制流
	 */
	public static byte[] execToByte(String command, String scriptFile, Long timeout, String... params) {
		// 标准输出：print （null if watchdog timeout）
		// 错误输出：logging + 异常 （still exists if watchdog timeout）
		// 标准输入
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
			execCmd(command, scriptFile, params, streamHandler, timeout);
			return outputStream.toByteArray();
		} catch (Exception e) {
			log.warn("execToStringError", e);
		}
		return null;
	}

}
