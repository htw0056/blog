package com.htw.hadoop.study.chart4;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

/*
 * 利用压缩工厂自动解析压缩方式
 */
public class FileDecompressor {

	public static void main(String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		
		Path inputPath = new Path(uri);
		//获得codec工厂
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		//利用工厂自动解析压缩方式
		CompressionCodec codec = factory.getCodec(inputPath);
		if (codec == null) {
			System.err.println("No codec found for " + uri);
			System.exit(-1);
		}

		//获得去除扩展名
		String outputUri = CompressionCodecFactory.removeSuffix(uri,
				codec.getDefaultExtension());
		InputStream in = null;
		OutputStream out = null;
		try{
			in = codec.createInputStream(fs.open(inputPath));
			out = fs.create(new Path(outputUri));
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}

	}

}
