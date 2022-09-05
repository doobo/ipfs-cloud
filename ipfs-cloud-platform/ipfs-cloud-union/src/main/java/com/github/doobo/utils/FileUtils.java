package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 文件常用工具
 */
@Slf4j
public abstract class FileUtils {

	/**
	 * 读取文件成字符串，并去掉换行符
	 */
	public static String getFileStrNoLine(File file) throws FileNotFoundException {
		StringBuffer sb = new StringBuffer();
		InputStream in = new FileInputStream(file);
		Reader reader = new InputStreamReader(in);
		char[] chars = new char[512];
		readFileStringInLine(sb, reader, chars);
		return sb.toString();
	}

	/**
	 * 一次性读取文本文件
	 */
	public static String getFileStr(String filePath){
		File file=new File(filePath);
		try(FileInputStream in=new FileInputStream(file)){
			// size 为字串的长度 ，这里一次性读完
			int size=in.available();
			byte[] buffer=new byte[size];
			in.read(buffer);
			return new String(buffer,UTF_8.name());
		} catch (IOException e) {
			log.error("getFileStrError", e);
		}
		return null;
	}

	private static void readFileStringInLine(StringBuffer sb, Reader reader, char[] chars) {
		try {
			int start;
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((start = reader.read(chars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((start == chars.length) && (chars[chars.length - 1] != '\r')) {
					sb.append(chars);
				} else {
					for (int i = 0; i < start; i++) {
						if (chars[i] != '\r') {
							sb.append(chars[i]);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("readFileStringInLineError:", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					log.error("readFileStringInLineCloseError:", e);
				}
			}
		}
	}

	/**
	 * 读取资源文件成字符串,不替换换行符
	 */
	public static String readResourcesFile(String fileName){
		ClassPathResource classPathResource = new ClassPathResource(fileName);
		try (InputStream inputStream = classPathResource.getInputStream();
			 ByteArrayOutputStream result = new ByteArrayOutputStream()){
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			return result.toString(UTF_8.name());
		} catch (IOException e) {
			log.error("readResourcesFileError", e);
		}
		return null;
	}

	/**
	 * 读取资源文件成byte数组,不替换换行符
	 */
	public static byte[] readResourcesByte(String fileName){
		ClassPathResource classPathResource = new ClassPathResource(fileName);
		try (InputStream inputStream = classPathResource.getInputStream()){
			return StreamUtils.copyToByteArray(inputStream);
		} catch (IOException e) {
			log.error("readResourcesByte", e);
		}
		return new byte[0];
	}


	/**
	 * 递归扫描指定文件夹下面的指定文件
	 */
	public static ArrayList<String> scanFilesWithRecursion(String folderPath, ArrayList<String> scanFiles){
		scanFiles = scanFiles == null ? new ArrayList<>():scanFiles;
		File directory = new File(folderPath);
		if(!directory.isDirectory()){
			throw new RuntimeException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");
		}
		if(directory.isDirectory()){
			File [] listFiles = directory.listFiles();
			if(listFiles == null || listFiles.length == 0){
				return scanFiles;
			}
			/**如果当前是文件夹，进入递归扫描文件夹**/
			/**递归扫描下面的文件夹**/
			for (File listFile : listFiles) {
				/**如果当前是文件夹，进入递归扫描文件夹**/
				if (listFile.isDirectory()) {
					/**递归扫描下面的文件夹**/
					scanFilesWithRecursion(listFile.getAbsolutePath(), scanFiles);
				} else {
					scanFiles.add(listFile.getAbsolutePath());
				}
			}
		}
		return scanFiles;
	}

	/**
	 *
	 * 非递归方式扫描指定文件夹下面的所有文件
	 */
	public static List<String> scanFilesWithNoRecursion(String folderPath){
		List<String> scanFiles = new ArrayList<>();
		LinkedList<File> queueFiles = new LinkedList<>();
		File directory = new File(folderPath);
		if(!directory.isDirectory()){
			throw new RuntimeException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");
		}else{
			//首先将第一层目录扫描一遍
			File [] files = directory.listFiles();
			if(files == null){
				return scanFiles;
			}
			//遍历扫出的文件数组，如果是文件夹，将其放入到linkedList中稍后处理
			for (File file : files) {
				if (file.isDirectory()) {
					queueFiles.add(file);
				} else {
					//暂时将文件名放入scanFiles中
					scanFiles.add(file.getAbsolutePath());
				}
			}
			//如果linkedList非空遍历linkedList
			while(!queueFiles.isEmpty()){
				//移出linkedList中的第一个
				File headDirectory = queueFiles.removeFirst();
				File [] currentFiles = headDirectory.listFiles();
				for (File currentFile : currentFiles) {
					if (currentFile.isDirectory()) {
						//如果仍然是文件夹，将其放入linkedList中
						queueFiles.add(currentFile);
					} else {
						scanFiles.add(currentFile.getAbsolutePath());
					}
				}
			}
		}
		return scanFiles;
	}

	/**
	 * 创建目录以及文件
	 */
	public static File createFile(String filePath, String fileName) {
		File folder = new File(filePath);
		//文件夹路径不存在
		if (!folder.exists()) {
			if(folder.mkdirs()){
				log.info("mkdirs {} success", filePath);
			}
		}
		filePath = filePath.replace("\\\\","/").replace("\\","/");
		// 如果文件不存在就创建
		File file = new File(filePath, fileName);
		try {
			if(!file.createNewFile()){
				log.info("file exist:{}/{}" ,filePath, fileName);
			}
		}catch(Exception e) {
			log.error("createFileError", e);
		}
		return file;
	}

	/**
	 * 写入文件
	 */
	public static void writFile(String fileData, String fileName) {
		//新建一个文件对象，如果不存在则创建一个该文件
		File f = new File(fileName);
		try (FileWriter fw=new FileWriter(f)){
			//将字符串写入到指定的路径下的文件中
			fw.write(fileData);
		} catch (IOException e) {
			log.error("FileUtilsError", e);
		}
	}

	/**
	 * 如果目录不存在,则创建目录
	 */
	public static File createDirIfAbsent(String path){
		File folder = new File(path);
		//文件夹路径不存在
		if (!folder.exists() && folder.mkdirs()){
			log.info("createDirIfAbsent success:{}", path);
		}
		return folder;
	}

	/**
	 * 删除文件
	 */
	public static void deleteFiles(List<String> tempList) {
		if (!CollectionUtils.isEmpty(tempList)) {
			for (String fileName : tempList) {
				File file = new File(fileName);
				if(!file.exists()){
					continue;
				}
				Path path = file.toPath();
				try {
					Files.delete(path);
				} catch (Exception e) {
					log.info("deleteFilesError", e);
				}
			}
		}
	}
}
