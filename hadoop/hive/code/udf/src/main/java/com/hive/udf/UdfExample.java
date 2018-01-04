package com.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/*
 * UDF
 * 继承UDF，实现 evaluate() 函数
 * 对于简单的类型(int,string,double...)可以使用该UDF处理
 * 
 */
public class UdfExample extends UDF {
	/*
	 * 实现字符串后面追加数字功能
	 * 其中有null则直接返回null
	 * - 可以直接使用java内置类型：string，int(Integer)，double(Double)
	 * - 建议使用hadoop序列化类型:Text, IntWritable,DoubleWritable,效率更高
	 * - 建议使用class类，也就是不要用int,double
	 */
	public Text evaluate(Text s, IntWritable i) {
		if (s == null)
			return null;
		if (i == null)
			return null;
		return new Text(s.toString() + i.get());
	}

	public String evaluate(String s, Integer i) {
		if (s == null)
			return null;
		if (i == null)
			return null;
		return s+ i;
	}
}
