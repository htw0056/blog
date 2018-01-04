package com.htw.hadoop.study.chart4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.util.ReflectionUtils;

/*
 * 使用CodecPool
 * 使用方式:echo "hello world" | hadoop jar xxx.jar class | gunzip
 */
public class PooledStreamCompressor {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(GzipCodec.class, conf);

		Compressor compressor = null;
		try {
			// 从pool中获得compressor实例
			compressor = CodecPool.getCompressor(codec);
			CompressionOutputStream out = codec.createOutputStream(System.out,
					compressor);
			IOUtils.copyBytes(System.in, out, 4096, false);
			out.finish();

		} finally {
			// 释放
			CodecPool.returnCompressor(compressor);
		}

	}

}
