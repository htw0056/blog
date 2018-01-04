package com.htw.avrostudy.serializing;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

/*
 * avro序列化：
 * 使用通用映射
 * 在内存中序列化并反序列化
 */
public class SerializingInFile {

	public static void main(String[] args) throws IOException {
		// 获得schema
		Schema schema = new Schema.Parser().parse(new File("src/main/avro/user.avsc"));
		GenericRecord user1 = new GenericData.Record(schema);
		user1.put("name", "htw");
		user1.put("favorite_number", 111);
		user1.put("favorite_color", "white");

		// 写
		File file = new File("data.avro");
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(writer);
		dataFileWriter.create(schema, file);
		dataFileWriter.append(user1);
		dataFileWriter.close();

		// 读
		// 因为文件中有schema，此处生成DatumReader可以不传入schema
		DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();
		DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, reader);
		GenericRecord result = dataFileReader.next();
		System.out.println(result.get("name"));
		System.out.println(result.get("favorite_number"));
		System.out.println(result.get("favorite_color"));

	}

}
