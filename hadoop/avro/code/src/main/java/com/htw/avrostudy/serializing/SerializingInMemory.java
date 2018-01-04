package com.htw.avrostudy.serializing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

/*
 * avro序列化：
 * 使用通用映射
 * 在内存中序列化并反序列化
 */
public class SerializingInMemory {

	public static void main(String[] args) throws IOException {
		// 获得schema
		Schema schema = new Schema.Parser().parse(new File("src/main/avro/user.avsc"));
		GenericRecord user1 = new GenericData.Record(schema);
		user1.put("name", "htw");
		user1.put("favorite_number", 111);
		user1.put("favorite_color", "white");

		// 写
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
		Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
		writer.write(user1, encoder);
		encoder.flush();
		out.close();

		// 可以发现，这种序列化并没有将schema写入.可与SerializingInFile对比

		System.out.println(out.toString());

		// 读
		// 因为没有schema,解析时候必须传入schema
		DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
		Decoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(),null);
		GenericRecord result = reader.read(null, decoder);
		System.out.println(result.get("name"));
		System.out.println(result.get("favorite_color"));
		System.out.println(result.get("favorite_number"));
	}

}
