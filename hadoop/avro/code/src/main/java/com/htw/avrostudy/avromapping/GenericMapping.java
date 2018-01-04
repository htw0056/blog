package com.htw.avrostudy.avromapping;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/*
 * 通用映射
 */
public class GenericMapping {

	public static void main(String[] args) throws IOException {
		//通过解析模式文件获得模式
		Schema schema=new Schema.Parser().parse(new File("src/main/avro/user.avsc"));
		GenericRecord user1=new GenericData.Record(schema);
		user1.put("name", "htw");
		user1.put("favorite_number", 3);
		user1.put("favorite_color", "white");
		
		System.out.println(user1);
		

	}

}
