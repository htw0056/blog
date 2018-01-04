package com.htw.hadoop.study.chart4;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class TestSerialize {

	/*
	 * 序列化函数
	 */
	public static byte[] serizlize(Writable writable) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		writable.write(dataOut);
		dataOut.close();
		return out.toByteArray();
	}
	/*
	 * 反序列化函数
	 */
	public static byte[] deserialize(Writable writable, byte[] bytes)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		DataInputStream dataIn = new DataInputStream(in);
		writable.readFields(dataIn);
		dataIn.close();
		return bytes;
	}
	@Test
	public void testSerialize() throws IOException {

		IntWritable writable = new IntWritable(163);
		// 序列化,IntWritable使用4个字节
		byte[] bytes = serizlize(writable);
		assertThat(bytes.length, is(4));
		// 163的二进制表示为000000a3
		assertThat(StringUtils.byteToHexString(bytes), is("000000a3"));

		// 反序列化
		IntWritable newWritable = new IntWritable();
		deserialize(newWritable, bytes);
		assertThat(newWritable.get(), is(163));

	}

	// ----------------------------------------------------------------------------
	// 获取并使用Rawcomparator
	// ----------------------------------------------------------------------------
	@Test
	public void testRawcomparator() throws IOException {
		RawComparator<IntWritable> comparator = WritableComparator
				.get(IntWritable.class);
		IntWritable w1 = new IntWritable(163);
		IntWritable w2 = new IntWritable(67);
		assertThat(comparator.compare(w1, w2), greaterThan(0));
		byte[] b1 = serizlize(w1);
		byte[] b2 = serizlize(w2);
		assertThat(comparator.compare(b1, 0, b1.length, b2, 0, b2.length),
				greaterThan(0));

	}
	// part three
	@Test
	public void testString() throws UnsupportedEncodingException {
		String s = "\u0041\u00DF\u6771\uD801\uDC00";
		// string 以char为单位
		assertThat(s.length(), is(5));
		assertThat(s.getBytes("UTF-8").length, is(10));

		assertThat(s.indexOf("\u0041"), is(0));
		assertThat(s.indexOf("\u00DF"), is(1));
		assertThat(s.indexOf("\u6771"), is(2));
		assertThat(s.indexOf("\uD801\uDC00"), is(3));
		assertThat(s.indexOf("\uD801"), is(3));
		assertThat(s.indexOf("\uDC00"), is(4));

		assertThat(s.charAt(0), is('\u0041'));
		assertThat(s.charAt(1), is('\u00DF'));
		assertThat(s.charAt(2), is('\u6771'));
		assertThat(s.charAt(3), is('\uD801'));
		assertThat(s.charAt(4), is('\uDC00'));

		assertThat(s.codePointAt(0), is(0x0041));
		assertThat(s.codePointAt(1), is(0x00DF));
		assertThat(s.codePointAt(2), is(0x6771));
		assertThat(s.codePointAt(3), is(0x10400));
	}
	@Test
	public void testText() {
		Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");

		// Text 以UTF-8编码后的字节为单位
		assertThat(t.getLength(), is(10));
		assertThat(t.find("\u0041"), is(0));
		assertThat(t.find("\u00DF"), is(1));
		assertThat(t.find("\u6771"), is(3));
		assertThat(t.find("\uD801\uDC00"), is(6));
		assertThat(t.find("\uD801"), is(-1));

		assertThat(t.charAt(0), is(0x0041));
		assertThat(t.charAt(1), is(0x00DF));
		assertThat(t.charAt(3), is(0x6771));
		assertThat(t.charAt(6), is(0x10400));
		assertThat(t.charAt(2), is(-1));

	}
	/*
	 * 遍历Text对象中的字符
	 */
	@Test
	public void testTextIterator() {
		Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");
		ByteBuffer buf = ByteBuffer.wrap(t.getBytes(), 0, t.getLength());
		int cp;
		while (buf.hasRemaining() && (cp = Text.bytesToCodePoint(buf)) != -1) {
			System.out.println(Integer.toHexString(cp));
		}
	}
	/*
	 * Text的set方法陷阱
	 */
	@Test
	public void testTrickInText() {
		Text t = new Text("hadoop");
		// t.set("pig");
		t.set(new Text("pig"));
		assertThat(t.getLength(), is(3));
		assertThat(t.getBytes().length, is(6));
	}

}
