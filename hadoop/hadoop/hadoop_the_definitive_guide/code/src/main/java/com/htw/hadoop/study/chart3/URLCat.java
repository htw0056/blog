package com.htw.hadoop.study.chart3;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/*
 * 根据URL获取hdfs文件数据
 */
public class URLCat {

	static {
		// 通过该设置使java程序能够识别Hadoop的hdfsURL方案
		// 只设置一次，否则会报错
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	public static void main(String[] args) {
		InputStream in = null;
		try {

			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(in);

		}
	}

}
