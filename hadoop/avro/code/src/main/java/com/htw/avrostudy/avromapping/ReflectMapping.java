package com.htw.avrostudy.avromapping;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

import example.avro.User;

/*
 * 自反映射
 * 这种模式效率差，一般不用
 * 自反映射需要先生成代码，然后解析回schema使用--如果我没理解错的话~
 */
public class ReflectMapping {

	public static void main(String[] args) {
		ReflectData reflectData = ReflectData.get();
		Schema schema = reflectData.getSchema(User.class);
		
		System.out.println(schema);
	}

}
