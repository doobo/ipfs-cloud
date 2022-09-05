package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 简单解压/压缩工具
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-05 17:38
 */
@Slf4j
public abstract class CompressorUtils {

	/**
	 * 获取zip里面指定的文件
	 */
	public static byte[] queryFileInZip(byte[] in, String fileName) throws IOException {
		try(ByteArrayInputStream is = new ByteArrayInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZipInputStream zin = new ZipInputStream(is)){
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.getName().equals(fileName)) {
					byte[] buffer = new byte[8192];
					int len;
					while ((len = zin.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					break;
				}
			}
			return out.toByteArray();
		}
	}


	/**
	 * 解压tar.gz文件
	 * tar文件只是把多个文件或文件夹打包合成一个文件,本身并没有进行压缩,gz是进行过压缩的文件
	 */
	public static void deGzipArchive(String dir, String filepath) throws Exception {
		final File input = new File(filepath);
		try(InputStream is = new FileInputStream(input);
			CompressorInputStream in = new GzipCompressorInputStream(is, true);
			TarArchiveInputStream tin = new TarArchiveInputStream(in)) {
			TarArchiveEntry entry = tin.getNextTarEntry();
			while (entry != null) {
				File archiveEntry = new File(dir, entry.getName());
				boolean mks = archiveEntry.getParentFile().mkdirs();
				if (entry.isDirectory()) {
					mks = archiveEntry.mkdir();
					entry = tin.getNextTarEntry();
					Optional.of(mks).filter(f->f)
						.ifPresent(c -> log.info("create next dir:{}", archiveEntry.getAbsolutePath()));
					continue;
				}
				Optional.of(mks).filter(f->f)
					.ifPresent(c -> log.info("create parent dir:{}", archiveEntry.getParentFile().getAbsolutePath()));
				OutputStream out = new FileOutputStream(archiveEntry);
				IOUtils.copy(tin, out);
				out.close();
				entry = tin.getNextTarEntry();
			}
		}
	}
}
