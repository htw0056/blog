package com.htw.hadoop.study.chart3;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/*
 * 文件的基本操作
 */
public class FileBasicOperator {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(args[0]), conf);
		boolean sign;
		// 创建一个目录
		sign = fs.mkdirs(new Path(args[1]));
		System.out.println(sign);
		// 在该目录下创建文件
		sign = fs.createNewFile(new Path(args[1] + args[2]));
		System.out.println(sign);
		sign = fs.createNewFile(new Path(args[1] + args[3]));
		System.out.println(sign);

		// 列出目录下的所有文件信息
		FileStatus[] listStatus = fs.listStatus(new Path(args[1]));
		for (FileStatus f : listStatus) {
			System.out.println(
					f.getPath() + " " + f.getOwner() + " " + f.getLen());
		}
		// 删除文件
		sign = fs.delete(new Path(args[1] + args[2]), true);
		System.out.println(sign);

	}

}
