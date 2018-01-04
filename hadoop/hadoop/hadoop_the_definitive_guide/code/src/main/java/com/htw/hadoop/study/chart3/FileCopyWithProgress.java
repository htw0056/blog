package com.htw.hadoop.study.chart3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

/*
 * 复制本地文件到HDFS
 */
public class FileCopyWithProgress {

	public static void main(String[] args) {
		String localSrc = args[0];
		String dst = args[1];
		String dst2 = args[2];
		Configuration conf = new Configuration();
		try {

			/*
			 * method one
			 */
			// 获取本地文件输入流
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(localSrc));

			// 获取文件系统
			FileSystem fs = FileSystem.get(URI.create(dst), conf);
			// 创建hdfs中的文件输出流
			FSDataOutputStream out = fs.create(new Path(dst),
					new Progressable() {
						public void progress() {
							System.out.print(".");
						}
					});
			// copy,并且自动关闭流
			IOUtils.copyBytes(in, out, 4096, true);
			/*
			 * method two
			 */
			// 使用copyFromLocalFile同样可实现该功能
			fs.copyFromLocalFile(new Path(localSrc), new Path(dst2));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
