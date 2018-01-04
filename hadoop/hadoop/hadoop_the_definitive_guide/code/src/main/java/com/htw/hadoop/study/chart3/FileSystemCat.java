package com.htw.hadoop.study.chart3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/*
 * 根据文件系统获取hdfs文件信息
 */
public class FileSystemCat {

	public static void main(String[] args) {
		String uri = args[0];
		// 获取hadoop配置
		Configuration conf = new Configuration();
		InputStream in = null;
		try {

			// 根据uri获取文件系统
			FileSystem fs = FileSystem.get(URI.create(uri), conf);
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			IOUtils.closeStream(in);
		}

	}

}
