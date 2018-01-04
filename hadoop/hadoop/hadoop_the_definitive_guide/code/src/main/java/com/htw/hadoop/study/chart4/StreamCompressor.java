package com.htw.hadoop.study.chart4;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.util.ReflectionUtils;

/*
 * gzip压缩
 */
public class StreamCompressor {

	/*
	 * 调用方式 hadoop jar xx.jar class | gunzip
	 */
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		// 创建gzip压缩
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(GzipCodec.class, conf);
		// 创建压缩输入流
		CompressionOutputStream out = codec.createOutputStream(System.out);

		// 输入压缩的文字
		out.write("hello world".getBytes());
		out.close();

	}

}
