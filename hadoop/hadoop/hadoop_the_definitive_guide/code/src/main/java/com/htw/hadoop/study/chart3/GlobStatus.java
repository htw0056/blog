package com.htw.hadoop.study.chart3;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/*
 * 使用通配符和正则来过滤文件
 */
public class GlobStatus {

	static class RegexExcludePathFilter implements PathFilter {
		private String regex;

		public RegexExcludePathFilter(String regex) {
			this.regex = regex;
		}
		public boolean accept(Path path) {
			return !path.toString().matches(this.regex);
		}

	}
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(args[0]), conf);

		// 第一个参数：通配符来匹配文件 第二个参数：利用正则过滤通配符筛选过后的文件
		FileStatus[] globStatus = fs.globStatus(new Path(args[1]),
				new RegexExcludePathFilter(args[2]));
		for (FileStatus f : globStatus) {
			System.out.println(f.getPath());
		}

	}

}
